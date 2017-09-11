package com.lklpay.www.updateutil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.lklpay.www.CounterActivity;
import com.lklpay.www.R;
import com.lklpay.www.application.MyApplication;
import com.lklpay.www.base.BaseActivity;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.UiUtils;
import com.lklpay.www.tools.Xutils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CheckForUpdateTask extends AsyncTask<String, Integer, InputStream> {

    private static final int REQUEST_TIMEOUT = 5 * 1000;
    private static final int SO_TIMEOUT = 10 * 1000;
    private static final int msgWhat = 0x123;

    private String versionXmlPath;

    private HashMap<String, String> mHashMap;
    private Context mContext;
    private String mType;
    private boolean needUpdate;
    private int MandatoryUpdate = 0;// 强制更新 ：0，不强制更新：1

    private int serviceName;
    private int versionCode;
    private String apkName;
    private String apkPath;
    private String updateInfo = "";
    private DownloadAndInstall downloadAndInstall;
    public MaterialDialog dialog;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            JSONObject jo = (JSONObject) msg.obj;
            try {
                JSONObject version = jo.getJSONObject("version");
                serviceName = version.getInt("version");
                apkPath = version.getString("path");
                updateInfo = version.getString("updateInfo");
                MandatoryUpdate = version.getInt("isUpdate");
                apkName = MyApplication.apkName + serviceName + ".apk";
                if (serviceName > versionCode) {
                    showNoticeDialog();
                }
                if (serviceName <= versionCode
                        && mType.equals(MyApplication.GENGXIN_TYPE_UPDATE)) {
                    MethodUtil.showToast("已是最新版，无需更新");
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    public CheckForUpdateTask(Context context, String type) {
        this.mContext = context;
        this.mType = type;
        mHashMap = new HashMap<String, String>();
        needUpdate = false;
    }

    @Override
    protected InputStream doInBackground(String... params) {

        PackageManager manager = mContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    mContext.getPackageName(), 0);
            // versionName = info.versionName;
            versionCode = info.versionCode;
        } catch (NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("platform", "android");
        versionXmlPath = MethodUtil.getContext().getResources().getString(R.string.gen)
                + MyApplication.versionXmlPath;

        Xutils.getInstance().post(versionXmlPath, map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jo = new JSONObject(result);
                    Message msg1 = new Message();
                    msg1.obj = jo;
                    handler.sendMessage(msg1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(InputStream result) {
        if (needUpdate) {
            showNoticeDialog();
        }
    }

    private int getNativeVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    MyApplication.packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void showNoticeDialog() {
        updateInfo = updateInfo.replace("#", "\n");

        dialog = UiUtils.MaterialDialogDefault(mContext, updateInfo);
        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        if (MandatoryUpdate == 0) {
                            BaseActivity.closeCurrentAll();
                        }
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        downloadAndInstall = new DownloadAndInstall(mContext,
                                apkPath, apkName,MandatoryUpdate);
                        downloadAndInstall.showDownloadDialog();
                    }
                }
        );
        if (!((Activity) mContext).isFinishing()) {// 防止
            // 进入Activity在dialog还未来得及弹出时，立即点退出，会报这样的错误
            dialog.show();
        }

        /**
         * 监听dialog  的返回键点击事件
         */
        dialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    // 此处把dialog dismiss掉，然后把本身的activity finish掉
                    BaseActivity.closeCurrentAll();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
