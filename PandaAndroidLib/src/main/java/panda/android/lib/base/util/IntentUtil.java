package panda.android.lib.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

import panda.android.lib.R;
import panda.android.lib.base.ui.fragment.BaseActivityWithExtrasData;
import panda.android.lib.base.ui.fragment.BaseFragment;

/**
 * Intent相关的工具类。
 *
 * @author shitianci
 */
public class IntentUtil {


    private static final String TAG = IntentUtil.class.getSimpleName();

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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(context, intent);
    }

    /**
     * 发出相应的Intent
     * @param context
     * @param mIntent
     */
    public static void startActivity(Context context, Intent mIntent) {
        startActivity(context, mIntent, context.getString(R.string.can_not_find_activity));
    }

    /**
     * 发出相应的Intent
     * @param context
     * @param mIntent
     */
    public static void startActivity(Context context, Intent mIntent, String errInfo) {
        try {
            context.startActivity(mIntent);
        }
        catch (Exception e){
            e.printStackTrace();
            DevUtil.showInfo(context, errInfo);
        }
    }


    /**
     * 发出相应的Intent
     * @param context
     * @param mIntent
     */
    public static void startActivityForResult(BaseFragment context, Intent mIntent, int requestCode) {
        startActivityForResult(context, mIntent, requestCode, context.getString(R.string.can_not_find_activity));
    }

    /**
     * 发出相应的Intent
     * @param context
     * @param mIntent
     */
    public static void startActivityForResult(BaseFragment context, Intent mIntent, int requestCode, String errInfo) {
        try {
            context.startActivityForResult(mIntent, requestCode);
        }
        catch (Exception e){
            e.printStackTrace();
            DevUtil.showInfo(context.getActivity(), errInfo);
        }
    }

    /**
     * 打印Intent的数据
     * @param intent
     */
    public static String getIntentInfo(Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("IntentInfo: ");
        try {
            Bundle bundle = intent.getExtras();
            for (String key: bundle.keySet())
            {
                sb.append(key + ":" +bundle.get(key) + ";");
            }
//            Log.i(TAG, sb.toString());
        }
        catch (Exception e){
        }
        return sb.toString();
    }
}
