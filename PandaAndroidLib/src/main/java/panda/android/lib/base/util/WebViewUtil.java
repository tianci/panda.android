package panda.android.lib.base.util;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 *
 * 参考资料：[简单的WebView自适应并嵌套在ScrollView里](http://blog.csdn.net/janronehoo/article/details/45100447)
 * Created by shitianci on 16/3/16.
 */
public class WebViewUtil {

    public static void setBodyHtml(WebView webView, String body) {
//        webView.setWebViewClient(new SimpleWebViewClient(title));
        config(webView);
        webView.loadData(getHtmlData(body), "text/html; charset=utf-8", "utf-8");
    }

    public static void config(WebView webView) {
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    }

    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
