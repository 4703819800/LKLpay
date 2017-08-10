package com.lklpay.www.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.widget.MaterialDialog;
import com.google.gson.Gson;
import com.lklpay.www.R;
import com.lklpay.www.tools.AppManager;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.UiUtils;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeCurrent();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        ImmersionBar.with(this).destroy();//销毁bar
    }
}
