package panda.android.lib.base.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;

import panda.android.lib.R;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.FragmentUtil;
import panda.android.lib.base.util.Log;

/**
 * 针对 标签栏+内容区 的结构
 * 
 * 和 panda_fragment_tab.xml配合使用
 * @author shitianci
 * 
 */
public abstract class TabFragment extends BaseFragment {


	private static final String TAG = TabFragment.class.getSimpleName();
	private View[] mTabs;
	private int currentTabIndex;
	private long firstTime;
	private int mDefaultPage = 0;
	private boolean isExitDoubleCheck = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View onCreateView = super.onCreateView(inflater, container,
				savedInstanceState);
		initTabView(onCreateView, null);
		return onCreateView;
	}

	public void initTabView(View createView, View[] assignTabView) {
		int[] btnIds = getBtnIds();
		mTabs = new View[btnIds.length];
		Log.d(TAG, "initTabView, btnIds.length =" + btnIds.length);
		for (int i = 0; i < btnIds.length; i++) {
			Log.d(TAG, "initTabView, btnIds[" + i +"] =" + btnIds[i]);
			View findViewById = createView.findViewById(btnIds[i]);
			if (findViewById == null) {
				findViewById = assignTabView[i];
			}
			mTabs[i] = findViewById;
			mTabs[i].setOnClickListener(this);
			mTabs[i].setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
//					Log.d(TAG, v + ":" + hasFocus);
					if (hasFocus) {
						chooseTab(v);
					}
				}
			});
		}
		
		mTabs[0].setNextFocusLeftId(mTabs[mTabs.length-1].getId());
		mTabs[mTabs.length-1].setNextFocusRightId(mTabs[0].getId());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public void enableBackStackChangedListener() {
		getActivity().getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);
	}
	
	public void enableExitDoubleCheck() {
		isExitDoubleCheck  = true;
	}
	
	@Override
	public void onClick(View v) {
		chooseTab(v);
	}
	
	private void chooseTab(View v) {
		Log.d(TAG, "choose Tab:" + v);
		int index = 0;
		for (int i : getBtnIds()) {
			if (i == v.getId()) {
				break;
			}
			index++;
		}
		if (currentTabIndex == index || index >= getBtnIds().length) {
			return;
		}
		mTabs[currentTabIndex].setSelected(false);
		chooseTab(index);
	}

	public void chooseTab(int index) {
		Log.d(TAG, "chooseTab " + index);
		openFragment(getChildFragments()[currentTabIndex], getChildFragments()[index]);
		currentTabIndex = index;
		mTabs[currentTabIndex].setSelected(true);
		mTabs[currentTabIndex].setFocusable(true);
		mTabs[currentTabIndex].requestFocus();
	}

	private void openFragment(BaseFragment Lastfragment,
			BaseFragment newfragment) {
		FragmentUtil.addFragmentToStack(Lastfragment, newfragment, this,
				R.id.main_content);
	}

	public static void openSecondFragment(FragmentActivity activity,
			BaseFragment newfragment) {
		FragmentUtil.addFragmentToStack(newfragment, activity, R.id.container);
	}

	private boolean needFinish() {
		if (!isExitDoubleCheck) {
			return true; 
		}
		if (currentTabIndex == mDefaultPage) {
			if (firstTime + 2000 > System.currentTimeMillis()) {
				return true;
			} else {
				DevUtil.showInfo(getActivity(), "再点击一次退出程序！");
			}
			firstTime = System.currentTimeMillis();
		}
		if (mTabs != null) {
			chooseTab(mTabs[mDefaultPage]);
		}
		return false;
	}

	public void exit() {
		if (needFinish()) {
			super.exit();
		}
	};

	/**
	 * 获取Tab栏按钮的ID
	 * @return
	 */
	public abstract int[] getBtnIds();

	/**
	 * 获取按钮栏对应的Fragment
	 * @return
	 */
	public abstract BaseFragment[] getChildFragments();

	public int getDefaultPage() {
		return mDefaultPage;
	}

	public void setDefaultPage(int defaultPage) {
		this.mDefaultPage = defaultPage;
		currentTabIndex = defaultPage;
		chooseTab(defaultPage);
	}

	OnBackStackChangedListener onBackStackChangedListener = new OnBackStackChangedListener() {
		private int mLastBackStackEntryCount = 0;

		public void onBackStackChanged() {
			FragmentActivity activity = getActivity();
			if (activity == null) {
				Log.e(TAG, "onBackStackChanged, getActivity() is null");
				return;
			}
			Log.i(TAG, "onBackStackChanged");
			FragmentManager manager = activity.getSupportFragmentManager();

			if (manager != null) {
				int backStackEntryCount = manager.getBackStackEntryCount();
				// Log.d(TAG, "manager.getFragments() is " +
				// manager.getFragments().size());
				Log.d(TAG, "backStackEntryCount is " + backStackEntryCount);
				if (backStackEntryCount == 0) {
					BaseFragment currMainFrag = (BaseFragment) getChildFragmentManager()
							.findFragmentById(R.id.main_content);
					if (currMainFrag != null) {
						Log.d(TAG, "currMainFrag is " + currMainFrag);
						currMainFrag.onFragmentResume();
					}
				} else if (mLastBackStackEntryCount > backStackEntryCount) {
					BaseFragment currFrag = (BaseFragment) manager
							.findFragmentById(R.id.container);
					Log.d(TAG, "currFrag is " + currFrag);
					currFrag.onFragmentResume();
				}
				mLastBackStackEntryCount = backStackEntryCount;
			}
		}
	};


}
