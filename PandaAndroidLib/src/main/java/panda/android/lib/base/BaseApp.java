package panda.android.lib.base;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;
import com.umeng.analytics.MobclickAgent;

import java.util.LinkedList;
import java.util.List;

import panda.android.lib.base.model.net.BaseRepositoryCollection;


/**
 * 基础的Application
 * 
 * @author shitianci
 * 
 */
public class BaseApp extends Application {

	private List<Activity> mList = new LinkedList<>();
	private static BaseApp instance;

    /**
     * 获取网络访问器
     * @return
     */
    public LiteHttp getLiteHttp() {
        return liteHttp;
    }

    private LiteHttp liteHttp;

    public static BaseApp getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// BaseRepositoryCollection.initClientCCG(getApplicationContext(),
		// null);
        HttpConfig config = new HttpConfig(this) // configuration quickly
                .setDebugged(true)                   // log output when debugged
                .setDetectNetwork(true)              // detect network before connect
                .setDoStatistics(true)               // statistics of time and traffic
                .setUserAgent("Mozilla/5.0 (...)")   // set custom User-Agent
                .setTimeOut(10000, 10000);             // connect and socket timeout: 10s
        liteHttp = BaseRepositoryCollection.initLiteHttp(config);

        MobclickAgent.openActivityDurationTrack(false);

//		MobclickAgent.setDebugMode(true);
//		MobclickAgent.reportError(getApplicationContext(), "上传测试日志");
//		String test = null;
//		test.toString();
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	/**
	 * 退出整个应用
	 */
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

	/**
	 * 获取系统默认文件夹
	 * 
	 * @return 系统文件加的名称
	 */
	public String getAppDir() {
		return Environment.getExternalStorageDirectory().getPath()+ "/" + getPackageName() + "/";
	}
}
