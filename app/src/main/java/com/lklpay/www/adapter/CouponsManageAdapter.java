package com.lklpay.www.adapter;

import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lklpay.www.R;
import com.lklpay.www.bean.couponsBean;
import com.lklpay.www.tools.MethodUtil;

import java.util.List;


/**
 * 文 件 名: PullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class CouponsManageAdapter extends BaseQuickAdapter<couponsBean.InfoBean, BaseViewHolder> {
    public CouponsManageAdapter(List<couponsBean.InfoBean> data) {
        super(R.layout.layout_coupons_manage, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, couponsBean.InfoBean item) {

        helper.setText(R.id.coupons_name, item.getName());
        helper.setText(R.id.coupons_text, "到期时间：" + item.getEndDate());
        helper.setText(R.id.coupons_date, "使用时间：" + item.getStartTime() + "~" + item.getEndTime());


        if (getItem(helper.getLayoutPosition()).getShowCheckBox()) {
            helper.setVisible(R.id.checkBox, true);
        } else {
            helper.setVisible(R.id.checkBox, false);
        }

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
