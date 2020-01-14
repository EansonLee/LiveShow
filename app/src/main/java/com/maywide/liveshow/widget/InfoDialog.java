package com.maywide.liveshow.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.maywide.liveshow.R;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewHolder;

/**
 * Created by liyizhen on 2020/1/8.
 */

public class InfoDialog extends BaseNiceDialog{
    //图片地址
    private String imgUrl;

    @Override
    public int intLayoutId() {
        return R.layout.dialog_info;
    }

    public static InfoDialog newInstance( ){

        InfoDialog infoDialog = new InfoDialog();
        return infoDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {

    }
}
