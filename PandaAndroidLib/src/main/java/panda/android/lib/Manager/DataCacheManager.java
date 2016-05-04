package panda.android.lib.Manager;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;

import panda.android.lib.base.configuration.AppDirConfiguration;
import panda.android.lib.base.control.cache.ACache;
import panda.android.lib.base.util.Log;
import panda.android.lib.base.util.TextUtil;

/**
 *
 * Created by shitianci on 16/4/28.
 */
public class DataCacheManager {
    private static final String TAG = DataCacheManager.class.getSimpleName();

    private static ACache mJsonACache; //存放Json类信息
    private static ACache mVideoACache; //存放视频类信息
    private static ACache mImageACache; //存放图片类信息
    private static ACache mFileACache; //存放其它文件类信息

    /**
     * @param context
     */
    public static void init(Context context){
        String externalStoragePublicDirectory = AppDirConfiguration.getExternalStoragePublicDirectory();
        mJsonACache = ACache.get(new File(externalStoragePublicDirectory + "/json"));
        mImageACache = ACache.get(new File(externalStoragePublicDirectory + "/image"));
        mVideoACache = ACache.get(new File(externalStoragePublicDirectory + "/video"));
        mFileACache = ACache.get(new File(externalStoragePublicDirectory + "/file"));
    }

    public static ACache getJsonACache() {
        return mJsonACache;
    }

    public static ACache getVideoACache() {
        return mVideoACache;
    }

    public static ACache getImageACache() {
        return mImageACache;
    }

    public static ACache getFileACache() {
        return mFileACache;
    }

    /**
     * 获取附件本地文件
     *
     * @param attachmentModel
     * @return 本地文件，或者null
     */
    public static File getCacheFile(IAttachmentModel attachmentModel) {
        Log.d(TAG, "getCacheFile, attachmentModel = " + attachmentModel);
        File file = null;
        try {
            if (!TextUtil.isNull(attachmentModel.getLocalPath())){
                return new File(attachmentModel.getLocalPath());
            }
            if (attachmentModel.getMimetype().startsWith("image")) {
                file = mImageACache.getFile(attachmentModel.getCacheKey());
            } else if (attachmentModel.getMimetype().startsWith("video")) {
                file = mVideoACache.getFile(attachmentModel.getCacheKey());
            }
            else{
                file = mFileACache.getFile(attachmentModel.getCacheKey());
            }
            attachmentModel.setLocalPath(file.getAbsolutePath());
            Log.d(TAG, "getCacheFile, file = " + file.getAbsolutePath());
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return file;
    }


    /**
     * 下载附件
     */
    public static void downloadAttachmentModel(List<IAttachmentModel> list) {
//        if (list == null || list.size() == 0){
//            return;
//        }
        for (IAttachmentModel attachmentModel : list) {
            if (DataCacheManager.getCacheFile(attachmentModel) != null){
                continue;
            }
            Log.d(TAG, "downloadAttachmentModel, " + attachmentModel);
            DataCacheManager.downloadFile(attachmentModel);
        }
    }

    /**
     * 下载附件
     * @param attachmentModel
     */
    private static void downloadFile(IAttachmentModel attachmentModel) {
        try {
            if (attachmentModel.getMimetype().startsWith("image")) {
                //保存图片
                OutputStream ostream = null;
                ostream = mImageACache.put(attachmentModel.getCacheKey());
                mImageACache.downloadFile(attachmentModel.getDownloadUrl(), ostream);
            } else if (attachmentModel.getMimetype().startsWith("video")) {
                //保存视频
                OutputStream ostream = mVideoACache.put(attachmentModel.getCacheKey());
                mVideoACache.downloadFile(attachmentModel.getDownloadUrl(), ostream);
            }
            else{
                //其它类型
                OutputStream ostream = null;
                ostream = mFileACache.put(attachmentModel.getCacheKey());
                mFileACache.downloadFile(attachmentModel.getDownloadUrl(), ostream);
            }
            attachmentModel.setLocalPath(getCacheFile(attachmentModel).getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
