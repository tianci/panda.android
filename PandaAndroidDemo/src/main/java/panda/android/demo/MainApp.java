package panda.android.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import panda.android.lib.Manager.CrashHandler;
import panda.android.lib.base.configuration.LiteHttpConfiguration;
import panda.android.lib.base.configuration.UniversalImageLoaderConfiguration;
import panda.android.lib.base.model.net.BaseRepositoryCollection;
import panda.android.lib.commonapp.push.IPushInterface;
import panda.android.lib.base.BaseApp;
import panda.android.lib.base.util.IntentUtil;
import panda.android.lib.base.util.Log;

/**
 * Created by shitianci on 15/6/16.
 */
public class MainApp extends BaseApp {

    private static final String TAG = MainApp.class.getSimpleName();
    public static IPushInterface iPushInterface;

    public static final String MESSAGE_RECEIVED_ACTION = "panda.android.demo.MESSAGE_RECEIVED_ACTION";

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                Log.d(TAG, IntentUtil.getIntentInfo(intent));
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        UniversalImageLoaderConfiguration.configure(getApplicationContext(), R.drawable.banner);
        LiteHttpConfiguration.configure(getApplicationContext());//网络请求


        iPushInterface = new IPushInterface() {
            @Override
            public void setDebugMode(boolean b) {
                JPushInterface.setDebugMode(b);
            }

            @Override
            public void initPush(Context context) {
                JPushInterface.init(context);
            }

            @Override
            public void setAlias(Context context, String alias) {
                JPushInterface.setAlias(context, alias, new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {

                    }
                });

            }

            @Override
            public void resumePush(Context context) {
                JPushInterface.resumePush(context);
            }


            @Override
            public void stopPush(Context context) {
                JPushInterface.stopPush(context);
            }
        };


    }
}
