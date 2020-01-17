package com.maywide.liveshow.fragment;

import android.os.Bundle;
import android.view.View;

import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseFragment;


/**
 * 粉丝
 * Created by liyizhen on 2020/1/17.
 */

public class FensFragment extends BaseFragment {

    public static FensFragment newInstance() {
        FensFragment fragment = new FensFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fens;
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
