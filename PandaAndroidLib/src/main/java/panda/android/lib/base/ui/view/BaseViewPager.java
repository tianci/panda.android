package panda.android.lib.base.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class BaseViewPager extends ViewPager {

    private boolean isCanScroll = true;

    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 触摸没有反应就可以了
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (isCanScroll) {
                return super.onTouchEvent(event);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            if (isCanScroll) {
                if (getChildCount() > 0) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                return super.onInterceptTouchEvent(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setPagingEnabled(boolean b) {
        isCanScroll = b;
    }
}