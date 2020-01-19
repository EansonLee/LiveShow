package com.maywide.liveshow.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.maywide.liveshow.Handler.MyTTTRtcEngineEventHandler;
import com.maywide.liveshow.LocalConfig;
import com.maywide.liveshow.Service.JWebSocketClient;
import com.maywide.liveshow.Service.JWebSocketClientService;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.ChannelChangReceiver;
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

    private ChannelChangReceiver channelChangReceiver;
    //webSocket
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;

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

        initChannelChangReceiver();
        //启动服务
        startJWebSClientService();
        //绑定服务
        bindService();
    }


    /**
     * 初始化长连接监听广播
     */
    private void initChannelChangReceiver() {
        channelChangReceiver = new ChannelChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xch.servicecallback.content");
        registerReceiver(channelChangReceiver, filter);
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(this, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(this, JWebSocketClientService.class);
        startService(intent);
    }

    /**
     * 服务连接器
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("BaseActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("BaseActivity", "服务与活动成功断开");
        }
    };

    /**
     * 结束
     */
    @Override
    public void onTerminate() {
        Log.e("----","finish");
        unregisterReceiver(channelChangReceiver);
        unbindService(serviceConnection);
        super.onTerminate();
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
