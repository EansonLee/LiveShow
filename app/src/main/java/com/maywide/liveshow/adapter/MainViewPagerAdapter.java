package com.maywide.liveshow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maywide.liveshow.fragment.BiDataFragment;
import com.maywide.liveshow.fragment.FocusGDFragment;
import com.maywide.liveshow.fragment.HomeFragment;
import com.maywide.liveshow.fragment.MyFragment;
import com.maywide.liveshow.fragment.UCommunityFagment;

import java.util.ArrayList;
import java.util.List;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.clear();

        fragments.add(HomeFragment.newInstance());
        fragments.add(BiDataFragment.newInstance());
        fragments.add(UCommunityFagment.newInstance());
        fragments.add(FocusGDFragment.newInstance());
        fragments.add(MyFragment.newInstance());

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}