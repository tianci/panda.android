package panda.android.lib.base.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import panda.android.lib.R;
import panda.android.lib.base.control.SimpleSafeTask;
import panda.android.lib.base.model.ListNetResultInfo;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.ui.UIUtil;
import panda.android.lib.base.util.Log;

/**
 * 加载网页数据的通用模型，包含 进度条控制、结果显示控制。
 * @author shitianci
 * 变动日志：
 * 1、增加对Dialog的支持
 * @param <T> 
 */
public abstract class NetFragment<T extends NetResultInfo> extends BaseFragment {

	private static final String TAG = NetFragment.class.getSimpleName();
	protected View mViewProgress = null;
    protected Dialog mDialogProgress = null;
    protected View mViewResult = null;
    protected View mViewNoResult = null;
    protected SimpleSafeTask<T> netTask = null;
	
	protected abstract T onDoInBackgroundSafely();
	

	@Override
	public int getLayoutId() {
		return R.layout.panda_fragment_net;
	}

	/**
	 * 设置正在加载显示的view
	 * 
	 * @param view
	 *            the mViewNoResult to set
	 */
	public void setViewProgress(View view) {
		this.mViewProgress = view;
	}

	/**
	 * 设置有结果时显示的view
	 * 
	 * @param view
	 *            the mViewNoResult to set
	 */
	public void setViewResult(View view) {
		this.mViewResult = view;
	}

	/**
	 * 设置没有结果时显示的view
	 * 
	 * @param view
	 *            the mViewNoResult to set
	 */
	public void setViewNoResult(View view) {
		this.mViewNoResult = view;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        if(false){
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
		hiddenNoResult();
		hiddenProgress();
//		hiddenResult();
		return createdView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadNetData();
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
			};

			@Override
			protected T doInBackgroundSafely() throws Exception {
				return onDoInBackgroundSafely();
			}

			@Override
			protected void onPostExecuteSafely(T result, Exception e) {
				hiddenProgress();
                hiddenResult();
				super.onPostExecuteSafely(result, e);
				if (e != null || result == null){
					showNetErrResult();
					return;
				}
                hiddenNetErrResult();
				if (result.getRespCode() != NetResultInfo.RETURN_CODE_000000) {
                    showNoResult();
					return;
				}
                hiddenNoResult();
                if (result instanceof ListNetResultInfo && ((ListNetResultInfo)result).getList().size() == 0){
                    showNoResult(result);
                    return;
                }
                hiddenNoResult();
				showResult(result);
			}

			protected void onCancelled() {
				hiddenProgress();
			};

		};
		netTask.execute();
		return;
	}

    protected void showProgress() {
		Log.d(TAG, "showProgress, mViewProgress = " + mViewProgress);
		if (mViewProgress != null) {
			mViewProgress.setVisibility(View.VISIBLE);
		}
		Log.d(TAG, "showProgress, mDialogProgress = " + mDialogProgress);
		if (mDialogProgress != null) {
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
		if (mDialogProgress != null) {
			mDialogProgress.dismiss();
		}
		loadingNetData = false;
	}

	protected void showResult(T result) {
        hiddenNoResult();
        showResult();
	}

	protected void showResult() {
        Log.d(TAG, "showResult");
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
	 * @return the mDialogProgress
	 */
	public Dialog getDialogProgress() {
		return mDialogProgress;
	}

	/**
	 * 设置进度加载框
	 * @param dialogProgress the dialogProgress to set
	 */
	public void setDialogProgress(Dialog dialogProgress) {
		this.mDialogProgress = dialogProgress;
	}

}
