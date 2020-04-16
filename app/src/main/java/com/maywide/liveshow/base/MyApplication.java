package com.maywide.liveshow.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.maywide.liveshow.Service.JWebSocketClient;
import com.maywide.liveshow.Service.JWebSocketClientService;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.ChannelChangReceiver;
import com.maywide.liveshow.utils.ScreenAdapter;


/**
 * Created by heyongbiao-pc on 2018/11/13.
 */

public class MyApplication extends Application {


    public static ChannelChangReceiver channelChangReceiver;
    //webSocket
    public static JWebSocketClient client;
    private static JWebSocketClientService.JWebSocketClientBinder binder;
    public static JWebSocketClientService jWebSClientService;

    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitClient.init(this);

        //屏幕适配
        ScreenAdapter.setup(this);
        ScreenAdapter.register(this, 375, ScreenAdapter.MATCH_BASE_WIDTH, ScreenAdapter.MATCH_UNIT_DP);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    /**
     * 服务连接器
     */
    public static ServiceConnection serviceConnection = new ServiceConnection() {
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

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        res.updateConfiguration(configuration, res.getDisplayMetrics());
        return res;
    }
}
