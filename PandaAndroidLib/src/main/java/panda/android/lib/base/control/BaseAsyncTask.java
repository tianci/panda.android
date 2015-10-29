package panda.android.lib.base.control;

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
	
	public BaseAsyncTask(Context context) {
		this.setContext(context);
	}

	@Override
	protected void onPostExecuteSafely(Result result, Exception e) {
		if(e == null){
			return;
		}
		Log.d(TAG, e.getMessage());
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
