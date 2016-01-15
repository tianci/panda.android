package panda.android.lib.base.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.Response;

import java.io.File;

import butterknife.ButterKnife;
import panda.android.lib.R;
import panda.android.lib.base.model.net.BaseRepositoryCollection;
import panda.android.lib.base.util.DevUtil;
import panda.android.lib.base.util.IntentUtil;
import panda.android.lib.base.util.Log;

/**
 * 生成一些常用修饰性view模块，如 加载进度框、 升级框、下载框
 */
public class UIUtil {

    private static final String TAG = UIUtil.class.getSimpleName();

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


    static AbstractRequest upgradeRequest = null;

    /**
     * 获取文件下载提示框
     *
     * @param context
     * @param url 下载地址
     * @param description 描述信息
     * @param filePath 保存路径
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
     * 获取文件下载提示框
     *
     * @param context
     * @param url 下载地址
     * @param description 描述信息
     * @param filePath 保存路径
     * @return
     */
    public static Dialog getFileDownloadDlg(final Context context, final String url, final String description, final String filePath) {
        return getFileDownloadDlg(context, url, description, filePath, "文件下载", "立刻下载", "稍后在说");
    }
    /**
     * 获取升级提示框
     *
     * @param context
     * @param url 下载地址
     * @param description 描述信息
     * @param filePath 文件下载路径
     * @return
     */
    public static Dialog getUpgradeDlg(final Context context, final String url, final String description, final String filePath) {
        return getFileDownloadDlg(context, url, description, filePath, "升级提示", "立刻升级", "稍后再说");
    }

}
