package panda.android.lib.base.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import panda.android.lib.R;
import panda.android.lib.base.control.SimpleSafeTask;
import panda.android.lib.base.model.ListNetResultInfo;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.ui.UIUtil;
import panda.android.lib.base.util.Log;

/**
 * 加载网页数据的通用模型，包含 进度条控制、结果显示控制。
 *
 * @param <T>
 * @author shitianci
 *         变动日志：
 *         1、增加对Dialog的支持
 */
public abstract class NetFragment<T extends NetResultInfo> extends BaseFragment {

    private static final String TAG = NetFragment.class.getSimpleName();
    protected View mViewProgress = null;
    protected Dialog mDialogProgress = null;
    protected View mViewResult = null;
    protected View mViewNoResult = null;
    protected SimpleSafeTask<T> netTask = null;

    private boolean enableDialogProgress;
    protected BGARefreshLayout mRefreshLayout;

    protected abstract T onDoInBackgroundSafely();


    @Override
    public int getLayoutId() {
        return R.layout.panda_fragment_net;
    }

    /**
     * 是否自动显示加载框
     *
     * @param enableDialogProgress
     */
    public void setEnableDialogProgress(boolean enableDialogProgress) {
        this.enableDialogProgress = enableDialogProgress;
    }

    /**
     * 设置正在加载显示的view
     * 直接采用 R.id.net_progress 标识
     *
     * @param view the mViewNoResult to set
     */
    @Deprecated
    public void setViewProgress(View view) {
        this.mViewProgress = view;
    }

    /**
     * 设置有结果时显示的view
     * 直接使用R.id.net_result标识
     *
     * @param view the mViewNoResult to set
     */
    @Deprecated
    public void setViewResult(View view) {
        this.mViewResult = view;
    }

    /**
     * 设置没有结果时显示的view
     * 直接使用 R.id.net_no_result 标识
     *
     * @param view the mViewNoResult to set
     */
    @Deprecated
    public void setViewNoResult(View view) {
        this.mViewNoResult = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (false) {
            /**
             * 在机务段中出现过这样一个bug：
             * 描述：      切换账号后，mViewResult残留上个版本的值。而savedInstanceState是为null，
             * 原因：      可能跟Android或者java内部的对象复用机制有关。
             * 处理措施：  对象必须初始化为null。
             */
            Log.w(TAG, "onCreateView, mViewResult = " + mViewResult);
            Log.w(TAG, "onCreateView, savedInstanceState = " + savedInstanceState);
            Log.w(TAG, "onCreateView, mViewResult = " + mViewResult);
        }
        View createdView = super.onCreateView(inflater, container,
                savedInstanceState);
        //如果顶层没有设置的话，尝试寻找默认的view
        mViewProgress = createdView.findViewById(R.id.net_progress);
        mViewResult = createdView.findViewById(R.id.net_result);
        mViewNoResult = createdView.findViewById(R.id.net_no_result);
        mDialogProgress = UIUtil.getLoadingDlg(getActivity(), true);
        mDialogProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                netTask.cancel(true);
                netTask = null;
                exit();
            }
        });
        configRefreshLayout(createdView);
        if (mRefreshLayout != null || mViewNoResult != null) {
            enableDialogProgress = false;
        } else {
            enableDialogProgress = true;
        }
        hiddenNoResult();
        hiddenProgress();
//		hiddenResult();
        return createdView;
    }

    protected void configRefreshLayout(View createdView) {
        mRefreshLayout = (BGARefreshLayout) createdView.findViewById(R.id.net_refresh);
        if (mRefreshLayout == null) {
            return;
        }
//        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
//        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                loadNetData();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return false;
            }
        });
    }


//    private void configPull() {
//        if(ptrClassicFrameLayout == null){
//            return;
//        }
//        ptrClassicFrameLayout.setPtrHandler(new PtrHandler() {
//			@Override
//			public void onRefreshBegin(PtrFrameLayout frame) {
//				loadNetData();
//			}
//
//			@Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                /**
//                 * 经过测验：frame包括content和header两个子元素，frame[0]=content, frame[0]=header
//                 */
//                return !loadingNetData && checkContentCanBePulledDown(mViewResult);
//            }
//
//            public boolean checkContentCanBePulledDown(View content) {
//                /**
//                 * 如果 Content 不是 ViewGroup，返回 true,表示可以下拉</br>
//                 * 例如：TextView，ImageView
//                 */
//                if (!(content instanceof ViewGroup)) {
//                    return true;
//                }
//
//                ViewGroup viewGroup = (ViewGroup) content;
//
//                /**
//                 * 如果 Content 没有子 View（内容为空）时候，返回 true，表示可以下拉
//                 */
//                if (viewGroup.getChildCount() == 0) {
//                    return true;
//                }
//
//                /**
//                 * 如果 Content 是 AbsListView（ListView，GridView），当第一个 item 不可见是，返回 false，不可以下拉。
//                 */
//                if (viewGroup instanceof AbsListView) {
//                    AbsListView listView = (AbsListView) viewGroup;
//                    if (listView.getFirstVisiblePosition() > 0) {
//                        return false;
//                    }
//                }
//
//                /**
//                 * 如果 SDK 版本为 14 以上，可以用 canScrollVertically 判断是否能在竖直方向上，向上滑动</br>
//                 * 不能向上，表示已经滑动到在顶部或者 Content 不能滑动，返回 true，可以下拉</br>
//                 * 可以向上，返回 false，不能下拉
//                 */
//                if (Build.VERSION.SDK_INT >= 14) {
//                    return !content.canScrollVertically(-1);
//                } else {
//                    /**
//                     * SDK 版本小于 14，如果 Content 是 ScrollView 或者 AbsListView,通过 getScrollY 判断滑动位置 </br>
//                     * 如果位置为 0，表示在最顶部，返回 true，可以下拉
//                     */
//                    if (viewGroup instanceof ScrollView) {
//                        return viewGroup.getScrollY() == 0;
//                    }
//
//                    if (viewGroup instanceof AbsListView) {
//                        final AbsListView absListView = (AbsListView) viewGroup;
//                        return absListView.getChildCount() > 0
//                                && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
//                                .getTop() < absListView.getPaddingTop());
//                    }
//                }
//
//                /**
//                 * 最终判断，判断第一个子 View 的 top 值</br>
//                 * 如果第一个子 View 有 margin，则当 top==子 view 的 marginTop+content 的 paddingTop 时，表示在最顶部，返回 true，可以下拉</br>
//                 * 如果没有 margin，则当 top==content 的 paddinTop 时，表示在最顶部，返回 true，可以下拉
//                 */
//                View child = viewGroup.getChildAt(0);
//                ViewGroup.LayoutParams glp = child.getLayoutParams();
//                int top = child.getTop();
//                if (glp instanceof ViewGroup.MarginLayoutParams) {
//                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) glp;
//                    return top == mlp.topMargin + viewGroup.getPaddingTop();
//                } else {
//                    return top == viewGroup.getPaddingTop();
//                }
//            }
//        });
//		ptrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
//
//		// the following are default settings
//		ptrClassicFrameLayout.setResistance(1.7f);
//		ptrClassicFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
//		ptrClassicFrameLayout.setDurationToClose(200);
//		ptrClassicFrameLayout.setDurationToCloseHeader(1000);
//		// default is false
//		ptrClassicFrameLayout.setPullToRefresh(false);
//		// default is true
//		ptrClassicFrameLayout.setKeepHeaderWhenRefresh(true);
//
//		// scroll then refresh
//		// comment in base fragment
//		ptrClassicFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ptrClassicFrameLayout.autoRefresh();
//            }
//        }, 150);
//	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mRefreshLayout == null) {
            loadNetData();
        } else {
            mRefreshLayout.beginRefreshing();
        }
    }

    boolean loadingNetData = false;

    public void loadNetData() {
        if (loadingNetData) {
            return;
        }
        loadingNetData = true;
        netTask = new SimpleSafeTask<T>(getActivity()) {

            protected void onPreExecuteSafely() throws Exception {
                showProgress();
                hiddenNoResult();
            }

            @Override
            protected T doInBackgroundSafely() throws Exception {
                T result = onDoInBackgroundSafely();
                return result;
            }

            @Override
            protected void onPostExecuteSafely(T result, Exception e) {
                hiddenProgress();
                hiddenResult();
                super.onPostExecuteSafely(result, e);
                if (e != null || result == null) {
                    showNetErrResult();
                    return;
                }
                hiddenNetErrResult();
                if (result.getRespCode() != NetResultInfo.RETURN_CODE_000000) {
                    showNoResult();
                    return;
                }
                hiddenNoResult();
                if (result instanceof ListNetResultInfo && ((ListNetResultInfo) result).getList().size() == 0) {
                    showNoResult();
                    return;
                }
                hiddenNoResult();
                showResult(result);
            }

            protected void onCancelled() {
                hiddenProgress();
            }

            ;

        };
        netTask.execute();
        return;
    }

    protected void showProgress() {
        Log.d(TAG, "showProgress, mViewProgress = " + mViewProgress);
        if (mRefreshLayout != null){
            mRefreshLayout.setVisibility(View.VISIBLE);
        }
        if (mViewProgress != null) {
            mViewProgress.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "showProgress, mDialogProgress = " + mDialogProgress);
        if (enableDialogProgress && mDialogProgress != null) {
            mDialogProgress.show();
            mDialogProgress.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (netTask != null && !netTask.isCancelled()) {
                        netTask.cancel(true);
                        netTask = null;
                    }
                }
            });
        }
    }

    protected void hiddenProgress() {
        Log.d(TAG, "hiddenProgress, mViewProgress = " + mViewProgress);
        if (mViewProgress != null) {
            mViewProgress.setVisibility(View.GONE);
        }
        if (enableDialogProgress && mDialogProgress != null) {
            mDialogProgress.dismiss();
        }
        loadingNetData = false;
        if (mRefreshLayout != null) {
            mRefreshLayout.endRefreshing();
            mRefreshLayout.endLoadingMore();
        }
    }

    protected void showResult(T result) {
        hiddenNoResult();
        showResult();
    }

    protected void showResult() {
        Log.d(TAG, "showResult");
        if (mRefreshLayout != null) {
            mRefreshLayout.setVisibility(View.VISIBLE);
        }
        if (mViewResult != null) {
            mViewResult.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 访问网络失败
     */
    public void showNetErrResult() {
        // TODO: 15/11/19 需要增加独立的页面
        Log.d(TAG, "showNetErrResult");
        if (mViewNoResult != null) {
            mViewNoResult.setVisibility(View.VISIBLE);
        }
    }

    private void hiddenNetErrResult() {
        // TODO: 15/11/19 需要增加独立的页面
        hiddenNoResult();
    }


    protected void hiddenResult() {
        Log.d(TAG, "hiddenResult");
        if (mRefreshLayout != null) {
            mRefreshLayout.setVisibility(View.GONE);
        }
        if (mViewResult != null) {
            mViewResult.setVisibility(View.GONE);
        }
    }

    protected void showNoResult() {
        Log.d(TAG, "showNoResult");
        if (mViewNoResult != null) {
            mViewNoResult.setVisibility(View.VISIBLE);
        }
    }

    @Deprecated
    protected void showNoResult(T result) {
        Log.d(TAG, "showNoResult");
        showNoResult();
    }

    protected void hiddenNoResult() {
        Log.d(TAG, "hiddenNoResult");
        if (mViewNoResult != null) {
            mViewNoResult.setVisibility(View.GONE);
        }
    }

    /**
     * 获取进度加载框
     *
     * @return the mDialogProgress
     */
    public Dialog getDialogProgress() {
        return mDialogProgress;
    }

    /**
     * 设置进度加载框
     *
     * @param dialogProgress the dialogProgress to set
     */
    public void setDialogProgress(Dialog dialogProgress) {
        this.mDialogProgress = dialogProgress;
    }

}
