package panda.android.lib.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterfork.OnClick;
import panda.android.lib.B;
import panda.android.lib.R;
import panda.android.lib.base.control.SimpleSafeTask;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.model.net.BaseRepositoryCollection;

import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.Log;

/**
 * 加载网页数据的通用模型，包含 进度条控制、结果显示控制。
 *
 * @param <T>
 * @author shitianci
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
        return createdView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadNetData(false);
    }

    boolean loadingNetData = false;

    public void loadNetData() {
        loadNetData(false);
    }

    public void loadNetData(final boolean isLoadingMore) {
        Log.d(TAG, "loadNetData, loadingNetData = " + loadingNetData);
        Log.d(TAG, "loadNetData, isLoadingMore = " + isLoadingMore);
        onPreloadNetData(isLoadingMore);
        if (loadingNetData) {
            onPostLoadNetData(isLoadingMore);
            return;
        }
        if (!BaseRepositoryCollection.tryToDetectNetwork(getActivity())) {
            showOnlyView(R.id.net_cannot_access, isLoadingMore);
            onPostLoadNetData(isLoadingMore);
            return;
        }
        netTask = new SimpleSafeTask<T>(getActivity()) {

            protected void onPreExecuteSafely() throws Exception {
                loadingNetData = true;
                showOnlyView(R.id.net_progress, isLoadingMore);
            }

            @Override
            protected T doInBackgroundSafely() throws Exception {
                T result = onDoInBackgroundSafely();
                return result;
            }

            @Override
            protected void onPostExecuteSafely(T result, Exception e) {
                onPostLoadNetData(isLoadingMore);
                super.onPostExecuteSafely(result, e);
                loadingNetData = false;
                if (e != null || result == null) {
                    showOnlyView(R.id.net_fail, isLoadingMore);
                    return;
                }
                if (result.getRespCode() != NetResultInfo.RETURN_CODE_000000) {
                    showOnlyView(R.id.net_error, isLoadingMore);
                    return;
                }
                showOnlyView(R.id.net_result, isLoadingMore);
                onDisplayResult(result);
            }

            protected void onCancelled() {
                loadingNetData = false;
                showOnlyView(R.id.net_fail, isLoadingMore);
            }


        };
        netTask.execute();
        return;
    }

    /**
     * -------------------------
     * START: 最重要的流程方法
     * -------------------------
     */


    /**
     * 开始加载网络数据(这个方法一定会调用，方便上层开始相应的动画)
     * @param isLoadingMore
     */
    public void onPreloadNetData(boolean isLoadingMore) {
    }


    /**
     * 加载后台数据
     *
     * @return
     */
    protected abstract T onDoInBackgroundSafely();


    /**
     * 显示加载的正常数据（如果数据异常或者网络异常，则不执行该方法）
     *
     * @param result
     */
    protected abstract void onDisplayResult(T result);

    /**
     * 网络数据加载结束(这个方法一定会调用，方便上层中止相应的动画)
     * @param isLoadingMore 是否正在加载下一页
     */
    public void onPostLoadNetData(boolean isLoadingMore) {

    }

    /**
     * -------------------------
     * END
     * -------------------------
     */



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
                Log.d(TAG, "showOnlyView, i = " + i);
                Log.d(TAG, "showOnlyView, isLoadingMore = " + isLoadingMore);
                if (!isLoadingMore){
                    v.setVisibility(View.VISIBLE);
                }
                else{
                    if (viewId == R.id.net_fail || viewId == R.id.net_error || viewId == R.id.net_cannot_access) {
                        DevUtil.showInfo(getActivity(), getString(mViewToastString[i-1]));
                    }
                    return;
                }


            } else {
                if (!isLoadingMore){
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
    public void clickNetNoResult(){
        loadNetData();
    }

    @OnClick(B.id.net_error)
    public void clickNetError(){
        loadNetData();
    }

    @OnClick(B.id.net_fail)
    public void clickNetFail(){
        loadNetData();
    }

    @OnClick(B.id.net_cannot_access)
    public void clickNetCannotAccess(){
        loadNetData();
    }

    /**
     * -------------------------
     * END
     * -------------------------
     */

}
