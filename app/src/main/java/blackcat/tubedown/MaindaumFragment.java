package blackcat.tubedown;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import blackcat.tubedown.service.DownInfo;
import blackcat.tubedown.service.InterfaceMapping;
import blackcat.tubedown.util.ApplicationController;
import blackcat.tubedown.util.SharedPreferenceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaindaumFragment extends Fragment {

    public MaindaumFragment() {
        InterfaceMapping.getInstance().setFragment(this);
    }

    final String strPref_Download_ID = "PREF_DOWNLOAD_ID";

    MainActivity activity;
    public String web_url, type, downloadUrl, Video_title;
    public static View view;
    public long id;
    public String down_name, down_date, down_path;
    private ArrayList<ListData> data = new ArrayList<ListData>();
    private ListView mListView;
    private ListAdapter mAdapter;
    SharedPreferences mSharedPreference1;
    SharedPreferenceUtils sh;

    Tracker t;
    @Override
    public void onAttach(Activity activity) {
        this.activity = (MainActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(blackcat.tubedown.R.layout.activity_main_daum, null);
        t = ((ApplicationController)getActivity().getApplication()).getTracker(ApplicationController.TrackerName.APP_TRACKER);

        t.setScreenName("Log_activity");
        t.send(new HitBuilders.AppViewBuilder().build());
        sh = new SharedPreferenceUtils(getActivity());
        mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initWidget(view);
        return view;
    }

    private void initWidget(View view) {

        AdView adView = (AdView) view.findViewById(R.id.adView_daum);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mListView = (ListView) view.findViewById(R.id.download_list);
        mAdapter = new ListAdapter(getActivity(), data);
        mListView.setAdapter(mAdapter);
        mListView.setItemsCanFocus(false);
        DownInfo downInfo = null;
        int size = sh.getValue("downlist_cnt", 0);
        Log.e("downlist", String.valueOf(size));

        if (size != 0) {
            Log.e("size",String.valueOf(size));
            for (int i = 0; i < size; size--) {
                downInfo = new DownInfo();
                down_name = "downlist_num" + String.valueOf(size);
                down_date = "downlist_date" + String.valueOf(size);
                down_name = sh.getValue(down_name, "다운받은 영상이없습니다.");
                down_date = sh.getValue(down_date, "");
                downInfo.mDownname = down_name;
                downInfo.mDowndate = down_date;
                data.add(new ListData(downInfo));
            }
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
                convertView = inflater.inflate(R.layout.downlist_item, parent, false);
                vh.name = (TextView) convertView.findViewById(R.id.download_name);
                vh.date = (TextView) convertView.findViewById(R.id.download_date);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.name.setText(mData.get(position).info.mDownname);
            vh.date.setText(mData.get(position).info.mDowndate);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    private class ViewHolder {
        protected TextView name, date;
    }

    private class ListData {
        protected DownInfo info;

        ListData(DownInfo info) {
            this.info = info;
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(getActivity().getApplication()).reportActivityStart(getActivity());
    }
    @Override
    public void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(getActivity().getApplication()).reportActivityStart(getActivity());
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
