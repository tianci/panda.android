package panda.android.lib.base;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import panda.android.lib.base.model.net.BaseRepositoryCollection;
import panda.android.lib.base.util.Log;


/**
 * 基础的Application
 * 
 * @author shitianci
 * 
 */
public class BaseApp extends Application {
    private static final String TAG = BaseApp.class.getSimpleName();
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

        /**
         * 打开调试模式，可是实时看到后台日志
         */
//		MobclickAgent.setDebugMode(true);
        /**
         * 主动出发崩溃日志上传，看到后台日志是否正常
         */
//		MobclickAgent.reportError(getApplicationContext(), "上传测试日志");
//		String test = null;
//		test.toString();

        initAppDirs();
	}

    private void initAppDirs() {
        File cacheDir = new File(getAppDir());
//        try {
//            cacheDir.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (cacheDir.exists() && !cacheDir.isDirectory()){
            //删除同名文件
            Log.d(TAG,  cacheDir.getAbsolutePath() + "is not directory");
            cacheDir.delete();
        }
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
        }
        Log.d(TAG,  cacheDir.getAbsolutePath() + ".exists() = " + cacheDir.exists());
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
	 * 获取系统默认文件夹，不会被应用删除
	 * 
	 * @return 系统文件加的名称
	 */
	public String getAppDir() {
		return Environment.getExternalStorageDirectory().getPath()+ "/" + getPackageName();
	}

    /**
     * 用于获取/data/data/<application package>/cache目录
     * @return
     */
    public String getCachePath() {
        return getCacheDir().getPath();
    }

    /**
     * 用于获取/data/data/<application package>/files目录
     * @return
     */
    public String getFilesPath() {
        return getFilesDir().getPath();
    }

    /**
     * 用于获取SDCard/Android/data/你的应用包名/cache/目录
     *
     * 对应 设置->应用->应用详情里面的『清除缓存』选项
     * @return
     */
    public String getExternalCachePath() {
        return getExternalCacheDir().getPath();
    }

    /**
     * SDCard/Android/data/你的应用的包名/files/<type>
     *
     * 对应 设置->应用->应用详情里面的『清除数据』选项
     */
    public String getExternalFilesPath(String type) {
        return getExternalFilesDir(type).getPath();
    }
}
