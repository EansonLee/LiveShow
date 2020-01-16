package com.maywide.liveshow.base;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;

import com.maywide.liveshow.Handler.MyTTTRtcEngineEventHandler;
import com.maywide.liveshow.LocalConfig;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.ScreenAdapter;
import com.wushuangtech.utils.PviewLog;
import com.wushuangtech.wstechapi.TTTRtcEngine;

import java.io.File;
import java.util.Random;

/**
 * Created by heyongbiao-pc on 2018/11/13.
 */

public class MyApplication extends Application {

    /**
     * 回调类引用，用于接收SDK各种回调信令。
     */
    public MyTTTRtcEngineEventHandler mMyTTTRtcEngineEventHandler;

    @Override
    public void onCreate() {
        super.onCreate();

		RetrofitClient.init(this);

        //屏幕适配
        ScreenAdapter.setup(this);
        ScreenAdapter.register(this, 375, ScreenAdapter.MATCH_BASE_WIDTH, ScreenAdapter.MATCH_UNIT_DP);

        //1.创建自定义的 SDK 的回调接收类，继承自SDK的回调基类 TTTRtcEngineEventHandler
        mMyTTTRtcEngineEventHandler = new MyTTTRtcEngineEventHandler(getApplicationContext());
        //2.创建SDK的实例对象，APPID需要去官网上申请获取。
        TTTRtcEngine mTTTEngine = TTTRtcEngine.create(getApplicationContext(), "1b25d805f99393bad26c3642096b93e6",
                false, mMyTTTRtcEngineEventHandler);
        if (mTTTEngine == null) {
            System.exit(0);
            return;
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration configuration =new Configuration();
        configuration.setToDefaults();
        res.updateConfiguration(configuration,res.getDisplayMetrics());
        return res;
    }
}
