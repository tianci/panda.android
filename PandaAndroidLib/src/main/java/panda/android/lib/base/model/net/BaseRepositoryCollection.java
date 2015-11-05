package panda.android.lib.base.model.net;

import android.app.Dialog;
import android.content.Context;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.network.Network;
import com.litesuits.http.parser.impl.FileParser;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.FileRequest;
import com.litesuits.http.request.JsonRequest;
import com.litesuits.http.request.content.JsonBody;
import com.litesuits.http.request.param.HttpParamModel;
import com.litesuits.http.response.Response;

import java.io.File;
import java.lang.reflect.Type;

import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.Log;

/**
 * 网络连接基类，对于json格式数据的读取只需1行代码
 *
 * @author shitianci
 */
public class BaseRepositoryCollection {
    private static final String TAG = BaseRepositoryCollection.class.getSimpleName();
    public static final String NET_ERR_TIPS = "网络连接失败，请稍后重试";

    public static LiteHttp getLiteHttp() {
        return mLiteHttp;
    }

    private static LiteHttp mLiteHttp;

    /**
     * try to detect the network
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     */
    public static boolean tryToDetectNetwork(Context context){
        Network.NetType type = null;
        type = Network.getConnectedType(context);
        if (type == Network.NetType.None) {
            DevUtil.showInfo(context, NET_ERR_TIPS);
            return false;
        }
        return true;
    }

    public static LiteHttp initLiteHttp(HttpConfig config) {
        mLiteHttp = LiteHttp.newApacheHttpClient(config);
        return mLiteHttp;
    }

    public static class HttpListener<T> extends com.litesuits.http.listener.HttpListener<T> {

        private final Context mContext;
        private Dialog mLoadingDlg;

        public HttpListener(Context context, Dialog loadingDlg){
            super(true, false, false);
            mContext = context;
            mLoadingDlg = loadingDlg;
        }

        public HttpListener(Context context, Dialog loadingDlg, boolean runOnUiThread, boolean readingNotify, boolean uploadingNotify){
            super(runOnUiThread, readingNotify, uploadingNotify);
            mContext = context;
            mLoadingDlg = loadingDlg;
        }

        @Override
        public void onStart(AbstractRequest<T> request) {
            super.onStart(request);
            if (mLoadingDlg != null){
                mLoadingDlg.show();
            }
        }

        @Override
        public void onSuccess(T data, Response<T> response) {
            super.onSuccess(data, response);
            Log.d(TAG, "response = " + response);
            if (mLoadingDlg != null){
                mLoadingDlg.dismiss();
            }
        }

        @Override
        public void onCancel(T data, Response<T> response) {
            super.onCancel(data, response);
            if (mLoadingDlg != null){
                mLoadingDlg.dismiss();
            }
        }

        @Override
        public void onFailure(HttpException e, Response<T> response) {
            super.onFailure(e, response);
            if (mLoadingDlg != null){
                mLoadingDlg.dismiss();
            }
            DevUtil.showInfo(mContext, "onFailure");
        }
    }


    /**
     * 传输json参数到服务端（在UI线程执行）
     * @param httpparams 输入参数（implements HttpParamModel）
     * @param resultType 输出参数的类型（extends NetResultInfo）
     * @param listener 监听器
     */
    public static <T> void executeJsonRequestAsync(HttpParamModel httpparams, Type resultType, HttpListener<T> listener){
        AbstractRequest<T> request = new JsonRequest(httpparams, resultType);
        request.addHeader("Content-Type", "application/json");
        request.setHttpBody(new JsonBody(httpparams));
        request.setHttpListener(listener);
        if (mLiteHttp == null){
            Log.e(TAG, "mLiteHttp == null");
            return;
        }
        mLiteHttp.executeAsync(request);
    }

    /**
     * 下载文件（在UI线程执行）
     * @param url 网络地址
     * @param path 输出参数的类型（extends NetResultInfo）
     * @param listener 监听器
     */
    public static void executeFileRequestAsync(String url, String path, HttpListener<File> listener){
        FileRequest request = new FileRequest(url);
        request.setDataParser(new FileParser(new File(path)));
        request.setHttpListener(listener);
        if (mLiteHttp == null){
            Log.e(TAG, "mLiteHttp == null");
            return;
        }
        mLiteHttp.executeAsync(request);
    }
}
