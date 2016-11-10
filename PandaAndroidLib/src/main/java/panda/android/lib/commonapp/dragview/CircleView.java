package panda.android.lib.commonapp.dragview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import panda.android.lib.R;


/**
 * Created by shitianci on 16/9/28.
 */

public class CircleView extends View {
    /**
     * 默认颜色
     */
//    private final int DEFAULT_COLOR = Color.LTGRAY;
    private final int DEFAULT_COLOR = Color.TRANSPARENT;
    /**
     * 默认半径dp
     */
    private final float DEFAULT_RADIUS = 32;
    private int mColor;
    private Paint mCirclePaint;
    private float mRadius;
    private float mCenterX;
    private float mCenterY;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DragView);
        mColor = array.getColor(R.styleable.DragView_color_circle, DEFAULT_COLOR);
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mColor);

        mCenterY = mCenterX = mRadius = getMeasuredWidth() == 0 ? DEFAULT_RADIUS : getMeasuredWidth() / 2;
        array.recycle();
    }

    public float getRadius() {
        return mRadius;
    }

    public int getColor() {
        return mColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mCirclePaint);
    }
}