package panda.android.demo.filetest.Cache;

import java.io.File;
import java.io.InputStream;

/**
 * Created by CJstar on 15/8/24.
 */
public interface FileCache {
    /**
     * Get the file by key, if the key is file path get the file by path ,</br>
     * else if the key is net URL, get path by URL at first and get file by path</br>
     * If the key is not match path and URL ,return null
     *
     * @param key this key is file path or net URL
     * @return file if the file is existed else return null
     */
    public File getDiskFile(String key);

    /**
     * Add a InputStream to disk cache, the path is get from key, if the key or inputStream is null,</br>
     * do nothing for this, else store the file on disk.
     *
     * @param key this key is file path or net URL
     * @param inputStream
     */
    public void addDiskFile(String key, InputStream inputStream);

    /**
     * Wether the key has a exist disk file
     * @param key this key is file path or net URL
     * @return true if it is exist else return false
     */
    public boolean isExist(String key);

    /**
     * remove the disk file by key
     * @param key this key is file path or net URL
     */
    public void removeDiskFile(String key);

    /**
     * remove all disk files
     */
    public void removeAllDiskFiles();

}
