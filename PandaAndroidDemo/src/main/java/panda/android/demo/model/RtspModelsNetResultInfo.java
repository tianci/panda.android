package panda.android.demo.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import panda.android.lib.base.model.ListNetResultInfo;

/**
 * Created by shitianci on 16/1/27.
 */
public class RtspModelsNetResultInfo extends ListNetResultInfo<RtspModel> {
    /**
     * sEcho : 0
     * iTotalRecords : 0
     * iTotalDisplayRecords : 0
     * aaData : [{"id":1000,"title":"12","num":0,"pic":"http://192.168.1.107:8081/rtsp","url":"rtmp://example.com/xxxx/1000"},{"id":1001,"title":"asfdsd","num":0,"pic":"http://192.168.1.107:8081/rtsp","url":"rtmp://example.com/xxxx/1001"}]
     */

    private int sEcho;
    private int iTotalRecords;
    private int iTotalDisplayRecords;
    /**
     * id : 1000
     * title : 12
     * num : 0
     * pic : http://192.168.1.107:8081/rtsp
     * url : rtmp://example.com/xxxx/1000
     */

    private List<RtspModel> aaData;

    @Override
    public List<RtspModel> getList() {
        return aaData;
    }

    public static RtspModelsNetResultInfo getMockData() {
        RtspModelsNetResultInfo result = new RtspModelsNetResultInfo();
        result.setRespCode(RETURN_CODE_000000);
        result.setRespDesc("OK");
        result.aaData = new ArrayList<>();
        RtspModel test = null;
        for (int i = 0; i < 9; i++){
            test = getRtspModel();
            result.aaData.add(test);
        }
        return result;
    }

    @NonNull
    private static RtspModel getRtspModel() {
        RtspModel test;
        test = new RtspModel();
//        test.setUrl("rtmp://live.hkstv.hk.lxdns.com/live/hks");
//        test.setUrl("http://120.24.67.92/hls/hks.m3u8");
        test.setUrl("http://cache.utovr.com/201508270528174780.m3u8");
//        test.setUrl("http://120.24.67.92/hls/VRdemo01.m3u8");
//        test.setUrl("http://120.24.67.92/hls/10019.m3u8");
//        test.setUrl("http://120.24.67.92/hls/VRdemo02.m3u8");
        test.setTitle("测试频道");
        test.setPic("http://s3.fanxing.com/fxroomcover/20141122/20141122215858557909.jpg");
        return test;
    }

    public void setSEcho(int sEcho) {
        this.sEcho = sEcho;
    }

    public void setITotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public void setITotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public void setAaData(List<RtspModel> aaData) {
        this.aaData = aaData;
    }

    public int getSEcho() {
        return sEcho;
    }

    public int getITotalRecords() {
        return iTotalRecords;
    }

    public int getITotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public List<RtspModel> getAaData() {
        return aaData;
    }

}
