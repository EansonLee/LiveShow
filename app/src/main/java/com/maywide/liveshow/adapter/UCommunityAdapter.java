package com.maywide.liveshow.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maywide.liveshow.R;
import com.maywide.liveshow.net.resp.HomeKpiResp;

import java.util.List;

/**
 * Created by heyongbiao-pc on 2018/11/28.
 */

public class UCommunityAdapter extends BaseMultiItemQuickAdapter<HomeKpiResp, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public UCommunityAdapter(List<HomeKpiResp> data) {
        super(data);
        addItemType(1, R.layout.item__uccommunity);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeKpiResp item) {
        switch (helper.getItemViewType()){
            case 1:

                break;
            case 2:

                break;
        }
    }
}
