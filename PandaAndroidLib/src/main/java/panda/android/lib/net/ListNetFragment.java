package panda.android.lib.net;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import panda.android.lib.R;
import panda.android.lib.base.model.ListNetResultInfo;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.Log;

/**
 * Created by admin on 2016/9/20.
 */
public abstract class ListNetFragment<O> extends BaseFragment {
    private static final String TAG = ListNetFragment.class.getSimpleName();
    public View headView;
    public ListNetController mNetController; //网络加载控制器


    protected View mViewNoResult = null; //空数据界面
    protected View mViewError = null; //参数异常界面
    protected View mViewFail = null; //加载失败页面
    protected View mViewCannotAccess = null; //无法访问网络

    protected View[] mViewArray;
    private int[] mViewArrayID = new int[]{
            R.id.net_progress, //加载界面
            R.id.net_result, //有效界面
            R.id.net_no_result, //空数据界面
            R.id.net_error, //参数异常界面
            R.id.net_fail, //加载失败页面
            R.id.net_cannot_access}; //无法访问网络

    private int[] mViewToastString = new int[]{
            R.string.net_progress, //加载界面
            R.string.net_result, //有效界面
            R.string.net_no_result, //空数据界面
            R.string.net_error, //参数异常界面
            R.string.net_fail, //加载失败页面
            R.string.net_cannot_access}; //无法访问网络


    //头布局Id
    public int getHeadLayoutId() {
        return 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.net_result);
        abnormalView = new TextView(getContext());
        initAbnormalView(); //初始化异常界面的布局

        mNetController = new MyListNetController(getActivity(),
                swipeRefreshLayout,
                (ListView) swipeRefreshLayout.findViewById(R.id.net_list));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mNetController.refresh();
            }
        });

        if (getHeadLayoutId() != 0) {
            headView = View.inflate(getContext(), getLayoutId(), null);
            mNetController.addHeadView(headView);
        }
        mNetController.setListAdapter();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mNetController.refresh();

    }

    public O getItem(int position) {
        if (mNetController.mDataAdapter == null) {
            Log.d(TAG, "dataAdapter 没有初始化");
            return null;
        }
        return (O) mNetController.mDataAdapter.getItem(position);
    }

    public View getHeadView() {
        return headView;
    }


    /**
     * 异常界面布局
     */
    public int getAbnormalViewLayoutId() {
        return R.layout.net_abnormal_view_layout;
    }

    /**
     * 初始化空界面
     */
    private View abnormalView;

    private void initAbnormalView() {
        abnormalView = View.inflate(getContext(), getAbnormalViewLayoutId(), null);
        mViewNoResult = abnormalView.findViewById(R.id.net_no_result);
        mViewError = abnormalView.findViewById(R.id.net_error);
        mViewFail = abnormalView.findViewById(R.id.net_fail);
        mViewCannotAccess = abnormalView.findViewById(R.id.net_cannot_access);
        mViewArray = new View[]{mViewNoResult, mViewError, mViewFail, mViewCannotAccess};

        mViewNoResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickNetNoResult();
            }
        });
        mViewError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickNetError();
            }
        });
        mViewFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickNetFail();
            }
        });
        mViewCannotAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickNetCannotAccess();
            }
        });
    }

    /**
     * 返回异常状态下的显示布局
     *
     * @param state
     * @return
     */
    public View getAbnormalView(IListModel.STATE state) {
        switch (state) {
            case ASK_ED_CANNOT_ACCESS:
                showOnlyView(R.id.net_cannot_access, false);
                break;
            case ASK_ED_FAIL:
                showOnlyView(R.id.net_fail, false);
                break;
            case ASK_ED_ERROR:
                showOnlyView(R.id.net_error, false);
                break;
            case ASK_ED_EMPTY:
                showOnlyView(R.id.net_no_result, false);
                break;
        }
        return abnormalView;
    }

    /**
     * 显示指定的View，剩下的界面都隐藏。
     *
     * @param isLoadingMore 是否启用加载更多模式，在加载第2……n次数据时，无需切换其它界面。
     */
    protected void showOnlyView(int viewId, boolean isLoadingMore) {
        int i = 0;
        for (View v : mViewArray) {
            i++;
            if (v == null) {
                continue;
            } else if (v.getId() == viewId) {
                Log.d(TAG, "showData, i = " + i);
                Log.d(TAG, "showData, isLoadingMore = " + isLoadingMore);
                if (!isLoadingMore) {
                    v.setVisibility(View.VISIBLE);
                } else {
                    if (viewId == R.id.net_fail || viewId == R.id.net_error || viewId == R.id.net_cannot_access) {
                        DevUtil.showInfo(getActivity(), getString(mViewToastString[i - 1]));
                    }
                    return;
                }
            } else {
                if (!isLoadingMore) {
                    v.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * -------------------------
     * START：各个主要界面的点击事件
     * -------------------------
     */


    public void clickNetNoResult() {
        mNetController.refresh();
    }

    public void clickNetError() {
        mNetController.refresh();
    }

    public void clickNetFail() {
        mNetController.refresh();
    }

    public void clickNetCannotAccess() {
        mNetController.refresh();
    }


    public class MyListNetController extends ListNetController<O> {
        public MyListNetController(Context context, SwipeRefreshLayout swipeRefreshLayout, ListView listView) {
            super(context, swipeRefreshLayout, listView);
        }

        @Override
        protected ListNetResultInfo onDoInBackgroundSafely() {
            return super.onDoInBackgroundSafely();

        }

        @Override
        protected O getErrItem(IListModel.STATE state) {
            return ListNetFragment.this.getErrItem(state);
        }

        @Override
        protected ListNetResultInfo<O> onDoInBackgroundSafely(int startIndex, int pageSize) {
            return ListNetFragment.this.onDoInBackgroundSafely(startIndex, pageSize);
        }

        @Override
        public int getItemTextViewResourceId() {
            return ListNetFragment.this.getItemTextViewResourceId();
        }

        @Override
        public int getItemLayoutId() {
            return ListNetFragment.this.getItemLayoutId();
        }

        @Override
        public View bindView(int position, View view, ViewGroup parent) {
            return ListNetFragment.this.bindView(position, view, parent);
        }

        @Override
        public View getAbnormalView(IListModel.STATE state) {
            return ListNetFragment.this.getAbnormalView(state);
        }

    }

    protected abstract O getErrItem(IListModel.STATE state);


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


}
