package com.maywide.liveshow.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maywide.liveshow.R;
import com.maywide.liveshow.net.resp.HomeKpiResp;

import java.util.List;

public class ManageKpiAdapter extends BaseMultiItemQuickAdapter<HomeKpiResp, BaseViewHolder> {
    //常用指标
    public static final int SHOW_TYPE_COMMON = 1;
    //其他指标
    public static final int SHOW_TYPE_OHTER = 2;
    //设置是否显示操作图标
    private boolean isShowEvenIcom = false;

    private OnItemEvenListener onItemEvenListener;

    public void setOnItemEvenListener(OnItemEvenListener onItemEvenListener) {
        this.onItemEvenListener = onItemEvenListener;
    }

    public void setShowEvenIcom(boolean showEvenIcom) {
        isShowEvenIcom = showEvenIcom;
        notifyDataSetChanged();
    }

    private int itemshowType;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ManageKpiAdapter(List<HomeKpiResp> data, int showType) {
        super(data);
        this.itemshowType = showType;
        addItemType(2, R.layout.item_manage_kpi_type1);
        addItemType(1, R.layout.item_manage_kpi_type2);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HomeKpiResp item) {
        LinearLayout linearLayout = helper.getView(R.id.ll_item);
        //显示类型 换背景
        if (itemshowType == SHOW_TYPE_COMMON) {
            linearLayout.setBackgroundResource(R.mipmap.bg_orange_kpi);
            helper.setImageResource(R.id.iv_item_event, R.mipmap.icon_reduce);
        } else {
            linearLayout.setBackgroundResource(R.mipmap.bg_blue_kpi);
            helper.setImageResource(R.id.iv_item_event, R.mipmap.icon_add);
        }


        ImageView imageView = helper.getView(R.id.iv_item_event);
        //显示编辑状态
        if (isShowEvenIcom) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemEvenListener != null) {
                    onItemEvenListener.OnitemEdit(item);
                }
            }
        });

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemEvenListener != null) {
                    if (isShowEvenIcom) {
                        onItemEvenListener.OnitemEdit(item);
                    } else {
                        onItemEvenListener.OnItemClick(item);
                    }
                }
            }
        });

        switch (helper.getItemViewType()) {
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

        }
    }


    public interface OnItemEvenListener {
        void OnItemClick(HomeKpiResp item);

        void OnitemEdit(HomeKpiResp item);
    }
}
