package com.lklpay.www;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lklpay.www.adapter.VipManageAdapter;
import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.bean.vipBean;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.Xutils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VipManageListActivity extends BaseActivityBar implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rlist)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.btn_check_all)
    Button btnCheckAll;
    @BindView(R.id.btn_send_stamps)
    Button btnSendStamps;

    private boolean check_all = true;
    private String url = "getAllMembersByShopId";
    private vipBean vipBean;
    private VipManageAdapter vipManageAdapter;

    private String vip = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_manage_list;
    }

    @Override
    protected void initTitle() {
        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.vip_manage));
    }

    @Override
    protected void initData() {
        MethodUtil.kq_loadingDialog(VipManageListActivity.this);
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
                vipBean = gson.fromJson(result, vipBean.class);
                initAdapter(vipBean.getMemberList());
            }
        });


    }

    private void initAdapter(List<vipBean.MemberListBean> data) {

        vipManageAdapter = new VipManageAdapter(data);
        vipManageAdapter.setOnLoadMoreListener(this, rvList);
        // 默认提供5种方法（渐显ALPHAIN、缩放SCALEIN、从下到上SLIDEIN_BOTTOM，从左到右SLIDEIN_LEFT、从右到左SLIDEIN_RIGHT）
        vipManageAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        rvList.setAdapter(vipManageAdapter);


    }

    /**
     * 刷新listView
     */
    @Override
    public void onRefresh() {
        vipManageAdapter.setEnableLoadMore(false);
        Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + url, map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                vipBean = gson.fromJson(result, vipBean.class);
                vipManageAdapter.setNewData(vipBean.getMemberList());
                swipeLayout.setRefreshing(false);
                vipManageAdapter.setEnableLoadMore(true);
            }
        });

    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        rvList.setEnabled(false);
//        if (vipManageAdapter.getData().size() < PAGE_SIZE) {
        vipManageAdapter.loadMoreEnd(true);
//        } else {
//            if (mCurrentCounter >= TOTAL_COUNTER) {
//                //                    pullToRefreshAdapter.loadMoreEnd();//default visible
//                vipManageAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
//            } else {
//                if (isErr) {
//                    vipManageAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
//                    mCurrentCounter = vipManageAdapter.getData().size();
//                    vipManageAdapter.loadMoreComplete();
//                } else {
//                    isErr = true;
//                    Toast.makeText(VipManageListActivity.this, R.string.load_error, Toast.LENGTH_LONG).show();
//                    vipManageAdapter.loadMoreFail();
//
//                }
//            }
//            rvList.setEnabled(true);
//        }
    }

    @OnClick({R.id.btn_check_all, R.id.btn_send_stamps})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_check_all:
                if (check_all) {
                    btnCheckAll.setText(MethodUtil.getContext().getResources().getString(R.string.un_check_all));
                    check_all = false;
                    for (int i = 0; i < vipBean.getMemberList().size(); i++) {
                        vipBean.getMemberList().get(i).setCheckBox(true);
                    }
                } else {
                    btnCheckAll.setText(MethodUtil.getContext().getResources().getString(R.string.check_all));
                    check_all = true;
                    for (int i = 0; i < vipBean.getMemberList().size(); i++) {
                        vipBean.getMemberList().get(i).setCheckBox(false);
                    }
                }
                vipManageAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_send_stamps:
                vip = "";
                for (int i = 0; i < vipBean.getMemberList().size(); i++) {
                    if (vipBean.getMemberList().get(i).getCheckBox()) {
                        vip = vip + vipBean.getMemberList().get(i).getUserId() + ",";
                    }
                }
                if (vip.length() > 0) {
                    vip = vip.substring(0, vip.length() - 1);
                }else{
                 MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.vip_select));
                    return;
                }
                intent = new Intent(VipManageListActivity.this, CouponsSendStampsActivity.class);
                bundle.putString("vip", vip);
                intent.putExtras(bundle);
                MethodUtil.startActivity(intent);
                break;
        }
    }
}
