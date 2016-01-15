package panda.android.lib.base.control;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import panda.android.lib.base.ui.fragment.BaseFragment;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.IntentUtil;
import panda.android.lib.base.util.Log;
import panda.android.lib.base.util.TextUtil;

/**
 * Created by shitianci on 16/1/13.
 */
public class FileSelectorController {

    private static final String TAG = FileSelectorController.class.getSimpleName();
    private BaseFragment fragment;
    private int requestCode;
    private String filePath;

    public void startIntent(BaseFragment fragment, int requestCode, String type, String tips) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            DevUtil.showInfo(fragment.getActivity(), "没有储存卡");
            return;
        }
        this.fragment = fragment;
        this.requestCode = requestCode;
        filePath = "";
        Toast.makeText(fragment.getActivity(), tips, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        IntentUtil.startActivityForResult(fragment, intent, requestCode, "请安装文件选择器");
    }

    public void startIntent(BaseFragment fragment, int requestCode, String type) {
        startIntent(fragment, requestCode, type, "请选择文件");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode = " + requestCode);
        Log.d(TAG, "resultCode = " + resultCode);
        Log.d(TAG, "data = " + IntentUtil.getIntentInfo(data));
        if (resultCode == Activity.RESULT_OK && requestCode == this.requestCode) {
            Uri uri = data.getData();
            uri.getScheme();
            uri.getPath();
            uri.getQuery();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = fragment.getActivity().getContentResolver().query(
                        uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            } else {
                filePath = uri.getPath();
            }
        }  //end if 打开图片
        else if (resultCode == Activity.RESULT_CANCELED) {
            filePath = null;
        }
        Log.d(TAG, "filePath = " + filePath);
    }

    /**
     * 获取文件路径
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        if(TextUtil.isNull(filePath)){
            return "";
        }
        File file = new File(filePath);
        if(file !=null  && file.exists()){
            return file.getName();
        }
        else{
            return "";
        }
    }
}
