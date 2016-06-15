package panda.android.demo.view;

import panda.android.lib.base.ui.BaseActivity;
import panda.android.lib.commonapp.DeviceInfoFragment;


public class MainActivity extends BaseActivity<MainFragment> {

    @Override
    public MainFragment initMainFragment() {
        return new MainFragment();
    }

}
