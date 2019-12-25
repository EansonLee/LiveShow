package com.maywide.liveshow.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maywide.liveshow.R;
import com.maywide.liveshow.net.resp.HomeNewsResp;

import java.util.List;


/**
 * Created by liyizhen-pc on 2018/11/19.
 */

public class HomeNewsAdapter extends BaseQuickAdapter<HomeNewsResp.NewsDetail, BaseViewHolder> {


    public HomeNewsAdapter(int layoutResId, @Nullable List<HomeNewsResp.NewsDetail> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeNewsResp.NewsDetail item) {
        helper.setText(R.id.tv_news_title, item.getTitle());
//        helper.setText(R.id.tv_news_author, item.getAuthor());
        helper.setText(R.id.tv_news_date, item.getPublishDate());

        //todo 从服务器获取新闻列表图片地址
        Glide.with(mContext)
                .load(item.getImgUrl())
                .error(R.mipmap.news_load_err)
                .into((ImageView) helper.getView(R.id.iv_news));

    }
}
