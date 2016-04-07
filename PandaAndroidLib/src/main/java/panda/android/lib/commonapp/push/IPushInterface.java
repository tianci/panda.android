package panda.android.lib.commonapp.push;

import android.content.Context;

/**
 * Created by shitianci on 16/4/8.
 */
public interface IPushInterface {
    public void setDebugMode(boolean b);
    public void initPush(Context context);
    public void setAlias(Context context, String alias);
    public void resumePush(Context context);
    public void stopPush(Context context);
}
