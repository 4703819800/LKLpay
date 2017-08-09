package com.lklpay.www;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lklpay.www.application.MyApplication;
import com.lklpay.www.base.BaseActivity;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rl_btn_vip_shop)
    RelativeLayout rlBtnVipShop;
    @BindView(R.id.rl_btn_sk)
    RelativeLayout rlBtnSk;
    @BindView(R.id.btn_coupons_manage)
    RelativeLayout btnCouponsManage;
    @BindView(R.id.btn_order_manage)
    RelativeLayout btnOrderManage;
    @BindView(R.id.btn_vip_manage)
    RelativeLayout btnVipManage;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.login)
    RelativeLayout login;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initTitle() {
        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.app_name));

    }

    @Override
    protected void initData() {

        if (shopId != null) {
            tvLogin.setText(MethodUtil.getContext().getResources().getString(R.string.action_logout));
        } else {
            tvLogin.setText(MethodUtil.getContext().getResources().getString(R.string.action_sign_in));
        }

    }

    @OnClick({R.id.rl_btn_vip_shop, R.id.rl_btn_sk, R.id.btn_coupons_manage, R.id.btn_order_manage, R.id.btn_vip_manage, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_btn_vip_shop:
                intent = new Intent(MainActivity.this, QrCodeActivity.class);
                bundle.putString("type", "main");
                intent.putExtras(bundle);
                MethodUtil.startActivity(intent);
                break;
            case R.id.rl_btn_sk:
                MethodUtil.startActivity(new Intent(MainActivity.this, CounterActivity.class));
                break;
            case R.id.btn_coupons_manage:
                MethodUtil.startActivity(new Intent(MainActivity.this, CouponsManageActivity.class));
                break;
            case R.id.btn_order_manage:
                MethodUtil.startActivity(new Intent(MainActivity.this, OrderManageListActivity.class));
                break;
            case R.id.btn_vip_manage:
                MethodUtil.startActivity(new Intent(MainActivity.this, VipManageListActivity.class));
                break;
            case R.id.login:
//                if (PrefUtils.getString("shopId", null) != null) {
                PrefUtils.clear();
                PrefUtils.clear(MyApplication.PREF_USER);
//                }
                MethodUtil.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                closeCurrent();
                break;
        }
    }


    long oldTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        long currentTime = System.currentTimeMillis();

        if (keyCode == KeyEvent.KEYCODE_BACK
                && currentTime - oldTime > 2 * 1000) {
            MethodUtil.showToast(MethodUtil.getContext().getResources()
                    .getString(R.string.zdyc_tcyy));
            oldTime = System.currentTimeMillis();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                && currentTime - oldTime < 2 * 1000) {
            oldTime = 0;
            closeCurrent();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
