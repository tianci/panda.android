package panda.android.lib.base.configuration;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by shitianci on 15/12/5.
 */
public class UmengAnalyticsConfiguration {

    /**
     * 配置友盟统计
     * @param context 上下文
     */
    public static void configure(Context context) {
        MobclickAgent.openActivityDurationTrack(false);

        /**
         * 打开调试模式，可是实时看到后台日志
         */
//		MobclickAgent.setDebugMode(true);
        /**
         * 主动出发崩溃日志上传，看到后台日志是否正常
         */
//		MobclickAgent.reportError(context, "上传测试日志");
//		String test = null;
//		test.toString();
    }
}
