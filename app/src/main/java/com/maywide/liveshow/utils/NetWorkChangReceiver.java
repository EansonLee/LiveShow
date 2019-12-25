package com.maywide.liveshow.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.maywide.liveshow.widget.NetErrPopwindow;

import android.os.Handler;

public class NetWorkChangReceiver extends BroadcastReceiver {
    private View view;
    public NetErrPopwindow netErrPopwindow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (netErrPopwindow != null && netErrPopwindow.isShowing()) {
                    netErrPopwindow.dismiss();
                }

            } else if (msg.what == 2) {
                if (netErrPopwindow != null && !netErrPopwindow.isShowing()) {
                    netErrPopwindow.showAtLocation(view, Gravity.TOP, 0, 350);
                }

            }
        }
    };

    public NetWorkChangReceiver(View view, Activity context) {
        this.view = view;
        netErrPopwindow = new NetErrPopwindow(context);
    }

    /**
     * 获取连接类型
     *
     * @param type
     * @return
     */
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.e("TAG", "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }

        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Log.i("TAG", getConnectionType(info.getType()) + "连上");
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    Log.i("TAG", getConnectionType(info.getType()) + "断开");
                    Log.i("TAG", getConnectionType(info.getType()) + "断开" + context.getPackageName());
                    handler.sendEmptyMessage(2);
                }
            }
        }
    }
}
