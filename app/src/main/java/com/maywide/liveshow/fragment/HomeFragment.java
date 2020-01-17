package com.maywide.liveshow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.maywide.liveshow.activity.KpiBoxReportActivity;
import com.maywide.liveshow.activity.ManageKpiActivity;
import com.maywide.liveshow.adapter.HomeCommonKpiAdapter;
import com.maywide.liveshow.adapter.HomeNewsAdapter;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.base.BaseFragment;
import com.maywide.liveshow.net.req.BannerReq;
import com.maywide.liveshow.net.req.HomeKpiReq;
import com.maywide.liveshow.net.req.HomeNewsReq;
import com.maywide.liveshow.net.resp.BannerResp;
import com.maywide.liveshow.net.resp.HomeKpiResp;
import com.maywide.liveshow.net.resp.HomeNewsResp;
import com.maywide.liveshow.net.resp.ResponseList;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by heyongbiao-pc on 2018/11/13.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 跳转到管理kpi界面
     */
    public static final int INTENT_MANAGEKPI = 1000;

    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.rcy_home_news)
    RecyclerView rcyHomeNews;
    @BindView(R.id.ll_status_bar)
    LinearLayout llStatusBar;

    Unbinder unbinder;
    RecyclerView rcyHomeKpi;
    Banner banner;
    TextView tvAllKpi;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;


    private HomeNewsAdapter newsAdapter;
    private HomeCommonKpiAdapter kpiAdapter;
    //轮播图点击后跳转链接集合
    private List<String> selectedImgUrls;

    List<HomeKpiResp> homeKpiList = new ArrayList<>();
    List<HomeNewsResp.NewsDetail> newsDetails = new ArrayList<>();

    private int TOTAL_COUNT = 0;
    private int mCurrentCounter = 0;
    private int mPageSize = 10;
    private int mPageIndex = 1;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {

        newsAdapter = new HomeNewsAdapter(R.layout.item_home_news, newsDetails);

        initHeaderView();

        //轮播图设置
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setDelayTime(2000);
        banner.isAutoPlay(true);

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent();
                intent.putExtra(BaseWebActivity.URL_TAG, selectedImgUrls.get(position));
                intent.setClass(mContext, BaseWebActivity.class);
                startActivity(intent);

            }
        });

        tvAllKpi.setOnClickListener(this);

        kpiAdapter = new HomeCommonKpiAdapter(homeKpiList);
        rcyHomeKpi.setLayoutManager(new GridLayoutManager(mContext, 3));
        rcyHomeKpi.setAdapter(kpiAdapter);
        kpiAdapter.bindToRecyclerView(rcyHomeKpi);
        kpiAdapter.setEmptyView(R.layout.view_homekpi_empty);
        kpiAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ManageKpiActivity.class);
                startActivity(intent);
            }
        });
        kpiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(mContext, KpiBoxReportActivity.class);
                intent.putExtra(KpiBoxReportActivity.TAG_BOXID, homeKpiList.get(position).getBoxId());
                startActivity(intent);
            }
        });


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
        }, rcyHomeNews);
        newsAdapter.disableLoadMoreIfNotFullPage();


        rcyHomeNews.setLayoutManager(new LinearLayoutManager(mContext));
        rcyHomeNews.setAdapter(newsAdapter);


        //动画
        newsAdapter.openLoadAnimation();
        kpiAdapter.openLoadAnimation();

        swipeRefreshLayout.setColorSchemeResources(R.color.main_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                bannerReq();
                getHomeKpiReq();
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

    /**
     * 初始化头部控件
     */
    private void initHeaderView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_header_home, null);
        rcyHomeKpi = view.findViewById(R.id.rcy_home_kpi);
        banner = view.findViewById(R.id.banner);
        tvAllKpi = view.findViewById(R.id.tv_all_kpi);

        newsAdapter.addHeaderView(view);
    }

    @Override
    protected void initData() {

//        bannerReq();
        getHomeKpiReq();
        newsReq(1, mPageSize);

    }

    @Override
    protected View getStatusBarView() {
        return llStatusBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 首页轮播请求
     */
//    private void bannerReq() {
//
//        BannerReq req = new BannerReq();
//        req.setPhone(BaseAcitivity.mobile);
//
//        RetrofitClient
//                .getInstance()
//                .api(API.class)
//                .bannerReq(req)
//                .enqueue(new Callback<ResponseList<BannerResp>>() {
//                    @Override
//                    public void onResponse(Call<ResponseList<BannerResp>> call, Response<ResponseList<BannerResp>> response) {
//                        if (response.body() == null) {
//                            showToast(getString(R.string.home_load_err));
//                            return;
//                        }
//
//                        if ("0".equals(response.body().getCode()) && null != response.body().getData()) {
//
//                            List<BannerResp> bannerRespList = response.body().getData();
//                            List<String> imgList = new ArrayList<>();
//                            List<String> titleList = new ArrayList<>();
//                            selectedImgUrls = new ArrayList<>();
//                            for (int i = 0; i < bannerRespList.size(); i++) {
//                                imgList.add(i, bannerRespList.get(i).getImg());
//                                titleList.add(i, bannerRespList.get(i).getTitle());
//                                selectedImgUrls.add(i, bannerRespList.get(i).getSelectedImgUrl());
//                            }
//
//                            banner.setImages(imgList);
//                            banner.setBannerTitles(titleList);
//                            banner.start();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseList<BannerResp>> call, Throwable t) {
//                        showToast(getString(R.string.net_err));
//                    }
//                });
//
//    }


    /**
     * 考核指标请求
     */
    private void getHomeKpiReq() {
        HomeKpiReq req = new HomeKpiReq();
        req.setIndexType("1");
        req.setPhone(BaseAcitivity.mobile);

        showProgressDialog("");
        RetrofitClient.getInstance().api(API.class).getHomeKpi(req).enqueue(new Callback<ResponseList<HomeKpiResp>>() {
            @Override
            public void onResponse(Call<ResponseList<HomeKpiResp>> call, Response<ResponseList<HomeKpiResp>> response) {
                dismissProgressDialog();
                if (response.body() == null) {
                    return;
                }

                if ("0".equals(response.body().getCode())) {
                    homeKpiList.clear();
                    homeKpiList.addAll(response.body().getData());
                    kpiAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResponseList<HomeKpiResp>> call, Throwable t) {
                showToast(getString(R.string.net_err));
                dismissProgressDialog();

            }
        });

    }


    /**
     * 新闻列表请求
     */
    private void newsReq(final int pageIndex, int pageSize) {

        HomeNewsReq homeNewsReq = new HomeNewsReq();
        homeNewsReq.setPhone(BaseAcitivity.mobile);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all_kpi:
                Intent intent = new Intent();
                intent.setClass(mContext, ManageKpiActivity.class);
                startActivityForResult(intent, INTENT_MANAGEKPI);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_MANAGEKPI) {
            getHomeKpiReq();
        }
    }
}
