package com.maywide.liveshow.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maywide.liveshow.R;
import com.maywide.liveshow.adapter.DetailBoxKpiAdapter;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.net.req.DetailBoxKpiReq;
import com.maywide.liveshow.net.resp.DetailBoxKpiResp;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * KPI-box详情报表
 */
public class KpiBoxReportActivity extends BaseAcitivity implements View.OnClickListener {
    public static final String TAG_BOXID = "BOXID";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_status_bar)
    LinearLayout llStatusBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_report_area)
    TextView tvReportArea;
    @BindView(R.id.ll_report_title)
    LinearLayout llReportTitle;
    @BindView(R.id.rcv_kpibox_report)
    RecyclerView rcvKpiboxReport;


    private List<DetailBoxKpiResp.Title> titleList = new ArrayList<>();
    private List<HashMap<String, String>> dataList = new ArrayList<>();

    private DetailBoxKpiAdapter detailBoxKpiAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_kpibox_report;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setStatusWhite(this, getStatusBarView());
        ivBack.setOnClickListener(this);

        detailBoxKpiAdapter = new DetailBoxKpiAdapter(dataList, titleList, this);
        rcvKpiboxReport.setLayoutManager(new LinearLayoutManager(this));
        rcvKpiboxReport.setAdapter(detailBoxKpiAdapter);

    }

    @Override
    protected void initData() {

//        DetailBoxKpiResp.Title title = new DetailBoxKpiResp.Title();
//        title.setId("region");
//        title.setTitleName("区域");
//        titleList.add(title);
//
//        DetailBoxKpiResp.Title title2 = new DetailBoxKpiResp.Title();
//        title2.setId("rank");
//        title2.setTitleName("排名");
//        titleList.add(title2);
//
//        DetailBoxKpiResp.Title title3 = new DetailBoxKpiResp.Title();
//        title3.setId("kpi1");
//        title3.setTitleName("完成率");
//        titleList.add(title3);
//
//        DetailBoxKpiResp.Title title4 = new DetailBoxKpiResp.Title();
//        title4.setId("kpi2");
//        title4.setTitleName("用户数");
//        titleList.add(title4);
//
//
//        for (int i = 0; i < 20; i++) {
//            HashMap<String, String> map = new HashMap<>();
//            map.put("imgUrl", "http://210.21.65.90:9093/static/regionimg/maoming.png");
//            map.put("region", "城市" + i);
//            map.put("rank", i + "");
//            map.put("kpi1", "100%");
//            map.put("kpi2", "123456");
//            dataList.add(map);
//        }
//
//        detailBoxKpiAdapter.notifyDataSetChanged();
//
//        setTitleView();


        String boxId = getIntent().getStringExtra(TAG_BOXID);
        reqKpi(boxId);

    }

    private void reqKpi(String boxId) {
        DetailBoxKpiReq req = new DetailBoxKpiReq();
        req.setMobile(BaseAcitivity.mobile);
        req.setBoxId(boxId);
        req.setCity("GD");

        RetrofitClient.getInstance().api(API.class).detailBoxKpi(req).enqueue(new Callback<ResponseObj<DetailBoxKpiResp>>() {
            @Override
            public void onResponse(Call<ResponseObj<DetailBoxKpiResp>> call, Response<ResponseObj<DetailBoxKpiResp>> response) {
                if (response.body() == null) {
                    return;
                }
                if ("0".equals(response.body().getCode())) {
                    titleList.clear();
                    dataList.clear();

                    titleList.addAll(response.body().getData().getTitleList());
                    dataList.addAll(response.body().getData().getDataList());

                    detailBoxKpiAdapter.notifyDataSetChanged();
                    setTitleView();


                } else {
                    showToast(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResponseObj<DetailBoxKpiResp>> call, Throwable t) {
                showToast(getString(R.string.net_err));
            }
        });
    }

    @Override
    protected View getStatusBarView() {
        return llStatusBar;
    }

    @Override
    protected View getNetErrView() {
        return llStatusBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void setTitleView() {

        if (titleList == null) {
            return;
        }
        if (titleList.size() > 1) {
            tvReportArea.setText(titleList.get(0).getTitleName());
        }

        for (int i = 1; i < titleList.size(); i++) {
            llReportTitle.addView(genValueTextView(titleList.get(i).getTitleName()));
        }
    }

    /**
     * 动态添加标题
     *
     * @return
     */
    private TextView genValueTextView(String titleStr) {

        TextView tv = new TextView(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        if (titleList != null && titleList.size() > 1) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            tv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
        }

        tv.setTextColor(getResources().getColor(R.color.vercode_text_color));
        tv.setGravity(Gravity.CENTER);
        // tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv.setText(titleStr);

        return tv;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }

    }
}
