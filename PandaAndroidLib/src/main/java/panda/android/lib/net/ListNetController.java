package panda.android.lib.net;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import panda.android.lib.base.model.ListNetResultInfo;
import panda.android.lib.base.util.Log;

/**
 * Created by shitianci on 16/8/23.
 * <p/>
 * 用于处理列表的 下拉刷新和加载更多的网络 控制逻辑
 */
public abstract class ListNetController<O> extends NetController<ListNetResultInfo<O>> {
    private String TAG = ListNetController.class.getSimpleName();
    private int mStartIndex = 0;
    private int mPageSize = 10;
    public ArrayList<O> mAllDataList = new ArrayList<O>();  //请求的所有List数据

    public enum Type {
        REFRESH,
        LOAD_MORE,
    }

    private final SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshHelper mSwipeRefreshHelper;
    private final ListView mListView;
    private final IListModel mMockData; //用于模拟不正常的数据
    public DataAdapter mDataAdapter = null;
    Type type = Type.REFRESH;


    public ListNetController(Context context, SwipeRefreshLayout swipeRefreshLayout, ListView listView) {
        this(context, swipeRefreshLayout, listView, null);
    }

    /**
     * @param context            上下文环境
     * @param swipeRefreshLayout 下拉刷新布局
     * @param listView           列表布局
     * @param abnormalData       异常数据，上层new一个即可。
     */
    public ListNetController(Context context, SwipeRefreshLayout swipeRefreshLayout, ListView listView, IListModel abnormalData) {
        super(context);
        mSwipeRefreshLayout = swipeRefreshLayout;
        mListView = listView;
        mMockData = abnormalData;
        configRefreshLayout();
    }

    /**
     * 下拉刷新数据
     */
    public void refresh() {
        mSwipeRefreshHelper.autoRefresh();
    }


    @Override
    public void loadNetData() {
        if (loadingNetData) {
            mSwipeRefreshHelper.refreshComplete();
            return;
        }
        super.loadNetData();
    }

    /**
     * 加载网络数据
     */
    @Override
    protected ListNetResultInfo<O> onDoInBackgroundSafely() {
        return onDoInBackgroundSafely(mStartIndex, mPageSize);
    }

    /**
     * 获取列表的单条个数据
     */
    public O getItem(int position) {
        if (this.mDataAdapter == null) {
            Log.d(TAG, "dataAdapter 没有初始化");
            return null;
        } else {
            return this.mDataAdapter.getItem(position);
        }
    }

    /**
     * 设置加载的Adapter
     */
    public void setListAdapter() {
        mDataAdapter = new DataAdapter(mContext);
        mListView.setAdapter(mDataAdapter);
    }

    /**
     * 初始化加载的控件
     */
    protected void configRefreshLayout() {
        //！！！为了保证能够加载下一页，这个方法必须调用，且必须放在mSwipeRefreshHelper初始化前面，具体原因看代码
        mSwipeRefreshLayout.setColorSchemeColors(panda.android.lib.R.color.app_primary);
        mSwipeRefreshHelper = new SwipeRefreshHelper(mSwipeRefreshLayout);

        try {
            Field field = mSwipeRefreshHelper.getClass().getDeclaredField("mContentView");
            field.setAccessible(true);
            field.set(mSwipeRefreshHelper, mListView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSwipeRefreshHelper.setLoadMoreEnable(true);
        mSwipeRefreshHelper.setAutoLoadMoreEnable(true);

        mSwipeRefreshHelper.setOnSwipeRefreshListener(new SwipeRefreshHelper.OnSwipeRefreshListener() {
            @Override
            public void onfresh() {
                Log.d(TAG, "下拉刷新");
                mStartIndex = 0;          //刷新时重置
                type = Type.REFRESH;
                isLoadedAllNetData = false;
                loadNetData();     //加载数据

            }
        });

        mSwipeRefreshHelper.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                Log.d(TAG, "加载更多, isLoadedAllNetData = " + isLoadedAllNetData);
                //无更多数据
                if (isLoadedAllNetData) {
                    mSwipeRefreshHelper.loadMoreComplete(false);
                    return;
                }
                type = Type.LOAD_MORE;
                loadNetData();
            }
        });
    }


    /**
     * 是否能够刷新
     */
    public void setRefreshEnable(boolean isRefreshEnable) {
        mSwipeRefreshLayout.setEnabled(isRefreshEnable);
    }

    /**
     * 是否能加载更多
     */
    private boolean isLoadedMoreEnable = true;

    public void setLoadMoreEnable(boolean isLoadMore) {
        mSwipeRefreshHelper.setLoadMoreEnable(isLoadMore);
        isLoadedMoreEnable = isLoadMore;
    }

    /**
     * 添加头布局
     */
    public void addHeadView(View headView) {
        if (headView != null) {
            mListView.addHeaderView(headView);
        }
    }

    /**
     * 添加脚布局
     */
    public void addFootView(View footView) {
        if (footView != null) {
            mListView.addFooterView(footView);
        }
    }

    /**
     * 设置加载的页数
     */
    public void setPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
    }


    public boolean isLoadedAllNetData = false; //是否所有的数据都加载完成


    @Override
    protected boolean isEmpty(ListNetResultInfo result) {
        if (result.getList().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param state
     * @param result
     */
    @Override
    protected void showData(IListModel.STATE state, ListNetResultInfo<O> result) {
        android.util.Log.d(TAG, "showData: " + state.value);
        switch (state) {
            case ASK_PRE:
                break;
            case ASK_ING:
                break;
            case ASK_ED:
                switch (type) {
                    case REFRESH:
                        mSwipeRefreshHelper.refreshComplete();
                        break;
                    case LOAD_MORE:
                        mSwipeRefreshHelper.loadMoreComplete(true);
                        break;
                }
                break;
            case ASK_ED_CANNOT_ACCESS:
            case ASK_ED_FAIL:
            case ASK_ED_ERROR:
                showErrorView(state);
                break;
            case ASK_ED_EMPTY:
                showEmptyView(state);
                break;
            case ASK_ED_AVAILABILITY:
                showAvailabilityView(result);
                break;
        }
    }

    /**
     * “异常数据” View和数据的控制
     */
    private void showErrorView(IListModel.STATE state) {
        switch (type) {
            case REFRESH:
                if (mAllDataList.isEmpty()) {
                    mDataAdapter.clear();
                    if (mDataAdapter.getCount() == 0) {
                        android.util.Log.d(TAG, "showErrorView: " + state.value);
                        //显示虚拟布局
                        mDataAdapter.add(getErrItem(state));
                        mSwipeRefreshHelper.setLoadMoreEnable(false);
                    }
                }
                break;
        }
    }


    //获取一个错误数据项
    protected abstract O getErrItem(IListModel.STATE state);

    /**
     * “空数据” View和数据的控制
     */
    private void showEmptyView(IListModel.STATE state) {
        switch (type) {
            case REFRESH:
                mDataAdapter.clear();
                if (mDataAdapter.getCount() == 0) {
                    //显示虚拟布局
                    mDataAdapter.add(getErrItem(state));
                    mSwipeRefreshHelper.setLoadMoreEnable(false);
                }
                break;
            case LOAD_MORE:
                isLoadedAllNetData = true;
                mSwipeRefreshHelper.setNoMoreData();
                break;
        }
    }

    /**
     * 注意 ：  加载更多  mSwipeRefreshHelper.setNoMoreData()的显示与否 与 “能否加载更多”有关
     * 为有效数据时的  View和数据的控制
     */
    private void showAvailabilityView(ListNetResultInfo<O> result) {
        Log.d(TAG, "mStartIndex: " + mStartIndex);
        if (isLoadedMoreEnable) {
            mSwipeRefreshHelper.setLoadMoreEnable(true);
        }
        List<O> list = result.getList();
        mStartIndex += list.size();
        switch (type) {
            case REFRESH:
                mAllDataList.clear();
                mDataAdapter.clear();
                for (int i = 0; i < list.size(); i++) {
                    mDataAdapter.add(list.get(i));
                    mAllDataList.add(list.get(i));
                }
                mDataAdapter.notifyDataSetChanged();

                if (mPageSize > list.size()) {
                    isLoadedAllNetData = true;
                    mSwipeRefreshHelper.setNoMoreData();
                    mSwipeRefreshHelper.setLoadMoreEnable(false);
                } else {
                    isLoadedAllNetData = false;
                }
                break;
            case LOAD_MORE:
                for (int i = 0; i < list.size(); i++) {
                    mDataAdapter.add(list.get(i));
                    mAllDataList.add(list.get(i));
                }
                mDataAdapter.notifyDataSetChanged();

                if (mPageSize > list.size()) {
                    isLoadedAllNetData = true;
                    mSwipeRefreshHelper.setNoMoreData();
                } else {
                    isLoadedAllNetData = false;
                }
                break;
        }
    }


    /**
     * 成功的请求结果
     */


    /**
     * listView   的适配器
     */
    public class DataAdapter extends ArrayAdapter<O> {
        public DataAdapter(Context context) {
            super(context, getItemLayoutId(), getItemTextViewResourceId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            O item = getItem(position);
            View result;
            if (item instanceof IListModel) {
                IListModel.STATE state = ((IListModel) item).getState();
                android.util.Log.d(TAG, "getView: " + state.value);
                switch (state) {
                    case ASK_ED_CANNOT_ACCESS:
                    case ASK_ED_FAIL:
                    case ASK_ED_ERROR:
                    case ASK_ED_EMPTY:
                        result = getAbnormalView(state);
                        break;
                    case ASK_ED_AVAILABILITY:
                    default:
                        result = bindView(position, super.getView(position, convertView, parent), parent);
                        break;
                }
            } else {
                result = bindView(position, super.getView(position, convertView, parent), parent);
            }
            return result;
        }

        @Override
        public int getViewTypeCount() {
            return IListModel.STATE.ASK_ED_AVAILABILITY.value + 1;
        }

        @Override
        public int getItemViewType(int position) {
            O item = getItem(position);
            int result = IListModel.STATE.ASK_ED_AVAILABILITY.value;
            if (item instanceof IListModel) {
                IListModel.STATE state = ((IListModel) item).getState();
                result = state.value;
            }
            return result;
        }
    }


    TextView abnormalView = new TextView(mContext);

    /**
     * 返回异常状态下的显示布局
     *
     * @param state
     * @return
     */
    public View getAbnormalView(IListModel.STATE state) {
        switch (state) {
            case ASK_ED_CANNOT_ACCESS:
                abnormalView.setText("无法访问网络");
                break;
            case ASK_ED_FAIL:
                abnormalView.setText("无法访问服务器");
                break;
            case ASK_ED_ERROR:
                abnormalView.setText("请求参数错误");
                break;
            case ASK_ED_EMPTY:
                abnormalView.setText("数据为空");
                break;
        }
        return abnormalView;
    }

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
}
