package panda.android.lib.base.configuration;

import android.content.Context;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.data.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import panda.android.lib.base.model.net.BaseRepositoryCollection;

/**
 * Created by shitianci on 15/12/5.
 */
public class LiteHttpConfiguration {

    private static LiteHttp liteHttp;

    /**
     * 配置LiteHttp
     * @param context 上下文
     */
    public static void configure(Context context) {
        List<NameValuePair> commonHeaders = new ArrayList<>();
//        NameValuePair pair1 = new NameValuePair("Content-type","text/html");
//        commonHeaders.add(pair1);
        NameValuePair pair2 = new NameValuePair("charset","utf-8");
        commonHeaders.add(pair2);
        HttpConfig config = new HttpConfig(context) // configuration quickly
                .setDebugged(true)                   // log output when debugged
                .setDetectNetwork(true)              // detect network before connect
                .setDoStatistics(true)               // statistics of time and traffic
//                .setUserAgent("Mozilla/5.0 (...)")   // set custom User-Agent
                .setCommonHeaders(commonHeaders)
                .setTimeOut(10000, 10000);             // connect and socket timeout: 10s
        liteHttp = BaseRepositoryCollection.initLiteHttp(config);
    }

    /**
     * 获取网络访问器
     * @return
     */
    public  static LiteHttp getLiteHttp() {
        return liteHttp;
    }
}
