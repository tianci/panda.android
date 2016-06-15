package panda.android.demo.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import panda.android.lib.base.model.ListNetResultInfo;

/**
 * Created by shitianci on 16/1/27.
 */
public class ChannelModelsNetResultInfo extends ListNetResultInfo<ChannelModel> {

    private List<ChannelModel> channel_list;

    public void setChannel_list(List<ChannelModel> channel_list) {
        this.channel_list = channel_list;
    }

    public List<ChannelModel> getChannel_list() {
        return channel_list;
    }

    @Override
    public List<ChannelModel> getList() {
        return channel_list;
    }

    public static ChannelModelsNetResultInfo getMockData() {
        ChannelModelsNetResultInfo result = new ChannelModelsNetResultInfo();
        result.setRespCode(RETURN_CODE_000000);
        result.setRespDesc("OK");
        result.channel_list = new ArrayList<>();
        ChannelModel test = null;
        for (int i = 0; i < 3; i++){
            test = getChannelModel(i);
            result.channel_list.add(test);
        }
        return result;
    }

    @NonNull
    private static ChannelModel getChannelModel(int i) {
        ChannelModel test;
        test = new ChannelModel();
        test.setName("频道类别"+i);
        test.setList(RtspModelsNetResultInfo.getMockData().getList());
        return test;
    }
}
