package com.lklpay.www;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.lklpay.www.application.MyApplication;
import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.bean.userBean;
import com.lklpay.www.manager.ThreadManager;
import com.lklpay.www.tools.FileUtils;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.QRCodeUtil;
import com.lklpay.www.tools.Xutils;

import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

public class QrCodeActivity extends BaseActivityBar {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.textView2)
    TextView textView2;

    private userBean userBean;

    public String type;

    private Map<String, String> mapUserId;
    private Map<String, String> mapQrCode;
    private Callback.Cancelable cancelable;
    public String urlString = "";
    public String filePath = "";
    public String getCodeUrl = "";
    public String lacarraNum = "";
    public Handler handler = new Handler();
    public Runnable runnable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qr_code;
    }

    @Override
    protected void initTitle() {
        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.vip_shop));
    }

    @Override
    protected void initData() {

        intent = getIntent();
        bundle = intent.getExtras();
        type = bundle.getString("type");

        filePath = FileUtils.getIconDir() + File.separator + "code_pay"
                + ".jpg";
        lacarraNum = PrefUtils.getString("lacarraNum", "");
        mapQrCode = new HashMap<String, String>();
        getCodeUrl = MethodUtil.getContext().getResources().getString(R.string.qr_code) + lacarraNum;
        File file = new File(filePath);
        if (file.exists()) {
            image.setImageBitmap(BitmapFactory.decodeFile(filePath));
        } else {
            getQrCode(mapQrCode, false);
        }


        mapUserId = new HashMap<String, String>();
        mapUserId.put("lacarraNum", lacarraNum);
        getUserId(mapUserId);

    }

    /**
     * 生成二维码
     */
    private void getQrCode(Map<String, String> map, final boolean type) {

        MethodUtil.kq_loadingDialog(QrCodeActivity.this);

        Xutils.getInstance().get(getCodeUrl, map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    urlString = jsonObject.getString("url");
                    initQRCode(filePath, urlString, type);
                } catch (Exception e) {

                }

            }
        });

    }

    /**
     * 获取扫码用户信息
     */
    private void getUserId(Map<String, String> map) {

        cancelable = Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + "getUserByLacarraNum", map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                userBean = gson.fromJson(result, userBean.class);
                if (userBean.isStatus()) {

                    PrefUtils.setString("userId", userBean.getInfo().getUserId(), MyApplication.PREF_USER);
                    PrefUtils.setString("name", userBean.getInfo().getName(), MyApplication.PREF_USER);
                    PrefUtils.setString("img", userBean.getInfo().getImg(), MyApplication.PREF_USER);

                    closeCurrent();
                    if (type.equals("main"))
                        MethodUtil.startActivity(new Intent(QrCodeActivity.this, CounterActivity.class));

                } else {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            getUserId(mapUserId);
                        }
                    };
                    handler.postDelayed(runnable, 1500);
                }

            }
        });

    }

    private void initQRCode(final String path, final String url, final boolean type) {
        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(url, wm[0],
                        wm[0], null, path);
//                BitmapFactory.decodeResource(getResources(),
//                        R.mipmap.ic_launcher);// 添加logo
                MethodUtil.gb_loadingDialog();
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageBitmap(BitmapFactory
                                    .decodeFile(path));
                            if (type)
                                MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.rebuild_code_success));


                        }
                    });
                } else {
                    MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.rebuild_code_no));
                }
            }
        });
    }


    @OnClick(R.id.textView2)
    public void onViewClicked() {
        File file = new File(filePath);
        MethodUtil.deleteFile(file);

        getQrCode(mapQrCode, true);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e("onRestart");
        getUserId(mapUserId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause");
        if (handler != null)
            handler.removeCallbacks(runnable);
        if (cancelable != null)
            cancelable.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("onStop");
        if (handler != null)
            handler.removeCallbacks(runnable);
        if (cancelable != null)
            cancelable.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
        if (handler != null)
            handler.removeCallbacks(runnable);
        if (cancelable != null)
            cancelable.cancel();
    }
}
