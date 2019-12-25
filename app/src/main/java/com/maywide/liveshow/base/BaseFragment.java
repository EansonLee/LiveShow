package com.maywide.liveshow.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maywide.liveshow.R;
import com.maywide.liveshow.utils.SharedPreferencesUtils;
import com.maywide.liveshow.utils.StatusBarUtils;

import butterknife.ButterKnife;

/**
 * Created by heyongbiao-pc on 2018/11/13.
 */

public abstract class BaseFragment extends Fragment {

    public Context mContext;
    private View mView;
    protected SharedPreferencesUtils sharedPreferencesUtils;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null && getActivity() != null) {
            mView = inflater.inflate(getLayoutId(), container, false);
        } else if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        ButterKnife.bind(this, mView);
        StatusBarUtils.setStatusDark(getActivity(), getStatusBarView());
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(getActivity());

        initView();
        initData();
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    /**
     * 初始化布局
     *
     * @author ljt
     */
    protected abstract int getLayoutId();

    protected abstract void initView();

    //设置状态栏高度
    protected abstract View getStatusBarView();

    /**
     * 参数设置
     *
     * @author ljt
     */
    protected abstract void initData();

    protected void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = getString(R.string.http_loading);
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
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

}
