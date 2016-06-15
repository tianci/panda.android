package panda.android.lib.base.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;

import java.util.ArrayList;
import java.util.List;

import panda.android.lib.R;
import panda.android.lib.base.model.ListNetResultInfo;
import panda.android.lib.base.util.Log;


/**
 * 加载网络List数据的通用模型。
 *
 * @author shitianci
 */
public abstract class ListNetFragment<O> extends NetFragment<ListNetResultInfo<O>> {

	private static final String TAG = ListNetFragment.class.getSimpleName();
	private int mStartIndex = 0; //开始数据
	private int mPageSize = 3;  //起始页的数据
	public ArrayList<O> mAllDataList = new ArrayList<O>();
	private boolean isLoadedAllNetData = false;
	protected Parcelable mViewResultState = null;
	private boolean isLoadingMore = false;
	private SwipeRefreshLayout mRefreshLayout;



	public DataAdapter dataAdapter = null;
	public ListView mListView;
	private SwipeRefreshHelper mSwipeRefreshHelper;
	private SwipeRefreshLayout currentSwipeRefreshLayout; //当前显示的currentSwipeRefreshLayout

	/**
	 * -------------------------
	 * START: 最重要的流程方法
	 * -------------------------
	 */

	/**
	 * 加载list数据
	 *
	 * @param startIndex 起始数据项
	 * @param pageSize   预期加载多少项
	 * @return
	 */
	protected abstract ListNetResultInfo<O> onDoInBackgroundSafely(int startIndex, int pageSize);


	/**
	 * 获取item布局中一个textview的id
	 *
	 * @return
	 */
	public abstract int getItemTextViewResourceId();

	/**
	 * 获取对应Item布局的id
	 *
	 * @return
	 */
	public abstract int getItemLayoutId();

	/**
	 * 将view和数据绑定
	 *
	 * @param position
	 * @param view
	 * @param parent
	 * @return
	 */
	public abstract View bindView(int position, View view, ViewGroup parent);


	/**
	 * -------------------------
	 * END
	 * -------------------------
	 */



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		if (mViewResult instanceof SwipeRefreshLayout) {
			configRefreshLayout();
		} else {
			Log.e(TAG, "没有找到SwipeRefreshLayoutt");
		}
		return rootView;
	}


	protected void configRefreshLayout() {
		mRefreshLayout = (SwipeRefreshLayout) mViewResult;
		if (mRefreshLayout == null) {
			return;
		}
		mListView = (ListView) mViewResult.findViewById(R.id.net_list);

		mRefreshLayout.setColorSchemeColors(R.color.app_primary);

		mSwipeRefreshHelper = new SwipeRefreshHelper(mRefreshLayout);
		mSwipeRefreshHelper.setLoadMoreEnable(true);

		mSwipeRefreshHelper.setOnSwipeRefreshListener(new SwipeRefreshHelper.OnSwipeRefreshListener() {
			@Override
			public void onfresh() {
				Log.d(TAG, "下拉刷新");

				dataAdapter = null;
				isLoadingMore = false;
				loadNetData(isLoadingMore);
			}
		});
		mSwipeRefreshHelper.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void loadMore() {
				Log.d(TAG, "加载更多");
				mSwipeRefreshHelper.setLoadMoreEnable(!isLoadedAllNetData);
				if (isLoadedAllNetData) {
					mSwipeRefreshHelper.loadMoreComplete(false);
					return;
				}
				loadNextPageNetData();
			}
		});

		for (final View v : mViewArray) {
			if (v == null || v.getId() == R.id.net_result) {
				continue;
			}
			if (v instanceof SwipeRefreshLayout) {
				final SwipeRefreshHelper swipeRefreshHelper = new SwipeRefreshHelper((SwipeRefreshLayout) v);
				v.setTag(swipeRefreshHelper);

				swipeRefreshHelper.setOnSwipeRefreshListener(new SwipeRefreshHelper.OnSwipeRefreshListener() {
					@Override
					public void onfresh() {
						Log.d(TAG, "下拉刷新");

						//清除上次的数据
						if (currentSwipeRefreshLayout != null) {
							currentSwipeRefreshLayout.setRefreshing(false);
							currentSwipeRefreshLayout = null;
						}
						currentSwipeRefreshLayout = (SwipeRefreshLayout) v;

						dataAdapter = null;
						isLoadingMore = false;
						loadNetData(isLoadingMore);
					}
				});

			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mRefreshLayout.setRefreshing(true);
	}

	/**
	 * 显示list数据
	 *
	 * @param list 最近一次交互获取的数据
	 */
	private void displayResult(List<O> list) {
//        Log.d(TAG, "displayResult: " + list.toString());
		if (dataAdapter == null) {
			dataAdapter = new DataAdapter(getActivity());
			mListView.setAdapter(dataAdapter);
		}
		for (int i = 0; i < list.size(); i++) {
			dataAdapter.add(list.get(i));
		}
		dataAdapter.notifyDataSetChanged();
	}

	//不同的订单的适配器
	public class DataAdapter extends ArrayAdapter<O> {

		public DataAdapter(Context context) {
			super(context, getItemLayoutId(), getItemTextViewResourceId());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return bindView(position, super.getView(position, convertView, parent), parent);
		}

	}

	public O getItem(int position) {
		if (dataAdapter == null) {
			Log.d(TAG, "dataAdapter 没有初始化");
			return null;
		}
		return dataAdapter.getItem(position);
	}


	@Override
	public void onPreloadNetData(boolean isLoadingMore) {
		super.onPreloadNetData(isLoadingMore);
		if (!isLoadingMore) {
			mStartIndex = 0;
			mAllDataList.clear();
			dataAdapter = null;
			isLoadedAllNetData = false;
		}
		mRefreshLayout.setVisibility(View.VISIBLE);
		//保存位置状态
		if (mViewResult != null) {
			if (mViewResult instanceof AbsListView) {
				mViewResultState = ((AbsListView) mViewResult.findViewById(R.id.net_list)).onSaveInstanceState();
				Log.d(TAG, "onSaveInstanceState, mViewResultState = " + mViewResultState);
			}
		}
	}

	@Override
	public void onPostLoadNetData(boolean isLoadingMore) {
		super.onPostLoadNetData(isLoadingMore);
		if (!isLoadingMore) {
			mSwipeRefreshHelper.refreshComplete();
			mSwipeRefreshHelper.setLoadMoreEnable(true);
		} else {
			mSwipeRefreshHelper.loadMoreComplete(isLoadedAllNetData);
		}

		if (currentSwipeRefreshLayout != null) {
			currentSwipeRefreshLayout.setRefreshing(false);
			currentSwipeRefreshLayout = null;
		}

	}

	/**
	 * 加载下一页数据
	 */
	private void loadNextPageNetData() {
		Log.d(TAG, "loadNextPageNetData, isLoadedAllNetData = " + isLoadedAllNetData);
		if (mStartIndex != mAllDataList.size()) {
			Log.w(TAG, "mStartIndex = " + mStartIndex);
			Log.w(TAG, "mAllDataList.size() = " + mAllDataList.size());
			mStartIndex = mAllDataList.size();
		}
		isLoadingMore = true;
		super.loadNetData(isLoadingMore);
	}


	/**
	 * 所有数据加载完毕
	 */
	public void onLoadAllNetData() {
		Log.d(TAG, "所有数据加载完毕");
	}

	@Override
	protected ListNetResultInfo<O> onDoInBackgroundSafely() {
//        try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
		return onDoInBackgroundSafely(mStartIndex, mPageSize);
	}

	final protected void onDisplayResult(ListNetResultInfo<O> result) {
		if (result.getList() == null || result.getList().size() == 0) {
			showOnlyView(R.id.net_no_result, isLoadingMore);
			return;
		}
		if (result.getList().size() < mPageSize) {
			Log.w(TAG, "onLoadAllNetData");
			isLoadedAllNetData = true;
			mSwipeRefreshHelper.setNoMoreData();
			onLoadAllNetData();
		}

		List<O> list = result.getList();
		for (O o : list) {
			mAllDataList.add(o);
		}
		mStartIndex += list.size();
		displayResult(list);
		//恢复原来的位置状态
		if (mViewResult != null) {
			if (mViewResult instanceof AbsListView) {
				Log.d(TAG, "onRestoreInstanceState, mViewResultState = " + mViewResultState);
				((AbsListView) mViewResult).onRestoreInstanceState(mViewResultState);
			}
		}
	}


	/**
	 * 获取每一页数据的大小
	 *
	 * @return
	 */
	public int getPageSize() {
		return mPageSize;
	}

	/**
	 * 设置每一页数据的大小
	 *
	 * @param mPageSize
	 */
	public void setPageSize(int mPageSize) {
		this.mPageSize = mPageSize;
	}

	/**
	 * 获取下一次加载的其实位置
	 *
	 * @return
	 */
	public int getStartIndex() {
		return mStartIndex;
	}

	/**
	 * 设置下一次加载的其实位置
	 *
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

}
