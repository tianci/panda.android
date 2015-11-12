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
	private View mViewProgress;
	private Dialog mDialogProgress;
	private View mViewResult;
	private View mViewNoResult;
	private SimpleSafeTask<T> netTask;
	
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
		View createdView = super.onCreateView(inflater, container,
				savedInstanceState);
		//如果顶层没有设置的话，尝试寻找默认的view
		if (mViewProgress == null) {
			mViewProgress = createdView.findViewById(R.id.net_progress);
		}
		if (mViewResult == null) {
			mViewResult = createdView.findViewById(R.id.net_result);
		}
		if (mViewNoResult == null) {
			mViewNoResult = createdView.findViewById(R.id.net_no_result);
		}
		mDialogProgress = UIUtil.getLoadingDlg(getActivity(), false);
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
				super.onPostExecuteSafely(result, e);
				if (result == null
						|| result.getRespCode() != NetResultInfo.RETURN_CODE_000000) {
					showNoResult(result);
					return;
				}
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
		showResult();
	}

	protected void showResult() {
		if (mViewResult != null) {
			mViewResult.setVisibility(View.VISIBLE);
		}
	}

	protected void hiddenResult() {
		if (mViewResult != null) {
			mViewResult.setVisibility(View.GONE);
		}
	}

	protected void showNoResult(T result) {
		if (mViewNoResult != null) {
			mViewNoResult.setVisibility(View.VISIBLE);
		}
	}

	protected void hiddenNoResult() {
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
