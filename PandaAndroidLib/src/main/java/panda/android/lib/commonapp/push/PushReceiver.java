package panda.android.lib.commonapp.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import panda.android.lib.base.util.IntentUtil;
import panda.android.lib.base.util.Log;

/**
 * Created by shitianci on 16/4/6.
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = PushReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, IntentUtil.getIntentInfo(intent));
    }
}
