package blackcat.tubedown;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import blackcat.tubedown.service.NoticeInfo;

/**
 * Created by sgsgf on 2015-10-02.
 */

public class NoticeActivity extends Activity {

    public LinearLayout notice_ll, question2, question3, question4, question5, question6;
    public TextView equstion, answer, answer3, answer4, answer5, answer6;
    public boolean answer1_c, answer2_c, answer3_c, answer4_c, answer5_c, answer6_c;
    public ListView notice_list;
    private ListAdapter mAdapter;
    private ArrayList<ListData> data = new ArrayList<ListData>();
    String test1, test2, test3, test4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(blackcat.tubedown.R.layout.activity_notice);
        notice_list = (ListView) findViewById(R.id.notice_list);
        mAdapter = new ListAdapter(NoticeActivity.this, data);
        notice_list.setAdapter(mAdapter);
        notice_list.setItemsCanFocus(false);
        NoticeInfo noticeInfo;
        String[] name = new String[] {
                "다운로드 화질은 어떻게 되나요",
                "음악파일을 다운받았는데 재생이 안되요.",
                "음악파일 재생하는방법은 없나요?",
                "다운로드가 너무 끊겨요",
                "불편하거나 궁금한거는 어디다 물어보나요",
                "TUBEMATE 이름에서 바뀐이유",
                "경로설정하는부분이 없어졌네요?"
        };
        String[] answer = new String[]{
                "소스의 간략화를 위하여 기본적으로 720p 화질로 다운받지만 동영상 자체에서 720p 화질을 지원하지 않으면 다음단계인 360p 화질로 다운받습니다.",
                "부분은 아직제작중인 부분입니다 간단히 설명해드리면 유튜브에서 다운로드를 할때 m4a라는 확장자로 포멧해서 다운받는데 핸드폰 기종에 따라서 이 확장자를 재생못시키는 기종이 있습니다. 네 핸드폰 문제에요",
                "자료를 찾아 나중에 수정할사항이지만 우선은 파워앰프 (Poweramp)라는 음악플레이어 또는 m4a가 재생가능한 어플을 다운받아서 들으시면 됩니다.",
                "다운로드는 안드로이드 내부기능인 다운로드매니저를 사용합니다 그부분에 대해서는 제가 따로 다운로드방식을 만들지 않는한 도움드리긴 힘들듯합니다만 대부분은 인터넷연결문제와 저장경로가 잘못된경우가 많으니 한번확인해주세요.",
                "설정탭에 보시면 개발자 블로그가 있는데 간단하게 네이버블로그에 댓글달아주시면 도와드리겠습니다.( 근데 이게 공부용으로 만들었다가 괜찮아서 배포하기 시작한 어플이라 빠른 수정은 못해드려요 )",
                "눈치채신분이 있으시겠지만 원래는 TubeDown으로 제작했습니다만 제작자가 네이버에 업로드하다가 실수로 tubemate로 이름을 잘못올려버렸어요.. 헷갈리게했다면 죄송합니다.",
                "원래는 사용자분들이 설정할수있도록 제작해뒀으나 이부분에 오류가 생겨서 아예 지워버렸습니다."
        };

        for (int i = 0; i < name.length; i++) {
            noticeInfo = new NoticeInfo();
            noticeInfo.mQuestion = name[i];
            noticeInfo.mAnwser = answer[i];
            data.add(new ListData(noticeInfo));
        }
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ListData> mData;

        ListAdapter(Context context, ArrayList<ListData> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder vh;
            final int pos = position;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vh = new ViewHolder();
                convertView = inflater.inflate(R.layout.notice_item, parent, false);
                vh.equstion = (TextView) convertView.findViewById(R.id.notice_question);
                vh.answer = (TextView) convertView.findViewById(R.id.notice_answer);
                vh.mlayout = (LinearLayout) convertView.findViewById(R.id.notice_ll);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.equstion.setText(mData.get(position).info.mQuestion);
            vh.answer.setText(mData.get(position).info.mAnwser);
            vh.mlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListData id = (ListData) vh.mlayout.getTag();
                    if (vh.answer.getVisibility() == View.VISIBLE) {
                        vh.answer.setVisibility(View.GONE);
                    } else {
                        vh.answer.setVisibility(View.VISIBLE);
                    }
                }
            });
            vh.mlayout.setTag(mData.get(position));
            return convertView;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    private class ViewHolder {
        protected TextView equstion, answer;
        protected LinearLayout mlayout;
    }

    private class ListData {
        protected NoticeInfo info;

        ListData(NoticeInfo info) {
            this.info = info;
        }
    }
}