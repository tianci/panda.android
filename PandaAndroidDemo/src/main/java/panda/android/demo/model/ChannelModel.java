package panda.android.demo.model;

import java.util.List;

import panda.android.lib.base.model.BaseModel;

/**
 * Created by shitianci on 16/1/27.
 */
public class ChannelModel extends BaseModel {

    private String name;
    private int size;
    private List<RtspModel> list;

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setList(List<RtspModel> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public List<RtspModel> getList() {
        return list;
    }
}
