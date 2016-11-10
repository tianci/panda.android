package panda.android.lib.commonapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 作为一个横向布局包含相同的子元素
 * Created by shitianci on 16/9/26.
 */
public class ItemsLinearLayout extends LinearLayout {

    private static final String TAG = ItemsLinearLayout.class.getSimpleName();
    private int width;
    private int height;

    public ItemsLinearLayout(Context context) {
        super(context);
    }

    public ItemsLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemsLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private View curView = null;




    public interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         */
        void onClick(int itemIndex);
    }

    OnClickListener onClickListener;

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void updateContain(LayoutInflater inflater, int layoutId, String[] items, int width, int height, int marginRight) {
        this.width = width;
        this.height = height;
        removeAllViews();
        if (items.length <= 0){
            setVisibility(INVISIBLE);
            return;
        }
        setVisibility(VISIBLE);
        curView = null;
        for (int i = 0; i < items.length; i++){
            TextView child = (TextView) inflater.inflate(layoutId, null);
            child.setText(items[i]);
            final int finalI = i;
            child.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    select(finalI, true);
                }
            });
            LayoutParams params = new LayoutParams(width,
                    height);
            params.setMargins(0, 0, marginRight, 0);
            addView(child, params);
        }
    }

    public void select(int itemIndex) {
        select(itemIndex, true);
    }

    /**
     *
     * @param itemIndex
     * @param isListener 是否返回给监听器, 防止循环传递监听事件。
     */
    public void select(int itemIndex, boolean isListener) {
        if (itemIndex < 0 || itemIndex > getChildCount()){
            return;
        }
        View view = getChildAt(itemIndex);

        if (curView != null){
            curView.setSelected(false);
        }
        view.setSelected(true);
        curView = view;
        if (isListener){
            onClickListener.onClick(itemIndex);
        }

        ViewParent parent = getParent();
        if (!isViewInScreen(curView) && parent instanceof HorizontalScrollView){
            ((HorizontalScrollView) parent).smoothScrollTo(itemIndex*width, 0);
        }
    }

    public boolean isViewInScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
//        Log.d(TAG, "location, x = " + location[0]);
//        Log.d(TAG, "location, y = " + location[1]);
        WindowManager manager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

//        getHitRect(Trect);
        if (location[0] + view.getWidth() > display.getWidth() || location[0] < 0){
            return false;
        }
        else{
            return true;
        }
//        return view.isEnabled() && view.isShown() && view.isClickable();
    }
}
