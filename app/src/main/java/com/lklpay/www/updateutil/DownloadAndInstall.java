package com.lklpay.www.updateutil;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lklpay.www.R;
import com.lklpay.www.base.BaseActivity;
import com.lklpay.www.tools.FileUtils;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.Xutils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class DownloadAndInstall {

    private static final int DOWNLOADING = 1;
    private static final int DOWNLOAD_FINISH = 2;

    private Context mContext;
    private String apkPath;
    private String apkName;
    private int MandatoryUpdate;

    private String mSavePath;

    private boolean cancelUpdate;
    private ProgressBar progressBar;
    private TextView textView;
    private int progress;
    private Callback.Cancelable cancelable;

    private boolean finished = true;


    public DownloadAndInstall(Context context, String apkPath, String apkName, int mandatoryUpdate) {
        this.mContext = context;
        this.apkPath = apkPath;
        this.apkName = apkName;
        this.MandatoryUpdate = mandatoryUpdate;
        cancelUpdate = false;
        textView = new TextView(mContext);

    }

    @SuppressLint("InflateParams")
    public void showDownloadDialog() {

        Builder builder = new Builder(mContext);
        builder.setTitle("正在下载...");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.softupdate_progress, null);
        progressBar = (ProgressBar) view.findViewById(R.id.update_progress);
        textView = (TextView) view.findViewById(R.id.progress_textview);
        builder.setView(view);

        builder.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if (MandatoryUpdate == 0) {
                    cancelable.cancel();
                    BaseActivity.closeCurrentAll();
                }
            }
        });
        builder.create().show();
        downloadApk();
    }


    public void downloadApk() {

        cancelable = Xutils.getInstance().downLoadFile(apkPath, null, apkName, new Xutils.XDownLoadCallBack() {
            @Override
            public void onResponse(File result) {
                //apk下载完成后，调用系统的安装方法
                installApk();
                finished = false;
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //当前进度和文件总大小
                textView.setText((int) ((1.0 * current / total) * 100) + "%");
                progressBar.setProgress((int) ((1.0 * current / total) * 100));
            }

            @Override
            public void onFinished() {
                LogUtils.e("onFinished");
                if (finished)
                    downloadApk();
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.e("onSuccess" + result);
            }
        });


    }

    public void installApk() {
        File apkFile = new File(FileUtils.getDirGen() + "", apkName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}