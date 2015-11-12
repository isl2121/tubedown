package blackcat.tubedown;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sgsgf on 2015-10-02.
 */

public class NoticeActivity extends Activity {

    public LinearLayout question1,question2,question3,question4,question5,question6;
    public TextView answer1,answer2,answer3,answer4,answer5,answer6;
    public boolean  answer1_c,answer2_c,answer3_c,answer4_c,answer5_c,answer6_c ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(blackcat.tubedown.R.layout.activity_notice);
        answer1_c = true;
        answer2_c = true;
        answer3_c = true;
        answer4_c = true;
        answer5_c = true;
        answer6_c = true;
        question1 = (LinearLayout)findViewById(blackcat.tubedown.R.id.notice_ll1);
        question2 = (LinearLayout)findViewById(blackcat.tubedown.R.id.notice_ll2);
        question3 = (LinearLayout)findViewById(blackcat.tubedown.R.id.notice_ll3);
        question4 = (LinearLayout)findViewById(blackcat.tubedown.R.id.notice_ll4);
        question5 = (LinearLayout)findViewById(blackcat.tubedown.R.id.notice_ll5);
        question6 = (LinearLayout)findViewById(blackcat.tubedown.R.id.notice_ll6);
        answer1 = (TextView)findViewById(blackcat.tubedown.R.id.notice_txt_answer1);
        answer2 = (TextView)findViewById(blackcat.tubedown.R.id.notice_txt_answer2);
        answer3 = (TextView)findViewById(blackcat.tubedown.R.id.notice_txt_answer3);
        answer4 = (TextView)findViewById(blackcat.tubedown.R.id.notice_txt_answer4);
        answer5 = (TextView)findViewById(blackcat.tubedown.R.id.notice_txt_answer5);
        answer6 = (TextView)findViewById(blackcat.tubedown.R.id.notice_txt_answer6);

        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer1_c) {  visibility(1); }else{ invisibility(); }
            }
        });
        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer2_c) {  visibility(2); }else{ invisibility();  }
            }
        });
        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer3_c) { visibility(3); }else{ invisibility(); }
            }
        });
        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer4_c) { visibility(4); }else{ invisibility(); }
            }
        });
        question5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer5_c) { visibility(5); }else{ invisibility();}
            }
        });
        question6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer6_c) { visibility(6); }else{ invisibility();}
            }
        });

    }

    public void visibility(int num){
        switch (num){
            case 1:
               answer1.setVisibility(View.VISIBLE);
                answer2.setVisibility(View.GONE);
                answer3.setVisibility(View.GONE);
                answer4.setVisibility(View.GONE);
                answer5.setVisibility(View.GONE);
                answer6.setVisibility(View.GONE);
                answer1_c = false;
                answer2_c = true;
                answer3_c = true;
                answer4_c = true;
                answer5_c = true;
                answer6_c = true;
                break;
            case 2:
                answer1.setVisibility(View.GONE);
                answer2.setVisibility(View.VISIBLE);
                answer3.setVisibility(View.GONE);
                answer4.setVisibility(View.GONE);
                answer5.setVisibility(View.GONE);
                answer6.setVisibility(View.GONE);
                answer1_c = true;
                answer2_c = false;
                answer3_c = true;
                answer4_c = true;
                answer5_c = true;
                answer6_c = true;
                break;
            case 3:
                answer1.setVisibility(View.GONE);
                answer2.setVisibility(View.GONE);
                answer3.setVisibility(View.VISIBLE);
                answer4.setVisibility(View.GONE);
                answer5.setVisibility(View.GONE);
                answer6.setVisibility(View.GONE);
                answer1_c = true;
                answer2_c = true;
                answer3_c = false;
                answer4_c = true;
                answer5_c = true;
                answer6_c = true;
                break;
            case 4:
                answer1.setVisibility(View.GONE);
                answer2.setVisibility(View.GONE);
                answer3.setVisibility(View.GONE);
                answer4.setVisibility(View.VISIBLE);
                answer5.setVisibility(View.GONE);
                answer6.setVisibility(View.GONE);
                answer1_c = true;
                answer2_c = true;
                answer3_c = true;
                answer4_c = false;
                answer5_c = true;
                answer6_c = true;
                break;
            case 5:
                answer1.setVisibility(View.GONE);
                answer2.setVisibility(View.GONE);
                answer3.setVisibility(View.GONE);
                answer4.setVisibility(View.GONE);
                answer5.setVisibility(View.VISIBLE);
                answer6.setVisibility(View.GONE);
                answer1_c = true;
                answer2_c = true;
                answer3_c = true;
                answer4_c = true;
                answer5_c = false;
                answer6_c = true;
                break;
            case 6:
                answer1.setVisibility(View.GONE);
                answer2.setVisibility(View.GONE);
                answer3.setVisibility(View.GONE);
                answer4.setVisibility(View.GONE);
                answer5.setVisibility(View.GONE);
                answer6.setVisibility(View.VISIBLE);
                answer1_c = true;
                answer2_c = true;
                answer3_c = true;
                answer4_c = true;
                answer5_c = true;
                answer6_c = false;
                break;
        }
    }
    public void invisibility(){
        answer1.setVisibility(View.GONE);
        answer2.setVisibility(View.GONE);
        answer3.setVisibility(View.GONE);
        answer4.setVisibility(View.GONE);
        answer5.setVisibility(View.GONE);
        answer6.setVisibility(View.GONE);
        answer1_c = true;
        answer2_c = true;
        answer3_c = true;
        answer4_c = true;
        answer5_c = true;
        answer6_c = true;
    }
}