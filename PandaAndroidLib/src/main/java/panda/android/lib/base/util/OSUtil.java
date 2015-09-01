package panda.android.lib.base.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class OSUtil {
	private static final String TAG = OSUtil.class.getSimpleName();
	private static final int USER_TYPE_GENERAL = 1;
	private static final int USER_TYPE_SYSTEM = 2;

	public static boolean isSDCardAvailable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static boolean isSDCardUnmounted(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED);
	}
	
	public static String getSDCardRoot(){
		return Environment.getExternalStorageDirectory().getPath() + "/"; 
	}

	public static int getApkMinSdkVersion(Context context, String filePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pakinfo = pm.getPackageArchiveInfo(filePath,
				PackageManager.GET_ACTIVITIES);
		if (pakinfo != null) {
			return pakinfo.applicationInfo.targetSdkVersion;
		}
		return -1;
	}

	public static String getStringFromManifest(Context context, String key) {
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = appInfo.metaData;
			if (bundle != null) {
				return bundle.get(key).toString();
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static int getIntFromManifest(Context context, String key) {
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = appInfo.metaData;
			if (bundle != null) {
				return bundle.getInt(key);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return 0;
	}
	
	public static String getAppPackageName(Context mContext){
		return mContext.getPackageName();
	}
	
	public static int getAppVerCode(Context mContext){
		try {  
	        PackageInfo pi=mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);  
	        return pi.versionCode;  
	    } catch (NameNotFoundException e) { 
	        Log.printStackTrace(e);  
	        return 0;  
	    }  
	}

	public static String getAppVerName(Context mContext){
		try {  
			PackageInfo pi=mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);  
			return pi.versionName;  
		} catch (Exception e) { 
			Log.printStackTrace(e);  
			return "";
		}  
	}
	
	public static String getPhotoImei(Context mContext){
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);  
		return tm.getDeviceId();
	}
	
	public static String getLocalMacAddress(Context mContext) {  
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress();  
    }
	
	public static DisplayMetrics getResolution(Context context) {
		WindowManager wndMgr = (WindowManager)context.getSystemService(Activity.WINDOW_SERVICE);
		if (null == wndMgr)
			return null;
		Display display = wndMgr.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		return metrics;
	}
	
	public static String getResolutionString(Context context) {
		WindowManager wndMgr = (WindowManager) context
				.getSystemService(Activity.WINDOW_SERVICE);
		if (null == wndMgr)
			return "";
		Display display = wndMgr.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		return metrics.widthPixels + "*" + metrics.heightPixels;
	}

    public static int dip2px(Context context, float dpValue) {
		float scale = getResolution(context).density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	
	public static boolean checkGpsEnabled(Context context) {
		LocationManager locMgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		return locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER) || locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
		
	public static void openGpsSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
        	context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // The Android SDK doc says that the location settings activity
            // may not be found. In that case show the general settings.
            // General settings activity
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
            	context.startActivity(intent);
            } catch (Exception e) {
            }
        }
	}
	
	public static boolean isNetConnected(Context context) {
		try {
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (null == connMgr) {
				return false;
			}
			NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
			if (null == networkinfo || !networkinfo.isAvailable()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			//Log.printStackTrace(e);
		}
		return false;
	}

	public static void install(Context context, String path) {
	    final String SYSTEM_VERSION_LOW= "您的系统版本过低，不能安装此应用�? ";
		try {

			if (-1 == getApkMinSdkVersion(context, path)) {
				Toast.makeText(context, SYSTEM_VERSION_LOW,
						Toast.LENGTH_LONG).show();
				return;
			}
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + path),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
	}
	
	public static String getAppDisplayName(Context context) {
		try {
			PackageManager pkgMgr = context.getPackageManager();
			ApplicationInfo appInfo = pkgMgr.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.loadLabel(pkgMgr).toString();
		} catch (NameNotFoundException e) {
			return context.getPackageName();
		}
	}


	public static int getUserType(Context context) {
		String install_path = context.getPackageCodePath();
		int user_type = USER_TYPE_GENERAL;
		if (install_path.substring(0, 7).equalsIgnoreCase("/system"))
			user_type = USER_TYPE_SYSTEM;
		return user_type;
	}

	public static String getMacAddress(Context context) {
		WifiManager wifiMgr = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (null == wifiMgr)
			return "";
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		return wifiInfo.getMacAddress();
	}

	public static boolean isKeyguardLocked(Context context) {
		KeyguardManager keyguardMgr = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		if (null == keyguardMgr)
			return false;
		return keyguardMgr.inKeyguardRestrictedInputMode();
	}

	public static String getSelfVerName(Context context) {
		String verName = "1.0";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			verName = pi.versionName;
		} catch (NameNotFoundException e) {
		}
		return verName;
	}

	public static int getSelfVerCode(Context context) {
		int verCode = 1;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			verCode = pi.versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}
	
	public static boolean isInside(Context mContext) {
		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String packageName = cn.getPackageName();  
		Log.w(TAG, "mContext.getApplicationContext().getPackageName() = " + mContext.getApplicationContext().getPackageName());
		Log.w(TAG, "packageName = " + packageName);

		if (packageName != null && packageName.equals(mContext.getApplicationContext().getPackageName()))
			return true;
		return false;
	}
	
	public static String getSerialNO() {
		String serialno = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			serialno = (String) (get.invoke(c, "ro.serialno", "unknown"));
		} catch (Exception ignored) {
		}
		return serialno;
	}
	
	public static String exec(String command) {
		Log.d(TAG, "exec " + command);
		String str = "";
		Process process = null;
		BufferedReader reader = null;
		try {
			process = Runtime.getRuntime().exec(command);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				Log.d(TAG, "exec, line = " + line);
				str += line + "\n";
			}
			reader.close();
			process.waitFor();
//			System.out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return str;
	}
	
	public static String runAsRoot(String[] cmds) throws Exception {
		String result = "";
        Process p = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        InputStream is = p.getInputStream();
        for (String tmpCmd : cmds) {
            os.writeBytes(tmpCmd+"\n");
            int readed = 0;
            byte[] buff = new byte[4096];

            // if cmd requires an output
            // due to the blocking behaviour of read(...)
            boolean cmdRequiresAnOutput = true;
            if (cmdRequiresAnOutput) {
                while( is.available() <= 0) {
                    try { Thread.sleep(200); } catch(Exception ex) {}
                }

                while( is.available() > 0) {
                    readed = is.read(buff);
                    if ( readed <= 0 ) break;
                    String seg = new String(buff,0,readed);
                    result += seg;
                    Log.i(TAG, seg);
                }
            }
        }        
        os.writeBytes("exit\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
		while ((line = reader.readLine()) != null) {
			Log.d(TAG, "exec, line = " + line);
			result += line + "\n";
		}
        os.flush();
        reader.close();
        p.waitFor();
        return result;
    }

	public static String getDeviceInfoByUmeng(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtil.isNull(device_id)) {
				device_id = mac;
			}

			if (TextUtil.isNull(device_id)) {
				device_id = Settings.Secure.getString(
						context.getContentResolver(),
						Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getObjectInfo(Object object) {
		return object.getClass().getSimpleName()+"@"+object.hashCode();
	}
	
	/**
	 * 读取apk内部的渠道号文件，原理参考 http://tech.meituan.com/mt-apk-packaging.html
	 * @param context
	 * @param prefix 
	 * @return
	 */
	public static String getChannel(Context context, String prefix) {
//		String prefix = "mtchannel";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
				if (entryName.startsWith("META-INF/"+prefix)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {
            return ret.substring(split[0].length() + 1);
        } else {
            return "";
        }
    }

}
