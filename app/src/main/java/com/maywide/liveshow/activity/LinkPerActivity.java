package com.maywide.liveshow.activity;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    @BindView(R.id.iv_close)
    ImageView ivClose;
    //显示的fragment
    private String showFragment;
    //标题下划线
    private Drawable bottomDrawable = null;

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

        bottomDrawable = getResources().getDrawable(R.mipmap.title_line);
        bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(),
                bottomDrawable.getMinimumHeight());

        showFragment = getIntent().getStringExtra("showFragment");

        viewPager.setAdapter(new LinkPerAdapter(getSupportFragmentManager()));
        viewPager.setPagingEnabled(true);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (viewPager.getCurrentItem() == 0) {
                    setFens(bottomDrawable);
                } else if (viewPager.getCurrentItem() == 1) {
                    setManage(bottomDrawable);
                }
            }
        });


        if (!TextUtils.isEmpty(showFragment)) {
            if (showFragment.equals("fens")) {
                //粉丝界面
                setFens(bottomDrawable);
            } else {
                //房管界面
                setManage(bottomDrawable);
            }
        } else {
            viewPager.setCurrentItem(0, false);
        }

        tvFans.setOnClickListener(this);
        tvManage.setOnClickListener(this);
        ivClose.setOnClickListener(this);
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
        return viewPager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //粉丝列表界面
            case R.id.tv_fans:
                setFens(bottomDrawable);
                break;
            //房管列表界面
            case R.id.tv_manager:
                setManage(bottomDrawable);
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }

    /**
     * 设置粉丝界面
     */
    private void setFens(Drawable bottomDrawable) {
        tvFans.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvManage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvFans.setCompoundDrawables(null, null, null, bottomDrawable);
        tvManage.setCompoundDrawables(null, null, null, null);
        viewPager.setCurrentItem(0);
    }

    /**
     * 设置房管界面
     */
    private void setManage(Drawable bottomDrawable) {
        tvFans.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvManage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvFans.setCompoundDrawables(null, null, null, null);
        tvManage.setCompoundDrawables(null, null, null, bottomDrawable);
        viewPager.setCurrentItem(1);
    }
}
