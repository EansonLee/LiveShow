package com.maywide.liveshow.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maywide.liveshow.R;
import com.maywide.liveshow.adapter.BIdataFragmentPagerAdapter;
import com.maywide.liveshow.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BiDataFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_status_bar)
    LinearLayout llStatusBar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static BiDataFragment newInstance() {
        BiDataFragment fragment = new BiDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bidata;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.bidata_title);

        String[] titles = {
                "舆情",
                "营收",
                "专题",
                "榜单",
                "收视"};

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(RevenueFragent.newInstance());
        fragmentList.add(RevenueFragent.newInstance());
        fragmentList.add(RevenueFragent.newInstance());
        fragmentList.add(RevenueFragent.newInstance());
        fragmentList.add(RevenueFragent.newInstance());

        BIdataFragmentPagerAdapter adapter = new BIdataFragmentPagerAdapter(getFragmentManager(), fragmentList, titles);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(5);
        tab.setupWithViewPager(viewpager);


    }

    @Override
    protected View getStatusBarView() {
        return llStatusBar;
    }

    @Override
    protected void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
