package panda.android.lib.net;

import panda.android.lib.base.model.BaseModel;

/**
 * Created by shitianci on 16/8/24.
 */
public class BaseListModel extends BaseModel {

    public enum STATE {
        ASK_PRE(-3), // 请求前
        ASK_ING(-2), // 请求中，有两种情形不会进入这种状态：1. 上一个网络请求还在进行中；2. 手机网络没有打开
        ASK_ED(-1), // 请求结束
        ASK_ED_CANNOT_ACCESS(0), //无法访问网络；
        ASK_ED_FAIL(1),   //服务器连接失败；
        ASK_ED_ERROR(2), //请求参数异常
        ASK_ED_EMPTY(3), //数据为空；
        ASK_ED_AVAILABILITY(4),; //获取到有效数据

        public final int value;

         STATE(int i) {
            value = i;
        }
    }

    public BaseListModel.STATE state = STATE.ASK_ED_AVAILABILITY;

}
