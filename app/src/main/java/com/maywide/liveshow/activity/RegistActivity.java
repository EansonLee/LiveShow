package com.maywide.liveshow.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;

import butterknife.BindView;

public class RegistActivity extends BaseAcitivity implements View.OnClickListener {

    //手机号
    @BindView(R.id.et_phone)
    EditText etPhone;
    //验证码
    @BindView(R.id.et_var)
    EditText etVar;
    //密码
    @BindView(R.id.et_pwd)
    EditText etPWd;
    //确认密码
    @BindView(R.id.et_conf_pwd)
    EditText etConfPwd;
    //验证码
    @BindView(R.id.tv_var)
    TextView tvVar;
    //找回密码
    @BindView(R.id.tv_find_back)
    TextView tvFindBack;

    //倒计时
    private TimeCount timeCount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initView() {

        timeCount = new TimeCount(60000, 1000);

        //验证码框监听
        etVar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvFindBack.setBackgroundResource(R.drawable.logout_selector);
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected View getStatusBarView() {
        return null;
    }

    @Override
    protected View getNetErrView() {
        return tvFindBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //验证码
            case R.id.tv_var:
                //隐藏输入键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                break;
            case R.id.tv_find_back:


                break;
        }
    }

    private void queVarReq() {
//        RetrofitClient
//                .getInstance()
//                .api(API.class)
//                .loginReq()
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
            tvVar.setClickable(false);
            tvVar.setText(millisUntilFinished / 1000 + "秒重新发送");
        }

        @Override
        public void onFinish() {
            tvVar.setText(R.string.login_get_vercoed_text);
            tvVar.setClickable(true);
        }
    }
}
