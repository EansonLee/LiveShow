package com.maywide.liveshow.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.maywide.liveshow.R;
import com.maywide.liveshow.adapter.LinkPerListAdapter;
import com.maywide.liveshow.base.BaseFragment;
import com.maywide.liveshow.net.resp.LinkPerResp;

import java.util.List;

import butterknife.BindView;

/**
 * 房管
 * Created by liyizhen on 2020/1/17.
 */

public class RoomManagerFragment extends BaseFragment {

    @BindView(R.id.rcv_manage)
    RecyclerView rcvManage;

    private LinkPerListAdapter perListAdapter;
    //列表数据
    private List<LinkPerResp> linkPerRespList;

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
        perListAdapter = new LinkPerListAdapter(R.layout.item_per_list,linkPerRespList);
        rcvManage.setLayoutManager(new LinearLayoutManager(mContext));
        rcvManage.setAdapter(perListAdapter);
    }

    @Override
    protected View getStatusBarView() {
        return null;
    }

    @Override
    protected void initData() {

    }
}
