package com.maywide.liveshow.activity;

import android.os.Bundle;
import android.view.View;

import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseAcitivity;

import butterknife.ButterKnife;

public class LiveActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected View getStatusBarView() {
        return null;
    }

    @Override
    protected View getNetErrView() {
        return null;
    }
}
