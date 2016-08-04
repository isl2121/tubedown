package blackcat.tubedown.service;

/**
 * Created by sgsgf on 2015-11-27.
 * package kr.co.dotname.battery.activity.optm;
 * <p/>
 * /**
 * Created by jin on 2015-03-10.
 */

import android.content.pm.ApplicationInfo;

public class DownInfo {

    public static interface AppFilter {
        public void init();

        public boolean filterApp(ApplicationInfo info);
    }

    public static interface System_Filter {
        public void init2();

        public boolean filterApp2(ApplicationInfo info);
    }

    // 어플리케이션 이름
    public String mDownname = null;
    public String mDowndate = null;
    /**
     * 서드파티 필터
     */
    public static final AppFilter THIRD_PARTY_FILTER = new AppFilter() {
        public void init() {
        }

        @Override
        public boolean filterApp(ApplicationInfo info) {
            if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                return true;
            } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                return true;
            }
            return false;
        }
    };
}
