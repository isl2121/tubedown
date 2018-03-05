package blackcat.tubedown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import blackcat.tubedown.service.InterfaceMapping;

public class SettingFragment extends Fragment {

    static FragmentActivity Setting_temp;
    SharedPreferences preferenceManager;

    public SettingFragment() {
        InterfaceMapping.getInstance().setFragment(this);
    }

    LinearLayout mMoveadmin_LL,mHowtoUse_LL,minternet_LL;
    MainActivity activity;
    TextView mWifi_Data_text;
    public static String temp ,temp2;
    int mWifi_Data;
    @Override
    public void onAttach(Activity activity) {
        this.activity = (MainActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(blackcat.tubedown.R.layout.activity_setting, null);
        preferenceManager  = PreferenceManager.getDefaultSharedPreferences(getActivity());
        temp =  preferenceManager.getString("get_file_path", Environment.DIRECTORY_DOWNLOADS);
        Setting_temp = getActivity();
        mWifi_Data = preferenceManager.getInt("mWifi_Data", 2);
        MainHomeFragment.first_help.setVisibility(View.GONE);
        mMoveadmin_LL = (LinearLayout)view.findViewById(blackcat.tubedown.R.id.move_admin_btn);
        mMoveadmin_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.naver.com/isl2121"));
                startActivity(intent);
            }
        });

        mHowtoUse_LL = (LinearLayout)view.findViewById(blackcat.tubedown.R.id.use_info_btn);
        mHowtoUse_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NoticeActivity.class);
                startActivity(intent);
            }
        });

        minternet_LL = (LinearLayout) view.findViewById(blackcat.tubedown.R.id.wifi_data_btn);
        mWifi_Data_text = (TextView) view.findViewById(blackcat.tubedown.R.id.wifi_data_txt);

        switch (mWifi_Data){
            case 0:
                temp2 = "와이파이";
                break;
            case 1:
                temp2 = "데이터";
                break;
            case 2:
                temp2 = "와이파이&데이터";
                break;
        }
        mWifi_Data_text.setText("다운로드 방식선택 :" + temp2);
        minternet_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),wifi_data_setting.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
