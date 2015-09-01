package panda.android.demo;

import panda.android.lib.base.ui.BaseActivity;


public class MainActivity extends BaseActivity<MainFragment> {

    @Override
    public MainFragment initMainFragment() {
        return new MainFragment();
    }

}
