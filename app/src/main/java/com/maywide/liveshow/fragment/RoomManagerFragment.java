package com.maywide.liveshow.fragment;

import android.os.Bundle;
import android.view.View;

import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseFragment;

/**
 * 房管
 * Created by liyizhen on 2020/1/17.
 */

public class RoomManagerFragment extends BaseFragment {

    public static RoomManagerFragment newInstance() {
        RoomManagerFragment fragment = new RoomManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_manager;
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
