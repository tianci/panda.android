package panda.android.demo.filetest;

import android.util.Log;

import java.io.File;


public class SDCardCheck {
    public static final long SIZE_KB = 1024L;
    public static final long SIZE_MB = 1024 * 1024L;
    public static final long SIZE_GB = 1024L * 1024L * 1024L;
    private File path;
    private int SDstate;
    private long nSDTotalSize;
    private long nSDFreeSize;


    public SDCardCheck() {
        String sDStateString = android.os.Environment.getExternalStorageState();
        if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            Log.d("x", "the SDcard mounted");
            SDstate = 1;
        } else if (sDStateString.endsWith(android.os.Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Log.d("x", "the SDcard just can be read");
            SDstate = 2;
        } else if (sDStateString.endsWith(android.os.Environment.MEDIA_UNMOUNTED)) {
            Log.d("x", "the SDcard unmounted");
            SDstate = -1;
            System.exit(0);
        } else if (sDStateString.endsWith(android.os.Environment.MEDIA_REMOVED)) {
            Log.d("x", "the SDcard has been removed");
            SDstate = -2;
            System.exit(0);
        }
    }


    public File getSDCardDir() {
        if (SDstate > 0) {
            path = android.os.Environment.getExternalStorageDirectory();
            Log.d("x", path.toString());
            return path;
        }
        return null;
    }

    public void SDCardSize() {
        if (SDstate > 0) {
            path = android.os.Environment.getExternalStorageDirectory();
            android.os.StatFs statfs = new android.os.StatFs(path.getPath());

            long nTotalBlocks = statfs.getBlockCount();

            long nBlocSize = statfs.getBlockSize();

            long nAvailaBlock = statfs.getAvailableBlocks();

            long nFreeBlock = statfs.getFreeBlocks();

            nSDTotalSize = nTotalBlocks * nBlocSize ;

            nSDFreeSize = nAvailaBlock * nBlocSize ;
        }

    }


    public long getnSDFreeSize() {
        return nSDFreeSize;
    }


    public long getnSDTotalSize() {
        return nSDTotalSize;
    }


    @Override
    public String toString() {
        return "SDCardCheck [path=" + path + ", SDstat=" + SDstate + "]"
                + nSDTotalSize + "SDTotalSize" + "...SDFreeSize" + nSDFreeSize;
    }


    public static String getSizeSting(long size) {

        if (size < SIZE_KB) {
            return size + "B";
        }

        if (size < SIZE_MB) {
            return Math.round(size * 100.0 / SIZE_KB) / 100.0 + "KB";
        }

        if (size < SIZE_GB) {
            return Math.round(size * 100.0 / SIZE_MB) / 100.0 + "MB";
        }

        return Math.round(size * 100.0 / SIZE_GB) / 100.0 + "G";

    }



    public File getPath() {
        return path;
    }


    public int getSDstat() {
        return SDstate;
    }


}
