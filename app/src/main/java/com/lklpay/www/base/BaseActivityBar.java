package com.lklpay.www.base;


import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lklpay.www.R;

import butterknife.BindView;


public abstract class BaseActivityBar extends BaseActivity {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    protected void initActionBar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                closeCurrent();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
