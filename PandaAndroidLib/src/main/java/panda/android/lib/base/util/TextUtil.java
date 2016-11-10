package panda.android.lib.base.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextUtil
{
    private static DecimalFormat sDecFmt = new DecimalFormat("#0.00");
    
    /**
     * 安全地获取某个值，为null则返回""
     * @param s
     * @return
     */
    public static String getString(String s){
        return getString(s, "");
    }

    /**
     * 安全地获取某个值，为null则返回value
     * @param s
     * @param value
     * @return
     */
    public static String getString(String s, String value){
        return isNull(s) ? value : s;
    }

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
//    public static boolean isEmail(String email)
//    {
//        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(email);
//        return m.matches();
//    }


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


	/**
	 *
	 * 手机号码：以1开头的11位数字
	 * 正则表达式为：^(1)\d{10}$
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone)
	{
//        String regex = "^((13[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
		String regex = "^(1)\\d{10}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 姓名：2到10位字符,不包含数字和特殊字符（`~!@#$%^&*()-=_+[]{};':"",./<>?"）
	 * 正则表达式为 ^[^~!@#\$%\^&\*\(\)\-=_\+\[\]\{\};':"",./<>\?]{2,100}$
	 * @param string
	 * @return
     */

	public static boolean isName(String string) {
		String regex = "^[^`~!@#\\$%\\^&\\*\\(\\)\\-=_\\+\\[\\]\\{\\};':\"\",./<>\\?]{2,10}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	/**
	 * 身份证号码：18位的，且前17位为数字，最后一位为数字或者大写X
	 * 正则表达式为：^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$
	 */
	public static boolean isIdentification(String string) {
		String regex = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	/**
	 * 邮箱
	 * 1. 必须包含一个并且只有一个符号“@”
	 * 2. 第一个字符不得是“@”或者“.”
	 * 3. 不允许出现“@.”或者.@
	 * 4. 结尾不得是字符“@”或者“.”
	 * 5. 允许“@”前的字符中出现“＋”
	 * 正则表达式为：^[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?$
	 */
	public static boolean isEmail(String string) {
		String regex = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	/**
	 * 验证码：4到6位数字或字母
	 * 正则表达式为：^[\da-zA-Z]{4,6}$
	 */
	public static boolean isVerificationCode(String string) {
		String regex = "^[\\da-zA-Z]{4,6}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
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
	 * 体重  1到3位   整数或小数（小数点后1位）
	 *
	 * @return
	 */
	public static boolean isWeight(float weight) {
		if (weight > 10.0 && weight <= 999.9) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 身高
	 * 2到3位整数或小数（小数点后1位）
	 */
	public static boolean isHight(float hight) {
		if (hight > 10.0 && hight <= 999.9) {
			return true;
		}
		return false;
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
