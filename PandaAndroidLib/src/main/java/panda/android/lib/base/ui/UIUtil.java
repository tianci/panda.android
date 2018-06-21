package panda.android.lib.base.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.Response;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import panda.android.lib.R;
import panda.android.lib.base.model.net.BaseRepositoryCollection;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.IntentUtil;
import panda.android.lib.base.util.Log;
import panda.android.lib.base.util.TimeUtil;

/**
 * 生成一些常用修饰性view模块，如 加载进度框、 升级框、下载框
 * todo 用DialogFragment来实现升级框等功能。
 */
public class UIUtil {

    private static final String TAG = UIUtil.class.getSimpleName();
    public static AbstractRequest upgradeRequest = null;

    public static void showDataTimePicker(Activity context, final TextView textView, final DialogInterface.OnDismissListener listener) {
        Calendar c = Calendar.getInstance();
        long time = 0;
        try {
            String strTime = textView.getText().toString();
            time = TimeUtil.stringToLong(strTime, TimeUtil.FORMAT_DATE_TIME);
            if (time != 0) {
                c.setTimeInMillis(time);
            } else {
                time = System.currentTimeMillis();
            }
        } catch (Exception e) {
        }
        DateTimePickDialogUtil dateTimePicker = new DateTimePickDialogUtil(context, time);
        dateTimePicker.dateTimePickDialog(textView, listener);
        return;
    }


    /**
     * 获取加载进度框
     *
     * @param context
     * @param cancelable
     * @return
     */
    public static Dialog getLoadingDlg(Context context,
                                       boolean cancelable) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.net_progress, null);
        Dialog loadingDialog = UIFactory.getDialog(context, contentView, cancelable);
        return loadingDialog;
    }

    /**
     * 获取信息提示框（只有一个确定按钮）
     *
     * @param context
     * @param cancelable
     * @param message
     * @return
     */
    public static Dialog getTipsShowerDlg(final Context context, boolean cancelable, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage(message)
                .setCancelable(cancelable)
                .setNegativeButton("确定", null);
        Dialog dialog = builder.create();
        return dialog;
    }

    /**
     * 获取确认框（一个确定按钮、一个取消按钮）
     *
     * @param context
     * @param cancelable
     * @param message
     * @param negativeListener
     * @return
     */
    public static Dialog getChooseDlg(final Context context, boolean cancelable, String title, String message,
                                      DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setNegativeButton("确定", negativeListener)
                .setPositiveButton("取消", positiveListener);
        Dialog dialog = builder.create();
        return dialog;
    }

    static class DialogProgressViewHolder {
        ProgressBar mProgressBar;
        TextView mProgressBarInfo;
        View mFlProgress;
        Button mBtnOk;
        Button mBtnCancel;

        DialogProgressViewHolder(View view) {
            ButterKnife.bind(this, view);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            mProgressBarInfo = (TextView) view.findViewById(R.id.progress_bar_info);
            mFlProgress = view.findViewById(R.id.fl_progress);
            mBtnOk = (Button) view.findViewById(R.id.btn_ok);
            mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        }
    }


    /**
     * 获取文件下载提示框
     *
     * @param context
     * @param url         下载地址
     * @param description 描述信息
     * @param filePath    保存路径
     * @return
     */
    private static Dialog getFileDownloadDlg(final Context context, final String url, final String description, final String filePath, final String title, final String leftBtnText, final String rigthBtnText) {
        Log.d(TAG, "url = " + url);
        Log.d(TAG, "description = " + description);
        Log.d(TAG, "filePath = " + filePath);
        final View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(description)
                .setView(contentView)
                .setCancelable(false);
        final Dialog dialog = builder.create();

        final DialogProgressViewHolder DialogProgressViewHolder = new DialogProgressViewHolder(contentView);
        DialogProgressViewHolder.mFlProgress.setVisibility(View.INVISIBLE);
        DialogProgressViewHolder.mBtnOk.setText(leftBtnText);
        DialogProgressViewHolder.mBtnCancel.setText(rigthBtnText);
        DialogProgressViewHolder.mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upgradeRequest != null) {
                    DevUtil.showInfo(context, "正在下载中……");
                    return;
                }
                if (!BaseRepositoryCollection.tryToDetectNetwork(context)) {
                    return;
                }
                DialogProgressViewHolder.mFlProgress.setVisibility(View.VISIBLE);
                BaseRepositoryCollection.executeFileRequestAsync(url, filePath,
                        new BaseRepositoryCollection.HttpListener<File>(context, null, true, true, false) {

                            @Override
                            public void onStart(AbstractRequest<File> request) {
                                super.onStart(request);
                                Log.d(TAG, "onStart, ");
                                upgradeRequest = request;
                            }

                            @Override
                            public void onLoading(AbstractRequest<File> request, long total, long len) {
                                super.onLoading(request, total, len);
                                upgradeRequest = request;
                                Log.d(TAG, "onLoading, total = " + total + ", len = " + len);
                                DialogProgressViewHolder.mProgressBar.setMax((int) total);
                                DialogProgressViewHolder.mProgressBar.setProgress((int) len);
                                DialogProgressViewHolder.mProgressBarInfo.setText(String.format("正在下载%.0f%%", len * 100.0f / total * 1.0f));
                            }

                            @Override
                            public void onSuccess(File data, Response<File> response) {
                                super.onSuccess(data, response);
                                IntentUtil.openFile(context, new File(response.getResult().getPath()));
                                dialog.dismiss();
                                upgradeRequest = null;
                            }

                            @Override
                            public void onEnd(Response<File> response) {
                                super.onEnd(response);
                                upgradeRequest = null;
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(HttpException e, Response<File> response) {
                                super.onFailure(e, response);
                                upgradeRequest = null;
                            }

                            @Override
                            public void onCancel(File data, Response<File> response) {
                                super.onCancel(data, response);
                                upgradeRequest = null;
                            }
                        });
            }
        });
        DialogProgressViewHolder.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upgradeRequest != null) {
                    upgradeRequest.cancel();
                    upgradeRequest = null;
                }
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (upgradeRequest != null) {
                    upgradeRequest.cancel();
                    upgradeRequest = null;
                }
            }
        });
        return dialog;
    }


    /**
     * 获取Apk文件下载提示框
     *
     * @param context
     * @param url         下载地址
     * @param description 描述信息
     * @param filePath    保存路径
     * @return
     */
    public static Dialog getApkDownloadDlg(final Context context, final String url, final String description, final String filePath) {
        return getApkDownloadDlg(context, R.layout.dialog_progress, url, description, filePath, "文件下载", "立刻下载", "稍后在说");
    }

    /**
     * 获取Apk文件下载提示框
     *
     * @param context
     * @param layoutId
     * @param url         下载地址
     * @param description 描述信息
     * @param filePath    保存路径    @return
     */
    private static Dialog getApkDownloadDlg(final Context context, int layoutId, final String url, final String description, final String filePath, final String title, final String leftBtnText, final String rigthBtnText) {
        Log.d(TAG, "url = " + url);
        Log.d(TAG, "description = " + description);
        Log.d(TAG, "filePath = " + filePath);
        final View contentView = LayoutInflater.from(context).inflate(layoutId, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(description)
                .setView(contentView)
                .setCancelable(false);
        final Dialog dialog = builder.create();

        final DialogProgressViewHolder DialogProgressViewHolder = new DialogProgressViewHolder(contentView);
        DialogProgressViewHolder.mFlProgress.setVisibility(View.INVISIBLE);
        DialogProgressViewHolder.mBtnOk.setText(leftBtnText);
        DialogProgressViewHolder.mBtnCancel.setText(rigthBtnText);
        DialogProgressViewHolder.mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upgradeRequest != null) {
                    DevUtil.showInfo(context, "正在下载中……");
                    return;
                }
                if (!BaseRepositoryCollection.tryToDetectNetwork(context)) {
                    return;
                }
                DialogProgressViewHolder.mFlProgress.setVisibility(View.VISIBLE);
                BaseRepositoryCollection.executeFileRequestAsync(url, filePath,
                        new BaseRepositoryCollection.HttpListener<File>(context, null, true, true, false) {

                            @Override
                            public void onStart(AbstractRequest<File> request) {
                                super.onStart(request);
                                Log.d(TAG, "onStart, ");
                                upgradeRequest = request;
                            }

                            @Override
                            public void onLoading(AbstractRequest<File> request, long total, long len) {
                                super.onLoading(request, total, len);
                                upgradeRequest = request;
                                Log.d(TAG, "onLoading, total = " + total + ", len = " + len);
                                DialogProgressViewHolder.mProgressBar.setMax((int) total);
                                DialogProgressViewHolder.mProgressBar.setProgress((int) len);
                                DialogProgressViewHolder.mProgressBarInfo.setText(String.format("正在下载%.0f%%", len * 100.0f / total * 1.0f));
                            }

                            @Override
                            public void onSuccess(File data, Response<File> response) {
                                super.onSuccess(data, response);
                                dialog.dismiss();
                                upgradeRequest = null;

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //android4.0以后需要添加这行代码
                                intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
                                context.startActivity(intent);
                            }

                            @Override
                            public void onEnd(Response<File> response) {
                                super.onEnd(response);
                                upgradeRequest = null;
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(HttpException e, Response<File> response) {
                                super.onFailure(e, response);
                                upgradeRequest = null;
                            }

                            @Override
                            public void onCancel(File data, Response<File> response) {
                                super.onCancel(data, response);
                                upgradeRequest = null;
                            }
                        });
            }
        });
        DialogProgressViewHolder.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upgradeRequest != null) {
                    upgradeRequest.cancel();
                    upgradeRequest = null;
                }
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (upgradeRequest != null) {
                    upgradeRequest.cancel();
                    upgradeRequest = null;
                }
            }
        });
        return dialog;
    }

    /**
     * 获取文件下载提示框
     *
     * @param context
     * @param url         下载地址
     * @param description 描述信息
     * @param filePath    保存路径
     * @return
     */
    public static Dialog getFileDownloadDlg(final Context context, final String url, final String description, final String filePath) {
        return getFileDownloadDlg(context, url, description, filePath, "文件下载", "立刻下载", "稍后在说");
    }

    /**
     * 获取升级提示框
     *
     * @param context
     * @param url         下载地址
     * @param description 描述信息
     * @param filePath    文件下载路径
     * @return
     */
    public static Dialog getUpgradeDlg(final Context context, final String url, final String description, final String filePath) {
        return getFileDownloadDlg(context, url, description, filePath, "升级提示", "立刻升级", "稍后再说");
    }

    public interface IContentGetter {
        String getContent(Object o);
    }
    
    /**
     * 向指定的容器填充布局文件
     * TODO: 可以利用adapter机制统一替换
     * @param viewGroup
     * @param inflater
     * @param layoutId
     * @param data
     * @param listener
     * @param contentGetter
     */
    public static void addViewsToLinearLayout(ViewGroup viewGroup, LayoutInflater inflater, int layoutId,
                                              List data, final View.OnClickListener listener, IContentGetter contentGetter) {
        viewGroup.removeAllViews();
        for (int i = 0; i < data.size(); i++){
            ViewGroup child = (ViewGroup) inflater.inflate(layoutId, null);
            TextView tv = (TextView) child.getChildAt(0);
            Object model = data.get(i);
            tv.setText(contentGetter.getContent(model));
            child.setTag(model);
            child.setOnClickListener(listener);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewGroup.addView(child, params);
        }
    }

}
