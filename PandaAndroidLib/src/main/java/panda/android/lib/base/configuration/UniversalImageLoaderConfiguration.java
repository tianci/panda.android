package panda.android.lib.base.configuration;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by shitianci on 15/12/5.
 */
public class UniversalImageLoaderConfiguration {

    /**
     * 配置UniversalImageLoader
     * @param context 上下文
     * @param defaultImage 默认图片（用于*图片uri为空*、*加载失败*、*正在加载中的三种情形*）
     */
    public static void configure(Context context, int defaultImage) {
        configure(context, defaultImage, defaultImage, defaultImage);
    }

    /**
     * 配置UniversalImageLoader
     * @param context 上下文
     * @param emptyUriImage 默认图片（*图片uri为空*)
     * @param failImage 默认图片（*加载失败*)
     * @param loadingImage 默认图片（*正在加载中的三种情形*)
     */
    public static void configure(Context context, int emptyUriImage, int failImage, int loadingImage) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(emptyUriImage) //
                .showImageOnFail(failImage) //
                .showImageOnLoading(loadingImage)
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(context)//
                .defaultDisplayImageOptions(defaultOptions)//
                //  .discCacheSize(50 * 1024 * 1024)//
                //  .discCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs()//
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();//
        ImageLoader.getInstance().init(config);
    }
}
