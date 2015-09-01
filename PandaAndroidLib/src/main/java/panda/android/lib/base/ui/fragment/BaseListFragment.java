package panda.android.lib.base.ui.fragment;

import android.os.Bundle;

public abstract class BaseListFragment extends BaseFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		runTask();
	}

	/**
	 * 获取数据
	 */
	public abstract void runTask();

}
