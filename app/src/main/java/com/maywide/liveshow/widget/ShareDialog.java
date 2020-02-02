package com.maywide.liveshow.widget;

import android.view.View;

import com.maywide.liveshow.R;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewHolder;

/**
 * Created by liyizhen on 2020/1/14.
 */

public class ShareDialog extends BaseNiceDialog {

    private onFensClickListener onFensClickListener;
    private onManageClickListener onManageClickListener;
    private onShareClickListener onShareClickListener;
    private onLogoutClickListener onLogoutClickListener;
    private onLayOutClickListener onLayOutClickListener;

    public void setOnFensClickListener(ShareDialog.onFensClickListener onFensClickListener) {
        this.onFensClickListener = onFensClickListener;
    }

    public void setOnManageClickListener(ShareDialog.onManageClickListener onManageClickListener) {
        this.onManageClickListener = onManageClickListener;
    }

    public void setOnShareClickListener(ShareDialog.onShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public void setOnLogoutClickListener(ShareDialog.onLogoutClickListener onLogoutClickListener) {
        this.onLogoutClickListener = onLogoutClickListener;
    }

    public void setOnLayOutClickListener(ShareDialog.onLayOutClickListener onLayOutClickListener) {
        this.onLayOutClickListener = onLayOutClickListener;
    }

    public static ShareDialog newInstance() {
        ShareDialog shareDialog = new ShareDialog();
        return shareDialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_share;
    }

    @Override
    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
        //粉丝列表
        holder.setOnClickListener(R.id.ly_fens, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onFensClickListener) {
                    onFensClickListener.onFensClick();
                }
                dialog.dismiss();
            }
        });
        //管理列表
        holder.setOnClickListener(R.id.ly_manage, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onManageClickListener) {
                    onManageClickListener.onManageClick();
                }
                dialog.dismiss();
            }
        });
        //分享链接
        holder.setOnClickListener(R.id.ly_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onShareClickListener) {
                    onShareClickListener.onShareClick();
                }
                dialog.dismiss();
            }
        });
        //退出登录
        holder.setOnClickListener(R.id.ly_logout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onLogoutClickListener) {
                    onLogoutClickListener.onLogoutClick();
                }
                dialog.dismiss();
            }
        });
        //整个界面点击
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

    public interface onFensClickListener {
        void onFensClick();
    }

    public interface onManageClickListener {
        void onManageClick();
    }

    public interface onShareClickListener {
        void onShareClick();
    }

    public interface onLogoutClickListener {
        void onLogoutClick();
    }
}
