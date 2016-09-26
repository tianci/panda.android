package panda.android.demo.view;

import panda.android.lib.base.ui.BaseActivity;
import panda.android.lib.commonapp.DeviceInfoFragment;
import panda.android.lib.net.TestListFragment;


public class MainActivity extends BaseActivity<TestListFragment> {

    @Override
    public TestListFragment initMainFragment() {
        return new TestListFragment();
    }

}
