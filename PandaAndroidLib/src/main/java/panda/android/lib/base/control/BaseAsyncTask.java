package panda.android.lib.base.control;

import android.app.Dialog;
import android.content.Context;

import com.litesuits.android.async.SafeTask;

import panda.android.lib.base.util.Log;


/**
 * 基础的异步任务类
 * @author Administrator
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends SafeTask<Params, Progress, Result> {

	private static final String TAG = BaseAsyncTask.class.getSimpleName();
	private Context context;
    private Dialog mLoadingDlg;
	
	public BaseAsyncTask(Context context) {
		this(context, null);
	}

	public BaseAsyncTask(Context context, Dialog loadingDlg) {
		setContext(context);
        mLoadingDlg = loadingDlg;
	}

    @Override
    protected void onPreExecuteSafely() throws Exception {
        super.onPreExecuteSafely();
        if (mLoadingDlg != null){
            mLoadingDlg.show();
        }
    }

    @Override
	protected void onPostExecuteSafely(Result result, Exception e) {
        if (mLoadingDlg != null){
            mLoadingDlg.dismiss();
        }
		if(e == null){
			return;
		}
		Log.d(TAG, e.getMessage());
	}

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mLoadingDlg != null){
            mLoadingDlg.dismiss();
        }
    }

    public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
