package panda.android.lib.base.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

/**
 * 基于 https://github.com/litesuits/android-common/blob/be12cecd7de03bfb41d2ef84f1f7073f929ac4b7/src/com/litesuits/common/utils/NotificationUtil.java
 *
 * Created by shitianci on 16/2/23.
 */
public class NotificationUtil {

    private static final String TAG = NotificationUtil.class.getSimpleName();

    public interface NotificationData{
        String getTitle();
        String getContentText();
        String getTicker();
        int getId();
    }

    public static void notice(Context context, int icon, String ticker, String title, String msg) {
        // 设置通知的事件消息
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            intent.setPackage(context.getPackageName());
        }
        notice(context, intent, 0, icon, ticker, title, msg);
    }

    public static void notice(Context context, Uri uri,
                                    int icon, String ticker, String title, String msg) {
        Log.i(TAG, "notice uri :" + uri);
        // 设置通知的事件消息
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            intent.setPackage(context.getPackageName());
        }
        intent.setData(uri);
        notice(context, intent, 0, icon, ticker, title, msg);
    }

    public static void notice(Context context, String activityClass, Bundle bundle,
                                    int icon, String ticker, String title, String msg) {
        // 设置通知的事件消息
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            intent.setPackage(context.getPackageName());
        }
        intent.putExtras(bundle);
        intent.setComponent(new ComponentName(context.getPackageName(), activityClass));
        notice(context, intent, 0, icon, ticker, title, msg);
    }

    public static void notice(Context context, Intent intent, NotificationData notificationData, int icon) {
        notice(context, intent, notificationData.getId(), icon, notificationData.getTicker(), notificationData.getTitle(), notificationData.getContentText());
    }

    public static void notice(Context context, Intent intent, int id,
                                    int icon, String ticker, String title, String msg) {
        /**
         * requestCode值必须和id保持一致，否则后面的消息会覆盖前面的消息。
         * 参考：http://blog.csdn.net/bdmh/article/details/41804695
         */
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notice(context, pendingIntent, id, icon, ticker, title, msg);
    }

    public static void notice(Context context, PendingIntent pendingIntent, int id,
                                    int icon, String ticker, String title, String msg) {
        Log.d(TAG, "notice, id = " + id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(icon)
                    .setContentTitle(title)
                    .setTicker(ticker)  //通知首次出现在通知栏，带上升动画效果的
                    .setContentText(msg)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setLights(0xFFFFFF00, 0, 2000)
                    .setVibrate(new long[]{0, 100, 300})
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent) //点击后的意图
                    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                    .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间

            Notification baseNF;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                baseNF = builder.getNotification();
            } else {
                builder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
                baseNF = builder.build();
            }
            //发出状态栏通知
            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            nm.notify(id, baseNF);
        } else {
            // 创建一个NotificationManager的引用
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
            // 定义Notification的各种属性
            Notification notification = new Notification(icon, ticker, System.currentTimeMillis());
            notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_SHOW_LIGHTS;
            notification.defaults = Notification.DEFAULT_ALL;
            notification.ledARGB = Color.GREEN;
            notification.ledOnMS = 5000; //闪光时间，毫秒

            notification.tickerText = ticker;
//            notification.setLatestEventInfo(context, title, msg, pendingIntent); //在6.0（23）已经被弃用。
            // 把Notification传递给NotificationManager
            notificationManager.notify(id, notification);
        }
    }

    public static void cancel(Context context, int id) {
        Log.d(TAG, "cancel, id = " + id);
        NotificationManager nm;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //发出状态栏通知
            nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        } else {
            // 创建一个NotificationManager的引用
            nm = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        }
        nm.cancel(id);
    }
}
