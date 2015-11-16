package blackcat.tubedown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import blackcat.tubedown.service.InterfaceMapping;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaindaumFragment extends Fragment {

    public MaindaumFragment() {
        InterfaceMapping.getInstance().setFragment(this);
    }

    final String strPref_Download_ID = "PREF_DOWNLOAD_ID";

    SharedPreferences preferenceManager;
    DownloadManager downloadManager;
    MainActivity activity;
    private WebView mWebView;
    public static String File_name;
    public String web_url, type, downloadUrl, Video_title;
    public static View view;
    public CustomDialog mCustomDialog;
    public ProgressBar progressDialog;
    public SharedPreferences.Editor PrefEdit;
    public long id;
    public Boolean first_check;
    public static ImageView first_help;
    private AlertDialog mDialog = null;
    private Button mMp3_button, mAvi_button;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (MainActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(blackcat.tubedown.R.layout.activity_main_daum, null);
        progressDialog = (ProgressBar) view.findViewById(blackcat.tubedown.R.id.daum_loading_bar);
        progressDialog.setVisibility(ProgressBar.GONE);
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PrefEdit = preferenceManager.edit();

        mAvi_button = (Button) view.findViewById(R.id.mp4_daum_button);
        mMp3_button = (Button) view.findViewById(R.id.m4a_daum_button);
        mWebView = (WebView) view.findViewById(blackcat.tubedown.R.id.webview_daum);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://tvpot.daum.net/");
        mWebView.setWebViewClient(new WishWebViewClient());
        AdView adView = (AdView) view.findViewById(R.id.adView_daum_home);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mAvi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web_url = mWebView.getUrl();
                web_url = "http://tvpot.daum.net/mypot/View.do?clipid=7236439";
                String extension = MimeTypeMap.getFileExtensionFromUrl(web_url);
                if (extension != null) {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String mimeType = mime.getMimeTypeFromExtension(extension);
                    if (mimeType != null) {
                        if (mimeType.toLowerCase().contains("video")){
                            DownloadManager mdDownloadManager = (DownloadManager) getActivity()
                                    .getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(
                                    Uri.parse(web_url));
                            File destinationFile = new File(
                                    Environment.getExternalStorageDirectory(),
                                    getFileName(web_url));
                            request.setDescription("Downloading via Your app name..");
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationUri(Uri.fromFile(destinationFile));
                            mdDownloadManager.enqueue(request);
                        }
                    }
                }
            }
        });

        mMp3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web_url = mWebView.getUrl();
                web_url = replace(web_url);
                if (!web_url.equals("false")) {
                    mAvi_button.setEnabled(false);
                    mMp3_button.setEnabled(false);
                    progressDialog.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "음원을 추출중입니다", Toast.LENGTH_SHORT).show();
                    getYoutubeDownurl(web_url, ".m4a");
                }
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //This is the filter
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

    private class WishWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    public String getFileName(String url) {
        String filenameWithoutExtension = "";
        filenameWithoutExtension = String.valueOf(System.currentTimeMillis()
                + ".mp4");
        return filenameWithoutExtension;
    }
    private void getYoutubeDownurl(String url, String temp) {
        type = temp;
        YouTubeUriExtractor ytEx = new YouTubeUriExtractor(getActivity().getApplicationContext()) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles == null) {
                    return;
                }
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
                        Log.e("url", downloadUrl);
                    } catch (Exception e) {

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
                        Log.e("url", downloadUrl);
                    } catch (Exception e) {

                    }
                }
                try {
                    downloadUrl = URLDecoder.decode(downloadUrl, "UTF-8");
                    if (videoTitle.length() > 55) {
                        File_name = videoTitle.substring(0, 55) + type;
                    } else {
                        File_name = videoTitle + type;
                    }
                    File_name = File_name.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");
                    Video_title = videoTitle.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");
                    mAvi_button.setEnabled(true);
                    mMp3_button.setEnabled(true);
                    progressDialog.setVisibility(View.GONE);


                    mCustomDialog = new CustomDialog(getActivity(), videoTitle + type, leftListener, rightListener);
                    mCustomDialog.show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "동영상변환중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
        ytEx.setIncludeWebM(false);
        ytEx.setParseDashManifest(true);
        ytEx.execute(url);
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

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            CheckDwnloadStatus();
        }
    };

    private AlertDialog createDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle("저작권 알람");
        ab.setMessage("다운로드 받은 동영상은 영리 목적이 아닌 개인 목적 용도로만 사용해야하며, 가정 및 이에 준하는 한정된 범위안에서 이용해야합니다. 동영상 다운로드 및 다운로드 받은 동영상 사용에 대한 저작권 및 초상권 침해에 관한 책인은 개인에게 있습니다.");
        ab.setCancelable(false);
        ab.setNegativeButton("동의", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                PrefEdit.putBoolean("first", false);
                PrefEdit.commit();
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
        temp = url.replace("http://m.", "https://www.");

        return temp;
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

        preferenceManager = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String temp = preferenceManager.getString("get_file_path", Environment.DIRECTORY_DOWNLOADS);
        int temp2 = preferenceManager.getInt("mWifi_Data", 2);


        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);
        if (temp2 == 0) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        } else if (temp2 == 1) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        } else {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        }

        request.setAllowedOverRoaming(false);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(temp, fileName);
        Log.i("temp", temp);
        DownloadManager manager = (DownloadManager) getActivity().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

        id = manager.enqueue(request);
        //Save the request id   SharedPreferences.Editor PrefEdit = preferenceManager.edit();
        PrefEdit.putLong(strPref_Download_ID, id);
        PrefEdit.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void CheckDwnloadStatus() {

        // TODO Auto-generated method stub
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = cursor.getInt(columnReason);

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    String failedReason = "";
                    switch (reason) {
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
                    Toast.makeText(getActivity(),
                            "실패: " + failedReason,
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PAUSED:
                    String pausedReason = "";
                    switch (reason) {
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

                    Toast.makeText(getActivity(),
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
