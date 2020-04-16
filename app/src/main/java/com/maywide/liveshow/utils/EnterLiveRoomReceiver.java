package com.maywide.liveshow.utils;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.maywide.liveshow.R;
import com.maywide.liveshow.activity.LiveActivity;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.net.resp.BroadCastInfoResp;
import com.maywide.liveshow.net.resp.LoginResp;

import java.io.Serializable;

/**
 * 接收是否可以进入直播间广播
 */
public class EnterLiveRoomReceiver extends BroadcastReceiver {

    private ProgressDialog progressDialog;
    private boolean isLoging;
    private BaseAcitivity acitivity;
    private LoginResp.baseDetail broadCastInfoResp;

    public EnterLiveRoomReceiver(ProgressDialog progressDialog, boolean isLoging, BaseAcitivity acitivity, LoginResp.baseDetail broadCastInfoResp) {
        this.progressDialog = progressDialog;
        this.isLoging = isLoging;
        this.acitivity = acitivity;
        this.broadCastInfoResp = broadCastInfoResp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        if (MyTTTRtcEngineEventHandler.TAG.equals(action)) {
//            JniObjs mJniObjs = intent.getParcelableExtra(MyTTTRtcEngineEventHandler.MSG_TAG);
//            switch (mJniObjs.mJniType) {
//                case LocalConstans.CALL_BACK_ON_ENTER_ROOM:
//                    //界面跳转
//                    Intent activityIntent = new Intent();
//                    activityIntent.setClass(context, LiveActivity.class);
//                    activityIntent.putExtra("infoData",broadCastInfoResp);
//                    context.startActivity(activityIntent);
//                    acitivity.finish();
//                    isLoging = false;
//                    break;
//                case LocalConstans.CALL_BACK_ON_ERROR:
//                    progressDialog.dismiss();
//                    isLoging = false;
//                    int errorType = mJniObjs.mErrorType;
//                    if (errorType == Constants.ERROR_ENTER_ROOM_INVALIDCHANNELNAME) {
//                        Toast.makeText(context, context.getResources().getString(R.string.ttt_error_enterchannel_format), Toast.LENGTH_SHORT).show();
//                    } else if (errorType == Constants.ERROR_ENTER_ROOM_TIMEOUT) {
//                        Toast.makeText(context, context.getResources().getString(R.string.ttt_error_enterchannel_timeout), Toast.LENGTH_SHORT).show();
//                    } else if (errorType == Constants.ERROR_ENTER_ROOM_VERIFY_FAILED) {
//                        Toast.makeText(context, context.getResources().getString(R.string.ttt_error_enterchannel_token_invaild), Toast.LENGTH_SHORT).show();
//                    } else if (errorType == Constants.ERROR_ENTER_ROOM_BAD_VERSION) {
//                        Toast.makeText(context, context.getResources().getString(R.string.ttt_error_enterchannel_version), Toast.LENGTH_SHORT).show();
//                    } else if (errorType == Constants.ERROR_ENTER_ROOM_CONNECT_FAILED) {
//                        Toast.makeText(context, context.getResources().getString(R.string.ttt_error_enterchannel_unconnect), Toast.LENGTH_SHORT).show();
//                    } else if (errorType == Constants.ERROR_ENTER_ROOM_NOEXIST) {
//                        Toast.makeText(context, context.getResources().getString(R.string.ttt_error_enterchannel_room_no_exist), Toast.LENGTH_SHORT).show();
//                    } else if (errorType == Constants.ERROR_ENTER_ROOM_SERVER_VERIFY_FAILED) {
//                        Toast.makeText(context, context.getResources().getString(R.string.ttt_error_enterchannel_verification_failed), Toast.LENGTH_SHORT).show();
//                    } else if (errorType == Constants.ERROR_ENTER_ROOM_UNKNOW) {
//                        Toast.makeText(context, context.getResources().getString(R.string.ttt_error_enterchannel_unknow), Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//            }
//        }
    }
}
