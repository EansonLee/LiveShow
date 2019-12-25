package com.maywide.liveshow.base;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.ScreenAdapter;

/**
 * Created by heyongbiao-pc on 2018/11/13.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

		RetrofitClient.init(this);


        //屏幕适配
        ScreenAdapter.setup(this);
        ScreenAdapter.register(this, 375, ScreenAdapter.MATCH_BASE_WIDTH, ScreenAdapter.MATCH_UNIT_DP);

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
