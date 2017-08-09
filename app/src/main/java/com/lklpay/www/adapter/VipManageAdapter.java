package com.lklpay.www.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lklpay.www.R;
import com.lklpay.www.bean.vipBean;
import com.lklpay.www.tools.Xutils;

import java.util.List;


public class VipManageAdapter extends BaseQuickAdapter<vipBean.MemberListBean, BaseViewHolder> {
    public VipManageAdapter(List<vipBean.MemberListBean> data) {
        super(R.layout.layout_vip_manage, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, vipBean.MemberListBean item) {

        Xutils.getInstance().bindCircularImage((ImageView) helper.getView(R.id.iv_img), item.getInfo().getImg(), true);
        helper.setText(R.id.tv_Name, item.getInfo().getName());

        helper.setChecked(R.id.checkBox, item.getCheckBox());

        helper.getView(R.id.checkBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(helper.getLayoutPosition()).getCheckBox()) {
                    getItem(helper.getLayoutPosition()).setCheckBox(false);
                } else {
                    getItem(helper.getLayoutPosition()).setCheckBox(true);
                }
            }
        });

    }


}
