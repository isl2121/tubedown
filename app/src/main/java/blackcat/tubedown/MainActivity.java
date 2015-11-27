package blackcat.tubedown;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import blackcat.tubedown.drawer.NavigationDrawerCallbacks;
import blackcat.tubedown.drawer.NavigationDrawerFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private CharSequence mTitle;
    private BackPressCloseHandler backPressCloseHandler;

    SharedPreferences preferenceManager;
    DownloadManager downloadManager;
    final String strPref_Download_ID = "PREF_DOWNLOAD_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(blackcat.tubedown.R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);
        //mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //setSupportActionBar(mToolbar);
        preferenceManager
                = PreferenceManager.getDefaultSharedPreferences(this);
        downloadManager
                = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(blackcat.tubedown.R.id.fragment_drawer);


       // mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragmentToReplace = PlaceholderFragment.newInstance(position + 1);
        switch(position){
            case 0:
                // Report Health Issue
                fragmentToReplace = new MainHomeFragment();
                break;
            case 1:
                fragmentToReplace = new SettingFragment();
                // Profile Fragment
                break;
            case 2:
                fragmentToReplace = new MaindaumFragment();
                // Profile Fragment
                break;

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(blackcat.tubedown.R.id.container, fragmentToReplace)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "";
                break;
            case 2:
                mTitle = "설정";
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            backPressCloseHandler.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    public interface FragmentRefreshListener{
        void onRefresh();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == blackcat.tubedown.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(blackcat.tubedown.R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        CheckDwnloadStatus();
//        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        registerReceiver(downloadReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        //unregisterReceiver(downloadReceiver);
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
           // CheckDwnloadStatus();
        }
    };

    private void CheckDwnloadStatus(){

        // TODO Auto-generated method stub
        DownloadManager.Query query = new DownloadManager.Query();
        long id = preferenceManager.getLong(strPref_Download_ID, 0);
        Log.i("test",String.valueOf(id));
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = cursor.getInt(columnReason);

            switch(status){
                case DownloadManager.STATUS_FAILED:
                    String failedReason = "";
                    switch(reason){
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            failedReason = "일시정지를 할수없습니다.";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            failedReason = "기기를 찾을수없습니다.";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            failedReason = "파일이 이미 존재합니다.";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            failedReason = "파일오류";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            failedReason = "HTTP 오류";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            failedReason = "저장공간부족합니다.";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            failedReason = "여러파일을 다운로드중입니다.";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            failedReason = "취급하지 않는 코드입니다.";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            failedReason = "알수없는오류입니다.";
                            break;
                    }
                    Log.e("#######Download", failedReason);
                    Toast.makeText(MainActivity.this,
                            "실패: " + failedReason,
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PAUSED:
                    String pausedReason = "";
                    switch(reason){
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            pausedReason = "와이파이 대기중입니다.";
                            break;
                        case DownloadManager.PAUSED_UNKNOWN:
                            pausedReason = "알수없는오류입니다.";
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            pausedReason = "네트워크대기중입니다.";
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            pausedReason = "재시작대기중입니다.";
                            break;
                    }

                    Toast.makeText(MainActivity.this,
                            "일시정지: " + pausedReason,
                            Toast.LENGTH_LONG).show();
                    break;
//                case DownloadManager.STATUS_PENDING:
//                    Toast.makeText(MainActivity.this,
//                            "준비중",
//                            Toast.LENGTH_LONG).show();
//                    break;
//                case DownloadManager.STATUS_RUNNING:
//                    Toast.makeText(MainActivity.this,
//                            "다운로드 시작",
//                            Toast.LENGTH_LONG).show();
//                    break;
            }
        }
    }
}
