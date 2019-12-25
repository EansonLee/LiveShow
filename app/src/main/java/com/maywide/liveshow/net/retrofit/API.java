package com.maywide.liveshow.net.retrofit;


import com.maywide.liveshow.net.req.BaseReq;
import com.maywide.liveshow.net.req.DetailBoxKpiReq;
import com.maywide.liveshow.net.req.EditIndexReq;
import com.maywide.liveshow.net.req.HomeKpiReq;
import com.maywide.liveshow.net.req.HomeNewsReq;
import com.maywide.liveshow.net.req.LoginGetVerReq;
import com.maywide.liveshow.net.req.LoginReq;
import com.maywide.liveshow.net.resp.BannerResp;
import com.maywide.liveshow.net.resp.DetailBoxKpiResp;
import com.maywide.liveshow.net.resp.HomeKpiResp;
import com.maywide.liveshow.net.resp.HomeNewsResp;
import com.maywide.liveshow.net.resp.LoginResp;
import com.maywide.liveshow.net.resp.ResponseList;
import com.maywide.liveshow.net.resp.ResponseObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {

    //登录
    @Headers("Content-Type:application/json")
    @POST("login")
    Call<ResponseObj<LoginResp>> loginReq(@Body LoginReq loginReq);

    //获取验证码
    @Headers("Content-Type:application/json")
    @POST("sendverification")
    Call<ResponseObj<LoginResp>> loginVerReq(@Body LoginGetVerReq baseReq);

    //轮播图
    @Headers("Content-Type:application/json")
    @POST("homePage/image")
    Call<ResponseList<BannerResp>> bannerReq(@Body BaseReq baseReq);

    //首页KPI接口
    @Headers("Content-Type:application/json")
    @POST("homePage/homePageBiIndex")
    Call<ResponseList<HomeKpiResp>> getHomeKpi(@Body HomeKpiReq req);


    //首页新闻列表接口
    @Headers("Content-Type:application/json")
    @POST("homePage/bottomArticle")
    Call<ResponseObj<HomeNewsResp>> getHomeNews(@Body HomeNewsReq req);

    //编辑指标
    @Headers("Content-Type:application/json")
    @POST("edit/editIndex")
    Call<ResponseObj> editIndex(@Body EditIndexReq req);

    //box指标详情接口
    @Headers("Content-Type:application/json")
    @POST("BiKpi/detailBoxKpi")
    Call<ResponseObj<DetailBoxKpiResp>> detailBoxKpi(@Body DetailBoxKpiReq req);


}