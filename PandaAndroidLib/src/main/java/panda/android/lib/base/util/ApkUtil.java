package panda.android.lib.base.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ApkUtil {
    private static final String TAG = ApkUtil.class.getSimpleName();

    /**
     * 检查指定apk是否已经安装
     *
     * @param context     上下文
     * @param packageName apk包名
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (NameNotFoundException e) {
            // 捕捉到异常,说明未安装
            installed = false;
        }
        return installed;
    }

    public static int getPackageVersionCode(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        int versionCode = -1;
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // 捕捉到异常,说明未安装
        }
        return versionCode;
    }

    public static void install(Context context, String path) {
        Log.i(TAG, "install, path = " + path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void uninstall(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    public static void openApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi;
            pi = pm.getPackageInfo(packageName, 0);

            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);

            List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static boolean copyApkFromAssets(Context context, String fileName,
                                            String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    //跳转市场应用
    public static void gotoAppMarket(Context context, String appPackageName) {
        Uri uri = Uri.parse("market://details?id=" + appPackageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取 应用PackageInfo
     *
     * @param context
     * @return
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

}
