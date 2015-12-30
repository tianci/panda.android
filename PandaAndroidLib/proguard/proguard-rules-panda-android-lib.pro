######PandaAndroidLib
-keep public class * extends android.support.v4.app.Fragment
-keepclasseswithmembernames class * extends panda.android.lib.base.ui.fragment.BaseFragment{*;}
-keepclasseswithmembernames class * extends panda.android.lib.base.model.BaseModel{*;}
-keepclasseswithmembernames class * extends com.litesuits.http.request.param.HttpParam{*;}

#去除log日志
#-assumenosideeffects class android.util.Log { public * ; }
#-assumenosideeffects class panda.android.libs.Log {
#	public static *** d(...);
#}