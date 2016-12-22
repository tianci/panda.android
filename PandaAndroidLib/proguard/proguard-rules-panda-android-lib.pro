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

## rx混淆配置
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

## butterfork
-keep class butterfork.** { *; }
-dontwarn butterfork.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterfork.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterfork.* <methods>;
}

## panda.android.lib
-keep class panda.android.lib.** { *; }