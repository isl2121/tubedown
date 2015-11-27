package blackcat.tubemate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sgsgf on 2015-09-24.
 */
public class file_path extends Activity
        implements AdapterView.OnItemClickListener {
    String mRoot = "";
    String mPath = "";
    TextView mTextMsg;
    ListView mListFile;
    Button mOk_btn,mfalse_btn;
    ArrayList<String> mArFile;
    SharedPreferences preferenceManager;
    TextView mFilepath_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(blackcat.tubemate.R.layout.file_path);
        preferenceManager  = PreferenceManager.getDefaultSharedPreferences(file_path.this);
        // SD 카드가 장착되어 있지 않다면 앱 종료
        if( isSdCard() == false )
            finish();
        mTextMsg = (TextView)findViewById(blackcat.tubemate.R.id.textMessage);
        // SD 카드 루트 폴더의 경로를 구한다
        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        //mTextMsg.setText(mRoot);
        String[] fileList = getFileList(mRoot);
        for(int i=0; i < fileList.length; i++)
        // ListView 초기화
        initListView();
        fileList2Array(fileList);
    }

    // ListView 초기화
    public void initListView() {
        mOk_btn = (Button)findViewById(blackcat.tubemate.R.id.file_path_ok_btn);
        mfalse_btn = (Button)findViewById(blackcat.tubemate.R.id.file_path_false_btn);

        mOk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor PrefEdit = preferenceManager.edit();
                String temp;
                temp = mPath.replace(mRoot,"");
                if(temp.equals("")){
                    temp =  preferenceManager.getString("get_file_path", Environment.DIRECTORY_DOWNLOADS);
                    PrefEdit.putString("get_file_path",Environment.DIRECTORY_DOWNLOADS);
                    PrefEdit.commit();
                    mFilepath_text = (TextView)SettingFragment.Setting_temp.findViewById(blackcat.tubemate.R.id.file_path_txt);
                    mFilepath_text.setText("저장파일선택 :" + temp);
                }else{
                    PrefEdit.putString("get_file_path", temp + "/");
                    PrefEdit.commit();
                    mFilepath_text = (TextView)SettingFragment.Setting_temp.findViewById(blackcat.tubemate.R.id.file_path_txt);
                    mFilepath_text.setText("저장파일선택 :" + mPath+"/");
                }

                onBackPressed();

            }
        });

        mfalse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mArFile = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mArFile);

        mListFile = (ListView)findViewById(blackcat.tubemate.R.id.listFile);
        mListFile.setAdapter(adapter);
        mListFile.setOnItemClickListener(this);
    }

    // ListView 항목 선택 이벤트 함수
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        String strItem = mArFile.get(position);
        // 선택된 폴더의 전체 경로를 구한다
        String strPath = getAbsolutePath(strItem);
        // 선택된 폴더에 존재하는 파일 목록을 구한다
        String[] fileList = getFileList(strPath);
        // 파일 목록을 ListView 에 표시
        fileList2Array(fileList);
    }

    // 폴더명을 받아서 전체 경로를 반환하는 함수
    public String getAbsolutePath(String strFolder) {
        String strPath;
        // 이전 폴더일때
        if( strFolder == ".." ) {
            // 전체 경로에서 최하위 폴더를 제거
            int pos = mPath.lastIndexOf("/");
            strPath = mPath.substring(0, pos);
        }
        else
            strPath = mPath + "/" + strFolder;
        return strPath;
    }


    // SD 카드 장착 여부를 반환
    public boolean isSdCard() {
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED) == false) {
            Toast.makeText(this, "SD Card does not exist", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 특정 폴더의 파일 목록을 구해서 반환
    public String[] getFileList(String strPath) {
        // 폴더 경로를 지정해서 File 객체 생성
        File fileRoot = new File(strPath);
        // 해당 경로가 폴더가 아니라면 함수 탈출
        if( fileRoot.isDirectory() == false )
            return null;
        mPath = strPath;
        mTextMsg.setText(mPath);
        // 파일 목록을 구한다
        String[] fileList = fileRoot.list();
        return fileList;
    }

    // 파일 목록을 ListView 에 표시
    public void fileList2Array(String[] fileList) {
        if( fileList == null )
            return;
        mArFile.clear();
        // 현재 선택된 폴더가 루트 폴더가 아니라면
        if( mRoot.length() < mPath.length() )
            // 이전 폴더로 이동하기 위해서 ListView 에 ".." 항목을 추가
            mArFile.add("..");

        for(int i=0; i < fileList.length; i++) {
            mArFile.add(fileList[i]);
        }
        ArrayAdapter adapter = (ArrayAdapter)mListFile.getAdapter();
        adapter.notifyDataSetChanged();
    }

}
