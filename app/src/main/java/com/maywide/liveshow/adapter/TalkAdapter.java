package com.maywide.liveshow.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maywide.liveshow.R;
import com.maywide.liveshow.net.resp.ChatSocketResp;

import java.util.List;

public class TalkAdapter extends BaseQuickAdapter<ChatSocketResp, BaseViewHolder> {

    public TalkAdapter(@Nullable List<ChatSocketResp> data) {
        super(R.layout.item_talk, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatSocketResp item) {
        if (!TextUtils.isEmpty(item.getName())){
            //昵称
            helper.setText(R.id.tv_name, item.getName()+" :");
        }
        if (!TextUtils.isEmpty(item.getContent())){
            //内容
            helper.setText(R.id.tv_content, item.getContent());
        }
    }


}
