package panda.android.demo.filetest.Cache;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import panda.android.demo.filetest.SDCardCheck;
import panda.android.demo.filetest.SDCardUtil;

/**
 * Created by Olivine on 16/10/10
 */
public class LRUFileCache implements FileCache {

    /**
     * file cache  config
     */
    private FileCacheOptions options;
    /**
     * cache file suffix
     */
    private static final String WHOLESALE_CONV = "";
    /**
     * mini free space on SDCard  100M
     */
    private static final long FREE_SD_SPACE_NEEDED_TO_CACHE = 100 * 1024 * 1024L;

    private static LRUFileCache mLRUFileCache;

    public static LRUFileCache getInstance() {
        if (mLRUFileCache == null) {
            synchronized (LRUFileCache.class) {
                if (mLRUFileCache == null) {
                    mLRUFileCache = new LRUFileCache();
                }
            }
        }

        return mLRUFileCache;
    }

    /**
     * 缓存文件的配置
     */
    public void setFileLoadOptions(FileCacheOptions options) {
        this.options = options;
    }

    /**
     * use default options
     */
    private LRUFileCache() {
        this.options = new FileCacheOptions.Builder()
                .setCacheRootPath("FileCache")
                .setIsUseFileCache(true)
                .setMaxCacheSize(100 * 1024 * 1024L)//100MB
                .setMaxFileCount(100)
                .builder();
    }


    @Override
    public void addDiskFile(String key, InputStream inputStream) {
        if (TextUtils.isEmpty(key) || inputStream == null) {
            return;
        }

        String filename = convertUrlToFileName(key);
        String dir = options.getCacheRootPath();
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();
        File file = new File(dir + "/" + filename);
        OutputStream outStream;
        try {
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            outStream = new FileOutputStream(file);
            while (inputStream.available() != 0) {
                outStream.write(inputStream.read());
            }
            outStream.flush();
            outStream.close();
            inputStream.close();
        } catch (Throwable e) {
            Log.w("LRUFileCache", e.getMessage());
        }

        // free the space at every time to add a new file
        freeSpaceIfNeeded();
    }

    @Override
    public File getDiskFile(String key) {
        File file = new File(getFilePathByKey(key));

        if (file != null && file.exists()) {
            updateFileTime(file);

        } else {
            file = null;
        }
        return file;
    }

    @Override
    public boolean isExist(String key) {
        if (URLUtil.isNetworkUrl(key)) {
            return new File(options.getCacheRootPath() + "/" + convertUrlToFileName(key)).exists();

        } else if (URLUtil.isFileUrl(key)) {
            return new File(key).exists();

        } else {
            return false;
        }
    }

    @Override
    public void removeDiskFile(String key) {
        File file = getDiskFile(key);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    @Override
    public void removeAllDiskFiles() {
        new File(options.getCacheRootPath()).delete();
    }


    /**
     * This method will free the files which had not been used at a long time
     */
    public void freeSpaceIfNeeded() {
        File dir = new File(options.getCacheRootPath());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        long dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }
        // if the dir size larger than max size or the free space on SDCard is less than 100MB
        //free 40% space for system
        if (dirSize > options.getMaxCacheSize()
                || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // delete 40% files by LRU
            int removeFactor = (int) ((0.4 * files.length) + 1);
            // sort the files by modify time
            Arrays.sort(files, new FileLastModifSort());
            // delete files
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }

        //if file count is larger than max count, delete the last
        if (files.length > options.getMaxFileCount()) {
            Arrays.sort(files, new FileLastModifSort());
            // delete files
            for (int i = 0; i < files.length - options.getMaxFileCount(); i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }

    }

    /**
     * Modify the file time
     *
     * @param file the file which need to update time
     */
    public void updateFileTime(File file) {
        if (file != null && file.exists()) {
            long newModifiedTime = System.currentTimeMillis();
            file.setLastModified(newModifiedTime);
        }
    }

    /**
     * get the free space on SDCard
     *
     * @return free size in B
     */

    private long freeSpaceOnSd() {
        return SDCardUtil.getFreeSpace();
    }

    /**
     * Get the file name by file url
     *
     * @param url
     * @return file name
     */
    public String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1] + WHOLESALE_CONV;
    }

    /**
     * Get the file name by key
     *
     * @param key
     * @return file name
     */
    public String getFilePathByKey(String key) {
        if (URLUtil.isFileUrl(key)) {
            return key;

        } else if (URLUtil.isNetworkUrl(key)) {
            return options.getCacheRootPath() + "/" + convertUrlToFileName(key);

        } else {
            return null;
        }
    }

    /**
     * The comparator for the file modify, sort the files by modify time.
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
