package com.maywide.liveshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maywide.liveshow.R;
import com.maywide.liveshow.adapter.ManageKpiAdapter;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.net.req.EditIndexReq;
import com.maywide.liveshow.net.req.HomeKpiReq;
import com.maywide.liveshow.net.resp.HomeKpiResp;
import com.maywide.liveshow.net.resp.ResponseList;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageKpiActivity extends BaseAcitivity implements View.OnClickListener {
    @BindView(R.id.ll_status_bar)
    LinearLayout llStatusBar;
    @BindView(R.id.rcy_common_kpi)
    RecyclerView rcyCommonKpi;
    @BindView(R.id.rcy_other_kpi)
    RecyclerView rcyOtherKpi;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_edit)
    TextView tvEdit;

    private ManageKpiAdapter commonKpiAdapter;
    private ManageKpiAdapter otherKpiAdapter;
    private List<HomeKpiResp> comonkpidataList = new ArrayList<>();
    private List<HomeKpiResp> otherKpidataList = new ArrayList<>();

    @Override
    protected View getNetErrView() {
        return llStatusBar;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_kpi;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setStatusWhite(this, getStatusBarView());

        commonKpiAdapter = new ManageKpiAdapter(comonkpidataList, ManageKpiAdapter.SHOW_TYPE_COMMON);
        commonKpiAdapter.setOnItemEvenListener(new ManageKpiAdapter.OnItemEvenListener() {
            @Override
            public void OnItemClick(HomeKpiResp item) {
                Intent intent = new Intent();
                intent.setClass(ManageKpiActivity.this, KpiBoxReportActivity.class);
                intent.putExtra(KpiBoxReportActivity.TAG_BOXID,item.getBoxId());
                startActivity(intent);

            }

            @Override
            public void OnitemEdit(HomeKpiResp item) {
                editEven(false,item);

            }
        });


        rcyCommonKpi.setLayoutManager(new GridLayoutManager(this, 3));
        rcyCommonKpi.setAdapter(commonKpiAdapter);
        commonKpiAdapter.bindToRecyclerView(rcyCommonKpi);
        commonKpiAdapter.setEmptyView(R.layout.view_commonkpi_empty);
        commonKpiAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEdit(true);
            }
        });

        otherKpiAdapter = new ManageKpiAdapter(otherKpidataList, ManageKpiAdapter.SHOW_TYPE_OHTER);
        otherKpiAdapter.setOnItemEvenListener(new ManageKpiAdapter.OnItemEvenListener() {
            @Override
            public void OnItemClick(HomeKpiResp item) {
                Intent intent = new Intent();
                intent.setClass(ManageKpiActivity.this, KpiBoxReportActivity.class);
                intent.putExtra(KpiBoxReportActivity.TAG_BOXID,item.getBoxId());
                startActivity(intent);

            }

            @Override
            public void OnitemEdit(HomeKpiResp item) {
                //editKpiReq("1", item);
                editEven(true,item);

            }
        });

        rcyOtherKpi.setLayoutManager(new GridLayoutManager(this, 3));
        rcyOtherKpi.setAdapter(otherKpiAdapter);
        otherKpiAdapter.bindToRecyclerView(rcyOtherKpi);
        otherKpiAdapter.setEmptyView(R.layout.view_otherkpi_empty);

        ivBack.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        tvFinish.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        getKpiReq("1");
        getKpiReq("2");

    }

    @Override
    protected View getStatusBarView() {
        return llStatusBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    /**
     * 1 常用 2 其他
     *
     * @param type
     */
    private void getKpiReq(final String type) {
        HomeKpiReq req = new HomeKpiReq();
        req.setIndexType(type);
//        req.setPhone(BaseAcitivity.mobile);


        RetrofitClient.getInstance().api(API.class).getHomeKpi(req).enqueue(new Callback<ResponseList<HomeKpiResp>>() {
            @Override
            public void onResponse(Call<ResponseList<HomeKpiResp>> call, Response<ResponseList<HomeKpiResp>> response) {
                if (response.body() == null) {
                    return;
                }

                if ("0".equals(response.body().getCode())) {
                    if ("1".equals(type)) {
                        comonkpidataList.addAll(response.body().getData());
                        commonKpiAdapter.notifyDataSetChanged();
                    } else if ("2".equals(type)) {
                        otherKpidataList.addAll(response.body().getData());
                        otherKpiAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToast(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResponseList<HomeKpiResp>> call, Throwable t) {
                showToast(getString(R.string.net_err));

            }
        });
    }



    private void editKpiReq() {
        String boxIdList = "";

        for (int i = 0; i < comonkpidataList.size(); i++) {
            if(TextUtils.isEmpty(boxIdList)){
                boxIdList = comonkpidataList.get(i).getBoxId();
            }else {
                boxIdList = boxIdList+","+comonkpidataList.get(i).getBoxId();
            }

        }
        EditIndexReq req = new EditIndexReq();
//        req.setPhone(BaseAcitivity.mobile);
        req.setData(boxIdList);


        showProgressDialog("正在修改....");


        RetrofitClient.getInstance().api(API.class).editIndex(req).enqueue(new Callback<ResponseObj>() {
            @Override
            public void onResponse(Call<ResponseObj> call, Response<ResponseObj> response) {
                dismissProgressDialog();
                if (response.body() != null) {
                    if ("0".equals(response.body().getCode())) {
                        //editEven(isAdd, item);
                        finish();
                    } else {
                        showToast(response.body().getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseObj> call, Throwable t) {
                showToast("操作失败");
                dismissProgressDialog();

            }
        });

    }

    /**
     * 编辑事件
     *
     * @param isAdd
     * @param item
     */
    private void editEven(boolean isAdd, HomeKpiResp item) {
        if (isAdd) {
            comonkpidataList.add(item);
            otherKpidataList.remove(item);
        } else {
            otherKpidataList.add(item);
            comonkpidataList.remove(item);
        }
        commonKpiAdapter.notifyDataSetChanged();
        otherKpiAdapter.notifyDataSetChanged();
    }

    /**
     * 显示编辑
     *
     * @param isEdit
     */
    private void showEdit(boolean isEdit) {
        if (isEdit) {
            ivBack.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);
            tvCancel.setVisibility(View.VISIBLE);
            tvFinish.setVisibility(View.VISIBLE);
            otherKpiAdapter.setShowEvenIcom(true);
            commonKpiAdapter.setShowEvenIcom(true);

        } else {
            ivBack.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.GONE);
            tvFinish.setVisibility(View.GONE);
            otherKpiAdapter.setShowEvenIcom(false);
            commonKpiAdapter.setShowEvenIcom(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                showEdit(false);
                finish();
                break;
            case R.id.tv_edit:
                showEdit(true);
                break;
            case R.id.tv_finish:
                showEdit(false);
                editKpiReq();
                //finish();
                break;
        }
    }
}
