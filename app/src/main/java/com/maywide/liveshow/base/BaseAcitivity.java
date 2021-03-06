package com.maywide.liveshow.base;

import android.app.ProgressDialog;

import android.content.IntentFilter;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.maywide.liveshow.R;
import com.maywide.liveshow.utils.NetWorkChangReceiver;
import com.maywide.liveshow.utils.SharedPreferencesUtils;
import com.maywide.liveshow.utils.StatusBarUtils;

import butterknife.ButterKnife;

import static com.maywide.liveshow.base.MyApplication.channelChangReceiver;
import static com.maywide.liveshow.base.MyApplication.jWebSClientService;
import static com.maywide.liveshow.base.MyApplication.serviceConnection;

/**
 * Created by heyongbiao-pc on 2018/11/13.
 */

public abstract class BaseAcitivity extends AppCompatActivity {


    protected SharedPreferencesUtils sharedPreferencesUtils;
    private ProgressDialog progressDialog;
    private NetWorkChangReceiver netWorkChangReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtils.statusTransparent(this);

        setContentView(getLayoutId());
        ButterKnife.bind(this);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(this);

        StatusBarUtils.setStatusDark(this, getStatusBarView());
        initView();
        initData();
        initNetWorkChangReceiver();

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    //设置状态栏高度
    protected abstract View getStatusBarView();

    //设置网络监听显示的view
    protected abstract View getNetErrView();

    /**
     * 初始化网络监听广播
     */
    private void initNetWorkChangReceiver() {
        netWorkChangReceiver = new NetWorkChangReceiver(getNetErrView(), this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        return progressDialog;
    }

    public void showProgressDialog(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = getString(R.string.http_loading);
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        if (progressDialog.isShowing()) {
            progressDialog.setMessage(msg);
        } else {
            progressDialog.setMessage(msg);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        res.updateConfiguration(configuration, res.getDisplayMetrics());
        return res;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkChangReceiver);
    }
}
