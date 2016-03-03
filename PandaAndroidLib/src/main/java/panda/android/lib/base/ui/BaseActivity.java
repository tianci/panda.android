package panda.android.lib.base.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import panda.android.lib.R;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.FragmentUtil;
import panda.android.lib.base.util.Log;


public abstract class BaseActivity<T extends BaseFragment> extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private T mainFragment;

    private SystemBarTintManager tintManager;
    private long lastExitTime;
    private boolean isDoubleClickExit = false;
    private boolean userTintManager = false; //是否使用tintManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //修正内存回收时重进的画面重叠问题：参考:[用Fragment制作的Tab页面产生的UI重叠问题](http://blog.csdn.net/twilight041132/article/details/43812745)
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panda_activity_main);
        if (savedInstanceState == null) {
            mainFragment = initMainFragment();
            if (mainFragment == null){
                finish();
                return;
            }
            mainFragment.setCanFinishActivity(true);
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, mainFragment).commit();
            FragmentUtil.addFragmentToStack(mainFragment, this, R.id.container);
        }
        // Log.d(TAG, OSUtil.getDeviceInfoByUmeng(getApplicationContext()));

//		BaseApp.getInstance().addActivity(this);

        /**
         * 设置系统通知栏的的颜色
         */
        if (userTintManager){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
            }
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
        }
    }

    public abstract T initMainFragment();

    public T getMainFragment() {
        return mainFragment;
    }

    public void setMainFragment(T mainFragment) {
        this.mainFragment = mainFragment;
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void setIsDoubleClickExit(boolean mIsDoubleClickExit) {
        isDoubleClickExit = mIsDoubleClickExit;
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "backStackEntryCount = " + backStackEntryCount);
        if (mainFragment == null || backStackEntryCount >= 2) {
            Log.d(TAG, "onBackPressed 1");
            super.onBackPressed();
        } else {
            if (isDoubleClickExit){
                if((System.currentTimeMillis()- lastExitTime) > 2000){
                    Log.d(TAG, "onBackPressed 2");
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    lastExitTime = System.currentTimeMillis();
                    return;
                }
            }
            mainFragment.exit();
        }
    }

    /**
     * 获取intent里面某个值，如果取不到，则直接finish。
     *
     * @param key
     * @param classOfT
     * @return
     */
    public <TT> TT getIntentExtra(String key, Class<TT> classOfT) {
        TT info = null;
        try {
            info = (TT) getIntent().getExtras().get(key);
            Log.d(TAG, key);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }

        if (info == null) {
            finish();
        }
        return info;
    }

    /**
     * 设置一个颜色给系统状态栏
     * 如果不希望 APP 的内容被上拉到状态列 (Status bar) 的话，要记得在界面 (Layout) XML 档中，最外面的那层，要再加上一个属性 android:fitsSystemWindows="true"
     *
     * @param res
     */
    public void setStatusBarTintResource(int res) {
        if (!userTintManager){
            return;
        }
        tintManager.setTintColor(getResources().getColor(res));
//        tintManager.setStatusBarTintResource(android.R.color.holo_blue_dark);
//        tintManager.setNavigationBarTintResource(android.R.color.holo_blue_dark);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
