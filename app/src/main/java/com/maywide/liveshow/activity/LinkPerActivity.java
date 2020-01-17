package com.maywide.liveshow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.maywide.liveshow.R;
import com.maywide.liveshow.adapter.LinkPerAdapter;
import com.maywide.liveshow.base.BaseAcitivity;


import butterknife.BindView;

/**
 * 联系人
 */
public class LinkPerActivity extends BaseAcitivity {

    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_manager)
    TextView tvManage;
    @BindView(R.id.bottom_view_pager)
    AHBottomNavigationViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_link_per;
    }

    @Override
    protected void initView() {

        viewPager.setCurrentItem(0, false);
        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(new LinkPerAdapter(getSupportFragmentManager()));
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
