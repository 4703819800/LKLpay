package com.lklpay.www.tools;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.lklpay.www.application.MyApplication;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    Context context;

    // CrashHandler实例
    private static CrashHandler instance;

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (instance == null)
            instance = new CrashHandler();
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        this.context = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    // 实现uncaughException方法
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!solveException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // 等待2秒钟后关闭程序
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /*
     * 错误处理
     */
    private boolean solveException(Throwable e) {
        if (e == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                PrefUtils.clear(MyApplication.PREF_USER);
                Toast.makeText(context, "程序出现异常，2秒后退出", Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }
        }.start();
        return true;
    }

}
