package com.maywide.liveshow.adapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.maywide.liveshow.R;
import com.maywide.liveshow.net.resp.LinkPerResp;

import java.util.List;

/**
 * 粉丝或者房管列表adapter
 * Created by liyizhen on 2020/1/19.
 */

public class LinkPerListAdapter extends BaseQuickAdapter<LinkPerResp.perDetail, BaseViewHolder> {

    //0-粉丝，1-房管
    private int type;

    public LinkPerListAdapter(int layoutResId, @Nullable List<LinkPerResp.perDetail> data,int type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkPerResp.perDetail item) {

        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_id, item.getId());

        RoundedImageView icon = helper.getView(R.id.iv_icon);

        Glide.with(mContext)
                .load(item.getAvatar())
                .error(R.mipmap.news_load_err)
                .into(icon);

        //粉丝
        if (type==0){
            helper.setText(R.id.tv_title,R.string.link_up_to_manager);
        }else {
            //房管
            helper.setText(R.id.tv_title,R.string.link_decend_to_fens);
        }
    }
}
