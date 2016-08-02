package panda.android.lib.base.control;

import panda.android.lib.base.model.NetResultInfo;

/**
 * Created by shitianci on 16/8/2.
 */
public class NetResultInfoEvent extends BaseEvent  {

    private final NetResultInfo result;

    public NetResultInfoEvent(NetResultInfo result) {
        this.result = result;
    }

    public NetResultInfo getResult() {
        return result;
    }
}
