package com.maywide.liveshow.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.maywide.liveshow.R;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.ViewHolder;


/**
 * 确定dialog
 */
public class ConfirmDialog extends BaseNiceDialog {

    private String title;
    private String msg;
    private OnSureClickListener onSureClickListener;
    private OnCancleClickListener onCancleClickListener;
    private String cancelText;
    private String okText;

    public void setOnCancleClickListener(OnCancleClickListener onCancleClickListener) {
        this.onCancleClickListener = onCancleClickListener;
    }

    public void setOnSureClickListener(OnSureClickListener onSureClickListener) {
        this.onSureClickListener = onSureClickListener;
    }

    public static ConfirmDialog newInstance(String title, String msg) {
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        bundle.putString("title", title);
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public static ConfirmDialog newInstance(String title, String msg, String cancelText, String okText) {
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        bundle.putString("title", title);
        bundle.putString("cancel", cancelText);
        bundle.putString("ok", okText);
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        msg = bundle.getString("msg");
        title = bundle.getString("title");
        cancelText = bundle.getString("cancel", "");
        okText = bundle.getString("ok", "");
    }

    @Override
    public int intLayoutId() {
        return R.layout.confirm_layout;
    }

    @Override
    public void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
        viewHolder.setText(R.id.title, title);
        viewHolder.setText(R.id.message, msg);
        if (!TextUtils.isEmpty(cancelText)) {
            viewHolder.setText(R.id.cancel, cancelText);
        }
        if (!TextUtils.isEmpty(okText)) {
            viewHolder.setText(R.id.ok, okText);
        }

        viewHolder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancleClickListener != null) {
                    onCancleClickListener.onCancel();
                }
                baseNiceDialog.dismiss();
            }
        });

        viewHolder.setOnClickListener(R.id.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSureClickListener != null) {
                    onSureClickListener.onSureClik();
                }
                baseNiceDialog.dismiss();
            }
        });
    }

    public interface OnSureClickListener {
        void onSureClik();
    }

    public interface OnCancleClickListener {
        void onCancel();
    }
}
