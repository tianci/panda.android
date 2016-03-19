package panda.android.lib.base.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.umeng.analytics.MobclickAgent;

import panda.android.lib.base.ui.fragment.BaseFragment;

public class FragmentUtil {

	private static final String TAG = "FragmentUtil";

	/**
	 * 添加一个新fragment到Activity的指定位置
	 * @param newFragment
	 * @param activity
	 * @param containerViewId
	 */
	public static void addFragmentToStack(BaseFragment newFragment,
			FragmentActivity activity, int containerViewId) {
		// Add the fragment to the activity, pushing this transaction
		// on to the back stack.
		DevUtil.closeImm(activity);
		addFragmentToStack(null, newFragment, activity, containerViewId);
	}

	/**
	 * 在Activity层面隐藏lastfragment，显示newfragment。
	 * @param lastFragment
	 * @param newFragment
	 * @param activity
	 * @param containerViewId
	 */
	public static void addFragmentToStack(BaseFragment lastFragment, BaseFragment newFragment, FragmentActivity activity, int containerViewId) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		if (lastFragment == null) {
			//自动检测自后一个Fragment
			lastFragment = (BaseFragment) getTopFragment(fragmentManager, containerViewId);
		}
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if(lastFragment !=null && lastFragment != newFragment){
			lastFragment.onFragmentPause();
			ft.hide(lastFragment);
		}
		if(!newFragment.isAdded()){	
			ft.add(containerViewId, newFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.addToBackStack(null);
		}
		else{
			lastFragment.onFragmentResume();
			ft.show(newFragment);
		}
		ft.commit();
	}

	/**
	 * 获取当前顶部的Fragment
	 * @param fragmentManager
	 * @param containerViewId
	 * @return
	 */
	public static Fragment getTopFragment(FragmentManager fragmentManager, int containerViewId) {
		int backStackEntryCount = fragmentManager.getBackStackEntryCount();
		Log.d(TAG, "backStackEntryCount is " + backStackEntryCount);
		Fragment lastFragment = null;
		if(backStackEntryCount >= 1){
			lastFragment = fragmentManager.findFragmentById(containerViewId);
			Log.d(TAG, "lastFragment is " + lastFragment);
		}
		return lastFragment;
	}
	
	/**
	 * 在Fragment层面隐藏lastfragment，显示newfragment。
	 * @param lastFragment
	 * @param newFragment
	 * @param parentFragment
	 * @param containerViewId
	 */
	public static void addFragmentToStack(BaseFragment lastFragment, BaseFragment newFragment, BaseFragment parentFragment, int containerViewId) {
		FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if(lastFragment != newFragment){
			ft.hide(lastFragment);
		}
		if(!newFragment.isAdded()){	
			ft.add(containerViewId, newFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.addToBackStack(null);
		}
		else{
			ft.show(newFragment);
		}
		try {
			ft.commit();
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
	}
	
	/**
	 * 页面展示在用户面前
	 * @param object
	 */
	public static void onPageStart(Object object) {
		String simpleName = object.getClass().getSimpleName();
		Log.d(TAG, "onPageStart " + simpleName);
		MobclickAgent.onPageStart(simpleName);
	}
	
	/**
	 * 页面被隐藏
	 * @param object
	 */
	public static void onPageEnd(Object object) {
		String simpleName = object.getClass().getSimpleName();
		Log.d(TAG, "onPageEnd " + simpleName);
		MobclickAgent.onPageEnd(simpleName);
	}

}
