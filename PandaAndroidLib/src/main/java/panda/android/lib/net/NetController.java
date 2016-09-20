package panda.android.lib.net;

import android.content.Context;

import panda.android.lib.base.control.SimpleSafeTask;
import panda.android.lib.base.model.NetResultInfo;
import panda.android.lib.base.model.net.BaseRepositoryCollection;
import panda.android.lib.base.util.Log;
import panda.android.lib.net.BaseListModel.STATE;

/**
 * Created by shitianci on 16/8/23.
 * <p/>
 * 用于加载网络数据  不同请求结果的处理
 */
public abstract class NetController<T extends NetResultInfo> {
    private static final String TAG = NetController.class.getSimpleName();
    protected final Context mContext;

    boolean loadingNetData = false;  //

    public NetController(Context context) {
        mContext = context;
    }

    /**
     * 网络请求
     */
    public void loadNetData() {
        showData(STATE.ASK_PRE, null);
        if (loadingNetData) {
            Log.d(TAG, "loadNetData, loadingNetData = " + loadingNetData);
            showData(STATE.ASK_ED, null);
            return;
        }
        loadingNetData = true;
        if (!BaseRepositoryCollection.tryToDetectNetwork(mContext)) {
            loadingNetData = false;
            showData(STATE.ASK_ED, null);
            showData(STATE.ASK_ED_CANNOT_ACCESS, null);
            return;
        }

        SimpleSafeTask<T> netTask = new SimpleSafeTask<T>(mContext) {

            protected void onPreExecuteSafely() throws Exception {
                showData(STATE.ASK_ING, null);
            }

            @Override
            protected T doInBackgroundSafely() throws Exception {
                T result = onDoInBackgroundSafely();
                return result;
            }

            @Override
            protected void onPostExecuteSafely(T result, Exception e) {
                showData(STATE.ASK_ED, null);
                super.onPostExecuteSafely(result, e);
                loadingNetData = false;
                if (e != null || result == null) {
                    showData(STATE.ASK_ED_FAIL, result);
                    return;
                }
                if (result.getRespCode() != NetResultInfo.RETURN_CODE_000000) {
                    showData(STATE.ASK_ED_ERROR, result);
                    return;
                }
                if (isEmpty(result)) {
                    showData(STATE.ASK_ED_EMPTY, result);
                    return;
                }
                showData(STATE.ASK_ED_AVAILABILITY, result);
            }

            protected void onCancelled() {
                loadingNetData = false;
                showData(STATE.ASK_ED, null);
                showData(STATE.ASK_ED_FAIL, null);
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
     * 加载后台数据
     *
     * @return
     */
    protected abstract T onDoInBackgroundSafely();

    /**
     * 数据是否为空
     * 针对list情形，如果list size为0，则返回为true。
     *
     * @param result
     * @return
     */
    protected abstract boolean isEmpty(T result);

    /**
     * 返回状态
     *
     * @param state
     * @param result
     */
    protected abstract void showData(STATE state, T result);

    /**
     * -------------------------
     * END
     * -------------------------
     */
}
