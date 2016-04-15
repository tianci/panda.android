package panda.android.lib.base.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import panda.android.lib.R;
import panda.android.lib.base.model.BaseModel;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.FragmentUtil;
import panda.android.lib.base.util.Log;
import panda.android.lib.base.util.OSUtil;

/**
 * 占位符片段（配合panda_android_lib_placeholder使用）
 * 
 * @author Administrator
 * 
 */
public abstract class BaseFragment extends Fragment implements
		OnClickListener {

	private static final String TAG = BaseFragment.class.getSimpleName();
	protected static final boolean DEBUG = false;
	private String id = OSUtil.getObjectInfo(this);
	private boolean needPageStatistic = false; //是否需要统计页面跳转
	private boolean isCanFinishActivity = false; //退出时是否需要销毁Activity，表示它是主Fragment
	protected boolean isExitDoubleCheck = false;
	protected long firstTime;

	/**
	 * 设置初始化的参数
	 * @param o
	 */
	public void setArguments(Object o) {
		if (o != null){
			Bundle args = new Bundle();
			args.putString("args", o.toString());
			setArguments(args);
		}
	}

	/**
	 * 获取初始化的参数
	 *      可应用Fragment重建的时候。
	 * @param param
	 * @param <T>
	 * @return
	 */
	public <T> T getArguments(Class<? extends T> param){
		try{
			return BaseModel.getGson().fromJson(getArguments().getString("args"), param);
		}
		catch (Exception e){
			return null;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		Log.d(TAG, id + " onCreateView");
		int layoutId = getLayoutId();
		if (layoutId == 0) {
			return null;
		}
		View view = inflater.inflate(layoutId, container, false);
		int[] childModelResIds = getChildModelResIds();
		CharSequence[] childModelNames = getChildModelNames();

		if(childModelResIds !=null ){
			for (int i = 0; i < childModelResIds.length; i++) {
				TextView tv = (TextView) view.findViewById(childModelResIds[i]);
				if (childModelNames != null && i < childModelNames.length) {
					final CharSequence name = childModelNames[i];
					tv.setText(name);
				}
				
			}
		}

		int[] viewIds = getCanBeClickedViewID();
		if (viewIds != null) {
			for (int viewId : viewIds) {
				View tmp = view.findViewById(viewId);
				tmp.setOnClickListener(this);
			}
		}
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 防止透传点击事件。
			}
		});
		ButterKnife.bind(this, view);
		return view;
	}

	public abstract int getLayoutId();
	
	/**
	 * 建议用butterknife来控制。
	 * @return 返回可以被点击的布局id。
	 */
	@Deprecated
	public int[] getCanBeClickedViewID(){
		return null;
	}

	/**
	 * 返回代替模块的id。（在绘制原型的时候起作用）
	 * @return
	 */
	@Deprecated
	public int[] getChildModelResIds(){
		return null;
	};

	/**
	 * 返回代替模块的名字。（在绘制原型的时候起作用）
	 * @return
	 */
	@Deprecated
	public CharSequence[] getChildModelNames(){
		return null;
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (isCanFinishActivity) {
			OnBackStackChangedListener onBackStackChangedListener = new OnBackStackChangedListener() {
				private int mLastBackStackEntryCount = 1;

				public void onBackStackChanged() {
					FragmentManager manager = getActivity().getSupportFragmentManager();

					if (manager != null) {
						int backStackEntryCount = manager.getBackStackEntryCount();
						if(backStackEntryCount == 1){
							BaseFragment currMainFrag = (BaseFragment) getChildFragmentManager().findFragmentById(R.id.container);
							if (currMainFrag != null) {
								if(DEBUG)
									Log.d(TAG, "currMainFrag is " + currMainFrag);
								currMainFrag.onFragmentResume();
							}
						}
						else if(mLastBackStackEntryCount > backStackEntryCount){
							BaseFragment currFrag = (BaseFragment) manager.findFragmentById(R.id.container);
							if(DEBUG)
								Log.d(TAG, "currFrag is " + currFrag);
							currFrag.onFragmentResume();
						}
						mLastBackStackEntryCount = backStackEntryCount;
					}
				}
			};
			getActivity().getSupportFragmentManager().addOnBackStackChangedListener(
					onBackStackChangedListener);
		}
	}

	public void onFragmentResume() {
		if(DEBUG)
			Log.d(TAG, this.toString());
		FragmentUtil.onPageStart(this);
	}

	public void onFragmentPause() {
		if(DEBUG)
			Log.d(TAG, this.toString());
		FragmentUtil.onPageEnd(this);
	}
	
	@Override
	public void onDestroy() {
		if(DEBUG)
			Log.d(TAG, id + " onDestroy");
		DevUtil.closeImm(getActivity());
		super.onDestroy();
	}
	
	public void onResume() {
	    super.onResume();
	    if(DEBUG)
	    	Log.d(TAG, id + " onResume");
	    if (isNeedPageStatistic()) {
	    	FragmentUtil.onPageStart(this);
		}
	}
	
	public void onPause() {
	    super.onPause();
	    if(DEBUG)
	    	Log.d(TAG, id + " onPause");
	    if (isNeedPageStatistic()) {
	    	FragmentUtil.onPageEnd(this);
		}
	}
	
	@Override
	public void onStart() {
		if(DEBUG)
			Log.d(TAG, id + " onStart");
		super.onStart();
	}
	
	@Override
	public void onStop() {
		if(DEBUG)
			Log.d(TAG, id + " onStop");
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		if(DEBUG)
			Log.d(TAG, id + " onDestroyView");
		ButterKnife.unbind(this);
		super.onDestroyView();
	}
	
	@Override
	public void onAttach(Activity activity) {
		if(DEBUG)
			Log.d(TAG, id + " onAttach");
		super.onAttach(activity);
	}
	
	@Override
	public void onDetach() {
		if(DEBUG)
			Log.d(TAG, id + " onDetach");
		super.onDetach();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if(DEBUG)
			Log.d(TAG, id + " onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
	
	public void onClick(View v) {
	}

	public boolean isNeedPageStatistic() {
		return needPageStatistic;
	}

	/**
	 * 打开页面跳转统计
	 * @param needPageStatistic
	 */
	public void openPageStatistic(boolean needPageStatistic) {
		this.needPageStatistic = needPageStatistic;
	}

	public boolean isCanFinishActivity() {
		return isCanFinishActivity;
	}

	public void setCanFinishActivity(boolean isFinishActivity) {
		this.isCanFinishActivity = isFinishActivity;
	}

	public void enableExitDoubleCheck() {
		isExitDoubleCheck  = true;
	}


	protected boolean needFinish() {
		if (!isExitDoubleCheck) {
			return true;
		}
		if (firstTime + 2000 > System.currentTimeMillis()) {
			return true;
		} else {
			DevUtil.showInfo(getActivity(),getString(R.string.lib_exit_msg) );
		}
		firstTime = System.currentTimeMillis();
		return false;
	}

	public void exit() {
        Log.d(TAG, "exit");
		if (needFinish()) {
			DevUtil.closeImm(getActivity());
			if (isCanFinishActivity) {
				getActivity().finish();
			}
			else{
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.popBackStack();
			}
		}
	}

	public  <T extends View> T findViewById(View v, int id, OnClickListener listener) {
		try{
			T view = (T) v.findViewById(id);
			view.setOnClickListener(listener);
			return view;
		}
		catch (Exception e){
			Log.printStackTrace(e);
		}
		return  null;
	}

}
