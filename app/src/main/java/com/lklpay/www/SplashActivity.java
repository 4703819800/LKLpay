package com.lklpay.www;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;

import com.lklpay.www.base.BaseActivity;
import com.lklpay.www.tools.MethodUtil;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initTitle() {

    }


    @Override
    protected void initData() {
        HiPermission.create(SplashActivity.this)
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.color_blue_b5, getTheme()))
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.hipermission_close));
                    }

                    @Override
                    public void onFinish() {
                        //                        MethodUtil.showToast(SplashActivity.this,MethodUtil.getContext().getResources().getString(R.string.hipermission_open));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (shopId != null) {
                                    MethodUtil.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                } else {
                                    MethodUtil.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                }
                                closeCurrent();

                            }
                        }, 1500);
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                    }
                });
    }
}
