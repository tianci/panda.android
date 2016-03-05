package panda.android.demo.model;

import panda.android.lib.base.model.NetResultInfo;

/**
 * Created by shitianci on 16/3/5.
 */
public class SimpleModel extends NetResultInfo {
    String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String mInfo) {
        info = mInfo;
    }
}
