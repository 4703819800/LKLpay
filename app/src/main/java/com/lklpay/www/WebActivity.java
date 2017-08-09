//package com.lklpay.www;
//
//import android.graphics.Bitmap;
//import android.text.TextUtils;
//import android.view.View;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.just.library.AgentWeb;
//import com.just.library.ChromeClientCallbackManager;
//import com.lklpay.www.base.BaseActivity;
//import com.lklpay.www.tools.LogUtils;
//
//import butterknife.BindView;
//
//public class WebActivity extends BaseActivity implements View.OnClickListener{
//
//    @BindView(R.id.toolbar_title)
//    TextView mTitleTextView;
//    @BindView(R.id.container)
//    LinearLayout mLinearLayout;
//    @BindView(R.id.iv_back)
//    ImageView mBackImageView;
//    @BindView(R.id.iv_finish)
//    ImageView mFinishImageView;
//    @BindView(R.id.view_line)
//    View mLineView;
//
//    protected AgentWeb mAgentWeb;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_web;
//    }
//
//    @Override
//    protected void initTitle() {
//
//    }
//
//    @Override
//    protected void initData() {
//
//        mAgentWeb = AgentWeb.with(this)//
//                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//
//                .useDefaultIndicator()//
//                .defaultProgressBarColor()
//                .setWebViewClient(mWebViewClient)
//                .setReceivedTitleCallback(mCallback)
//                .setSecutityType(AgentWeb.SecurityType.default_check)
//                .createAgentWeb()//
//                .ready()
//                .go(null);
//
//        mAgentWeb.getLoader().loadUrl("http://www.jd.com");
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_back:
//
//                if (!mAgentWeb.back())
//                    closeCurrent();
//
//                break;
//            case R.id.iv_finish:
//                closeCurrent();
//                break;
//        }
//    }
//
//    protected WebViewClient mWebViewClient = new WebViewClient() {
//
//        //重写此方法可以让webview处理https请求。
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//            LogUtils.i("shouldOverrideUrlLoading");
//            return false;
//        }
//
//        //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//            if (url.equals("http://www.jd.com")) {
//                pageNavigator(View.GONE);
//            } else {
//                pageNavigator(View.VISIBLE);
//            }
//
//        }
//    };
//
//    /**
//     * 设置是否显示 <  or  x
//     *
//     * @param tag
//     */
//    private void pageNavigator(int tag) {
//        mBackImageView.setVisibility(tag);
//        mLineView.setVisibility(tag);
//    }
//
//    /**
//     * 获取网页的标题
//     */
//    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
//        @Override
//        public void onReceivedTitle(WebView view, String title) {
//            if (mTitleTextView != null && !TextUtils.isEmpty(title))
//                if (title.length() > 10)
//                    title = title.substring(0, 10) + "...";
//            mTitleTextView.setText(title);
//        }
//    };
//
//    /**
//     * 跟随 Activity Or Fragment 生命周期 ， 释放 CPU 更省电 。
//     */
//    @Override
//    protected void onPause() {
//        mAgentWeb.getWebLifeCycle().onPause();
//        super.onPause();
//
//    }
//
//    @Override
//    protected void onResume() {
//        mAgentWeb.getWebLifeCycle().onResume();
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mAgentWeb.getWebLifeCycle().onDestroy();
//    }
//}
