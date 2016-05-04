package panda.android.lib.base.ui.view;

import java.lang.Override;import panda.android.lib.base.ui.BaseActivity;
import panda.android.lib.base.ui.fragment.BaseFragment;

/**
 * Created by admin on 2016/4/24.
 */
public class ImageDetailActivity extends BaseActivity {

    @Override
    public BaseFragment initMainFragment() {
        return ImageDetailFragment.newInstance(getIntent().getStringExtra("url"),getIntent().getIntExtra("sex",0));

    }
}
