package com.lklpay.www;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.bean.statusSuccess;
import com.lklpay.www.tools.LogUtils;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.Xutils;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.picker.TimePicker;

public class CouponsNewActivity extends BaseActivityBar {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.coupons_name)
    EditText couponsName;
    @BindView(R.id.tv_coupons_type)
    TextView tvCouponsType;
    @BindView(R.id.coupons_money)
    EditText couponsMoney;
    @BindView(R.id.rg_group)
    RadioGroup rgGroup;
    @BindView(R.id.coupons_you_money)
    EditText couponsYouMoney;
    @BindView(R.id.coupons_start_time)
    TextView couponsStartTime;
    @BindView(R.id.coupons_end_time)
    TextView couponsEndTime;
    @BindView(R.id.coupons_start_time_min)
    TextView couponsStartTimeMin;
    @BindView(R.id.coupons_end_time_min)
    TextView couponsEndTimeMin;
    @BindView(R.id.submit)
    Button submit;

    public boolean conditions = false;

    public String CouponsType;
    public String start_date = "";
    public String end_date = "";
    public String start_time = "00:00";
    public String end_time = "23:59";

    public String couponsNameString;
    public String couponsMoneyString;
    public String couponsYouMoneyString;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupons_new;
    }

    @Override
    protected void initTitle() {

        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.coupons_new));

    }

    @Override
    protected void initData() {

        CouponsType = "你好";
        tvCouponsType.setText(CouponsType);


        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.coupons_wu:
                        conditions = false;
                        break;
                    case R.id.coupons_you:
                        conditions = true;
                        break;
                }

            }
        });
        rgGroup.check(R.id.coupons_wu);

    }


    @OnClick({R.id.tv_coupons_type, R.id.coupons_start_time, R.id.coupons_end_time, R.id.coupons_start_time_min, R.id.coupons_end_time_min, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_coupons_type:
                onCouponsTypePicker();
                break;
            case R.id.coupons_start_time:
                onYearMonthDayPicker(1);
                break;
            case R.id.coupons_end_time:
                onYearMonthDayPicker(2);
                break;
            case R.id.coupons_start_time_min:
                onTimePicker(1);
                break;
            case R.id.coupons_end_time_min:
                onTimePicker(2);
                break;
            case R.id.submit:
                couponsNameString = couponsName.getText().toString();
                couponsMoneyString = couponsMoney.getText().toString();
                couponsYouMoneyString = couponsYouMoney.getText().toString();

                if (couponsNameString.isEmpty()) {
                    MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.coupons_name_no));
                    return;
                } else if (couponsMoneyString.isEmpty()) {
                    MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.coupons_money_no));
                    return;
                } else if (conditions && couponsYouMoneyString.isEmpty()) {
                    MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.coupons_you_no));
                    return;
                } else if (start_date.length() < 5 && start_date.length() < 5) {
                    MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.coupons_time_no));
                    return;
                } else {
                    getData(couponsNameString, couponsMoneyString, couponsYouMoneyString);
                }

                break;
        }
    }

    /**
     * 提交数据
     */
    public void getData(String name, String money, String youMoney) {
        MethodUtil.kq_loadingDialog(CouponsNewActivity.this);
        map = new HashMap<String, String>();
        map.put("name", name);
        map.put("shopTypeId", PrefUtils.getString("typeId",""));
        map.put("shopId", shopId);
        map.put("money", money);
        map.put("startDate", start_date);
        map.put("endDate", end_date);
        map.put("startTime", start_time);
        map.put("endTime", end_time);

        Xutils.getInstance().post(MethodUtil.getContext().getResources().getString(R.string.gen) + "createTicket", map, new Xutils.XCallBack() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                MethodUtil.gb_loadingDialog();
                statusSuccess statusSuccess = gson.fromJson(result, statusSuccess.class);
                if (statusSuccess.isStatus()) {
                    closeCurrent();
                }
                MethodUtil.showToast(statusSuccess.getMessage());

            }
        });
    }

    /**
     * 年月日
     *
     * @param type
     */
    public void onYearMonthDayPicker(final int type) {
        final DatePicker picker = new DatePicker(this);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(2016, 8, 29);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        picker.setWeightEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                switch (type) {
                    case 1:
                        start_date = year + "-" + month + "-" + day;
                        couponsStartTime.setText(start_date);
                        break;
                    case 2:
                        end_date = year + "-" + month + "-" + day;
                        couponsEndTime.setText(end_date);
                        break;
                }
                MethodUtil.showToast(year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    /**
     * 时分
     *
     * @param type
     */
    public void onTimePicker(final int type) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 0);//23:00
        picker.setSelectedItem(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE));
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        picker.setWheelModeEnable(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                switch (type) {
                    case 1:
                        start_time = hour + ":" + minute;
                        couponsStartTimeMin.setText(start_time);
                        break;
                    case 2:
                        end_time = hour + ":" + minute;
                        couponsEndTimeMin.setText(end_time);
                        break;
                }
            }
        });
        picker.show();
    }

    public void onCouponsTypePicker() {
        SinglePicker<String> picker = new SinglePicker<>(this, new String[]{
                "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
                "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"
        });
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(50);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("请选择");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(12);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setCancelTextSize(13);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(13);
        picker.setSelectedTextColor(0xFFEE0000);
        picker.setUnSelectedTextColor(0xFF999999);
        LineConfig config = new LineConfig();
        config.setColor(0xFFEE0000);//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(200);
        picker.setBackgroundColor(0xFFE1E1E1);
        //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(7);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                CouponsType = item;
                tvCouponsType.setText(CouponsType);
                MethodUtil.showToast("index=" + index + ", item=" + item);
            }
        });
        picker.show();
    }
}
