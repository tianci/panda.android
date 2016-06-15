package panda.android.demo.model;

import panda.android.lib.base.model.BaseModel;

/**
 * Created by shitianci on 16/1/27.
 */
public class RtspModel extends BaseModel {
    private int id;
    private String title;
    private int num;
    private String pic;
    private String url;
    private String hlsUrl;

    private String type;
    private Object otherid;
    private String livetype;

    private String channel_name; //所属频道
    private int channel_pos;  //所属频道的位置
    private int channel_num;  //所属频道包含的channel数据

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getNum() {
        return num;
    }

    public String getPic() {
        return pic;
    }

    public String getUrl() {
        return url;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String mChannel_name) {
        channel_name = mChannel_name;
    }

    public int getChannel_pos() {
        return channel_pos;
    }

    public void setChannel_pos(int mChannel_pos) {
        channel_pos = mChannel_pos;
    }

    public int getChannel_num() {
        return channel_num;
    }

    public void setChannel_num(int mChannel_num) {
        channel_num = mChannel_num;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String mHlsUrl) {
        hlsUrl = mHlsUrl;
    }
}
