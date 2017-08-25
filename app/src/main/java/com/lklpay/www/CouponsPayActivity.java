package com.lklpay.www;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.dialog.listener.OnBtnClickL;
import com.lklpay.www.adapter.CouponsManageAdapter;
import com.lklpay.www.application.MyApplication;
import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.bean.couponsBean;
import com.lklpay.www.bean.orderPayBean;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.UiUtils;
import com.lklpay.www.tools.Xutils;

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
    @BindView(R.id.submit)
    Button submit;

    private Intent intent;
    private Bundle bundle;

    private String url = "getTicket";
    private String money;
    private String payMoney = "0.00";
    private String type;
    private String ticketId = "0";

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

    @OnClick(R.id.submit)
    public void onViewClicked() {

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
                MethodUtil.showToast(orderPayBean.getTicketMessage());

                if (orderPayBean.isTicketStatus()) {
                    MethodUtil.showToast(orderPayBean.getOrderNum());
                    setResult(MyApplication.PAY_SUCCESS);
                    closeCurrent();

                }

            }
        });

    }

}
