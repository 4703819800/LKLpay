package com.lklpay.www.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.widget.MaterialDialog;
import com.google.gson.Gson;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.system.AidlMerListener;
import com.lkl.cloudpos.aidl.system.AidlSystem;
import com.lklpay.www.R;
import com.lklpay.www.tools.AppManager;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.UiUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String LKL_SERVICE_ACTION = "lkl_cloudpos_mid_service"; //行业应用服务名
    private AidlSystem systemInf = null;
    public String LKL_SHOP_ID;//商户号
    public String LKL_SHOP_POS_ID;//商户终端号
    public static BaseActivity activity;
    public ActionBar actionBar;
    public Map<String, String> map;
    public Gson gson = new Gson();
    public Intent intent;
    public Bundle bundle = new Bundle();
    public String shopId;
    public int wm[];
    public MaterialDialog dialog;
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(BaseActivity.this);
        shopId = PrefUtils.getString("shopId", null);
        wm = UiUtils.getWindowManager(MethodUtil.getContext());

        AppManager.getInstance().addActivity(this);
        initTitle();
        initActionBar();
        initData();
    }

    /***
     * 初始化界面数据
     *
     * @return
     */
    protected abstract void initData();

    /***
     * 初始化标题栏
     *
     * @return
     */
    protected abstract void initTitle();

    /***
     * 获取界面Id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /***
     * 获取界面Bar
     *
     * @return
     */
    protected void initActionBar() {
    }


    /**
     * 隐藏软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (MethodUtil.isShouldHideInput(v, ev)) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                MethodUtil.hideSoftInput(v.getWindowToken(), im);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 关闭当前activity
     */
    public void closeCurrent() {
        AppManager.getInstance().removeCurrent();
    }

    /**
     * 关闭所有activity
     */
    public static void closeCurrentAll() {
        AppManager.getInstance().removeAll();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeCurrent();
        }
        return true;
    }

    /**
     * 设别服务连接桥
     */

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            LogUtils.d("行业SDK服务连接成功");
            if (serviceBinder != null) {
                //获取设备服务实例
                AidlDeviceService serviceManager = AidlDeviceService.Stub.asInterface(serviceBinder);
                try {
                    systemInf = AidlSystem.Stub.asInterface(serviceManager
                            .getSystemService());
                    getMerNum();
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d("行业SDK服务断开了");
        }
    };

    /**
     * 获取商户号，商户终端号
     */
    public void getMerNum() {
        try {
            systemInf.getMerMsg(new AidlMerListener.Stub() {

                @Override
                public void onFail(int arg0) throws RemoteException {

                }

                @Override
                public void onSuccess(String arg0, String arg1)
                        throws RemoteException {
                    LKL_SHOP_ID = arg0;
                    LKL_SHOP_POS_ID = arg1;
                }

            });
        } catch (RemoteException e) {
            e.printStackTrace();
            MethodUtil.showToast("读取商户号异常");

        }
    }

    /**
     * 绑定服务
     */
    public void bindService() {
        try {
            Intent intent = new Intent();
            intent.setAction(LKL_SERVICE_ACTION);
            Intent eintent = new Intent(getExplicitIntent(this, intent));
            boolean flag = false;
            flag = bindService(eintent, conn, Context.BIND_AUTO_CREATE);
            if (flag) {
                LogUtils.d("服务绑定成功");
            } else {
                LogUtils.d("服务绑定失败");
            }
        } catch (Exception e) {
            return;
        }
    }

    /**
     * 获取启动服务的Intent
     *
     * @param context
     * @param implicitIntent
     * @return
     */
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        //根据intent信息，获取相匹配的服务
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(implicitIntent, 0);
        //保证服务的唯一性
        if (resolveInfos == null || resolveInfos.size() != 1) {
            return null;
        }
        //获取组件信息，创建组件名
        ResolveInfo serviceInfo = resolveInfos.get(0);
        Log.d("PackageName", resolveInfos.size() + "");
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        /**
         *真实发布时打开
         * 手机测试时不打开否则运行报错
         */
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
        /**
         *真实发布时打开
         * 手机测试时不打开否则运行报错
         */
        this.unbindService(conn);
        //        ImmersionBar.with(this).destroy();//销毁bar
    }
}
