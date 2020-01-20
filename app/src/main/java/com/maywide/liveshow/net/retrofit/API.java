package com.maywide.liveshow.net.retrofit;


import com.maywide.liveshow.net.req.BaseReq;
import com.maywide.liveshow.net.req.DetailBoxKpiReq;
import com.maywide.liveshow.net.req.EditIndexReq;
import com.maywide.liveshow.net.req.HomeKpiReq;
import com.maywide.liveshow.net.req.HomeNewsReq;
import com.maywide.liveshow.net.req.LiveBroadCastReq;
import com.maywide.liveshow.net.req.LoginGetVerReq;
import com.maywide.liveshow.net.req.LoginReq;
import com.maywide.liveshow.net.req.UpGradeReq;
import com.maywide.liveshow.net.resp.BannerResp;
import com.maywide.liveshow.net.resp.BroadCastInfoResp;
import com.maywide.liveshow.net.resp.DetailBoxKpiResp;
import com.maywide.liveshow.net.resp.HomeKpiResp;
import com.maywide.liveshow.net.resp.HomeNewsResp;
import com.maywide.liveshow.net.resp.LinkPerResp;
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
    @POST("anchor/login.html")
    Call<ResponseObj<LoginResp>> loginReq(@Body LoginReq loginReq);

    //主播基本信息
    @Headers("Content-Type:application/json")
    @POST("base-info.html")
    Call<ResponseObj<BroadCastInfoResp>> baseInfoReq(@Body BaseReq baseReq);

    //将开始直播信息回传后台
    @Headers("Content-Type:application/json")
    @POST("anchor/start-live-broadcast.html")
    Call<ResponseObj<LoginResp>> liveBroadCastReq(@Body LiveBroadCastReq liveBroadCastReq);

    //退出直播
    @Headers("Content-Type:application/json")
    @POST("anchor/stop-live-broadcast.html")
    Call<ResponseObj<LoginResp>> stopLiveReq(@Body BaseReq baseReq);

    //粉丝或者房管列表
    @Headers("Content-Type:application/json")
    @POST("live-broadcast/fans-list.html")
    Call<ResponseObj<LinkPerResp>> linkePerListReq(@Body BaseReq baseReq);

    //设置房管或者降级粉丝
    @Headers("Content-Type:application/json")
    @POST("anchor/set-housing-management.html")
    Call<ResponseObj<LoginResp>> upGradeReq(@Body UpGradeReq upGradeReq);

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
