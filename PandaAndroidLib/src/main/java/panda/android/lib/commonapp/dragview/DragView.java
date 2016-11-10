package panda.android.lib.commonapp.dragview;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import panda.android.lib.R;

/**
 * Created by shitianci on 16/9/28.
 */

public class DragView extends RelativeLayout implements View.OnTouchListener {

    private Drawable mDragIcon;//拖动图标
    private ImageView mDragView;//拖动图标ImageView
    private int mCircleColor;//圆的颜色
    private Context mContext;//上下文
    private int mDotNum;//节点数量

    private int mWidth;//组件的宽度
    private int mHight;//组件的高度
    private int mCircleRadius;//园的半径
    private int mDragWidth;//拖动图标的宽度
    private int mLineWidth,mLineHeight;//中间线的长宽

    private float mStartX;//开始拖动的屏幕坐标
    private float mCurX;//开始拖动的触点坐标
    private float mMinX;//拖动范围的最小值
    private float mMaxX;//拖动范围的最大值

    private OnNodeSelect mOnNodeSelect;//回调接口
    private int ratio = 2; //超过多少即算滑动上

    public DragView(Context context) {
        this(context,null);
    }

    public DragView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.DragView);
        mDragIcon=array.getDrawable(R.styleable.DragView_icon_drag);
        mCircleColor=array.getColor(R.styleable.DragView_color_circle, Color.WHITE);
        mDotNum=array.getInt(R.styleable.DragView_dot_num,2);
        array.recycle();
        mContext=context;
        initView();
    }

    /**
     *视图初始化，这个方法的作用是把所有的视图添加进来，并做一些初始化的配置
     * 其实可以用LayoutInflater把xml的布局文件加载进来，这样比较简便
     */
    public void initView(){
        for (int i=0;i<mDotNum;i++){
            CircleView circleView=new CircleView(mContext);
            circleView.setOnTouchListener(this);
            addView(circleView);
        }

        View view=new View(mContext);
//        view.setBackgroundColor(mCircleColor);
        view.setBackgroundResource(android.R.color.transparent);
        addView(view);

        mDragView=new ImageView(mContext);

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LayoutParams params= new LayoutParams((int)(width/(720+0f)*88),(int)(height/(1280+0f)*80));
//        params.width=width/720*88;
//        params.height=height/1280*80;
        mDragView.setLayoutParams(params);

        mDragView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mDragView.setImageDrawable(mDragIcon);
        mDragView.setOnTouchListener(this);
        addView(mDragView);
    }

    /**
     *这一步主要是为了测量、配置每个view的大小，以便于后面触摸事件的处理
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth=getMeasuredWidth();
        mHight=getMeasuredHeight();

        mCircleRadius=dp2px(mContext,32);
        mDragWidth=dp2px(mContext,50);
        mLineWidth=mWidth-mDragWidth;
        mLineHeight=mCircleRadius/2;

        int count=getChildCount();
        for (int i=0;i<count;i++){
            View view=getChildAt(i);
            RelativeLayout.LayoutParams params;
            if (view instanceof CircleView){
                params=new RelativeLayout.LayoutParams(mCircleRadius,mCircleRadius);
                view.setLayoutParams(params);
            }else if (view instanceof ImageView){
                params=new RelativeLayout.LayoutParams(mDragWidth,mDragWidth);
                view.setLayoutParams(params);
            }else {
                params=new RelativeLayout.LayoutParams(mLineWidth,mLineHeight);
                view.setLayoutParams(params);
            }
        }

    }


    /**
     *这一步主要是为了放置每个view的位置，如果用LayoutInflater加载布局文件，那这一步就可以省略了
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//  super.onLayout(changed, l, t, r, b);
        if (changed){
            int count =getChildCount();

            int dLeft=0;
            int dTop=mHight/2-mDragWidth/2;

            int cLeft=mDragWidth/2-mCircleRadius/2;
            int cTop=dTop+mDragWidth/2-mCircleRadius/2;

            int lLeft=mDragWidth/2;
            int lTop=dTop+mDragWidth/2-mLineHeight/2;


            mMinX=dLeft;
            mMaxX=dLeft+mLineWidth;

            int cIndex=0;
            int cSpace=0;

            for (int i=0;i<count;i++){
                View view=getChildAt(i);
                if (view instanceof CircleView){
                    cLeft=cLeft+cSpace;
                    view.layout(cLeft,cTop,cLeft+mCircleRadius,cTop+mCircleRadius);
                    cIndex++;
                    cSpace=cIndex*mLineWidth;
                }else if (view instanceof ImageView){
                    view.layout(dLeft,dTop,dLeft+mDragWidth,dTop+mDragWidth);
                }else {
                    view.layout(lLeft,lTop,lLeft+mLineWidth,lTop+mLineHeight);
                }
            }
        }
    }


    /**
     *触碰事件的处理，这是自定义view比较重要的地方
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof ImageView){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    mStartX = (int) event.getRawX();
                    mCurX = v.getTranslationX();//获取view的偏移量
                    v.setPressed(true);
                    break;

                case MotionEvent.ACTION_MOVE:
                    float x = mCurX + event.getRawX() - mStartX;
                    if (x >= 0 && x <= mMaxX - mMinX) {
                        v.setTranslationX(mCurX + event.getRawX() - mStartX);
                    }

                    break;

                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
                case -1:
                    v.setTranslationX(mCurX + event.getRawX() - mStartX);
                    int distance=(int) (event.getRawX()-mStartX);
                    if (distance>0){
                        if (Math.abs(distance)>mLineWidth/ratio){
                            setAnim(mLineWidth,1);
                        }else {
                            setAnim(0,0);
                        }
                    }else if (distance<0){
                        setAnim(0,0);
                    }

                    break;
            }
        }else if (v instanceof CircleView){
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (event.getRawX()>getDisplayWidth()/ratio){
                        setAnim(mLineWidth,1);
                    }else {
                        setAnim(0,0);
                    }
                    break;
            }
        }
        return true;
    }

    //设置平移动画
    public void setAnim(float moveX, final int scrollPosition) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mDragView, "translationX", mDragView.getTranslationX(), moveX);
        animator.setDuration(300);
        animator.start();
        if (mOnNodeSelect != null) {
            mOnNodeSelect.onNodeSelect(scrollPosition);
        }
    }

    public void setNodeSelectListener(OnNodeSelect onNodeSelect) {
        mOnNodeSelect = onNodeSelect;
    }

    //事件回调接口
    public interface OnNodeSelect {
        void onNodeSelect(int position);
    }

    //dp转px
    public int dp2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //获取屏幕宽度
    public int getDisplayWidth(){
        DisplayMetrics metrics=new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}