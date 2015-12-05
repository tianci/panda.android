package panda.android.lib.base;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

import panda.android.lib.base.configuration.AppDirConfiguration;


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

    public static BaseApp getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
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
	@Deprecated
	public String getAppDir() {
		return AppDirConfiguration.getExternalStoragePublicDirectory();
	}
}
