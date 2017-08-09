package com.lklpay.www.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.lklpay.www.tools.CrashHandler;

import org.xutils.BuildConfig;
import org.xutils.x;


/**
 * Created by Administrator on 2015/12/11.
 */
public class MyApplication extends Application {
    private static MyApplication application = null;
    private static int mainTid;
    public static Context context = null;
    public static Handler handler = null;
    public static Thread mainThread = null;

    public static String PREF_USER = "prefuser";
    public static String userId = "0";

    public static final int PAY_SUCCESS = 10010;
    public static final int GOPAY = 10011;

    public static int mainThreadId = 0;
    public static final String ROOT = "lklpay";

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mainTid = android.os.Process.myTid();
        handler = new Handler();
        context = getApplicationContext();
        mainThread = Thread.currentThread();
        //初始化xutils
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

//        CrashHandler.getInstance().init(this);
    }

    public static Context getApplication() {
        return application;
    }

    public static int getMainTid() {
        return mainTid;
    }

    public static Handler getHandler() {
        return handler;
    }
}
