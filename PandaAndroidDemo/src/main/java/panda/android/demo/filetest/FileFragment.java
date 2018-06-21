package panda.android.demo.filetest;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;
import panda.android.demo.R;
import panda.android.demo.filetest.Cache.FileCacheOptions;
import panda.android.demo.filetest.Cache.LRUFileCache;
import panda.android.lib.base.model.net.BaseRepositoryCollection;
import panda.android.lib.base.ui.UIUtil;
import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.IntentUtil;

/**
 * 文件管理页面
 */

public class FileFragment extends BaseFragment {
    @BindView(R.id.btn_2)
    Button btn2;
    @BindView(R.id.btn_3)
    Button btn3;
    @BindView(R.id.btn_5)
    Button btn_5;
    @BindView(R.id.btn_4)
    Button btn_4;
    @BindView(R.id.btn_6)
    Button btn_6;
    @BindView(R.id.btn_7)
    Button btn_7;

    @BindView(R.id.tv_2)
    TextView tv2;

    @Override
    public int getLayoutId() {
        return R.layout.file_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        config();

        return rootView;
    }


    @OnClick(R.id.btn_2)
    public void btn2Click() {
        SDCardCheck sdCheck = new SDCardCheck();
        sdCheck.SDCardSize();
        long nFreeSize = sdCheck.getnSDFreeSize();
        long nTotalSize = sdCheck.getnSDTotalSize();
        tv2.setText("可用" + SDCardCheck.getSizeSting(nFreeSize) + "总共" + SDCardCheck.getSizeSting(nTotalSize) + " MB");
    }

    //下载
    @OnClick(R.id.btn_3)
    public void btn3Click() {
        String url = "http://games.ahgxtx.com/tvgame/Uploads/app/201604/572175e6c703f.apk";
        BaseRepositoryCollection.executeFileRequestAsync(url, Environment.getExternalStorageDirectory() + "/www.apk",
                new BaseRepositoryCollection.HttpListener<File>(getContext(), null, true, true, false) {


                    @Override
                    public void onStart(AbstractRequest<File> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onLoading(AbstractRequest<File> request, long total, long len) {
                        super.onLoading(request, total, len);
                        tv2.setText(SDCardCheck.getSizeSting(total) + "  " + SDCardCheck.getSizeSting(len));

                    }

                    @Override
                    public void onCancel(File data, Response<File> response) {
                        super.onCancel(data, response);
                        tv2.setText("onCancel");
                    }

                    @Override
                    public void onFailure(HttpException e, Response<File> response) {
                        super.onFailure(e, response);
                        tv2.setText("onFailure");
                    }
                });

    }

    //预警
    @OnClick(R.id.btn_4)
    public void btn4Click() {
//        SDSizeAlarmUtil.setAlarmSize(100, 99, new SDSizeAlarmUtil.AlarmListener() {
//            @Override
//            public void onAlarm() {
//                DevUtil.showInfo(getContext(), "超出预期值");
//            }
//        });
        SDSizeAlarmUtil.setSDAlarm(4 * 1024 * 1024 * 1024L, new SDSizeAlarmUtil.AlarmListener() {
            @Override
            public void onAlarm() {
                DevUtil.showInfo(getContext(), "磁盘空间不足");
            }
        });


    }

    @OnClick(R.id.btn_5)
    public void btn5Click() {
        IntentUtil.gotoActivity(getContext(), SearchActivity.class);
    }


    private DiskLruCache diskLruCache;


    private String url1 = "http://image.lxway.com/thumb/280x220/6/31/631c9f0df6197539dc26279a574fc1ea.png";

    private String url2 = "http://60.174.232.179/tvgame/Uploads/app/201604/572073c4e6cac.apk";

    private String url3 = "http://www.ablanxue.com/autoupload/201511/14466876121295944.jpg";

    @OnClick(R.id.btn_6)
    public void btn6Click() {


    }

    @OnClick(R.id.btn_7)
    public void btn7Click() {
        if (LRUFileCache.getInstance().getDiskFile(url1)==null){
            Dialog dialog = UIUtil.getFileDownloadDlg(getContext(), url1, null, distory + File.separator + LRUFileCache.getInstance().convertUrlToFileName(url1));
            dialog.show();
            LRUFileCache.getInstance().freeSpaceIfNeeded();
        }else{
            Log.d("sss", "btn7Click: "+LRUFileCache.getInstance().getDiskFile(url1).getPath());
        }

    }

    private String distory;
    /**
     * config the file cache
     */

    private void config() {
        distory = getContext().getExternalCacheDir().getPath() + File.separator + "uniqueName";
        LRUFileCache.getInstance().setFileLoadOptions(new FileCacheOptions.Builder()
                .setMaxFileCount(5)
                .setMaxCacheSize(200 * 1024L)
                .setIsUseFileCache(true)
                .setCacheRootPath(distory)
                .builder());
    }


    private InputStream getFromUrl(String url) {
        try {
            URL mURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
            return conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
