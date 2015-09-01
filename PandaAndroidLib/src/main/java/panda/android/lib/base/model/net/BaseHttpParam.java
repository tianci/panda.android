package panda.android.lib.base.model.net;

import panda.android.lib.base.model.BaseModel;

import com.litesuits.http.request.param.HttpParam;

/**
 * 所有json类网络传出参数的基类（在混淆必须排除）
 * 
 * -keepclasseswithmembernames class * extends panda.android.libs.base.model.net.BaseHttpParam{*;}
 * 
 * @author shitianci
 *
 */
@SuppressWarnings("serial")
public class BaseHttpParam extends BaseModel implements HttpParam{

}
