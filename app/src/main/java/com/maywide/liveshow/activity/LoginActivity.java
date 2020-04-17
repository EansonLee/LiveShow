package com.maywide.liveshow.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.base.MyApplication;
import com.maywide.liveshow.bean.MyPermissionBean;
import com.maywide.liveshow.net.req.LoginReq;
import com.maywide.liveshow.net.req.LoginSocketReq;
import com.maywide.liveshow.net.req.SocketBaseReq;
import com.maywide.liveshow.net.resp.LoginResp;

import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.MyPermissionManager;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maywide.liveshow.base.MyApplication.jWebSClientService;

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

    private String phoneNum;
    private String password;
    private String verCode;

    //权限
    private MyPermissionManager mMyPermissionManager;
    ArrayList<MyPermissionBean> mPermissionList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

//        StatusBarUtils.setStatusWhite(this, getStatusBarView());

        tvLogin.setOnClickListener(this);
        //设置输入模式
        etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        etVar.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD|InputType.TYPE_CLASS_NUMBER);

//        timeCount = new TimeCount(60000, 1000);

        addLayoutListener(layoutLogin, tvLogin);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {

        loginReq = new LoginReq();

        //权限设置
        mPermissionList.add(new MyPermissionBean(Manifest.permission.WRITE_EXTERNAL_STORAGE, getResources().getString(R.string.permission_write_external_storage)));
        mPermissionList.add(new MyPermissionBean(Manifest.permission.RECORD_AUDIO, getResources().getString(R.string.permission_record_audio)));
        mPermissionList.add(new MyPermissionBean(Manifest.permission.CAMERA, getResources().getString(R.string.permission_camera)));
        mPermissionList.add(new MyPermissionBean(Manifest.permission.READ_PHONE_STATE, getResources().getString(R.string.permission_read_phone_state)));

        if (checkPermission()) {
            phoneNum = sharedPreferencesUtils.getString("phone", "");
            verCode = sharedPreferencesUtils.getString("password", "");
        }
        //单点登录
        if (!TextUtils.isEmpty(phoneNum) && (!TextUtils.isEmpty(verCode))) {
            etPhone.setText(phoneNum);
            etVar.setText(verCode);
//            loginReq();
        }
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

                //隐藏输入键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                loginReq();
//                Intent intent = new Intent(this, TcpService.class);
//                bindService(intent, connection, BIND_AUTO_CREATE);
                break;
        }
    }


    /**
     * 检查所需权限
     */
    private boolean checkPermission() {
        mMyPermissionManager = new MyPermissionManager(this, new MyPermissionManager.PermissionUtilsInter() {
            @Override
            public List<MyPermissionBean> getApplyPermissions() {
                return mPermissionList;
            }

            @Override
            public AlertDialog.Builder getTipAlertDialog() {
                return null;
            }

            @Override
            public Dialog getTipDialog() {
                return null;
            }

            @Override
            public AlertDialog.Builder getTipAppSettingAlertDialog() {
                return null;
            }

            @Override
            public Dialog getTipAppSettingDialog() {
                return null;
            }
        });
        boolean isOk = mMyPermissionManager.checkPermission();
        return isOk;
    }

    /**
     * 登录请求
     */
    private void loginReq() {
        //本地单点登录
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(verCode)) {
            //手机号
            phoneNum = etPhone.getText().toString();
            //密码
            verCode = etVar.getText().toString();
        }
        if (isCorrectPhoneNum(phoneNum)) {
            loginReq.setPhone(phoneNum);
        } else {
            return;
        }

        if (isHaveVerCode(verCode)) {
            loginReq.setPassword(verCode);
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
                        if (response.body() == null) {
                            showToast(getString(R.string.net_err));
                            return;
                        }
                        LoginResp resp = response.body().getData();
                        if ("0".equals(response.body().getCode()) && null != resp) {
                            sharedPreferencesUtils.putString("phone", loginReq.getPhone());
                            sharedPreferencesUtils.putString("password", loginReq.getPassword());
                            sharedPreferencesUtils.putString("token", resp.getToken());
                            //主播基本信息
                            LoginResp.baseDetail baseDetail = resp.getAnchor();
                            //webScocket 发送消息
                            if (MyApplication.client != null && MyApplication.client.isOpen()) {

                                LoginSocketReq loginSocketReq = new LoginSocketReq();
                                loginSocketReq.setAnchor_code(baseDetail.getAnchor_code());
                                loginSocketReq.setAvatar(baseDetail.getHeadimgurl());
                                loginSocketReq.setName(baseDetail.getNickname());
                                loginSocketReq.setUser_id(baseDetail.getUser_id());
                                //主播才传，否则不传
                                loginSocketReq.setIs_anchor(1);

                                SocketBaseReq<LoginSocketReq> socketBaseReq = new SocketBaseReq<>();
                                socketBaseReq.setData(loginSocketReq);
                                socketBaseReq.setType("login");
                                String msg = new Gson().toJson(socketBaseReq);
                                jWebSClientService.sendMsg(msg);
                            }

                            Intent loginIntent = new Intent();
                            loginIntent.putExtra("infoData", baseDetail);
                            loginIntent.setClass(LoginActivity.this, StartLiveActivity.class);
                            startActivity(loginIntent);
                            finish();
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
     * ping-webSocket(要求)
     *
     * @param data
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String data) {
        String type = null;
        String content = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            type = jsonObject.getString("type");
            content = jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (type.equals("ping")) {
            SocketBaseReq<LoginSocketReq> socketBaseReq = new SocketBaseReq<>();
            socketBaseReq.setType("pong");
            String sendData = new Gson().toJson(socketBaseReq);
            jWebSClientService.sendMsg(sendData);
        }

    }
}
