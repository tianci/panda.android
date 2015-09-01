package panda.android.lib.base.model.net;

import android.content.Context;

import com.litesuits.http.LiteHttpClient;
import com.litesuits.http.impl.apache.ApacheHttpClient;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.parser.FileParser;
import com.litesuits.http.request.Request;
import com.litesuits.http.request.content.JsonBody;
import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.litesuits.http.response.Response;

import java.io.File;

import panda.android.lib.base.util.Log;

/**
 * 网络连接基类，对于json格式数据的读取只需1行代码
 *
 * @author shitianci
 */
public class BaseRepositoryCollection {
    @SuppressWarnings("serial")
    public static class NetException extends NullPointerException {

        public NetException(String canNotAccessDateByNetWork) {
            super(canNotAccessDateByNetWork);
        }

    }

    private static final String CAN_NOT_ACCESS_DATE_BY_NET_WORK = "网络拥挤，请稍后再试";
    private static final String TAG = BaseRepositoryCollection.class.getSimpleName();

    public static <T> T accessNetwork(Context context, String url, HttpParam params, Class<T> result) throws NullPointerException {
        ApacheHttpClient client = (ApacheHttpClient) LiteHttpClient.newApacheHttpClient(context);
        Request request = new Request(url);
        request.setMethod(HttpMethod.Post).setHttpBody(new JsonBody(params));
        Response res = client.execute(request);
        Log.d(TAG, "accessNetwork, " + res.toString());
        T netResult = res.getObject(result);
        if (netResult == null) {
            NullPointerException nullPointerException = new NetException(CAN_NOT_ACCESS_DATE_BY_NET_WORK);
            throw nullPointerException;
        }
        return netResult;
    }

    /**
     * 下载文件
     * TODO 支持断点续传
     *
     * @param context
     * @param url
     * @param localStorePath
     * @param httpListener
     * @return
     * @throws NullPointerException
     */
    public static File downloadFile(Context context, String url, String localStorePath, HttpListener httpListener) throws NullPointerException {
        ApacheHttpClient client = (ApacheHttpClient) LiteHttpClient.newApacheHttpClient(context);
        FileParser dataParser = new FileParser(localStorePath);
        Request request = new Request(url);
        request.setDataParser(dataParser).setMethod(HttpMethod.Get).setHttpListener(httpListener);
        client.execute(request);
        File netResult = dataParser.getData();
        if (netResult == null || !netResult.exists()) {
            Log.d(TAG, "netResult = " + netResult);
            Log.d(TAG, "netResult.exists() = " + netResult.exists());
            NullPointerException nullPointerException = new NetException(CAN_NOT_ACCESS_DATE_BY_NET_WORK);
            throw nullPointerException;
        }
        return netResult;
    }

}
