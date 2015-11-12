package blackcat.tubedown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sgsgf on 2015-09-30.
 */
public class wifi_data_setting extends Activity {
    Switch mWifi_switch,mData_switch,mWifi_Data_switch;
    int mWifi_Data,wifi_data;
    String temp;
    SharedPreferences preferenceManager;
    SharedPreferences.Editor PrefEdit;
    TextView mFilepath_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(blackcat.tubedown.R.layout.wifi_data_setting);
        preferenceManager  = PreferenceManager.getDefaultSharedPreferences(this);
        PrefEdit = preferenceManager.edit();
        mWifi_switch = (Switch) findViewById(blackcat.tubedown.R.id.wifi_switch);
        mData_switch = (Switch) findViewById(blackcat.tubedown.R.id.data_switch);
        mWifi_Data_switch = (Switch) findViewById(blackcat.tubedown.R.id.wifi_data_switch);

        mWifi_Data = preferenceManager.getInt("mWifi_Data",2);

        if(mWifi_Data==0){
            mWifi_switch.setChecked(true);
            mData_switch.setChecked(false);
            mWifi_Data_switch.setChecked(false);
        }
        if(mWifi_Data==1){
            mWifi_switch.setChecked(false);
            mData_switch.setChecked(true);
            mWifi_Data_switch.setChecked(false);
        }
        if(mWifi_Data==2){
            mWifi_switch.setChecked(false);
            mData_switch.setChecked(false);
            mWifi_Data_switch.setChecked(true);
        }

        mWifi_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PrefEdit.putInt("mWifi_Data", 0);
                    mWifi_switch.setChecked(true);
                    mData_switch.setChecked(false);
                    mWifi_Data_switch.setChecked(false);
                    wifi_data = 0;
                    select();
                }else{
                    inite();
                }
                PrefEdit.commit();
            }
        });

        mData_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mWifi_switch.setChecked(false);
                    mData_switch.setChecked(true);
                    mWifi_Data_switch.setChecked(false);
                    PrefEdit.putInt("mWifi_Data", 1);
                    wifi_data =1;
                    select();

                }else{
                    inite();
                }
                PrefEdit.commit();
            }
        });
        mWifi_Data_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mWifi_switch.setChecked(false);
                    mData_switch.setChecked(false);
                    mWifi_Data_switch.setChecked(true);
                    PrefEdit.putInt("mWifi_Data", 2);
                    wifi_data = 2;
                    select();
                }else{
                    inite();
                }
                PrefEdit.commit();
            }
        });

    }

    private void select() {
        mFilepath_text = (TextView)SettingFragment.Setting_temp.findViewById(blackcat.tubedown.R.id.wifi_data_txt);
        switch (wifi_data){
            case 0:
                temp = "와이파이";
                break;
            case 1:
                temp= "데이터";
                break;
            case 2:
                temp = "와이파이&데이터";
                break;
        }
        mFilepath_text.setText("저장파일선택 :" + temp);
    }

    private void inite() {
        if(!mWifi_switch.isChecked()&&!mData_switch.isChecked()&&!mWifi_Data_switch.isChecked()){
            mWifi_Data_switch.setChecked(true);
            Toast.makeText(wifi_data_setting.this,"하나는 선택되어야합니다",Toast.LENGTH_SHORT).show();
            PrefEdit.putInt("mWifi_Data", 2);
            PrefEdit.commit();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(wifi_data_setting.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("finishstatus", true);
        startActivity(intent);
        wifi_data_setting.this.finish();
    }
}
