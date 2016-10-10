package panda.android.demo.view;

import panda.android.demo.filetest.FileFragment;
import panda.android.lib.base.ui.BaseActivity;
import panda.android.lib.commonapp.DeviceInfoFragment;
import panda.android.lib.net.TestListFragment;


public class MainActivity extends BaseActivity<FileFragment> {

    @Override
    public FileFragment initMainFragment() {
        return new FileFragment();
    }

}
