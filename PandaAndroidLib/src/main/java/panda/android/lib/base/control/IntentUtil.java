package panda.android.lib.base.control;

import android.content.Context;
import android.content.Intent;

import panda.android.lib.base.ui.fragment.BaseActivityWithExtrasData;

/**
 * Intent相关的工具类。
 * @author shitianci
 *
 */
public class IntentUtil {

    /**
     * 跳转到制定组件。
     * @param context
     * @param cls
     */
    public static void gotoActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 跳转到制定组件(携带相应数据)。
     * @param context
     * @param cls
     * @param extra
     */
    public static void gotoActivity(Context context, Class<? extends BaseActivityWithExtrasData> cls, String extra) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(BaseActivityWithExtrasData.ACTIVITY_EXTRA_DATA, extra);
        context.startActivity(intent);
    }

}
