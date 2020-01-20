package com.maywide.liveshow.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.maywide.liveshow.R;
import com.maywide.liveshow.adapter.LinkPerListAdapter;
import com.maywide.liveshow.base.BaseFragment;
import com.maywide.liveshow.net.req.LinkPerReq;
import com.maywide.liveshow.net.resp.LinkPerResp;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 房管
 * Created by liyizhen on 2020/1/17.
 */

public class RoomManagerFragment extends BaseFragment {

    @BindView(R.id.rcv_manage)
    RecyclerView rcvManage;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;

    private int TOTAL_COUNT = 0;
    private int mCurrentCounter = 0;
    private int mPageSize = 20;
    private int mPageIndex = 1;

    private LinkPerListAdapter perListAdapter;
    //列表数据
    private List<LinkPerResp.perDetail> linkPerRespList = new ArrayList<>();

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
        perListAdapter = new LinkPerListAdapter(R.layout.item_per_list, linkPerRespList, 1);
        rcvManage.setLayoutManager(new LinearLayoutManager(mContext));
        rcvManage.setAdapter(perListAdapter);

        //设置上拉刷新
        perListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                if (mCurrentCounter >= TOTAL_COUNT) {
                    perListAdapter.loadMoreEnd();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCurrentCounter = perListAdapter.getItemCount();
                            Log.e("--", mCurrentCounter + "");
                            linkePerListReq(mPageIndex, mPageSize);
                        }
                    }, 1000);//一秒刷新
                }


            }
        }, rcvManage);
        perListAdapter.disableLoadMoreIfNotFullPage();
        perListAdapter.openLoadAnimation();

        //下拉刷新
        swipeRefreshLayout.setColorSchemeResources(R.color.main_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                linkePerListReq(1, mPageSize);
                mPageIndex = 1;
                linkPerRespList.clear();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);//取消刷新

                    }
                }, 1500);


            }
        });

    }

    @Override
    protected View getStatusBarView() {
        return null;
    }

    @Override
    protected void initData() {
        linkePerListReq(1, mPageSize);
    }
    /**
     * 房管列表请求
     *
     * @param pageIndex 页码
     * @param pageSize  页数据量
     */
    private void linkePerListReq(final int pageIndex, int pageSize) {

        LinkPerReq linkPerReq = new LinkPerReq();
        linkPerReq.setToken(sharedPreferencesUtils.getString("token", ""));
        linkPerReq.setPage(pageIndex);
        linkPerReq.setPageSize(pageSize);
        linkPerReq.setType("1");

        RetrofitClient
                .getInstance()
                .api(API.class)
                .linkePerListReq(linkPerReq)
                .enqueue(new Callback<ResponseObj<LinkPerResp>>() {
                    @Override
                    public void onResponse(Call<ResponseObj<LinkPerResp>> call, Response<ResponseObj<LinkPerResp>> response) {
                        if (response.body() == null) {
                            return;
                        }

                        if ("0".equals(response.body().getCode()) && null != response.body().getData()) {

                            LinkPerResp resp = response.body().getData();

                            if (resp.getList().size() != 0) {
                                mPageIndex++;
//                                linkPerRespList = resp.getList();
                                perListAdapter.addData(resp.getList());

                                perListAdapter.loadMoreComplete();
                                perListAdapter.setEnableLoadMore(true);
                            } else {
                                perListAdapter.loadMoreEnd();
                            }
                            TOTAL_COUNT = resp.getCount();
                            mCurrentCounter = perListAdapter.getData().size();

                        } else {
                            showToast(response.body().getMsg());
                            perListAdapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseObj<LinkPerResp>> call, Throwable t) {
                        showToast(getString(R.string.net_err));
                        perListAdapter.loadMoreEnd();
                    }
                });
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
