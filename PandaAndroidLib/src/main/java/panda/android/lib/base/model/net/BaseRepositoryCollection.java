package panda.android.lib.base.model.net;

/**
 * 网络连接基类，对于json格式数据的读取只需1行代码
 *
 * @author shitianci
 */
public class BaseRepositoryCollection {
    @SuppressWarnings("serial")
    public static class NetException extends NullPointerException {

        public NetException(String canNotAccessDateByNetWork) {
            super(canNotAccessDateByNetWork);
        }

    }
    private static final String TAG = BaseRepositoryCollection.class.getSimpleName();

}
