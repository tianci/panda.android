package panda.android.lib.base.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.lang.Override;import java.lang.String;import panda.android.lib.base.util.IntentUtil;

/**
 * Created by shitianci on 16/3/25.
 */
public abstract class BaseService extends Service {
    private static final String TAG = BaseService.class.getSimpleName();
    /**
     * START_NOT_STICKY
     * 如果系统在正常调用onStartCommand()方法返回后终止了服务(stopService)，则除非有挂起 Intent 要传递(就是在其他地方还有没来得及调用传递Intent的地方，例如在第一次启动Service时，然后在停止的时候立马又启动Service)，否则系统不会重建服务。这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。
     * START_STICKY
     * 如果系统在onStartCommand()返回后终止服务，则会重建服务并调用onStartCommand()，但绝对不会重新传递最后一个Intent。相反，除非有挂起 Intent 要启动服务在这种情况下，将传递这些 Intent），否则系统会通过空Intent调用onStartCommand()。这适用于不执行命令、但无限期运行并等待作业的媒体播放器（或类似服务）。（如果默认情况下没有修改onStartCommand()方法的返回值，该值为默认值）
     * START_REDELIVER_INTENT
     * 如果系统在 onStartCommand() 返回后终止服务，则会重建服务，并通过传递给服务的最后一个 Intent 调用 onStartCommand()。任何挂起 Intent 均依次传递。这适用于主动执行应该立即恢复的作业（例如下载文件）的服务。
     */
    int mStartMode=START_STICKY; // indicates how to behave if the service is killed
    IBinder mBinder; // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    @Override
    public void onCreate()
    {
        // The service is being created
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // The service is starting, due to a call to startService()
        Log.d(TAG, "onStartCommand, intent = " + IntentUtil.getIntentInfo(intent));
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // A client is binding to the service with bindService()
        Log.d(TAG, "onBind, intent = " + IntentUtil.getIntentInfo(intent));
        return getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // All clients have unbound with unbindService()
        Log.d(TAG, "onUnbind, intent = " + IntentUtil.getIntentInfo(intent));
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent)
    {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Log.d(TAG, "onRebind, intent = " + IntentUtil.getIntentInfo(intent));
    }

    @Override
    public void onDestroy()
    {
        // The service is no longer used and is being destroyed
        Log.d(TAG, "onDestroy");
    }

    /**
     * 同进程的直接返回local binder即可，不同进程的需要使用remote binder（aidl）
     * @return
     */
    protected abstract IBinder getBinder();
}
