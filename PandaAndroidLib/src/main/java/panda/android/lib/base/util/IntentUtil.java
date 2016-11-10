package panda.android.lib.base.util;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import panda.android.lib.R;
import panda.android.lib.base.control.SimpleSafeTask;
import panda.android.lib.base.ui.fragment.BaseActivityWithExtrasData;
import panda.android.lib.base.ui.fragment.BaseFragment;

/**
 * Intent相关的工具类。
 *
 * @author shitianci
 */
public class IntentUtil {

    public interface IStartInent {
        void onCannotFindCommpent();
    }


    private static final String TAG = IntentUtil.class.getSimpleName();

    /**
     * 跳转到指定组件。
     *
     * @param context
     * @param cls
     */
    public static void gotoActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(context, intent);
    }

    /**
     * 跳转到指定组件(携带相应数据)。
     *
     * @param context
     * @param cls
     * @param extra
     */
    public static void gotoActivity(Context context, Class cls, String extra) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(BaseActivityWithExtrasData.ACTIVITY_EXTRA_DATA, extra);
        startActivity(context, intent);
    }

    /**
     * 调用系统组件打开文件
     *
     * @param context
     * @param file
     */
    public static void openFile(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.getAbsolutePath());
        intent.setDataAndType(uri, FileUtil.getMimeType(file));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(context, intent);
    }

    /**
     * 发出相应的Intent
     *
     * @param context
     * @param mIntent
     */
    public static void startActivity(Context context, Intent mIntent) {
        startActivity(context, mIntent, context.getString(R.string.lib_can_not_find_activity));
    }

    /**
     * 发出相应的Intent
     *
     * @param context
     * @param mIntent
     */
    public static void startActivity(Context context, Intent mIntent, String errInfo) {
        try {
            context.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
            DevUtil.showInfo(context, errInfo);
        }
    }

    /**
     * 发出相应的Intent
     *
     * @param context
     * @param mIntent
     */
    public static void startActivity(Context context, Intent mIntent, IStartInent iStartInent) {
        try {
            context.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
            iStartInent.onCannotFindCommpent();
        }
    }


    /**
     * 发出相应的Intent
     *
     * @param context
     * @param mIntent
     */
    public static void startActivityForResult(BaseFragment context, Intent mIntent, int requestCode) {
        startActivityForResult(context, mIntent, requestCode, context.getString(R.string.lib_can_not_find_activity));
    }

    /**
     * 发出相应的Intent
     *
     * @param context
     * @param mIntent
     */
    public static void startActivityForResult(BaseFragment context, Intent mIntent, int requestCode, String errInfo) {
        try {
            context.startActivityForResult(mIntent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            DevUtil.showInfo(context.getActivity(), errInfo);
        }
    }

    /**
     * 打印Intent的数据
     *
     * @param intent
     */
    public static String getIntentInfo(Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("IntentInfo: ");
        try {
            Bundle bundle = intent.getExtras();
            for (String key : bundle.keySet()) {
                sb.append(key + ":" + bundle.get(key) + ";");
            }
//            Log.i(TAG, sb.toString());
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static void startService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startService(intent);
    }

    public static void bindService(Context context, Class<?> cls, ServiceConnection serviceCon) {
        Intent intent = new Intent(context, cls);
        context.bindService(intent, serviceCon, Context.BIND_AUTO_CREATE);
    }


    /**
     * 拨号页面
     */
    public static void gotoDialPhone(Context context, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    /**
     * 原生的分享图片
     */
    public static void shareImages(Context context, ArrayList<Uri> uriList) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
//        context.startActivity(Intent.createChooser(shareIntent, "成长记忆"));
        context.startActivity(shareIntent);
    }

    /**
     * 获取某个view的图片，并启动分享
     * @param context
     * @param view
     */
    public static void shareImage(final Context context, final View view, Dialog loadingDialog) {
        view.setDrawingCacheEnabled(true);
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        shareImage(context, bitmap, view, loadingDialog);
    }

    private static void shareImage(final Context context, final Bitmap bitmap, final View view, Dialog loadingDialog){
        new SimpleSafeTask<Void>(context, loadingDialog){

            File file;

            @Override
            protected Void doInBackgroundSafely() throws Exception {
                FileOutputStream fos;
                File sdRoot = Environment.getExternalStorageDirectory();
                String filename = "share.png";
                file = new File(sdRoot, filename);
                try {
                    // 判断手机设备是否有SD卡
                    boolean isHasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                    if (isHasSDCard) {
                        // SD卡根目录
                        fos = new FileOutputStream(file);
                    } else
                        throw new Exception("创建文件失败!");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    file = null;
                }
                if (view != null){
                    view.destroyDrawingCache();
                }
                return null;
            }

            @Override
            protected void onPostExecuteSafely(Void aVoid, Exception e) {
                super.onPostExecuteSafely(aVoid, e);
                view.setDrawingCacheEnabled(false);
                if (file == null){
                    DevUtil.showInfo(context, "分享失败，请检查sdcard是否正常！");
                }
                else{
//                    IntentUtil.shareImage(context, file);
                    ArrayList<File> fileList = new ArrayList<File>();
                    fileList.add(file);
                    IntentUtil.shareImagesToWeixin(context, "",  fileList);
                }
            }
        }.execute();
    }

    /**
     * 分享单个图片
     * @param context
     * @param file
     */
    public static void shareImage(Context context, File file){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/*");
//        context.startActivity(Intent.createChooser(shareIntent, "成长记忆"));
        context.startActivity(shareIntent);
    }

    /**
     * 分享多图到朋友圈，多张图片加文字
     */
    public  static void shareImagesToWeixin(Context context, String title, ArrayList<File> files) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file: files){
            uris.add(Uri.fromFile(file));
        }

        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putExtra("Kdescription", title);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        try {
            context.startActivity(intent);
        }
        catch (Exception e){
            DevUtil.showInfo(context, "温馨提示：您的设备没有安装微信");
        }
    }

//    /**
//     * 分享多个图片
//     * @param context
//     * @param files
//     */
//    public static void shareImages(Context context, ArrayList<File> files){
//        ArrayList<Uri> uris = new ArrayList<>();
//        for (File file: files){
//            uris.add(Uri.fromFile(file));
//        }
//
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//        shareIntent.setType("image/*");
////        context.startActivity(Intent.createChooser(shareIntent, "成长记忆"));
//        context.startActivity(shareIntent);
//    }


    /**
     * 拨打电话
     * @param context
     * @param phone
     */
    public static void callPhone(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }
}
