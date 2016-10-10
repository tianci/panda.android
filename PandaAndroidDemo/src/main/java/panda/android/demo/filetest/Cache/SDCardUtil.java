package panda.android.demo.filetest.Cache;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import panda.android.demo.filetest.SDSizeAlarmUtil;

/**
 * Created by admin on 2016/10/10.
 */

public class SDCardUtil {
    public static final long SIZE_KB = 1024L;
    public static final long SIZE_MB = 1024L * 1024L;
    public static final long SIZE_GB = 1024L * 1024L * 1024L;

    public interface AlarmListener {
        void onAlarm();
    }

    //设置超出最大值
    public static void setAlarmSize(long currentSize, long maxSize, SDSizeAlarmUtil.AlarmListener listener) {
        if (currentSize > maxSize) {
            if (listener != null) {
                listener.onAlarm();
            }
        }
    }

    //设置超出最小磁盘值预警
    public static void setSDAlarm(long minSize, SDSizeAlarmUtil.AlarmListener listener) {
        long freeSize = getFreeSpace();
        if (freeSize < minSize) {
            if (listener != null) {
                listener.onAlarm();
            }
        }
    }


    public static boolean sDCardIsCanable() {
        String sDStateString = Environment.getExternalStorageState();
        if (sDStateString.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("x", "the SDcard mounted");
            return true;
        }
        return false;

    }


    public static long getFreeSpace() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

        long sdFreeMB = stat.getAvailableBlocks() * stat
                .getBlockSize();
        return sdFreeMB;
    }


    public static String getSizeToSting(long size) {

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

}
