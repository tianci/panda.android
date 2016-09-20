package panda.android.lib.net;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterfork.OnClick;
import panda.android.lib.B;
import panda.android.lib.R;
import panda.android.lib.base.control.SimpleSafeTask;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.Log;

/**
 * Created by admin on 2016/9/20.
 *
 * 用于常见的网络请求的界面
 * 处理了
 * 1. 加载界面
 * 2. 参数异常界面
 * 3. 加载失败页面
 * 4. 无法访问网络
 *
 * 提供了空数据和有效数据的接口
 *
 */
public abstract class NetFragment<T extends NetResultInfo> extends BaseFragment {
    private static final String TAG = NetFragment.class.getSimpleName();

    protected View mViewProgress = null;  //加载界面
    protected View mViewResult = null; //有效界面
    protected View mViewNoResult = null; //空数据界面
    protected View mViewError = null; //参数异常界面
    protected View mViewFail = null; //加载失败页面
    protected View mViewCannotAccess = null; //无法访问网络

    protected SimpleSafeTask<T> netTask = null;
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

    public NetController mNetController;//网络请求控制器


    @Override
    public int getLayoutId() {
        return R.layout.panda_fragment_net;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View createdView = super.onCreateView(inflater, container,
                savedInstanceState);
        mViewProgress = createdView.findViewById(R.id.net_progress);
        mViewResult = createdView.findViewById(R.id.net_result);
        mViewNoResult = createdView.findViewById(R.id.net_no_result);
        mViewError = createdView.findViewById(R.id.net_error);
        mViewFail = createdView.findViewById(R.id.net_fail);
        mViewCannotAccess = createdView.findViewById(R.id.net_cannot_access);
        mViewArray = new View[]{mViewProgress, mViewResult, mViewNoResult, mViewError, mViewFail, mViewCannotAccess};

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

        mNetController = new MyNetController(getActivity());
        return createdView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNetController.loadNetData();
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

    @OnClick(B.id.net_no_result)
    public void clickNetNoResult() {
        mNetController.loadNetData();
    }

    @OnClick(B.id.net_error)
    public void clickNetError() {
        mNetController.loadNetData();
    }

    @OnClick(B.id.net_fail)
    public void clickNetFail() {
        mNetController.loadNetData();
    }

    @OnClick(B.id.net_cannot_access)
    public void clickNetCannotAccess() {
        mNetController.loadNetData();
    }

    /**
     * -------------------------
     * END
     * -------------------------
     */


    private class MyNetController extends NetController<T> {
        public MyNetController(FragmentActivity activity) {
            super(activity);
        }

        @Override
        public void loadNetData() {
            super.loadNetData();
        }

        @Override
        protected boolean isEmpty(NetResultInfo result) {
            return false;
        }

        @Override
        protected void showData(BaseListModel.STATE state, T result) {
            switch (state) {
                case ASK_PRE:
                    break;
                case ASK_ING:
                    showOnlyView(R.id.net_progress, false);
                    break;
                case ASK_ED:
                    break;
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
                case ASK_ED_AVAILABILITY:
                    NetFragment.this.showSuccessData(state,result);
                    break;
            }
        }
        @Override
        protected T onDoInBackgroundSafely() {
            return NetFragment.this.onDoInBackgroundSafely();
        }



    }








    /**
     * 展示网络请求成功后的数据   包含为空的情况
     */
    public void showSuccessData(BaseListModel.STATE state, T result){
        showOnlyView(R.id.net_result, false);
        NetFragment.this.onDisplayResult(result);
    }


    /**
     * 请求数据
     */
    public abstract T onDoInBackgroundSafely();

    /**
     * 数据成功  包含为空的情况
     */
    protected abstract void onDisplayResult(T var1);



}
