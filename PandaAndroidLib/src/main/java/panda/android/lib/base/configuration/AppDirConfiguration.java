package panda.android.lib.base.configuration;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import panda.android.lib.base.control.cache.ACache;
import panda.android.lib.base.util.FileUtil;

/**
 *
 * 文件夹的规则参考：
 * 1. <a href="http://www.cnblogs.com/mengdd/p/3742623.html">Android存储访问及目录</a>
 * 2. <a href="http://blog.csdn.net/wwj_748/article/details/42737607">Android记录20-获取缓存大小和清除缓存功能</a>
 *
 * TODO: 增加清除缓存的功能
 * Created by shitianci on 15/12/5.
 */
public class AppDirConfiguration {

    private static Context mContext;

    /**
     * 配置App的目录和缓存
     * @param context 上下文
     */
    public static void configure(Context context) {
        mContext = context;
        FileUtil.checkDir(getExternalStoragePublicDirectory());
    }

    /**
     * 用于获取/data/data/<application package>/cache目录
     * @return
     */
    public static String getCachePath() {
        return mContext.getCacheDir().getPath();
    }

    /**
     * 用于获取/data/data/<application package>/files目录
     * @return
     */
    public static String getFilesPath() {
        return mContext.getFilesDir().getPath();
    }

    /**
     * 用于获取SDCard/Android/data/你的应用包名/cache/目录
     *
     * 对应 设置->应用->应用详情里面的『清除缓存』选项
     * @return
     */
    public static String getExternalCachePath() {
        return mContext.getExternalCacheDir().getPath();
    }

    /**
     * SDCard/Android/data/你的应用的包名/files/<type>
     *
     * 对应 设置->应用->应用详情里面的『清除数据』选项
     */
    public static String getExternalFilesPath(String type) {
        return mContext.getExternalFilesDir(type).getPath();
    }

    /**
     * 用于获取 SDCard/你的应用包名/ 目录
     * （卸载后不会被删除！！！）
     */
    public static String getExternalStoragePublicDirectory() {
        return Environment.getExternalStorageDirectory().getPath()+ "/" + mContext.getPackageName() + "/";
    }

    /**
     * 创建文件缓存
     * @param dirPath 缓存的文件夹绝对路径
     * @return
     */
    public static ACache getACache(String dirPath) {
        return ACache.get(new File(dirPath));
    }

    /**
     * 获取内部存储中的缓存：/data/data/<application package>/cache目录 （卸载后会被清除！！）
     * @return
     * @param relationDirPath 相对存储路径
     */
    public static ACache getCache(String relationDirPath) {
        return getACache(getCachePath()+relationDirPath);
    }

    /**
     * 获取外部存储中的缓存：SDCard/Android/data/你的应用包名/cache/目录 （卸载后会被清除！！）
     * @return
     * @param relationDirPath 相对存储路径
     */
    public static ACache getExternalCache(String relationDirPath) {
        return getACache(getExternalCachePath()+relationDirPath);
    }

    /**
     * 获取外部存储中的缓存：SDCard/Android/data/你的应用的包名/files/<type> （卸载后会被清除！！）
     * @return
     * @param relationDirPath 相对存储路径
     */
    public static ACache getExternalFilesCache(String relationDirPath) {
        return getACache(getExternalFilesPath(relationDirPath));
    }

    /**
     * 获取外部存储中的缓存：SDCard/你的应用的包名/<type> （卸载后不会被清除！！）
     * @return
     * @param relationDirPath 相对存储路径
     */
    public static ACache getExternalStoragePublicDirectory(String relationDirPath) {
        return getACache(getExternalStoragePublicDirectory()+"/"+relationDirPath);
    }
}
