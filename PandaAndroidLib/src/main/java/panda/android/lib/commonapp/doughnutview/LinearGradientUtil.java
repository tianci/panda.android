package panda.android.lib.commonapp.doughnutview;

import android.graphics.Color;

/**
 * Created by chenxiaoxuan1 on 16/3/25. 
 */  
public class LinearGradientUtil {  
    private int mStartColor;
    private int mEndColor;  
  
    public LinearGradientUtil(int startColor, int endColor) {  
        this.mStartColor = startColor;  
        this.mEndColor = endColor;  
    }  
  
    public void setStartColor(int startColor) {  
        this.mStartColor = startColor;  
    }  
  
    public void setEndColor(int endColor) {  
        this.mEndColor = endColor;  
    }  
  
    public int getColor(float radio) {  
        int redStart = Color.red(mStartColor);
        int blueStart = Color.blue(mStartColor);
        int greenStart = Color.green(mStartColor);
        int redEnd = Color.red(mEndColor);
        int blueEnd = Color.blue(mEndColor);
        int greenEnd = Color.green(mEndColor);
  
        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));  
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));  
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));  
        return Color.argb(255,red, greed, blue);  
    }  
}  