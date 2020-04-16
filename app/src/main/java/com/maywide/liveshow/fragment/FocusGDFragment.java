package com.maywide.liveshow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.maywide.liveshow.R;
import com.maywide.liveshow.activity.BaseWebActivity;
import com.maywide.liveshow.adapter.HomeNewsAdapter;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.base.BaseFragment;
import com.maywide.liveshow.net.req.HomeNewsReq;
import com.maywide.liveshow.net.resp.HomeNewsResp;
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
 * 聚焦广电
 */
public class FocusGDFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_status_bar)
    LinearLayout llStatusBar;
    Unbinder unbinder;
    @BindView(R.id.rcv_news)
    RecyclerView rcvNews;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int TOTAL_COUNT = 0;
    private int mCurrentCounter = 0;
    private int mPageSize = 10;
    private int mPageIndex = 1;

    List<HomeNewsResp.NewsDetail> newsDetails = new ArrayList<>();
    private HomeNewsAdapter newsAdapter;

    public static FocusGDFragment newInstance() {
        FocusGDFragment fragment = new FocusGDFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_focus_gd;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.focus_gd_title);

        newsAdapter = new HomeNewsAdapter(R.layout.item_home_news, newsDetails);
        rcvNews.setLayoutManager(new LinearLayoutManager(mContext));
        rcvNews.setAdapter(newsAdapter);

        //click
        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(BaseWebActivity.URL_TAG, newsDetails.get(position).getPageUrl());
                intent.setClass(mContext, BaseWebActivity.class);
                startActivity(intent);
            }
        });


        //设置上拉刷新
        newsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                if (mCurrentCounter >= TOTAL_COUNT) {
                    newsAdapter.loadMoreEnd();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCurrentCounter = newsAdapter.getItemCount();
                            Log.e("--", mCurrentCounter + "");
                            newsReq(mPageIndex, mPageSize);
                        }
                    }, 1000);//一秒刷新


                }


            }
        }, rcvNews);
        newsAdapter.disableLoadMoreIfNotFullPage();

        swipeRefreshLayout.setColorSchemeResources(R.color.main_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                newsReq(1, mPageSize);
                mPageIndex = 1;
                newsDetails.clear();

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
        return llStatusBar;
    }

    @Override
    protected void initData() {
        newsReq(1, mPageSize);

    }


    /**
     * 新闻列表请求
     */
    private void newsReq(final int pageIndex, int pageSize) {

        HomeNewsReq homeNewsReq = new HomeNewsReq();
//        homeNewsReq.setPhone(BaseAcitivity.mobile);
        homeNewsReq.setPageIndex(pageIndex);
        homeNewsReq.setPageSize(pageSize);

        RetrofitClient
                .getInstance()
                .api(API.class)
                .getHomeNews(homeNewsReq)
                .enqueue(new Callback<ResponseObj<HomeNewsResp>>() {
                    @Override
                    public void onResponse(Call<ResponseObj<HomeNewsResp>> call, Response<ResponseObj<HomeNewsResp>> response) {

                        if (response.body() == null) {
                            return;
                        }

                        if ("0".equals(response.body().getCode()) && null != response.body().getData()) {

                            HomeNewsResp resp = response.body().getData();

                            if (resp.pageList.size() != 0) {
                                mPageIndex++;
                                newsAdapter.addData(resp.pageList);

                                newsAdapter.loadMoreComplete();
                                newsAdapter.setEnableLoadMore(true);
                            } else {
                                newsAdapter.loadMoreEnd();
                            }
                            TOTAL_COUNT = resp.getConut();
                            mCurrentCounter = newsAdapter.getData().size();

                        } else {
                            showToast(response.body().getMsg());
                            newsAdapter.loadMoreFail();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseObj<HomeNewsResp>> call, Throwable t) {
                        showToast(getString(R.string.net_err));
                        newsAdapter.loadMoreEnd();
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
