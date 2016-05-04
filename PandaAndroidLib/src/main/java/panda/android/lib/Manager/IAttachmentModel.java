package panda.android.lib.Manager;

/**
 * Created by shitianci on 15/9/11.
 */
public interface IAttachmentModel  {
    /**
     * @return 文件名
     */
    public String getFilename();

    /**
     * @return 下载url
     */
    public String getDownloadUrl();

    /**
     * @return mimetype
     */
    public String getMimetype();

    /**
     * @return 缓存key
     */
    public String getCacheKey();

    /**
     * 设置本地地址
     * @param mLocalPath
     */
    public void setLocalPath(String mLocalPath);

    public String getLocalPath();
}
