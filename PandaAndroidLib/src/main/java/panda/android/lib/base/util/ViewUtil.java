package panda.android.lib.base.util;

import android.view.View;

/**
 * Created by shitianci on 16/3/7.
 */
public class ViewUtil {
    /**
     * 让某个view获得焦点
     * @param v
     */
    public static void focus(View v){
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
    }
}
