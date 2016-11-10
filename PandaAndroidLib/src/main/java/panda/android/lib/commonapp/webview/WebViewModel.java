package panda.android.lib.commonapp.webview;

import panda.android.lib.base.model.BaseModel;

/**
 * Created by admin on 2016/8/16.
 */
public class WebViewModel extends BaseModel {
    public static final int TYPE_URL = 1;
    public static final int TYPE_H5 = 2;
    private int type;
    private String title;
    private String shareTitle;
    private String data;   //   Url/h5
    private boolean isShare = false;//是否能分享  默认为No

    public WebViewModel() {
    }

    public WebViewModel(String title, String url) {
        this.type = TYPE_URL;
        this.isShare = false;
        this.title = title;
        this.data = url;
    }

    public WebViewModel(int type, String title, String data, boolean isShare) {
        this.type = type;
        this.title = title;
        this.data = data;
        this.isShare = isShare;
    }


    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public static WebViewModel getTestData() {
        WebViewModel model = new WebViewModel();
        model.setTitle("测试数据");
        model.setType(WebViewModel.TYPE_URL);
        model.setData("http://www.baidu.com");
        return model;
    }
}
