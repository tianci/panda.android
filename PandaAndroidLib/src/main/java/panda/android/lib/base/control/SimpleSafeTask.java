package panda.android.lib.base.control;

import android.content.Context;


/**
 * 简单的安全异步任务，仅仅指定返回结果的类型，不可输入参数（方便和调度器调度使用）
 */
public abstract class SimpleSafeTask<T> extends BaseAsyncTask<Object, Object, T> {
    public SimpleSafeTask(Context context) {
		super(context);
	}

	protected abstract T doInBackgroundSafely() throws Exception;

    //@Override
    //protected void onPreExecuteSafely() throws Exception {}

    @Override
    protected final T doInBackgroundSafely(Object... params) throws Exception {
        return doInBackgroundSafely();
    }

    //@Override
    //protected void onPostExecuteSafely(T result, Exception e) throws Exception {}
}
