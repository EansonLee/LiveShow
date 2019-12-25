package com.maywide.liveshow.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.maywide.liveshow.R;

public class NetErrPopwindow extends PopupWindow {
    private View mView;

    public NetErrPopwindow(final Activity context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_net_err, null);
        this.setContentView(mView);

        //this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        //this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(1000);
        this.setHeight(110);
        //this.setBackgroundDrawable(R.mipmap.bg_net_err);


    }
}
