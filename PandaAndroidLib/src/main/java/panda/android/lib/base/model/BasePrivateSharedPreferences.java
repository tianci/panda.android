package panda.android.lib.base.model;

import android.content.Context;
import android.content.SharedPreferences;

import panda.android.lib.base.util.Log;

/**
 * 分账户保存的配置文件
 * 
 * @author shitianci
 * 
 */
public class BasePrivateSharedPreferences {

	private static final String TAG = BasePrivateSharedPreferences.class
			.getSimpleName();

	private SharedPreferences mSharedPreferences = null;

	public BasePrivateSharedPreferences(Context context, String name) {
		mSharedPreferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
	}

	private SharedPreferences getSharedPreferences() {
		return mSharedPreferences;
	}

	public String getString(String key, String defValue) {
		return getSharedPreferences().getString(key, defValue);
	}

	public int getInt(String key, int defValue) {
		return getSharedPreferences().getInt(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return getSharedPreferences().getLong(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return getSharedPreferences().getBoolean(key, defValue);
	}

	public double getDouble(String key, double defValue) {
		if (!getSharedPreferences().contains(key))
			return defValue;
		return Double.longBitsToDouble(getSharedPreferences().getLong(key, 0L));
	}

	public boolean putInt(String key, int value) {
		Log.d(TAG, "key=" + key + ";value=" + value);
		return getSharedPreferences().edit().putInt(key, value).commit();
	}

	public boolean putString(String key, String value) {
		Log.d(TAG, "key=" + key + ";value=" + value);
		return getSharedPreferences().edit().putString(key, value).commit();
	}

	public boolean putLong(String key, long value) {
		Log.d(TAG, "key=" + key + ";value=" + value);
		return getSharedPreferences().edit().putLong(key, value).commit();
	}

	public boolean putBoolean(String key, Boolean value) {
		Log.d(TAG, "key=" + key + ";value=" + value);
		return getSharedPreferences().edit().putBoolean(key, value).commit();
	}

	public boolean putDouble(String key, double value) {
		Log.d(TAG, "key=" + key + ";value=" + value);
		return getSharedPreferences().edit()
				.putLong(key, Double.doubleToRawLongBits(value)).commit();
	}

	public boolean remove(String key) {
		Log.d(TAG, "remove key=" + key);
		return getSharedPreferences().edit().remove(key).commit();
	}

	public boolean clear() {
		Log.d(TAG, "clear");
		return getSharedPreferences().edit().clear().commit();
	}
}
