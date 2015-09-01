package panda.android.lib.base.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextUtil
{
	private static DecimalFormat sDecFmt = new DecimalFormat("#0.00");

    public static boolean isNull(CharSequence cs)
    {
        if (TextUtils.isEmpty(cs))
        {
            return true;
        }
        if(cs == "null"){
        	return true;
        }
        return TextUtils.isEmpty(cs.toString().trim());
    }
    public static boolean isEmail(String email)
    {
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPhone(String phone)
    {
        String regex = "^((13[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static boolean isNumeric(String str){ 
    	String regex = "[0-9]*";
        Pattern p = Pattern.compile(regex); 
        Matcher m = p.matcher(str);
        return m.matches();    
    } 
    public static boolean isUid(String str){
    	if(!isNumeric(str)){
    		return false;
    	}
    	if(str.length()<5 || str.length()>9){
    		return false;
    	}
    	return true;
    }
    
    
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	/**
	 * 密码仅支持数字、字母、符号
	 * @param s
	 * @return
	 */
	public static boolean isValidPassword(String s)
    {
		Pattern p = Pattern.compile("^[0-9a-zA-Z~!@#$%^&*()-_+=>.<,?/|]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
    }
	
	public static String getDisplay2DecimalPlaces(double d) {
		return sDecFmt.format(d);
	}
	
    public static String getStringByArray(int[] array) {
        String tmp = "";
        if(array == null) return ""; 
        int i = 0;
        for (i = 0; i < array.length; i++) {
            tmp = tmp + (char) array[i];
        }
        return tmp;
    }
    
    /**
     * 返回文件大小描述，带上合适的单位
     * @param size 以byte为单位的大小
     * @return
     */
    public static String getFileSize(long size) {
    	String unit = "B";
    	float showSize = size;
    	double divisor = 1024.0;
    	if(showSize > divisor){
    		showSize = (float) (showSize/1024.0);
    		unit = "K";
    	}
		if(showSize > divisor){
    		showSize = (float) (showSize/1024.0);
    		unit = "M";
    	}
		if(showSize > divisor){
			showSize = (float) (showSize/1024.0);
			unit = "G";
		}
    	return String.format("%.2f%s", showSize,unit);
    }
	public static String getFileName(String fName) {
		fName = fName.trim();
		fName = fName.substring(fName.lastIndexOf("/")+1);
		int i = fName.lastIndexOf(".");
		if(i>0 && i<fName.length()){
			fName = fName.substring(0, i);
		}
		return fName;
	}
}
