package com.maywide.liveshow.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maywide.liveshow.R;
import com.maywide.liveshow.net.resp.HomeKpiResp;

import java.util.List;

/**
 * Created by heyongbiao-pc on 2018/11/14.
 * 主页常用指标
 */

public class HomeCommonKpiAdapter extends BaseMultiItemQuickAdapter<HomeKpiResp, BaseViewHolder> {


    public HomeCommonKpiAdapter(@Nullable List<HomeKpiResp> data) {
        super(data);
        addItemType(1, R.layout.item_home_kpi_type2);
        addItemType(2, R.layout.item_home_kpi_type1);


    }

    @Override
    protected void convert(BaseViewHolder helper, HomeKpiResp item) {
        switch (helper.getItemViewType()) {
            case 2:
                List<HomeKpiResp.DataKpi> dataKpis = item.getDataList();
                if (dataKpis == null) {
                    return;
                }
                if (dataKpis.size() == 2) {
                    helper.setText(R.id.tv_title, dataKpis.get(0).getTitle());
                    TextView textViewTag = helper.getView(R.id.tv_num_tag);
                    String num = dataKpis.get(0).getNum();
                    if (num.endsWith("万")) {
                        num = num.replace("万", "");
                        textViewTag.setVisibility(View.VISIBLE);
                    } else {
                        textViewTag.setVisibility(View.GONE);

                    }
                    helper.setText(R.id.tv_num, num);

                    helper.setText(R.id.tv_title_rate, dataKpis.get(1).getTitle());
                    helper.setText(R.id.tv_rate, dataKpis.get(1).getNum());
                }
                break;
            case 1:
                List<HomeKpiResp.DataKpi> dataList = item.getDataList();
                if (dataList == null) {
                    return;
                }
                if (dataList.size() == 1) {
                    String title = dataList.get(0).getTitle();
                    int position = title.indexOf("(");

                    if (position == -1) {
                        return;
                    }
                    String realTitle = title.substring(0, position);
                    String containStr = title.substring(position);

                    TextView textViewTag = helper.getView(R.id.tv_num_tag);
                    String num = dataList.get(0).getNum();
                    if (num.endsWith("万")) {
                        num = num.replace("万", "");
                        textViewTag.setVisibility(View.VISIBLE);
                    } else {
                        textViewTag.setVisibility(View.GONE);

                    }
                    helper.setText(R.id.tv_num, num);
                    helper.setText(R.id.tv_title_key, realTitle + "\n" + containStr);
                    helper.setText(R.id.tv_title, realTitle);
                }
                break;
        }

    }


}
