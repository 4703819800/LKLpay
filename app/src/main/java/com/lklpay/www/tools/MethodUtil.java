package com.lklpay.www.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lkl.cloudpos.aidl.system.AidlMerListener;
import com.lkl.cloudpos.aidl.system.AidlSystem;
import com.lklpay.www.application.MyApplication;
import com.lklpay.www.base.BaseActivity;

import java.io.File;


/**
 * Created by Administrator on 2017/4/6.
 */

public class MethodUtil {
    private static Dialog loadingDialog;

    /**
     * 获取Context
     *
     * @return
     */
    public static Context getContext() {
        return MyApplication.getApplication();
    }

    /**
     * 获取Resource
     *
     * @return
     */
    public static Resources getResource() {
        return MyApplication.getApplication().getResources();
    }

    public static Drawable getDrawalbe(int id) {
        return getResource().getDrawable(id);
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */

    public boolean isNull(String s) {
        if (null == s || s.equals("") || s.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    /**
     * 吐司弹出框
     *
     * @param text
     */
    public static void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 可以打开activity
     *
     * @param intent
     */
    public static void startActivity(Intent intent) {
        // 如果不在activity里去打开activity 需要指定任务栈 需要设置标签
        if (BaseActivity.activity == null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } else {
            BaseActivity.activity.startActivity(intent);
        }
    }

    /**
     * 带请求码跳转
     *
     * @param intent
     * @param requestCode
     */
    public static void startActivityForResult(Intent intent, int requestCode) {
        // 如果不在activity里去打开activity 需要指定任务栈 需要设置标签
        if (BaseActivity.activity == null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity) getContext()).startActivityForResult(intent, requestCode);
        } else {
            BaseActivity.activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 把Runnable 方法提交到主线程运行
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        // 在主线程运行
        if (android.os.Process.myTid() == MyApplication.getMainTid()) {
            runnable.run();
        } else {
            // 获取handler
            MyApplication.getHandler().post(runnable);
        }
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */

    public static boolean isNetworkAvailable() {
        // Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    /**
     * 判断TextView的内容宽度是否超出其可用宽度
     *
     * @param tv
     * @return
     */
    public static boolean isOverFlowed(TextView tv) {
        int availableWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight();
        Paint textViewPaint = tv.getPaint();
        float textWidth = textViewPaint.measureText(tv.getText().toString());
        if (textWidth > availableWidth) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除指定文件
     *
     * @param file
     */

    public static Boolean deleteFile(File file) {

        try {
            if (file.exists()) { // 判断文件是否存在
                if (file.isFile()) { // 判断是否是文件
                    // file.delete(); // delete()方法 你应该知道 是删除的意思;
                    RenameAndDelete(file);
                } else if (file.isDirectory()) { // 否则如果它是一个目录
                    File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                    for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                        deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                // file.delete();
                RenameAndDelete(file);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 删除指定文件加以及里边的文件
     *
     * @param file
     */
    public static Boolean delete(File file) {
        try {
            if (file.isFile()) {
                // file.delete();
                RenameAndDelete(file);
                return true;
            }

            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    // file.delete();
                    RenameAndDelete(file);
                    return true;
                }

                for (int i = 0; i < childFiles.length; i++) {
                    delete(childFiles[i]);
                }
                // file.delete();
                RenameAndDelete(file);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 重命名文件并删除文件 避免删除的时候并没有释放文件锁，从而导致无法再次创建。
     *
     * @param file
     * @return
     */
    public static Boolean RenameAndDelete(File file) {
        final File to = new File(file.getAbsolutePath()
                + System.currentTimeMillis());
        file.renameTo(to);
        to.delete();
        return true;

    }

    /**
     * 开启请求等待
     *
     * @param context
     */
    public static void kq_loadingDialog(Context context) {
        try {
            loadingDialog = UiUtils.creatLoadingDialog(context);
            if (loadingDialog != null) {
                loadingDialog.show();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 关闭请求等待
     */
    public static void gb_loadingDialog() {
        try {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                // hideSoftInput(v.getWindowToken());
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    public static void hideSoftInput(IBinder token, InputMethodManager im) {
        if (token != null) {
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 读取终端序列号
     */
    public static String getTerminalSn(AidlSystem systemInf) {
        if(systemInf!=null){
            try {
                String terminalSn = systemInf.getSerialNo();
                return terminalSn;
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }


    /**
     * 读取终端IMSI
     */
    public static String getIMSI(AidlSystem systemInf) {
        if(systemInf!=null){
            try {
            String imsi = systemInf.getIMSI();
            return imsi;
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        }else{
            return null;
        }
    }

    /**
     * 读取终端IMEI号
     */
    public static String getIMEI(AidlSystem systemInf) {
        if(systemInf!=null){ try {
            String imsi = systemInf.getIMEI();
            return imsi;
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }}else{
            return null;
        }
    }

    /**
     * 读取操作系统版本信息
     */
    public static String getAndroidOsVersion(AidlSystem systemInf) {
        if(systemInf!=null){ try {
            String androidOsVersion = systemInf.getAndroidOsVersion();
            return androidOsVersion;
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }}else{
            return null;
        }
    }

    /**
     * 获取拉卡拉对安卓系统的定制规范版本
     *
     */
    public static String getLKLOSSpecsVersion(AidlSystem systemInf) {
        if(systemInf!=null){ try {
            String androidOsVersion = systemInf.getLKLOSSpecsVersion();
            return androidOsVersion;
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }}else{
            return null;
        }
    }


}
