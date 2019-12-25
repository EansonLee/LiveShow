package com.maywide.liveshow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maywide.liveshow.R;
import com.maywide.liveshow.net.resp.DetailBoxKpiResp;

import java.util.HashMap;
import java.util.List;

public class DetailBoxKpiAdapter extends BaseQuickAdapter<HashMap<String, String>, BaseViewHolder> {

    private List<DetailBoxKpiResp.Title> titleList;
    private Context mContext;

    public DetailBoxKpiAdapter(@Nullable List<HashMap<String, String>> data, List<DetailBoxKpiResp.Title> titleList, Context context) {
        super(R.layout.item_detail_box_kpi, data);
        this.titleList = titleList;
        this.mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, HashMap<String, String> item) {

        final LinearLayout llVavue = helper.getView(R.id.ll_report_vaule);

        //地区默认有值 从1开始
        llVavue.removeAllViews();
        for (int i = 1; i < titleList.size(); i++) {
            String id = titleList.get(i).getId();
            String value = item.get(id);
            llVavue.addView(genValueTextView(value));
        }

        String city = item.get("region");
        helper.setText(R.id.tv_city, city);

        ImageView imageView = helper.getView(R.id.iv_city);

        String url = item.get("imgUrl");

        Glide.with(mContext)
                .load(url)
                .error(R.mipmap.news_load_err)
                .into(imageView);


        int position = helper.getAdapterPosition();
        if (position % 2 == 0) {
            helper.itemView.setBackgroundColor(Color.rgb(255,245,241));
        }else {
            helper.itemView.setBackgroundColor(Color.rgb(255,255,255));

        }

    }


    /**
     * 动态添加标题
     *
     * @return
     */
    private TextView genValueTextView(String titleStr) {

        TextView tv = new TextView(mContext);
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

        tv.setTextColor(mContext.getResources().getColor(R.color.vercode_text_color));
        tv.setGravity(Gravity.CENTER);
        // tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv.setText(titleStr);

        return tv;
    }
}
