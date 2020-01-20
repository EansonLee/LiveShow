package com.maywide.liveshow.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.maywide.liveshow.R;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewHolder;

/**
 * 公告对话框
 * Created by liyizhen on 2020/1/7.
 */

public class BroadCastDialog extends BaseNiceDialog {

    private String title;
    private String content;

    private onLayOutClickListener onLayOutClickListener;

    public void setOnLayOutClickListener(BroadCastDialog.onLayOutClickListener onLayOutClickListener) {
        this.onLayOutClickListener = onLayOutClickListener;
    }

    public static BroadCastDialog getInstance(String title, String content) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        BroadCastDialog broadCastDialog = new BroadCastDialog();
        broadCastDialog.setArguments(bundle);
        return broadCastDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        title = bundle.getString("title");
        content = bundle.getString("content");
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_broadcast;
    }

    @Override
    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
        holder.setText(R.id.tv_title, title);
        holder.setText(R.id.tv_content, content);

        holder.setOnClickListener(R.id.ly_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onLayOutClickListener) {
                    onLayOutClickListener.onLayOutClick();
                }
                dialog.dismiss();
            }
        });
    }


    public interface onLayOutClickListener {
        void onLayOutClick();
    }
}
