package panda.android.lib.base.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import panda.android.lib.base.model.ListNetResultInfo;
import panda.android.lib.base.util.Log;


/**
 * 加载网络List数据的通用模型。
 * @author shitianci
 *
 * @param <T> 
 */
public abstract class ListNetFragment<O> extends NetFragment<ListNetResultInfo<O>> {
	
	private static final String TAG = ListNetFragment.class.getSimpleName();
	private int mStartIndex = 0; //开始数据
	private int mPageSize = 10;  //起始页的数据
	private ArrayList<O> mAllDataList = new ArrayList<O>();
	private boolean isLoadedAllNetData = false;
	
	/**
	 * 加载list数据
	 * @param startIndex 起始数据项
	 * @param pageSize 预期加载多少项
	 * @return
	 */
	protected abstract ListNetResultInfo<O> onDoInBackgroundSafely(int startIndex,
			int pageSize);
	
	/**
	 * 显示list数据
	 * @param list 最近一次交互获取的数据
	 */
	protected abstract void showResult(List<O> list);
	
	/**
	 * 加载第一页数据
	 */
	@Override
	public void loadNetData() {
		mStartIndex = 0;
		mAllDataList.clear();
		isLoadedAllNetData = false;
		super.loadNetData();
	}
	
	/**
	 * 加载下一页数据
	 */
	public void loadNextPageNetData() {
		Log.d(TAG, "loadNextPageNetData, isLoadedAllNetData = " + isLoadedAllNetData);
		if (isLoadedAllNetData) {
			showLoadAllNetData();
			return;
		}
		if (mStartIndex != mAllDataList.size()) {
			Log.w(TAG, "mStartIndex = " + mStartIndex);
			Log.w(TAG, "mAllDataList.size() = " + mAllDataList.size());
			mStartIndex = mAllDataList.size();
		}
		super.loadNetData();
	}
	
	/**
	 * 所有数据加载完毕
	 */
	public void showLoadAllNetData() {
		Log.d(TAG, "所有数据加载完毕");
		isLoadedAllNetData  = true;
	}
	
	@Override
	protected ListNetResultInfo<O> onDoInBackgroundSafely() {
		return onDoInBackgroundSafely(mStartIndex, mPageSize);
	}
	
	@Override
	final protected void showResult(ListNetResultInfo<O> result) {
		super.showResult(result);
		if (result == null || result.getList() == null || result.getList().size() == 0) {
			showNoResult(result);
			return;
		}
        hiddenNoResult();
		if (result.getList().size() < mPageSize) {
            Log.w(TAG, "showLoadAllNetData");
			showLoadAllNetData();
		}
		List<O> list = result.getList();
		for (O o : list) {
			mAllDataList.add(o);
		}
		mStartIndex += list.size();
		showResult(list);
	}

	/**
	 * 获取每一页数据的大小
	 * @return
	 */
	public int getPageSize() {
		return mPageSize;
	}

	/**
	 * 设置每一页数据的大小
	 * @param mPageSize
	 */
	public void setPageSize(int mPageSize) {
		this.mPageSize = mPageSize;
	}

	/**
	 * 获取下一次加载的其实位置
	 * @return
	 */
	public int getStartIndex() {
		return mStartIndex;
	}

	/**
	 * 设置下一次加载的其实位置
	 * @param mStartIndex
	 */
	public void setStartIndex(int mStartIndex) {
		this.mStartIndex = mStartIndex;
	}

	/**
	 * @return the mAllDataList
	 */
	public ArrayList<O> getAllDataList() {
		return mAllDataList;
	}

	/**
	 * @param mAllDataList the mAllDataList to set
	 */
	public void setAllDataList(ArrayList<O> mAllDataList) {
		this.mAllDataList = mAllDataList;
	}

	/**
	 * 是否加载完所有的数据
	 * @return the isLoadedAllNetData
	 */
	public boolean isLoadedAllNetData() {
		return isLoadedAllNetData;
	}

	/**
	 * @param isLoadedAllNetData the isLoadedAllNetData to set
	 */
	public void setLoadedAllNetData(boolean isLoadedAllNetData) {
		this.isLoadedAllNetData = isLoadedAllNetData;
	}

}
