package com.maywide.liveshow.fragment;

import android.os.Bundle;
import android.view.View;

import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseFragment;

public class RevenueFragent extends BaseFragment{


    public static RevenueFragent newInstance() {
        RevenueFragent fragment = new RevenueFragent();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_revenue;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected View getStatusBarView() {
        return null;
    }

    @Override
    protected void initData() {

    }
}
