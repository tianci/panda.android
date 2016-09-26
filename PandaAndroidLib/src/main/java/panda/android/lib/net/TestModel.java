package panda.android.lib.net;

import java.util.ArrayList;
import java.util.List;

import panda.android.lib.base.model.BaseModel;
import panda.android.lib.base.model.ListNetResultInfo;
import panda.android.lib.base.model.NetResultInfo;

/**
 * Created by shitianci on 16/8/5.
 */
public class TestModel extends BaseModel implements IListModel {

    private static List<TestModel> list = null;

    private int id;
    private String name;

    public TestModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static ListNetResultInfo<TestModel> getListNetResultInfo(final int count) {

        ListNetResultInfo<TestModel> listNetResultInfo = new ListNetResultInfo<TestModel>() {
            @Override
            public List<TestModel> getList() {
//                if (list == null) {
                list = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    TestModel tm = new TestModel(i, "name" + i);
                    list.add(tm);
                }
//                }
                return list;
            }
        };
        listNetResultInfo.setRespCode(NetResultInfo.RETURN_CODE_000000);
        return listNetResultInfo;

    }

    @Override
    public STATE getState() {
        return STATE.ASK_ED_AVAILABILITY;
    }
}
