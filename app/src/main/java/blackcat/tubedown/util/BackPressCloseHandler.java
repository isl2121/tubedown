package blackcat.tubedown.util;
import android.app.Activity;
import android.widget.Toast;

/**
 * Created by jin on 2015-03-12.
 * 메인에서 뒤로가기 두번 누를시 나가는 역활
 */
public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,activity.getResources().getString(blackcat.tubedown.R.string.Backpress), Toast.LENGTH_SHORT);
        toast.show();
    }
}
