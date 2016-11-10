package panda.android.lib.commonapp.webview;

import panda.android.lib.base.ui.fragment.BaseActivityWithExtrasData;

/**
 * Created by yyl on 2016/8/16.
 */
public class WebViewActivity extends BaseActivityWithExtrasData<WebViewFragment, WebViewModel> {
    @Override
    public WebViewFragment initMainFragment() {
        WebViewModel model = getExtrasData(WebViewModel.class);
        return WebViewFragment.newInstance(model);
    }
}
