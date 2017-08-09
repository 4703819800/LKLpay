package com.lklpay.www;


import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lklpay.www.adapter.OrderManageAdapter;
import com.lklpay.www.adapter.PullToRefreshAdapter;
import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.bean.orderBean;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.Xutils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class OrderManageListActivity extends BaseActivityBar implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rlist)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    private orderBean orderBean;
    private OrderManageAdapter orderManageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_manage_list;
    }

    @Override
    protected void initTitle() {
        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.order_manage));
    }

    @Override
    protected void initData() {
        MethodUtil.kq_loadingDialog(OrderManageListActivity.this);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvList.setLayoutManager(new LinearLayoutManager(this));

        map = new HashMap<String, String>();
        map.put("shopId", shopId);
        Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + "getOrdersByShopId", map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                MethodUtil.gb_loadingDialog();
                orderBean = gson.fromJson(result, orderBean.class);
                initAdapter(orderBean.getOrderList());
            }
        });



    }

    private void initAdapter(List<orderBean.OrderListBean> data) {

        orderManageAdapter = new OrderManageAdapter(data);
        orderManageAdapter.setOnLoadMoreListener(this, rvList);
        // 默认提供5种方法（渐显ALPHAIN、缩放SCALEIN、从下到上SLIDEIN_BOTTOM，从左到右SLIDEIN_LEFT、从右到左SLIDEIN_RIGHT）
        orderManageAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        rvList.setAdapter(orderManageAdapter);


    }

    /**
     * 刷新listView
     */
    @Override
    public void onRefresh() {
        orderManageAdapter.setEnableLoadMore(false);
        swipeLayout.setRefreshing(false);
        orderManageAdapter.setEnableLoadMore(true);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        rvList.setEnabled(false);
//        if (orderManageAdapter.getData().size() < PAGE_SIZE) {
        orderManageAdapter.loadMoreEnd(true);
//        } else {
//            if (mCurrentCounter >= TOTAL_COUNTER) {
//                //                    pullToRefreshAdapter.loadMoreEnd();//default visible
//                orderManageAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
//            } else {
//                if (isErr) {
//                    orderManageAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
//                    mCurrentCounter = orderManageAdapter.getData().size();
//                    orderManageAdapter.loadMoreComplete();
//                } else {
//                    isErr = true;
//                    Toast.makeText(OrderManageListActivity.this, R.string.load_error, Toast.LENGTH_LONG).show();
//                    orderManageAdapter.loadMoreFail();
//
//                }
//            }
//            rvList.setEnabled(true);
//        }
    }
}
