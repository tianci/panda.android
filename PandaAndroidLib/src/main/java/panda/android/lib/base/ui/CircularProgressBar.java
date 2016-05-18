package panda.android.lib.base.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import panda.android.lib.R;

/**
 * 基于 https://github.com/lwz0316/CircularProgressbar/blob/master/src/com/lwz/demo/circularprogressbar/widget/CircularProgressBar.java
 * Created by shitianci on 16/3/10.
 */
public class CircularProgressBar extends View {

    private int drawStyle;  //样式 0——空心圆；1——实心圆
    private int mMax = 100;
    private int mProgress = 30;

    private Paint mPaint = new Paint();
    private RectF mRectF = new RectF();

    private int mBackgroundColor = Color.LTGRAY;
    private int mPrimaryColor = Color.parseColor("#6DCAEC");
    private float mStrokeWidth = 10F;

    /**
     * 进度条改变监听
     *
     * {@link #onChange( int duration, int progress, float rate)}
     */
    public interface OnProgressChangeListener {
        /**
         * 进度改变事件，当进度条进度改变，就会调用该方法
         * @param duration 总进度
         * @param progress 当前进度
         * @param rate 当前进度与总进度的商 即：rate = (float)progress / duration
         */
        public void onChange( int duration, int progress, float rate);
    }

    private OnProgressChangeListener mOnChangeListener;

    /**
     * 设置进度条改变监听
     * @param l
     */
    public void setOnProgressChangeListener(OnProgressChangeListener l) {
        mOnChangeListener = l;
    }

    public CircularProgressBar(Context context) {
        super(context);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.Panda_CircleProgressBar);
        mBackgroundColor=tArray.getColor(R.styleable.Panda_CircleProgressBar_Panda_bgColor, Color.GRAY);
        mPrimaryColor=tArray.getColor(R.styleable.Panda_CircleProgressBar_Panda_primaryColor, Color.RED);
        drawStyle=tArray.getInt(R.styleable.Panda_CircleProgressBar_Panda_drawStyle, 0);
        mStrokeWidth=tArray.getDimension(R.styleable.Panda_CircleProgressBar_Panda_strokeWidth, 10);
        mMax=tArray.getInteger(R.styleable.Panda_CircleProgressBar_Panda_max, 100);
    }


    /**
     * 设置进度条的最大值, 该值要 大于 0
     * @param max
     */
    public void setMax( int max ) {
        if( max < 0 ) {
            max = 0;
        }
        mMax = max;
    }

    /**
     * 得到进度条的最大值
     * @return
     */
    public int getMax() {
        return mMax;
    }

    /**
     * 设置进度条的当前的值
     * @param progress
     */
    public void setProgress( int progress ) {
        if( progress > mMax) {
            progress = mMax;
        }
        mProgress = progress;
        if( mOnChangeListener != null ) {
            mOnChangeListener.onChange(mMax, progress, getRateOfProgress());
        }
        invalidate();
    }

    /**
     * 得到进度条当前的值
     * @return
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 设置进度条背景的颜色
     */
    public void setBackgroundColor( int color ) {
        mBackgroundColor = color;
    }

    /**
     * 设置进度条进度的颜色
     */
    public void setPrimaryColor( int color ) {
        mPrimaryColor = color;
    }

    /**
     * 设置环形的宽度
     * @param width
     */
    public void setCircleWidth(float width) {
        mStrokeWidth = width;

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() /2;
        int radius = halfWidth < halfHeight ? halfWidth : halfHeight;
        float halfStrokeWidth = mStrokeWidth / 2;

        // 设置画笔
        mPaint.setColor(mBackgroundColor);
        mPaint.setDither(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth);
        boolean opt;
        if(drawStyle==0){
            mPaint.setStyle(Paint.Style.STROKE);	//设置图形为空心
            opt = false;
        }else{
            mPaint.setStyle(Paint.Style.FILL);
            opt = true;
        }


        // 画背景
        canvas.drawCircle(halfWidth, halfHeight, radius - halfStrokeWidth, mPaint);

        // 画当前进度的圆环
        mPaint.setColor(mPrimaryColor);	// 改变画笔颜色
        mRectF.top = halfHeight - radius + halfStrokeWidth;
        mRectF.bottom = halfHeight + radius - halfStrokeWidth;
        mRectF.left = halfWidth - radius + halfStrokeWidth;
        mRectF.right = halfWidth + radius - halfStrokeWidth;
        canvas.drawArc(mRectF, -90, getRateOfProgress() * 360, opt, mPaint);
        canvas.save();
    }

    /**
     * 得到当前的进度的比率
     * <p> 用进度条当前的值 与 进度条的最大值求商 </p>
     * @return
     */
    private float getRateOfProgress() {
        return (float)mProgress / mMax;
    }

}