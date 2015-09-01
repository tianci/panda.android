package panda.android.lib.base.util;

public class Log {
	/**
	 * 在proguard-project.txt中利用一下代码去掉注释
	 * 
	 * #去除log日志 
	 * -assumenosideeffects class panda.android.libs.Log { public static *** d(...); }
	 */
	private static boolean openLog = true;

	public static void openLogging() {
		openLog = true;
	}

	public static void i(String tag, String msg) {
		if (openLog)
			android.util.Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (openLog)
			android.util.Log.e(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (openLog)
			android.util.Log.w(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (openLog)
			android.util.Log.d(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (openLog)
			android.util.Log.v(tag, msg);
	}

	public static void printStackTrace(Exception e) {
		if (openLog)
			e.printStackTrace();
	}

	public static void printStackTrace(Error e) {
		if (openLog)
			e.printStackTrace();
	}

	public static void println(String str) {
		if (openLog)
			System.out.println(str);
	}

	public static void printStackTrace(Throwable e) {
		if (openLog)
			e.printStackTrace();
	}
}
