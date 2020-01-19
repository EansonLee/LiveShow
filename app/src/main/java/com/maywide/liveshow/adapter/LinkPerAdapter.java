package com.maywide.liveshow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maywide.liveshow.fragment.FensFragment;
import com.maywide.liveshow.fragment.RoomManagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *联系人
 * Created by liyizhen on 2020/1/17.
 */

public class LinkPerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public LinkPerAdapter(FragmentManager fm) {
        super(fm);
        fragments.clear();

        fragments.add(FensFragment.newInstance());
        fragments.add(RoomManagerFragment.newInstance());

    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
