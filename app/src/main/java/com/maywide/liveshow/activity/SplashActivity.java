package com.maywide.liveshow.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.net.req.LoginReq;
import com.maywide.liveshow.net.resp.LoginResp;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseAcitivity {

    @BindView(R.id.iv_splash)
    ImageView ivSpalsh;
    //登录请求体
    private LoginReq loginReq;

    //跳转 登录页 or 首页
    private Intent intent;
    //是否第一次登录
    //public static boolean isFirstLogin = true;

    @Override
    protected View getNetErrView() {
        return ivSpalsh;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

        View view = View.inflate(this, R.layout.activity_splash, null);

        String url = "http://210.21.65.90:9093/static/start/android/qidongye.jpg";

        Glide.with(this)
//                .load(R.mipmap.loigin_page)
                .load(url)
                .error(R.mipmap.load_err)
                .into(ivSpalsh);

        // 渐变展示启动屏
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2000);
        view.startAnimation(animation);

        Log.e("----", "view");

    }


    @Override
    protected void initData() {
        Log.e("----", "data");

        intent = new Intent();
        intent.setClass(SplashActivity.this, LoginActivity.class);

        loginReq = new LoginReq();

        String sharePhone = sharedPreferencesUtils.getString("phone", "");
        String sharePwd = sharedPreferencesUtils.getString("password", "");
        String shareVerCode = sharedPreferencesUtils.getString("vercode", "");

        if (!TextUtils.isEmpty(sharePhone) && !TextUtils.isEmpty(sharePwd)) {

//            loginReq.setType("1");
            loginReq.setPhone(sharePhone);
            loginReq.setPassword(sharePwd);

//            RetrofitClient
//                    .getInstance()
//                    .api(API.class)
//                    .loginReq(loginReq)
//                    .enqueue();

        } else {
            //isFirstLogin = true;
            intent.setClass(SplashActivity.this, LoginActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 2000);


    }

    @Override
    protected View getStatusBarView() {
        return null;
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        finish();
//    }
}
