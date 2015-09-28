package panda.android.lib.base.model;

import com.google.gson.Gson;
import com.litesuits.orm.db.annotation.Ignore;

import java.io.Serializable;

/**
 * 所有数据的基类，支持和gson互转（在混淆必须排除）
 * 
 * -keepclasseswithmembernames class * extends panda.android.libs.base.model.BaseModel{*;}
 * 
 * @author shitianci
 *
 */
public class BaseModel implements Serializable {
	@Ignore
	private static Gson gson = new Gson();	
	
	@Override
	public String toString() {
		return getGson().toJson(this);
	}

	public static Gson getGson() {
		return gson;
	}

}
