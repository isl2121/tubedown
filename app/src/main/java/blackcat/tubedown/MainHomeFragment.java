package blackcat.tubedown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import blackcat.tubedown.service.InterfaceMapping;
import blackcat.tubedown.util.ApplicationController;
import blackcat.tubedown.util.SharedPreferenceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainHomeFragment extends Fragment {

    public MainHomeFragment() {
        InterfaceMapping.getInstance().setFragment(this);
    }

    final String strPref_Download_ID = "PREF_DOWNLOAD_ID";
    SharedPreferences mSharedPreference1;
    SharedPreferenceUtils sh;
    MainActivity activity;
    private WebView mWebView,mp3_web;
    public static String File_name;
    public String web_url, type, downloadUrl, Video_title, filepath, name, date;
    public static View view;
    public CustomDialog mCustomDialog;
    public ProgressBar progressDialog;
    public long id;
    public int data_type, downlist_cnt;
    public Boolean first_check;
    public static ImageView first_help;
    private AlertDialog mDialog = null;
    private Button mMp3_button, mAvi_button;
    Tracker t;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (MainActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sh = new SharedPreferenceUtils(getActivity());
        mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        view = inflater.inflate(blackcat.tubedown.R.layout.activity_main_home, null);
        progressDialog = (ProgressBar) view.findViewById(blackcat.tubedown.R.id.loading_bar);
        progressDialog.setVisibility(ProgressBar.GONE);
        first_check = sh.getValue("first", true);
        first_help = (ImageView) view.findViewById(blackcat.tubedown.R.id.first_help);
        if (first_check) {
            First_alram();
        } else {
            first_help.setVisibility(View.GONE);
        }

        downlist_cnt = sh.getValue("downlist_cnt", 0);
        mAvi_button = (Button) view.findViewById(R.id.mp4_button);
        mMp3_button = (Button) view.findViewById(R.id.m4a_button);
        mWebView = (WebView) view.findViewById(blackcat.tubedown.R.id.webview_youtube);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://www.youtube.com/");
        mWebView.setWebViewClient(new WishWebViewClient());
        AdView adView = (AdView) view.findViewById(R.id.adView_home);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
               Log.e("tubedown","권한 확인");
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(),  deniedPermissions.toString() + "\n 이 권한이 허가되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }


        };


        new TedPermission(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("만약에 권한을 해제하고 싶으시다면 \n\n 설정 -> 어플리케이션 -> [권한] 에 들어가셔서 해제할수있습니다.")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.GET_ACCOUNTS)
                .check();

        t = ((ApplicationController) getActivity().getApplication()).getTracker(ApplicationController.TrackerName.APP_TRACKER);
        t.setScreenName("Main_activity");
        t.send(new HitBuilders.AppViewBuilder().build());

        mAvi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.send(new HitBuilders.EventBuilder().setCategory("MainActivity").setAction("avi").setLabel("avi button Click").build());
                web_url = mWebView.getUrl();
               // Log.e("gwet", web_url);
                web_url = replace(web_url);
                if (!web_url.equals("false")) {
                    mAvi_button.setEnabled(false);
                    mMp3_button.setEnabled(false);
                    progressDialog.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "영상을 추출중입니다", Toast.LENGTH_SHORT).show();
                    getYoutubeDownurl(web_url, ".mp4");
                }
            }
        });

        mMp3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.send(new HitBuilders.EventBuilder().setCategory("MainActivity").setAction("mp3").setLabel("m4a button Click").build());
                web_url = mWebView.getUrl();
                web_url = replace(web_url);
                mAvi_button.setEnabled(false);
                mMp3_button.setEnabled(false);
                progressDialog.setVisibility(View.VISIBLE);
                if (!web_url.equals("false")) {
                    Toast.makeText(getActivity(), "음원을 추출중입니다", Toast.LENGTH_SHORT).show();
                    ///jsonmp3url(web_url);
                    getYoutubeDownurl(web_url, ".m4a");
                }
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebView.canGoBack()) {
                        mAvi_button.setEnabled(true);
                        mMp3_button.setEnabled(true);
                        progressDialog.setVisibility(View.GONE);
                        mWebView.goBack();
                    } else {
                        (getActivity()).onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
        return view;
    }


    private void First_alram() {
        mDialog = createDialog();
        mDialog.show();
    }

    private class WishWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void getYoutubeDownurl(String url, String temp) {
        Log.e("url", url);

        String youtubeLink = url;
       // String youtubeLink = "https://youtube.com/watch?v=asU010VqjSY";
        if (youtubeLink != null  && (youtubeLink.contains("://youtu.be/") || youtubeLink.contains("youtube.com/watch?v="))) {

            // We have a valid link

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "유튜브 링크에 문제를 확인했습니다. : " + youtubeLink, Toast.LENGTH_SHORT).show();
        }

        new YouTubeExtractor(this.getActivity()) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {

                if (ytFiles != null) {
                    int itag;
                    if (type.equals(".mp4")) {
                        try {
                            try {
                                itag = 22;
                                downloadUrl = ytFiles.get(itag).getUrl();
                            } catch (Exception e) {
                                itag = 18;
                                downloadUrl = ytFiles.get(itag).getUrl();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            try {
                                itag = 141;
                                downloadUrl = ytFiles.get(itag).getUrl();
                            } catch (Exception e) {
                                itag = 140;
                                downloadUrl = ytFiles.get(itag).getUrl();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        downloadUrl = URLDecoder.decode(downloadUrl, "UTF-8");
                        try{
                            Log.e("title",vMeta.getTitle());
                            if (vMeta.getTitle().length() > 55) {
                                File_name = vMeta.getTitle().substring(0, 55) + type;
                            } else {
                                File_name = vMeta.getTitle() + type;
                            }
                        } catch (NullPointerException e) {

                            long time = System.currentTimeMillis();

                            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

                            String str = dayTime.format(new Date(time));

                            File_name = "임시 제목" + str;
                            Toast.makeText(getActivity().getApplicationContext(), "유튜브 제목을 받아오는중 오류가 발생하여 이름을 변경합니다", Toast.LENGTH_SHORT).show();
                        }

                        Video_title = File_name.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");
                        mAvi_button.setEnabled(true);
                        mMp3_button.setEnabled(true);
                        progressDialog.setVisibility(View.GONE);
                        mCustomDialog = new CustomDialog(getActivity(), File_name, leftListener, rightListener);
                        mCustomDialog.show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        Toast.makeText(getActivity().getApplicationContext(), "동영상변환중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Log.e("here","here");
                }
            }
        }.extract(youtubeLink, true, true);

        type = temp;

    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle("저작권 알람");
        ab.setMessage("다운로드 받은 동영상은 영리 목적이 아닌 개인 목적 용도로만 사용해야하며, 가정 및 이에 준하는 한정된 범위안에서 이용해야합니다. 동영상 다운로드 및 다운로드 받은 동영상 사용에 대한 저작권 및 초상권 침해에 관한 책인은 개인에게 있습니다.");
        ab.setCancelable(false);
        ab.setNegativeButton("동의", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                sh.put("first", false);
            }
        });
        ab.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                getActivity().finish();
            }
        });
        return ab.create();
    }

    private String replace(String url) {
        String temp;
        if (url.contains("v=")) {

            if (url.contains("list")) {
                int start = url.indexOf("v=");
                if (url.contains("&mode=")) {
                    int end = url.indexOf("&mode=");
                    url = "https://www.youtube.com/watch?" + url.substring(start, end);
                } else {
                    url = "https://www.youtube.com/watch?" + url.substring(start);
                }
            }
        } else {
            mAvi_button.setEnabled(true);
            mMp3_button.setEnabled(true);
            progressDialog.setVisibility(View.GONE);
            Toast.makeText(getActivity().getApplicationContext(), "동영상을 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
            return "false";
        }
        if(url.contains("t=")){
            int start = url.indexOf("v=");
            if (url.contains("&mode=")) {
                int end = url.indexOf("&mode=");
                url = "https://www.youtube.com/watch?" + url.substring(start, end);
            } else {
                url = "https://www.youtube.com/watch?" + url.substring(start);
            }

        }
        url = url.replace("https://m.", "https://www.");
        url = url.replace("http://m.", "https://www.");
        Log.e("url", url);
        return url;
    }


    private View.OnClickListener leftListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downloadFromUrl(downloadUrl, Video_title, File_name);
            mCustomDialog.dismiss();
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCustomDialog.dismiss();
        }
    };

    private void downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName) {
        int temp = sh.getValue("downlist_cnt", 0) + 1;
        name = "downlist_num" + String.valueOf(temp);
        date = "downlist_date" + String.valueOf(temp);

        filepath = Environment.DIRECTORY_DOWNLOADS;
        data_type = sh.getValue("mWifi_Data", 2);

        sh.put(name, fileName);
        sh.put(date, NowTime());
        sh.put("downlist_cnt", temp);

        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);
        if (data_type == 0) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        } else if (data_type == 1) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        } else {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        }

        request.setAllowedOverRoaming(false);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(filepath, fileName);
        DownloadManager manager = (DownloadManager) getActivity().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

        id = manager.enqueue(request);
        //Save the request id   SharedPreferences.Editor PrefEdit = preferenceManager.edit();
        sh.put(strPref_Download_ID, id);

    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(getActivity().getApplication()).reportActivityStart(getActivity());
    }

    @Override
    public void onStop() {
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


    private String NowTime() {
        String time;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH시 mm분");
        String strCurDate = CurDateFormat.format(date);
        String strCurTime = CurTimeFormat.format(date);

        time = strCurDate + strCurTime;
        return time;
    }
}
