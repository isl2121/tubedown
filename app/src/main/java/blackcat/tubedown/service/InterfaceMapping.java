package blackcat.tubedown.service;

import blackcat.tubedown.MainHomeFragment;
import blackcat.tubedown.SettingFragment;

/**
 * Created by Petronela on 4/11/2015.
 */
public class InterfaceMapping {
    private static InterfaceMapping instance;
    private MainHomeFragment fragment;
    private SettingFragment se_fragment;

    private InterfaceMapping() {

    }

    public static InterfaceMapping getInstance() {
        if (instance == null)
            instance = new InterfaceMapping();
        return instance;
    }

    public void setFragment(MainHomeFragment fragment) {
        this.fragment = fragment;
    }
    public void setFragment(SettingFragment se_fragment) {
        this.se_fragment = se_fragment;
    }
    public MainHomeFragment getFragment() {
        return fragment;
    }

}
