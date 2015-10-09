package panda.android.lib.base.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.PopupWindow;

import panda.android.lib.R;

/**
 * 生产典型的修饰性view，如Dialog、Animation、PopupWindow
 * Created by shitianci on 15/10/9.
 */
public class UIFactory {

    /**
     * 获取进度框
     * 使用方法：dialog.show()/dismiss()
     * @param context
     * @param contentView 内容布局
     * @param cancelable 是否可以取消
     * @return
     */
    public static Dialog getDialog(Context context,
                                   View contentView, boolean cancelable) {
        Dialog loadingDialog = new Dialog(context, R.style.DialogTheme);
        loadingDialog.setContentView(contentView);
        loadingDialog.setCancelable(cancelable);
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }

    /**
     * 获取动画
     * 使用方法：view.startAnimation(??) / view.clearAnimation()
     * @param context
     * @param animId 动画资源id
     * @param i 执行时长的效果
     * @return
     */
    public static Animation getAnimation(Context context, int animId, Interpolator i) {
        Animation animProgress = AnimationUtils.loadAnimation(context, animId);
        animProgress.setInterpolator(i);
        return animProgress;
    }

    /**
     * 获取弹出框
     * 使用方法：popupWindow.showAsDropDown(??)/popupWindow.showAtLocation(??)
     * @param context
     * @param contentView
     */
    public static PopupWindow getPopupWindow(final Context context, View contentView) {
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x0a000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        return popupWindow;
    }
}
