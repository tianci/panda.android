package panda.android.lib.base;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;
import panda.android.lib.base.configuration.AppDirConfiguration;
import panda.android.lib.base.control.NetResultInfoEvent;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.util.DevUtil;
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

    public static BaseApp getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		EventBus.getDefault().register(this);
	}

	public void onEventMainThread(NetResultInfoEvent e){
		NetResultInfo result = e.getResult();
		processNetResultInfo(result);
	}

	/**
	 * 需要特殊处理的，请重载次方法
	 * @param result
     */
	public void processNetResultInfo(NetResultInfo result) {
		if(result == null){
			Log.w(TAG, "网络错误");
		}
		else{
			Log.d(TAG, result.getRespDesc());
			switch (result.getRespCode()){
				case NetResultInfo.NON_USER:
					Log.w(TAG, "不是用户");
					break;
			}
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		EventBus.getDefault().unregister(this);
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
