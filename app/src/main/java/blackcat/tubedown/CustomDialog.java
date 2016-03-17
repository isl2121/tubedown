package blackcat.tubedown;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

//import com.google.android.gms.ads.AdView;

/**
 * Created by sgsgf on 2015-09-23.
 */
public class CustomDialog extends Dialog{
    private TextView mContentView;
    private Button mLeftButton,mRightButton;
    private String mContent;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //다이얼로그 준비
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(blackcat.tubedown.R.layout.activity_custom_dialog);

        mContentView = (TextView) findViewById(blackcat.tubedown.R.id.txt_content);
        mLeftButton = (Button) findViewById(blackcat.tubedown.R.id.btn_left);
        mRightButton = (Button) findViewById(blackcat.tubedown.R.id.btn_right);
        //에드몹 부착
        AdView adView = new AdView(getContext());
        adView.setAdUnitId("ca-app-pub-8286911079963314/1460970785");
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        LinearLayout layout = (LinearLayout)findViewById(R.id.adView);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        mContentView.setText(mContent);
        if(mLeftClickListener != null && mRightClickListener != null){
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        }else if(mLeftClickListener != null && mRightClickListener == null){
            mLeftButton.setOnClickListener(mLeftClickListener);
        }else{

        }
    }
    public CustomDialog(Context context,String content,View.OnClickListener leftListener,View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }
}