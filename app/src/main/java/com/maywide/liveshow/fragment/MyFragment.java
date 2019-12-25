package com.maywide.liveshow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maywide.liveshow.R;
import com.maywide.liveshow.activity.LoginActivity;
import com.maywide.liveshow.base.BaseFragment;
import com.maywide.liveshow.utils.DataCleanManager;
import com.maywide.liveshow.widget.ConfirmDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 我的
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.ll_status_bar)
    LinearLayout llStatusBar;
    @BindView(R.id.ly_catche)
    LinearLayout lyCatche;
    @BindView(R.id.ly_update)
    LinearLayout lyUpdate;
    Unbinder unbinder;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.my_title);

        try {
            tvCache.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvPhone.setText(sharedPreferencesUtils.getString("phone", ""));

        lyCatche.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    protected View getStatusBarView() {
        return llStatusBar;
    }

    @Override
    protected void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_catche:
                catcheDialog();
                break;

            case R.id.ly_update:

                break;
            case R.id.tv_logout:
                logoutDialog();
                break;
        }
    }


    private void logoutDialog() {

        ConfirmDialog confirmDialog = ConfirmDialog.newInstance(getString(R.string.my_logout), getString(R.string.my_checklogout));
        confirmDialog.setOutCancel(false);
        confirmDialog.setMargin(60);
        confirmDialog.setOnSureClickListener(new ConfirmDialog.OnSureClickListener() {
            @Override
            public void onSureClik() {
                sharedPreferencesUtils.put("phone", "");
                sharedPreferencesUtils.put("password", "");
                sharedPreferencesUtils.put("vercode", "");

                Intent intent = new Intent();
                intent.setClass(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
        confirmDialog.show(getFragmentManager());
    }

    private void catcheDialog() {

        ConfirmDialog confirmDialog = ConfirmDialog.newInstance(getString(R.string.my_cacheclear), getString(R.string.my_checkcatche));
        confirmDialog.setOutCancel(false);
        confirmDialog.setMargin(60);
        confirmDialog.setOnSureClickListener(new ConfirmDialog.OnSureClickListener() {
            @Override
            public void onSureClik() {
                DataCleanManager.clearAllCache(mContext);
                tvCache.setText("0KB");
            }
        });
        confirmDialog.show(getFragmentManager());
    }
}
