package com.lklpay.www;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lklpay.www.base.BaseActivity;
import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.bean.loginBean;
import com.lklpay.www.tools.ImageOptionsHelper;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.Xutils;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivityBar {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tel)
    EditText tel;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.sign_in_button)
    Button signInButton;


    public String telString;
    public String pwString;
    public loginBean loginBean;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initTitle() {
        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.action_sign_in));
    }


    @Override
    protected void initData() {
//
//        x.image().loadFile("s", ImageOptionsHelper.getOptions(), new Callback.CacheCallback<File>() {
//            @Override
//            public boolean onCache(File result) {
//                return false;
//            }
//
//            @Override
//            public void onSuccess(File result) {
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });

    }

    @OnClick({R.id.sign_in_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                login();
                break;
        }
    }

    public void login() {
        telString = tel.getText().toString().trim();
        pwString = password.getText().toString().trim();
        if (telString.isEmpty()) {
            tel.setError(MethodUtil.getContext().getResources().getString(R.string.prompt_tel_no));
            return;
        } else if (pwString.isEmpty()) {
            password.setError(MethodUtil.getContext().getResources().getString(R.string.prompt_password_no));
            return;
        } else {
            signInButton.setClickable(false);
            MethodUtil.kq_loadingDialog(LoginActivity.this);
            map = new HashMap<String, String>();
            map.put("loginName", telString);
            map.put("password", pwString);
            Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + "shopLogin", map, new Xutils.XCallBack() {
                @Override
                public void onSuccess(String result) {
                    LogUtils.e(result);
                    MethodUtil.gb_loadingDialog();
                    signInButton.setClickable(true);
                    loginBean = gson.fromJson(result, loginBean.class);
                    if (!loginBean.isStatus()) {
                        MethodUtil.showToast(loginBean.getMessage());
                        return;
                    }

                    PrefUtils.setString("shopId", loginBean.getInfo().getId());
                    PrefUtils.setString("name", loginBean.getInfo().getName());
                    PrefUtils.setString("typeId", loginBean.getInfo().getTypeId());
                    PrefUtils.setString("loginName", loginBean.getInfo().getLoginName());
                    PrefUtils.setString("connectMan", loginBean.getInfo().getConnectMan());
                    PrefUtils.setString("phone", loginBean.getInfo().getPhone());
                    PrefUtils.setString("lacarraNum", loginBean.getInfo().getLacarraNum());
                    PrefUtils.setString("address", loginBean.getInfo().getAddress());
                    PrefUtils.setString("lnglat", loginBean.getInfo().getLnglat());
                    PrefUtils.setString("isTicket", loginBean.getInfo().getIsTicket());
                    PrefUtils.setString("discount", loginBean.getInfo().getDiscount());
                    PrefUtils.setString("content", loginBean.getInfo().getContent());
                    PrefUtils.setString("userAvatar", loginBean.getInfo().getImg());


                    MethodUtil.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    closeCurrent();
                }

            });

        }
    }
}

