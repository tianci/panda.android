package panda.android.demo.view;

import panda.android.lib.base.ui.BaseActivity;
import panda.android.lib.base.ui.fragment.BaseFragment;

/**
 * Created by shandong on 2015/9/16.
 */
public class LaunchActivity extends BaseActivity {
    @Override
    public BaseFragment initMainFragment() {
        return new LaunchFragment();
    }
}
