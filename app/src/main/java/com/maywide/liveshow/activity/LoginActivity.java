package com.maywide.liveshow.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maywide.liveshow.R;
import com.maywide.liveshow.Service.TcpService;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.net.req.LoginGetVerReq;
import com.maywide.liveshow.net.req.LoginReq;
import com.maywide.liveshow.net.resp.LoginResp;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.StatusBarUtils;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by user on 2018/11/13.
 */

public class LoginActivity extends BaseAcitivity implements View.OnClickListener {

//    @BindView(R.id.view_status_bar)
//    View statusBar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_var)
    EditText etVar;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    //登陆界面布局
    @BindView(R.id.layout_login)
    LinearLayout layoutLogin;

    //倒计时
//    private TimeCount timeCount;
    //请求体
    private LoginReq loginReq;
    private LoginGetVerReq getVerReq;

    private String phoneNum;
    private String password;
    private String verCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

//        StatusBarUtils.setStatusWhite(this, getStatusBarView());

        tvLogin.setOnClickListener(this);

//        timeCount = new TimeCount(60000, 1000);

        addLayoutListener(layoutLogin, tvLogin);

    }

    @Override
    protected void initData() {

        getVerReq = new LoginGetVerReq();
        loginReq = new LoginReq();

    }

    @Override
    protected View getStatusBarView() {
        return null;
    }

    @Override
    protected View getNetErrView() {
        return tvLogin;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_login:
                loginReq();
//                Intent intent = new Intent(this,TcpService.class);
//                bindService(intent,connection,BIND_AUTO_CREATE);

                break;
        }
    }

    /**
     * 登录请求
     */
    private void loginReq() {

        phoneNum = etPhone.getText().toString();
        verCode = etVar.getText().toString();

        if (isCorrectPhoneNum(phoneNum)) {

            loginReq.setMobile(phoneNum);

        } else {
            return;
        }

        if (isHaveVerCode(verCode)) {
            loginReq.setPassword(verCode);
            loginReq.setType("0");
        } else {
            return;
        }


        showProgressDialog("正在登录...");

        RetrofitClient
                .getInstance()
                .api(API.class)
                .loginReq(loginReq)
                .enqueue(new Callback<ResponseObj<LoginResp>>() {
                    @Override
                    public void onResponse(Call<ResponseObj<LoginResp>> call, Response<ResponseObj<LoginResp>> response) {
                        //

                        LoginResp resp = response.body().getData();
                        if ("0".equals(response.body().getCode()) && null != resp) {
                            sharedPreferencesUtils.putString("phone", resp.getMobile());
                            sharedPreferencesUtils.putString("password", "123456");
                            sharedPreferencesUtils.putString("vercode", resp.getVerification());
                            BaseAcitivity.mobile = resp.getMobile();

                            Intent loginIntent = new Intent();
                            loginIntent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(loginIntent);
                        } else {

                            showToast(response.body().getMsg());
                        }
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<ResponseObj<LoginResp>> call, Throwable t) {
                        showToast(getString(R.string.login_err));
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 手机号是否正确
     *
     * @param phoneNum
     */
    private boolean isCorrectPhoneNum(String phoneNum) {
        //判断手机号不为空
        if (!TextUtils.isEmpty(phoneNum)) {
            //判断手机号是否11位
            if (phoneNum.length() != 11) {
                showToast(getString(R.string.phone_err));
                return false;
            }
            return true;
        } else {
            showToast(getString(R.string.phone_empety));
            return false;
        }
    }

    /**
     * 密码判断
     *
     * @param VerCode
     * @return
     */
    private boolean isHaveVerCode(String VerCode) {

        if (!TextUtils.isEmpty(VerCode)) {
//            tvLogin.setBackgroundResource(R.drawable.logout_selector);
            return true;
        } else {
//            tvLogin.setBackgroundResource(R.drawable.login_selector);
            showToast("密码不能为空");
            return false;
        }

    }


    /**
     * 软键盘动态上移
     *
     * @param main
     * @param scroll
     */
    private void addLayoutListener(final View main, final View scroll) {

        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取main 在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rect);
                //获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                //屏幕高度
                int screenHeight = main.getRootView().getHeight();
                //不可见区域大于屏幕本身高度1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 3) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);

                    int scrollHeight = (location[1] + scroll.getHeight()) - rect.bottom;

                    if (scrollHeight != 0) {
                        //让界面整体上移键盘的高度
                        main.scrollTo(0, scrollHeight);
                    } else {
                        return;
                    }

                } else {
                    //不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }
            }
        });
    }


    /**
     * 实现验证码倒计时
     */
    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            tvVar.setClickable(false);
//            tvVar.setText(millisUntilFinished / 1000 + "秒重新发送");
        }

        @Override
        public void onFinish() {
//            tvVar.setText(R.string.login_get_vercoed_text);
//            tvVar.setClickable(true);
        }
    }

    /**
     * 长连接服务
     */
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TcpService.ClientBinder clientBinder = (TcpService.ClientBinder) service;
            clientBinder.startConnect();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
