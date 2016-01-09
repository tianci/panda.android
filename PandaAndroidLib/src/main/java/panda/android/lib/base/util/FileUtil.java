package panda.android.lib.base.util;

import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Locale;

/**
 * Created by shitianci on 15/11/19.
 */
public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getSuffix(String url){
        if (url.equals("") || url.endsWith(".")) {
            return null;
        }
        int index = url.lastIndexOf(".");
        if (index != -1) {
            return url.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    /**
     * 获取文件的mime
     * @param file
     * @return
     */
    public static String getMimeType(File file){
//        MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    /**
     * 获取文件的mime
     * @param url
     * @return
     */
    public static String getMimeType(String url){
        String suffix = getSuffix(url);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null && !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    /**
     * 确认文件夹是否存在，如果不存在，新建一个
     * @param dirPath
     */
    public static void checkDir(String dirPath) {
        File dirFile = new File(dirPath);
//        try {
//            cacheDir.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (dirFile.exists() && !dirFile.isDirectory()){
            //删除同名文件
            Log.d(TAG,  dirFile.getAbsolutePath() + "is not directory");
            dirFile.delete();
        }
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            throw new RuntimeException("can't make dirs in " + dirFile.getAbsolutePath());
        }
        Log.d(TAG, dirFile.getAbsolutePath() + ".exists() = " + dirFile.exists());
    }
}
