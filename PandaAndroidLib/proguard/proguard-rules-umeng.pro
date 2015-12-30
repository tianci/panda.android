######友盟
#-keepclassmembers class * {
#   public (org.json.JSONObject);
#}

### Warning: u.aly.bt: can't find referenced method 'int checkSelfPermission(java.lang.String)' in library class android.content.Context
-dontwarn u.aly.**

#-keep public class [您的应用包名].R$*{
-keep public class panda.android.demo.R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
