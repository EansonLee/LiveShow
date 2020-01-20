package com.maywide.liveshow.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.maywide.liveshow.R;
import com.maywide.liveshow.net.resp.LoginResp;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewHolder;

/**
 * Created by liyizhen on 2020/1/8.
 */

public class InfoDialog extends BaseNiceDialog{
    //图片地址
    private String imgUrl;
    //个人信息
    private LoginResp.baseDetail baseDetail;
    @Override
    public int intLayoutId() {
        return R.layout.dialog_info;
    }

    public static InfoDialog newInstance(LoginResp.baseDetail baseDetail){
        Bundle bundle = new Bundle();
        bundle.putSerializable("infoData",baseDetail);
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setArguments(bundle);
        return infoDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        baseDetail = (LoginResp.baseDetail) bundle.getSerializable("infoData");
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        //名称
        holder.setText(R.id.tv_name,baseDetail.getNickname());
        //id
        holder.setText(R.id.tv_id,baseDetail.getId());
        //关注
        holder.setText(R.id.tv_focus,baseDetail.getFollow_count());
        //粉丝数
        holder.setText(R.id.tv_fans,baseDetail.getFans_count());
        //性别图片
        ImageView ivSex = holder.getView(R.id.iv_sex);
        //1-男 2-女
        if (baseDetail.getSex() ==1){
            ivSex.setImageResource(R.mipmap.man_icon);
        }else {
            ivSex.setImageResource(R.mipmap.woman_icon);
        }
    }
}
