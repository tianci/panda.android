package panda.android.lib.base.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;

public class DevUtil {
    private static final String TAG = DevUtil.class.getSimpleName();

    public static void showInfo(Context context, String string) {
        if (!TextUtil.isNull(string)) {
            Log.d(TAG, "showInfo: " + string);
            Toast makeText = Toast
                    .makeText(context, string, Toast.LENGTH_SHORT);
            if (makeText != null) {
                makeText.show();
            }
        }
    }

    public static void closeImm(Activity activity) {
        if(activity == null){
            return;
        }
        View currentFocus = activity.getCurrentFocus();
        if(currentFocus == null){
            return;
        }
        closeImm(activity, currentFocus.getWindowToken());
    }

    public static void closeImm(Context context, IBinder windowToken) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 删除文件夹
     * @param dir
     */
    public static void deleteFolder(String dir) {
        File delfolder = new File(dir);
        File oldFile[] = delfolder.listFiles();
        try {
            for (int i = 0; i < oldFile.length; i++) {
                if (oldFile[i].isDirectory()) {
                    deleteFolder(dir + oldFile[i].getName() + "//");
                }
                oldFile[i].delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
