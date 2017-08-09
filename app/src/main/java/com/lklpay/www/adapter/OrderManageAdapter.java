package com.lklpay.www.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lklpay.www.R;
import com.lklpay.www.bean.orderBean;
import com.lklpay.www.tools.Xutils;

import java.util.List;

public class OrderManageAdapter extends BaseQuickAdapter<orderBean.OrderListBean, BaseViewHolder> {
    public OrderManageAdapter(List<orderBean.OrderListBean> data) {
        super(R.layout.layout_order_manage, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, orderBean.OrderListBean item) {

        Xutils.getInstance().bindCircularImage((ImageView) helper.getView(R.id.order_img),item.getImg(),true);
        helper.setText(R.id.order_Name, item.getName());
        helper.setText(R.id.order_Text, "消费："+item.getMoney());
        helper.setText(R.id.order_Date, item.getDate());
        helper.setText(R.id.order_Num,"订单号："+item.getOrderNum());
    }


}
