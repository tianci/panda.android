package panda.android.lib.base.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import panda.android.lib.base.util.Log;

/**
 * 所有用户共享的配置
 * @author shitianci
 *
 */
public class BaseGlobalSharedPreferences  {

	private static final String TAG = BaseGlobalSharedPreferences.class.getSimpleName();
	private static final String GLOBAL_INFO = "global_info";
	
	private static SharedPreferences mSharedPreferences = null;

	/**
	 * 初始化公共配置
	 * @param app
	 */
	public static void initGlobalPreferences(Application app) {
		mSharedPreferences = app.getSharedPreferences(GLOBAL_INFO, Context.MODE_PRIVATE);
	}
	
	private static SharedPreferences getSharedPreferences() {
		return mSharedPreferences;
	}

	public static String getString(String key, String defValue) {
		return getSharedPreferences().getString(key, defValue);
	}

	public static int getInt(String key, int defValue) {
		return getSharedPreferences().getInt(key, defValue);
	}

	public static long getLong(String key, long defValue) {
		return getSharedPreferences().getLong(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return getSharedPreferences().getBoolean(key, defValue);
	}
	
	public static double getDouble(String key, double defValue) {
		if ( !getSharedPreferences().contains(key))
	        return defValue;
		return Double.longBitsToDouble(getSharedPreferences().getLong(key, 0L));
	}

	public static boolean putInt(String key, int value) {
		Log.d(TAG, "key=" + key + ";value=" + value);
		return getSharedPreferences().edit().putInt(key, value).commit();
	}

	public static boolean putString(String key, String value) {
		Log.d(TAG, "key="+key+";value="+value);
		return getSharedPreferences().edit().putString(key, value).commit();
	}

	public static boolean putLong(String key, long value) {
		Log.d(TAG, "key="+key+";value="+value);
		return getSharedPreferences().edit().putLong(key, value).commit();
	}

	public static boolean putBoolean(String key, Boolean value) {
		Log.d(TAG, "key="+key+";value="+value);
		return getSharedPreferences().edit().putBoolean(key, value).commit();
	}
	
	public static boolean putDouble(String key, double value) {
		Log.d(TAG, "key="+key+";value="+value);
		return getSharedPreferences().edit().putLong(key, Double.doubleToRawLongBits(value)).commit();
	}
	
	public static boolean remove(String key) {
		Log.d(TAG, "remove key="+key);
		return getSharedPreferences().edit().remove(key).commit();
	}
	
	public static boolean clear() {
		Log.d(TAG, "clear");
		return getSharedPreferences().edit().clear().commit();
	}

}
