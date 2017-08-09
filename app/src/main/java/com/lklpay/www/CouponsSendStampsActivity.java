package com.lklpay.www;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.lklpay.www.adapter.CouponsManageAdapter;
import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.bean.Status;
import com.lklpay.www.bean.couponsBean;
import com.lklpay.www.bean.statusSuccess;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.UiUtils;
import com.lklpay.www.tools.Xutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponsSendStampsActivity extends BaseActivityBar implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.btn_new_coupons)
    RelativeLayout btnNewCoupons;
    @BindView(R.id.btn_delete_coupons)
    RelativeLayout btnDeleteCoupons;
    @BindView(R.id.btn_check_all)
    Button btnCheckAll;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.rlist)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    private String url = "getTicketByShopId";
    private String vip;
    private couponsBean couponsbean;
    private CouponsManageAdapter couponsManageAdapter;
    private statusSuccess statusSuccess;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupons_manage;
    }

    @Override
    protected void initTitle() {
        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.coupons_manage));
    }

    @Override
    protected void initData() {
        MethodUtil.kq_loadingDialog(CouponsSendStampsActivity.this);
        btnDeleteCoupons.setVisibility(View.GONE);

        intent = getIntent();
        bundle = intent.getExtras();
        vip = bundle.getString("vip");

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvList.setLayoutManager(new LinearLayoutManager(this));

        map = new HashMap<String, String>();
        map.put("shopId", shopId);
        Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + url, map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                MethodUtil.gb_loadingDialog();
                couponsbean = gson.fromJson(result, couponsBean.class);

                initAdapter(couponsbean.getInfo());

            }

        });

    }

    private void initAdapter(final List<couponsBean.InfoBean> data) {
        couponsManageAdapter = new CouponsManageAdapter(data);
        couponsManageAdapter.setOnLoadMoreListener(this, rvList);
        // 默认提供5种方法（渐显ALPHAIN、缩放SCALEIN、从下到上SLIDEIN_BOTTOM，从左到右SLIDEIN_LEFT、从右到左SLIDEIN_RIGHT）
        couponsManageAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        rvList.setAdapter(couponsManageAdapter);

        rvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, final int position) {
                dialog = UiUtils.MaterialDialogDefault(CouponsSendStampsActivity.this, MethodUtil.getContext().getResources().getString(R.string.send_stamps_success));
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
                                MethodUtil.kq_loadingDialog(CouponsSendStampsActivity.this);
                                Map<String, String> mapSendTicket = new HashMap<String, String>();
                                mapSendTicket.put("shopId", shopId);
                                mapSendTicket.put("userId", vip);
                                mapSendTicket.put("tId", data.get(position).getId());

                                Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + "sendTicket", mapSendTicket, new Xutils.XCallBack() {
                                    @Override
                                    public void onSuccess(String result) {
                                        LogUtils.e(result);
                                        MethodUtil.gb_loadingDialog();
                                        statusSuccess = gson.fromJson(result, statusSuccess.class);
                                        MethodUtil.showToast(statusSuccess.getMessage());
                                        if (statusSuccess.isStatus()) {
                                            dialog.dismiss();
                                           closeCurrent();
                                        }

                                    }
                                });


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
        map = new HashMap<String, String>();
        map.put("shopId", shopId);
        Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + url, map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                MethodUtil.gb_loadingDialog();
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
//        if (couponsManageAdapter.getData().size() < PAGE_SIZE) {
        couponsManageAdapter.loadMoreEnd(true);
//        } else {
//            if (mCurrentCounter >= TOTAL_COUNTER) {
//                //                    pullToRefreshAdapter.loadMoreEnd();//default visible
//                couponsManageAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
//            } else {
//                if (isErr) {
//                    couponsManageAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
//                    mCurrentCounter = couponsManageAdapter.getData().size();
//                    couponsManageAdapter.loadMoreComplete();
//                } else {
//                    isErr = true;
//                    Toast.makeText(CouponsManageActivity.this, R.string.load_error, Toast.LENGTH_LONG).show();
//                    couponsManageAdapter.loadMoreFail();
//
//                }
//            }
//            rvList.setEnabled(true);
//        }
    }

    @OnClick({R.id.btn_new_coupons})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_new_coupons:
                MethodUtil.startActivity(new Intent(CouponsSendStampsActivity.this, CouponsNewActivity.class));
                break;

        }
    }

}
