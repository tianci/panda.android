package panda.android.demo.filetest;

import panda.android.lib.base.ui.BaseActivity;

/**
 * Created by admin on 2016/10/8.
 */

public class SearchActivity extends BaseActivity<SearchFragment> {
    @Override
    public SearchFragment initMainFragment() {
        return new SearchFragment();
    }
}
