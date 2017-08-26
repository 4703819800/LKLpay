package com.lklpay.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.lklpay.www.application.MyApplication;
import com.lklpay.www.base.BaseActivityBar;
import com.lklpay.www.tools.Calculate;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.PrefUtils;
import com.lklpay.www.tools.UiUtils;
import com.lklpay.www.tools.Xutils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuming on 2017/7/19.
 */

public class CounterActivity extends BaseActivityBar {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.one)
    Button one;
    @BindView(R.id.two)
    Button two;
    @BindView(R.id.three)
    Button three;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.four)
    Button four;
    @BindView(R.id.five)
    Button five;
    @BindView(R.id.six)
    Button six;
    @BindView(R.id.minus)
    Button minus;
    @BindView(R.id.seven)
    Button seven;
    @BindView(R.id.eight)
    Button eight;
    @BindView(R.id.nine)
    Button nine;
    @BindView(R.id.plus)
    Button plus;
    @BindView(R.id.zero)
    Button zero;
    @BindView(R.id.dot)
    Button dot;
    @BindView(R.id.empty)
    Button empty;
    @BindView(R.id.equal)
    Button equal;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_name)
    TextView tvName;


    public String money = "";
    public String expression = "";
    public String m = "￥";
    public String zeroString = "0.00";
    public float textMoneySize = 36;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_counter;
    }

    @Override
    protected void initTitle() {

        toolbarTitle.setText(MethodUtil.getContext().getResources().getString(R.string.shoukuan));
        toolbarTvRight.setVisibility(View.VISIBLE);
        toolbarTvRight.setText(MethodUtil.getContext().getResources().getString(R.string.sm_youhui));
    }


    @Override
    protected void initData() {
        if (PrefUtils.getString("img", null, MyApplication.PREF_USER) != null)
            Xutils.getInstance().bindCircularImage(ivImage, PrefUtils.getString("img", "", MyApplication.PREF_USER), true);
        tvName.setText(PrefUtils.getString("name", "", MyApplication.PREF_USER));

    }

    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.dot, R.id.empty, R.id.delete, R.id.minus, R.id.plus, R.id.equal, R.id.submit, R.id.toolbar_tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one:
                setTv1("1");
                break;
            case R.id.two:
                setTv1("2");
                break;
            case R.id.three:
                setTv1("3");
                break;
            case R.id.four:
                setTv1("4");
                break;
            case R.id.five:
                setTv1("5");
                break;
            case R.id.six:
                setTv1("6");
                break;
            case R.id.seven:
                setTv1("7");
                break;
            case R.id.eight:
                setTv1("8");
                break;
            case R.id.nine:
                setTv1("9");
                break;
            case R.id.zero:
                setTv1("0");
                break;
            case R.id.dot:
                setTv1(".");
                break;
            case R.id.empty:
                money = "";
                expression = "";
                tv1.setTextSize(56);
                showTV1(zeroString);
                showET1(expression);
                break;
            case R.id.delete:
                if (expression.length() > 0) {
                    expression = expression.substring(0, expression.length() - 1);
                    if (expression.length() == 0) {
                        money = "";
                        expression = "";
                        tv1.setTextSize(56);
                        showTV1(zeroString);
                    }
                    showET1(expression);

                }
                break;
            case R.id.minus:
                setTv1("-");
                break;
            case R.id.plus:
                setTv1("+");
                break;
            case R.id.equal:
                setTv1("=");
                break;
            case R.id.toolbar_tv_right:
                if (PrefUtils.getString("userId", null, MyApplication.PREF_USER) == null) {
                    intent = new Intent(CounterActivity.this, QrCodeActivity.class);
                    bundle.putString("type", "counter");
                    intent.putExtras(bundle);
                    MethodUtil.startActivityForResult(intent, MyApplication.GOPAY);
                } else {
                    MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.vip_shop_ok));
                }
                break;

            case R.id.submit:
                if (money.length() > 0) {

                    intent = new Intent(CounterActivity.this, CouponsPayActivity.class);
                    bundle.putString("money", money);
                    bundle.putString("type", "type");
                    intent.putExtras(bundle);
                    MethodUtil.startActivityForResult(intent, MyApplication.GOPAY);
                } else {
                    MethodUtil.showToast(MethodUtil.getContext().getResources().getString(R.string.pay_money_no));
                }

                break;

        }
    }

    private void setTv1(String num) {
        String notation = "";
        switch (num) {
            case "+":
            case "-":
                if (expression.length() > 0) {
                    notation = expression.substring(expression.length() - 1, expression.length());
                    if (notation.equals("=") || notation.equals("+") || notation.equals("-")) {
                        expression = expression.substring(0, expression.length() - 1);
                    }
                } else {
                    return;
                }
            case "=":
                if (expression.length() > 0) {
                    notation = expression.substring(expression.length() - 1, expression.length());
                    if (notation.equals("+") || notation.equals("-")) {
                        expression = expression.substring(0, expression.length() - 1);
                    } else if (notation.equals("=")) {
                        return;
                    }
                } else {
                    return;
                }
            default:
                if (expression.length() > 0) {
                    notation = expression.substring(expression.length() - 1, expression.length());
                    if (notation.equals("=")) {
                        return;
                    }
                }
                expression += num;
                showET1(expression);
                break;

        }


    }

    /**
     * 显示计算结果
     *
     * @param s
     */
    public void showTV1(String s) {
        try {
            double sDouble = Double.parseDouble(s);
            s = String.format("%.2f", sDouble);
            tv1.setText(m + s);
            if (MethodUtil.isOverFlowed(tv1)) {
//            money = s.toString().substring(0, s.length() - 1);
                if (textMoneySize == 36) {
                    tv1.setTextSize(textMoneySize);
                    tv1.setText(m + s);
                    textMoneySize = 0;
                } else {
                    tv1.setText("超出计算范围");
                }
            }
        } catch (Exception e) {

        }


    }

    /**
     * 显示计算公式
     *
     * @param ex
     */

    public void showET1(String ex) {

        et1.setText(ex);
        if (ex.length() > 0 && ex.substring(ex.length() - 1, ex.length()).equals("=")) {
            try {
                money = Calculate.calculate(ex.substring(0, ex.length() - 1));
                showTV1(money);//显示计算结果
            } catch (Exception exception) {
                showTV1("表达式错误!");//显示计算结果
                expression = "";
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                showMaterialDialogDefault();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showMaterialDialogDefault();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showMaterialDialogDefault() {
        dialog = UiUtils.MaterialDialogDefault(CounterActivity.this, MethodUtil.getContext().getResources().getString(R.string.pay_money_out));
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
                        PrefUtils.clear(MyApplication.PREF_USER);
                        closeCurrent();
                        dialog.dismiss();
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PrefUtils.getString("img", null, MyApplication.PREF_USER) != null)
            Xutils.getInstance().bindCircularImage(ivImage, PrefUtils.getString("img", "", MyApplication.PREF_USER), true);
        tvName.setText(PrefUtils.getString("name", "", MyApplication.PREF_USER));

        if (requestCode == MyApplication.GOPAY && resultCode == MyApplication.PAY_SUCCESS) {
            PrefUtils.clear(MyApplication.PREF_USER);
            showTV1(zeroString);
            showET1(expression);
            money = "";
            closeCurrent();
        }

    }
}
