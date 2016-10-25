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
import com.litesuits.http.request.content.HttpBody;
import com.litesuits.http.request.content.JsonBody;
import com.litesuits.http.request.content.multi.FilePart;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.content.multi.StringPart;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.request.param.HttpParamModel;
import com.litesuits.http.response.Response;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

import de.greenrobot.event.EventBus;
import panda.android.lib.R;
import panda.android.lib.base.control.NetResultInfoEvent;
import panda.android.lib.base.model.BaseModel;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.FileUtil;
import panda.android.lib.base.util.Log;

/**
 * Created by shitianci on 16/4/20.
 * <p/>
 * BaseRepositoryCollection 代码规范：
 * 1. 所有的方法为静态方法 ；
 * 2. 所有的方法名为  execute{返回参数类型}Request ；（返回参数类型目前有 Json、File）
 * <p/>
 * todo: 所有名字不合法的方法都需要被标注为 Deprecated，或者私有化
 * <p/>
 * 网络基础知识：
 * 上传有
 * →8种操作：Get,Head,Trace,Options,Delete,Put,Post,Patch;
 * →4类传值的方法：拼接在url后面（get专用）；form-data形式；x-www-form-urlencoded形式；raw形式（包括json、text、xml、html）
 * <p/>
 * [litehttp2.x版本系列教程](https://zybuluo.com/liter/note/186533)
 * <p/>
 * ************* MultipartBody代码样例 **************
 * MultipartBody body = new MultipartBody();
 * body.addPart(new StringPart("key1", "hello"));
 * body.addPart(new StringPart("key2", "很高兴见到你", "utf-8", null));
 * body.addPart(new BytesPart("key3", new byte[]{1, 2, 3}));
 * body.addPart(new FilePart("pic", new File("/sdcard/aaa.jpg"), "image/jpeg"));
 * body.addPart(new InputStreamPart("litehttp", fis, "litehttp.txt", "text/plain"));
 * postRequest.setHttpBody(body);
 * <p/>
 * 或者------------------
 * <p/>
 * MultipartBody body = new MultipartBody();
 * if(httpparams != null) {
 * Map maps = ClassUtils.setObjParamsToMap(httpparams);
 * Iterator i$ = maps.keySet().iterator();
 * <p/>
 * while(i$.hasNext()) {
 * String key = (String)i$.next();
 * body.addPart(new StringPart(key, maps.get(key).toString()));
 * }
 * }
 */
public class BaseRepositoryCollection {
    private static final String TAG = BaseRepositoryCollection.class.getSimpleName();

    public static LiteHttp getLiteHttp() {
        return mLiteHttp;
    }

    private static LiteHttp mLiteHttp;

    /**
     * try to detect the network
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     */
    public static boolean tryToDetectNetwork(Context context) {
        Network.NetType type = null;
        type = Network.getConnectedType(context);
        if (type == Network.NetType.None) {
            EventBus.getDefault().post(new NetResultInfoEvent(null));
            return false;
        }
        return true;
    }

    public static LiteHttp initLiteHttp(HttpConfig config) {
        mLiteHttp = LiteHttp.newApacheHttpClient(config);
        return mLiteHttp;
    }

    /**
     * 针对上传json数据、获取json数据的情形
     *
     * @param url        网址
     * @param httpparams 上传参数
     * @param method     方法
     * @param resultType 返回参数类型
     * @param <T>        返回参数类型
     * @return
     */
    public static <T> T executeJsonRequest(String url, HttpParamModel httpparams, HttpMethods method, Type resultType) {
        Log.d(TAG, "httpparams = " + BaseModel.getGson().toJson(httpparams));
        if (method == HttpMethods.Get) {
            url = getUrlWithParams(url, httpparams);
        }
        return executeJsonRequest(url, new JsonBody(httpparams), method, resultType);
    }

    /**
     * 针对上传各种数据（比如MultipartBody）、获取json数据的情形
     *
     * @param url        网址
     * @param body       上传参数
     * @param method     方法
     * @param resultType 返回参数类型
     * @param <T>        返回参数类型
     * @return
     */
    public static <T> T executeJsonRequest(String url, HttpBody body, HttpMethods method, Type resultType) {
        Log.d(TAG, "body = " + body);
        JsonRequest request = new JsonRequest(url, resultType);
        request.setMethod(method);
        request.setHttpBody(body);
        return executeBaseRequest(request);
    }

    /**
     * 针对上传json数据、获取文件数据的情形
     *
     * @param url        网址
     * @param httpparams 上传参数
     * @param method     方法
     * @param <T>        返回参数类型
     * @return
     */
    public static <T> T executeFileRequest(String url, HttpParamModel httpparams, HttpMethods method) {
        Log.d(TAG, "httpparams = " + BaseModel.getGson().toJson(httpparams));
        if (method == HttpMethods.Get) {
            url = getUrlWithParams(url, httpparams);
        }
        return executeFileRequest(url, new JsonBody(httpparams), method);
    }


    /**
     * 针对上传各种数据（比如MultipartBody）、获取json数据的情形
     *
     * @param url    网址
     * @param body   上传参数
     * @param method 方法
     * @param <T>    返回参数类型
     * @return
     */
    public static <T> T executeFileRequest(String url, HttpBody body, HttpMethods method) {
        Log.d(TAG, "body = " + BaseModel.getGson().toJson(body));
        FileRequest request = new FileRequest(url);
        request.setMethod(method);
        request.setHttpBody(body);
        return executeBaseRequest(request);
    }

    /**
     * 获取带参数的url
     *
     * @param url
     * @param httpparams
     * @return
     */
    private static String getUrlWithParams(String url, HttpParamModel httpparams) {
        if (httpparams != null) {
            url = url + "?" + ClassUtils.obj2PostParams(httpparams);
        }
        return url;
    }

    /**
     * 最基础的网络访问方法，所有的网络访问都必须调用此方法。
     *
     * @param request
     * @param <T>
     * @return
     */
    public static <T> T executeBaseRequest(AbstractRequest request) {
        Object netResult = null;
        try {
            Response res = mLiteHttp.execute(request);
            Log.d(TAG, "accessNetwork, " + res.toString());
            netResult = res.getResult();
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        if (netResult == null || netResult instanceof NetResultInfo) {
            EventBus.getDefault().post(new NetResultInfoEvent((NetResultInfo) netResult));
        }
        return (T) netResult;
    }

    @Deprecated
    public static class HttpListener<T> extends com.litesuits.http.listener.HttpListener<T> {

        private final Context mContext;
        private Dialog mLoadingDlg;

        public HttpListener(Context context, Dialog loadingDlg) {
            super(true, false, false);
            mContext = context;
            mLoadingDlg = loadingDlg;
        }

        public HttpListener(Context context, Dialog loadingDlg, boolean runOnUiThread, boolean readingNotify, boolean uploadingNotify) {
            super(runOnUiThread, readingNotify, uploadingNotify);
            mContext = context;
            mLoadingDlg = loadingDlg;
        }

        @Override
        public void onStart(AbstractRequest<T> request) {
            super.onStart(request);
            if (mLoadingDlg != null) {
                mLoadingDlg.show();
            }
        }

        @Override
        public void onSuccess(T data, Response<T> response) {
            super.onSuccess(data, response);
            Log.d(TAG, "response = " + response);
            if (mLoadingDlg != null) {
                mLoadingDlg.dismiss();
            }
        }

        @Override
        public void onCancel(T data, Response<T> response) {
            super.onCancel(data, response);
            if (mLoadingDlg != null) {
                mLoadingDlg.dismiss();
            }
        }

        @Override
        public void onFailure(HttpException e, Response<T> response) {
            super.onFailure(e, response);
            if (mLoadingDlg != null) {
                mLoadingDlg.dismiss();
            }
            DevUtil.showInfo(mContext, mContext.getString(R.string.lib_download_failure));
        }
    }

    /**
     * 上传文件到服务端（在非UI线程执行）
     *
     * @param fileList
     */
    @Deprecated
    public static <T> T executeRequest(String url, String[] fileList, HttpMethods method, Type resultType) {
        MultipartBody body = new MultipartBody();
        for (String file : fileList) {
            Log.d(TAG, "executeRequest, file = " + file);
            body.addPart(new FilePart("files", new File(file), FileUtil.getMimeType(file)));
        }
        return executeRequest(url, body, method, resultType);
    }

    /**
     * 将参数拼接到url上，传输参数到服务端（在非UI线程执行）
     */
    @Deprecated
    public static <T> T executeGetRequest(String url, HttpParamModel httpparams, Type resultType) {
        if (httpparams != null) {
//            request.setHttpBody(new JsonBody(httpparams));
            url = url + "?" + ClassUtils.obj2PostParams(httpparams);
        }
        return executeRequest(url, (HttpBody) null, HttpMethods.Get, resultType);
    }

    /**
     * 将参数按照form形式组织，传输参数到服务端（在非UI线程执行）
     */
    @Deprecated
    public static <T> T executeRequest(String url, HttpParamModel httpparams, HttpMethods method, Type resultType) {
        try {
            Log.d(TAG, "httpparams = " + BaseModel.getGson().toJson(httpparams));
        } catch (Exception e) {

        }
        MultipartBody body = new MultipartBody();
        if (httpparams != null) {
//            request.setHttpBody(new JsonBody(httpparams));
//            url = url + "?" + ClassUtils.obj2PostParams(httpparams);
            Map<String, Object> maps = ClassUtils.setObjParamsToMap(httpparams);
            for (String key : maps.keySet()) {
                body.addPart(new StringPart(key, maps.get(key).toString()));
            }
        }
        return executeRequest(url, (HttpBody) body, method, resultType);
    }

    @Deprecated
    public static <T> T executeRequest(String url, HttpBody httpBody, HttpMethods method, Type resultType) {
        Log.d(TAG, "url = " + url);
        if (mLiteHttp == null) {
            Log.e(TAG, "mLiteHttp == null");
            return null;
        }
        AbstractRequest<T> request = new JsonRequest(url, resultType);
        request.setMethod(method);
        Log.d(TAG, "executeRequest, " + request.getMethod());
        if (httpBody != null) {
            request.setHttpBody(httpBody);
        }
        Response res = mLiteHttp.execute(request);
        Log.d(TAG, "accessNetwork, " + res.toString());
        T netResult = (T) res.getResult();
//        if (netResult == null) {
//            NullPointerException nullPointerException = new NetException(NET_ERR_TIPS);
//            throw nullPointerException;
//        }
        return netResult;
    }


    /**
     * 传输json参数到服务端（在UI线程执行）
     *
     * @param httpparams 输入参数（implements HttpParamModel）
     * @param resultType 输出参数的类型（extends NetResultInfo）
     * @param listener   监听器
     */
    @Deprecated
    public static <T> void executeJsonRequestAsync(HttpParamModel httpparams, Type resultType, HttpListener<T> listener) {
        AbstractRequest<T> request = new JsonRequest(httpparams, resultType);
        request.addHeader("Content-Type", "application/json");
        request.setHttpBody(new JsonBody(httpparams));
        request.setHttpListener(listener);
        if (mLiteHttp == null) {
            Log.e(TAG, "mLiteHttp == null");
            return;
        }
        mLiteHttp.executeAsync(request);
    }

    /**
     * 下载文件（在UI线程执行）
     *
     * @param url      网络地址
     * @param path     输出参数的类型（extends NetResultInfo）
     * @param listener 监听器
     */
    @Deprecated
    public static void executeFileRequestAsync(String url, String path, HttpListener<File> listener) {
        FileRequest request = new FileRequest(url);
        request.setDataParser(new FileParser(new File(path)));
        request.setHttpListener(listener);
        if (mLiteHttp == null) {
            Log.e(TAG, "mLiteHttp == null");
            return;
        }
        mLiteHttp.executeAsync(request);
    }
}
