package panda.android.lib.base.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shitianci on 16/6/28.
 */
public class AnimationUtil {
    private static final String TAG = AnimationUtil.class.getSimpleName();

    /**
     * 动画效果：开始的宽度为父容器的宽度，逐步向中间缩减为0。
     * 使用场景：微信录制小视频
     *
     */
    public static ValueAnimator startAnimation(final View view, final long duration, final Animator.AnimatorListener animatorListener) {
        ValueAnimator va = ObjectAnimator.ofInt(view.getWidth(), 0);
        va.setDuration(duration);
        va.addListener(animatorListener);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = value;
                view.setLayoutParams(params);
                view.requestLayout();
            }
        });

        //结束时恢复宽高
        final int width = view.getWidth();
        final int height = view.getHeight();
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(TAG, "onAnimationEnd");
                setViewLayoutParams(view, width, height);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Log.d(TAG, "onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                Log.d(TAG, "onAnimationRepeat");
            }
        });

        va.start();
        return va;
    }

    /**
     * 设置view的宽高
     * @param view
     * @param width
     * @param height
     */
    public static void setViewLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
        view.requestLayout();
    }
}
