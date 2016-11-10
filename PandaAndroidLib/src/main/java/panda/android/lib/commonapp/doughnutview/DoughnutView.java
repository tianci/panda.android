package panda.android.lib.commonapp.doughnutview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import panda.android.lib.R;


/**
 * 进度条控件，来自https://github.com/hellsam/DoughnutDemo_Android
 * Created by hellsam on 15/12/16.
 */
public class DoughnutView extends View {

    public interface ProgressCallBack {
        void callBack(int current);
    }

    private ProgressCallBack progressCallBack;

    public void setProgressCallBack(ProgressCallBack callBack) {
        this.progressCallBack = callBack;
    }

    //View默认最小宽度
    private static final int DEFAULT_MIN_WIDTH = 400;
    //圆环颜色
//    private int[] doughnutColors = new int[]{Color.RED, Color.BLUE};
    private int[] doughnutColors = new int[]{Color.parseColor("#f47920"), Color.parseColor("#f8c25a")};

    private int width;
    private int height;
    private float currentValue = 0f;
    private Paint paint = new Paint();

    public DoughnutView(Context context) {
        super(context);
    }

    public DoughnutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoughnutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void resetParams() {
        width = getWidth();
        height = getHeight();
    }

    private void initPaint() {
        paint.reset();
        paint.setAntiAlias(true);
    }

    public void setValue(int value) {
        setValue(value * 360.0f / 100);
        setCallBackValue(value);
    }

    public void setCallBackValue(int value) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, value);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                return 1 - (1 - v) * (1 - v) * (1 - v);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float temp = (float) valueAnimator.getAnimatedValue();
                if (progressCallBack != null)
                    progressCallBack.callBack((int)temp);
            }
        });
        valueAnimator.start();
    }

    public void setValue(float value) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentValue, value);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                return 1 - (1 - v) * (1 - v) * (1 - v);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        resetParams();
        //画背景白色圆环
        initPaint();
        float doughnutWidth = getResources().getDimension(R.dimen.x15);
        paint.setStrokeWidth(doughnutWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.LTGRAY);
//        paint.setColor(getResources().getColor(R.color.donut_progress_bg));
        paint.setAntiAlias(true);
        RectF rectF = new RectF((width > height ? Math.abs(width - height) / 2 : 0) + doughnutWidth / 2, (height > width ? Math.abs(height - width) / 2 : 0) + doughnutWidth / 2, width - (width > height ? Math.abs(width - height) / 2 : 0) - doughnutWidth / 2, height - (height > width ? Math.abs(height - width) / 2 : 0) - doughnutWidth / 2);
        canvas.drawArc(rectF, 0, 360, false, paint);

        //画彩色圆环
        initPaint();
        canvas.rotate(-90, width / 2, height / 2);
        paint.setStrokeWidth(doughnutWidth);
        paint.setStyle(Paint.Style.STROKE);
        if (doughnutColors.length > 1) {
            // paint.setShader(new SweepGradient(width / 2, height / 2, doughnutColors, null));
            paint.setShader(new LinearGradient(0, 0, width / 2, height / 2, doughnutColors[0], doughnutColors[1], Shader.TileMode.CLAMP));
        } else {
            paint.setColor(doughnutColors[0]);
        }
        canvas.drawArc(rectF, 0, currentValue, false, paint);

        //画中间数值
        if (false) {
            //画中间数值的背景
            int fontSize = 50;
            initPaint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(width / 2, height / 2, fontSize * 2, paint);

            canvas.rotate(90, width / 2, height / 2);
            initPaint();
//        paint.setColor(ColorUtils.getCurrentColor(currentValue / 360f, doughnutColors));
            paint.setColor(new LinearGradientUtil(doughnutColors[0], doughnutColors[1]).getColor(currentValue / 360f));
            paint.setTextSize(fontSize);
            paint.setTextAlign(Paint.Align.CENTER);
            float baseLine = height / 2 - (paint.getFontMetrics().descent + paint.getFontMetrics().ascent) / 2;
            canvas.drawText((int) (currentValue / 360f * 100) + "%", width / 2, baseLine, paint);
        }
    }

    /**
     * 当布局为wrap_content时设置默认长宽
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int origin) {
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}