package com.maywide.liveshow.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * socket长链接测试
 * Created by liyizhen on 2019/12/27.
 */

public class ChannelChangReceiver extends BroadcastReceiver {

    public ChannelChangReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("cmd");
        Log.e("---",data);
    }
}
