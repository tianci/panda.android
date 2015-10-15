package panda.android.lib.base.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import panda.android.lib.R;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.FragmentUtil;
import panda.android.lib.base.util.Log;


public abstract class BaseActivity<T extends BaseFragment> extends FragmentActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private T mainFragment;

    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panda_activity_main);
        if (savedInstanceState == null) {
            mainFragment = initMainFragment();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
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
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "backStackEntryCount = " + backStackEntryCount);
        if (mainFragment == null || backStackEntryCount >= 2) {
            Log.d(TAG, "onBackPressed 1");
            super.onBackPressed();
        } else {
            Log.d(TAG, "onBackPressed 2");
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
