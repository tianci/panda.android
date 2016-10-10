package panda.android.demo.filetest;

import panda.android.lib.base.util.DevUtil;

/**
 * Created by admin on 2016/10/9.
 */

public class SDSizeAlarmUtil {
    public interface AlarmListener {
        void onAlarm();
    }

    public static void setAlarmSize(long currentSize, long maxSize, AlarmListener listener) {
        if (currentSize > maxSize) {
            if (listener != null) {
                listener.onAlarm();
            }
        }
    }

    public static void setSDAlarm( long minSize, AlarmListener listener) {
        SDCardCheck sdCardCheck = new SDCardCheck();
        sdCardCheck.SDCardSize();
        long freeSize = sdCardCheck.getnSDFreeSize();
        if (freeSize < minSize) {
            if (listener != null) {
                listener.onAlarm();
            }
        }
    }


}
