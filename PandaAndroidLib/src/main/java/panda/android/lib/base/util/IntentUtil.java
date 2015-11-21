package panda.android.lib.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import panda.android.lib.R;
import panda.android.lib.base.ui.fragment.BaseActivityWithExtrasData;

/**
 * Intent相关的工具类。
 *
 * @author shitianci
 */
public class IntentUtil {

    /**
     * 跳转到指定组件。
     *
     * @param context
     * @param cls
     */
    public static void gotoActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(context, intent);
    }

    /**
     * 跳转到指定组件(携带相应数据)。
     *
     * @param context
     * @param cls
     * @param extra
     */
    public static void gotoActivity(Context context, Class<? extends BaseActivityWithExtrasData> cls, String extra) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(BaseActivityWithExtrasData.ACTIVITY_EXTRA_DATA, extra);
        startActivity(context, intent);
    }

    /**
     * 调用系统组件打开文件
     * @param context
     * @param file
     */
    public static void openFile(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.getAbsolutePath());
        intent.setDataAndType(uri, FileUtil.getMimeType(file));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(context, intent);
    }

    /**
     * 发出相应的Intent
     * @param context
     * @param mIntent
     */
    public static void startActivity(Context context, Intent mIntent) {
        try {
            context.startActivity(mIntent);
        }
        catch (Exception e){
            e.printStackTrace();
            DevUtil.showInfo(context, context.getString(R.string.can_not_find_activity));
        }
    }
}
