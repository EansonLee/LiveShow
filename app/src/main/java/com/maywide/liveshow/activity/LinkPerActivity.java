package com.maywide.liveshow.activity;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
public class LinkPerActivity extends BaseAcitivity implements View.OnClickListener {

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

    @Override
    public void onClick(View v) {
        Drawable bottomDrawable = null;
        bottomDrawable = getResources().getDrawable(R.mipmap.title_line);
        bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(),
                bottomDrawable.getMinimumHeight());
        switch (v.getId()){
            //粉丝列表界面
            case R.id.tv_fans:
                tvFans.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvManage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvFans.setCompoundDrawables(null,null,null,bottomDrawable);
                tvManage.setCompoundDrawables(null,null,null,null);
                viewPager.setCurrentItem(0, false);
                break;
            //房管列表界面
            case R.id.tv_manager:
                tvFans.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvManage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvFans.setCompoundDrawables(null,null,null,null);
                tvManage.setCompoundDrawables(null,null,null,bottomDrawable);
                viewPager.setCurrentItem(1, false);
                break;
        }
    }
}
