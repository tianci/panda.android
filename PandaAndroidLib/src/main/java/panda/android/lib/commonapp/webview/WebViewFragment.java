package panda.android.lib.commonapp.webview;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import panda.android.lib.R;
import panda.android.lib.R2;
import panda.android.lib.base.ui.UIUtil;
import panda.android.lib.base.ui.fragment.BaseFragment;

;


/**
 * Created by admin on 2016/8/16.
 */
public class WebViewFragment extends BaseFragment {
    private static final String TAG = WebViewFragment.class.getSimpleName();
    @BindView(R2.id.btn_goback)
    ImageView navGoBack;
    @BindView(R2.id.tv_title)
    TextView navTvTitle;

    @BindView(R2.id.web_view)
    WebView webView;
    private WebViewModel model;

    private Dialog loadingDialog; //加载框的

    @Override
    public int getLayoutId() {
        return R.layout.fragment_webview;
    }

    public static WebViewFragment newInstance(WebViewModel model) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(model);
        Log.d(TAG, "model = " + model);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        model = getArguments(WebViewModel.class);
        loadingDialog = UIUtil.getLoadingDlg(getActivity(), false);

        initWebView();
        return rootView;
    }

    private void initWebView() {
        Log.d(TAG, "initWebView: " + model.toString());
        navTvTitle.setText(model.getTitle());
        loadingDialog.show();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (model.getType() == WebViewModel.TYPE_URL) {
            webView.loadUrl(model.getData());

        } else if (model.getType() == WebViewModel.TYPE_H5) {
            webView.loadDataWithBaseURL(null, model.getData(), "text/html", "utf-8", null);
            loadingDialog.dismiss();
        }


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
                if (loadingDialog != null || loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (loadingDialog != null || loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }

        });

    }

    /**
     * 设置Dialog加载框
     * @param loadingDialog
     */
    public void setLoadingDialog(Dialog loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    @OnClick(R2.id.btn_goback)
    @Override
    public void exit() {
        super.exit();
    }


}
