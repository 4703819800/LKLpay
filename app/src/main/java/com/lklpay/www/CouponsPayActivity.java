package com.lklpay.www;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.dialog.listener.OnBtnClickL;
import com.lklpay.www.adapter.CouponsManageAdapter;
import com.lklpay.www.application.MyApplication;
import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.bean.TransactionEntity;
import com.lklpay.www.bean.couponsBean;
import com.lklpay.www.bean.orderPayBean;
import com.lklpay.www.tools.DateTimeUtil;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.UiUtils;
import com.lklpay.www.tools.Xutils;

import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponsPayActivity extends BaseActivityBar implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_money_reality)
    TextView tvMoneyReality;
    @BindView(R.id.tv_money_coupons)
    TextView tvMoneyCoupons;
    @BindView(R.id.rlist)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.btn_shuaka)
    Button btnShuaka;
    @BindView(R.id.btn_wx)
    Button btnWx;

    private Intent intent;
    private Bundle bundle;

    private String url = "getTicket";
    private String money;
    private String payMoney = "0.00";
    private String type;
    private String ticketId = "0";
    private String payType = "0";
    private String proc_cd;

    private Callback.Cancelable cancelable;
    private orderPayBean orderPayBean;
    private couponsBean couponsbean;
    private CouponsManageAdapter couponsManageAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupons_pay;
    }

    @Override
    protected void initTitle() {

        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.submit_order));

    }

    @Override
    protected void initData() {

        intent = getIntent();
        bundle = intent.getExtras();
        money = bundle.getString("money");
        type = bundle.getString("type");

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvList.setLayoutManager(new LinearLayoutManager(this));

        tvMoneyReality.setText(String.format("%.2f", Double.parseDouble(money)));
        tvMoneyCoupons.setText("0.00");


        userId = PrefUtils.getString("userId", MyApplication.userId, MyApplication.PREF_USER);
        if (!userId.equals("0")) {
            MethodUtil.kq_loadingDialog(CouponsPayActivity.this);
            map = new HashMap<String, String>();
            map.put("shopId", shopId);
            map.put("userId", userId);
            map.put("money", money);
            Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + url, map, new Xutils.XCallBack() {
                @Override
                public void onSuccess(String result) {
                    LogUtils.e(result);
                    MethodUtil.gb_loadingDialog();
                    couponsbean = gson.fromJson(result, couponsBean.class);
                    if (couponsbean.getInfo().size() > 0) {
                        if (Double.parseDouble(couponsbean.getInfo().get(0).getMoney()) < Double.parseDouble(money)) {
                            payMoney = String.format("%.2f", Double.parseDouble(money) - Double.parseDouble(couponsbean.getInfo().get(0).getMoney()));
                            tvMoneyReality.setText(payMoney);
                            tvMoneyCoupons.setText(couponsbean.getInfo().get(0).getMoney());
                        } else {
                            payMoney = "0.00";
                            tvMoneyReality.setText("0.00");
                            tvMoneyCoupons.setText(money);
                        }
                        ticketId = couponsbean.getInfo().get(0).getId();
                        initAdapter(couponsbean.getInfo());
                    }

                }

            });
        }

    }

    private void initAdapter(List<couponsBean.InfoBean> data) {

        couponsManageAdapter = new CouponsManageAdapter(data);
        couponsManageAdapter.setOnLoadMoreListener(this, rvList);
        // 默认提供5种方法（渐显ALPHAIN、缩放SCALEIN、从下到上SLIDEIN_BOTTOM，从左到右SLIDEIN_LEFT、从右到左SLIDEIN_RIGHT）
        couponsManageAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        //        pullToRefreshAdapter.setAutoLoadMoreSize(3);
        rvList.setAdapter(couponsManageAdapter);

        rvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {

                dialog = UiUtils.MaterialDialogDefault(CouponsPayActivity.this, MethodUtil.getContext().getResources().getString(R.string.send_stamps_employ));
                dialog.setOnBtnClickL(
                        new OnBtnClickL() {//left btn click listener
                            @Override
                            public void onBtnClick() {
                                dialog.dismiss();
                            }
                        },
                        new OnBtnClickL() {//right btn click listener
                            @Override
                            public void onBtnClick() {
                                if (Double.parseDouble(couponsbean.getInfo().get(position).getMoney()) < Double.parseDouble(money)) {
                                    payMoney = String.format("%.2f", Double.parseDouble(money) - Double.parseDouble(couponsbean.getInfo().get(position).getMoney()));
                                    tvMoneyReality.setText(payMoney);
                                    tvMoneyCoupons.setText(couponsbean.getInfo().get(position).getMoney());
                                } else {
                                    payMoney = "0.00";
                                    tvMoneyReality.setText("0.00");
                                    tvMoneyCoupons.setText(money);
                                }
                                ticketId = couponsbean.getInfo().get(position).getId();
                                dialog.dismiss();
                            }
                        }
                );

            }
        });


    }

    /**
     * 刷新listView
     */
    @Override
    public void onRefresh() {
        couponsManageAdapter.setEnableLoadMore(false);
        Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + url, map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                couponsbean = gson.fromJson(result, couponsBean.class);

                couponsManageAdapter.setNewData(couponsbean.getInfo());
                swipeLayout.setRefreshing(false);
                couponsManageAdapter.setEnableLoadMore(true);

            }

        });

    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        rvList.setEnabled(false);
        couponsManageAdapter.loadMoreEnd(true);

    }

    /**
     * 000000 消费
     * 200000 消费撤销
     * 660000 扫码支付
     * 680000 扫码撤销
     * 700000 扫码补单
     * 900000 结算
     *
     * @param view
     */
    @OnClick({R.id.btn_shuaka, R.id.btn_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_shuaka:
                payType = "0";
                proc_cd = "000000";
                order();
                break;
            case R.id.btn_wx:
                payType = "1";
                proc_cd = "660000";
                order();
                break;
        }

    }

    /**
     * 提交订单到服务器
     */
    private void order() {
        userId = PrefUtils.getString("userId", MyApplication.userId, MyApplication.PREF_USER);
        MethodUtil.kq_loadingDialog(CouponsPayActivity.this);
        Map<String, String> mapOrder = new HashMap<String, String>();
        mapOrder.put("userId", userId);
        mapOrder.put("shopId", shopId);
        mapOrder.put("money", payMoney);
        mapOrder.put("ticketId", ticketId);
        Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + "submitOrder", mapOrder, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                MethodUtil.gb_loadingDialog();
                orderPayBean = gson.fromJson(result, orderPayBean.class);
                if (orderPayBean.isStatus()) {
                    MethodUtil.showToast(orderPayBean.getTicketMessage());
                    lkl_order();
//                    setOrdetState(orderPayBean.getOrderNum());//测试
                }

            }
        });
    }

    /**
     * 收单接口
     */
    private void lkl_order() {
        try {
            Intent intent = setComponent();
            Bundle bundle = new Bundle();
            bundle.putString("msg_tp", "0200");
            bundle.putString("pay_tp", payType);
            bundle.putString("proc_tp", "00");
            //bundle.putString("return_type", returntype);
            //bundle.putString("adddataword", adddataword);

            bundle.putString("proc_cd", proc_cd);
            bundle.putString("amt", money);
            bundle.putString("order_no", orderPayBean.getOrderNum());
            //bundle.putString("print_info", printinfo);
            bundle.putString("appid", "com.lklpay.www");
            bundle.putString("time_stamp",
                    DateTimeUtil.getCurrentDate("yyyyMMddhhmmss"));
            intent.putExtras(bundle);
            LogUtils.d("========jsonArray======" + bundle.getString("reserve"));
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            LogUtils.e(e.toString());
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * 收单接口回调结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 应答码
        String msg_tp = data.getExtras().getString("msg_tp");
        // 检索参考号
        String refernumber = data.getExtras().getString("refernumber");
        // 订单号
        String order_no = data.getExtras().getString("order_no");
        // 批次流水号
        String batchbillno = data.getExtras().getString("batchbillno");
        // 失败原因
        String reason = data.getExtras().getString("reason");
        // 时间戳
        String time = data.getExtras().getString("time_stamp");
        // 附加数据
        String addword = data.getExtras().getString("adddataword");
        // 交易详情
        TransactionEntity transactionEntity = gson.fromJson(data.getExtras()
                .getString("txndetail"), TransactionEntity.class);
        switch (resultCode) {
            // 支付成功
            case Activity.RESULT_OK:

                setOrdetState(order_no);

                break;
            // 支付取消
            case Activity.RESULT_CANCELED:
                if (reason != null) {
                    MethodUtil.showToast(reason);
                }
                break;
            case -2:
                // 交易失败
                if (reason != null) {
                    MethodUtil.showToast(" 交易失败：\n\n\r" + reason);
                }
                break;
            default:

                break;
        }
    }

    /**
     * 更改服务器订单状态
     *
     * @param orderNum
     */
    private void setOrdetState(final String orderNum) {
        MethodUtil.kq_loadingDialog(CouponsPayActivity.this);
        Map<String, String> mapOrderState = new HashMap<String, String>();
        mapOrderState.put("orderNum", orderNum);
        cancelable = Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + "changePayStatus", mapOrderState, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                MethodUtil.gb_loadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.order_state_success));
                        setResult(MyApplication.PAY_SUCCESS);
                        closeCurrent();
                    } else {
                        showMaterialDialogDefault(orderNum);
                    }
                } catch (Exception e) {
                    showMaterialDialogDefault(orderNum);
                }


            }
        });
    }

    /**
     * 显示对话框
     */
    public void showMaterialDialogDefault(final String orderNum) {
        dialog = UiUtils.MaterialDialogDefault(CouponsPayActivity.this, MethodUtil.getContext().getResources().getString(R.string.order_state_fail));
        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        setOrdetState(orderNum);
                    }
                }
        );
    }

    /**
     * 设置跳转接口
     */
    public Intent setComponent() {
        ComponentName component = new ComponentName(
                "com.lkl.cloudpos.payment",
                "com.lkl.cloudpos.payment.activity.MainMenuActivity");
        Intent intent = new Intent();
        intent.setComponent(component);
        return intent;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause");

        if (cancelable != null)
            cancelable.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("onStop");

        if (cancelable != null)
            cancelable.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");

        if (cancelable != null)
            cancelable.cancel();
    }

}
