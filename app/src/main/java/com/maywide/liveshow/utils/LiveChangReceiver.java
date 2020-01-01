package com.maywide.liveshow.utils;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.maywide.liveshow.Handler.MyTTTRtcEngineEventHandler;
import com.maywide.liveshow.LocalConstans;
import com.maywide.liveshow.activity.LiveActivity;
import com.maywide.liveshow.bean.JniObjs;
import com.wushuangtech.library.Constants;

public class LiveChangReceiver extends BroadcastReceiver {

    private ProgressDialog progressDialog;

    public LiveChangReceiver(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (MyTTTRtcEngineEventHandler.TAG.equals(action)) {
            JniObjs mJniObjs = intent.getParcelableExtra(MyTTTRtcEngineEventHandler.MSG_TAG);
            switch (mJniObjs.mJniType) {
                case LocalConstans.CALL_BACK_ON_ENTER_ROOM:
                    //界面跳转
                    Intent activityIntent = new Intent();
                    activityIntent.setClass(context, LiveActivity.class);
                    context.startActivity(intent);
//                    context.startActivityForResult(activityIntent, ACTIVITY_MAIN);
//                    mIsLoging = false;
                    break;
                case LocalConstans.CALL_BACK_ON_ERROR:
                    progressDialog.dismiss();
//                    mIsLoging = false;
                    int errorType = mJniObjs.mErrorType;
                    if (errorType == Constants.ERROR_ENTER_ROOM_INVALIDCHANNELNAME) {

                    } else if (errorType == Constants.ERROR_ENTER_ROOM_TIMEOUT) {

                    } else if (errorType == Constants.ERROR_ENTER_ROOM_VERIFY_FAILED) {

                    } else if (errorType == Constants.ERROR_ENTER_ROOM_BAD_VERSION) {

                    } else if (errorType == Constants.ERROR_ENTER_ROOM_CONNECT_FAILED) {

                    } else if (errorType == Constants.ERROR_ENTER_ROOM_NOEXIST) {

                    } else if (errorType == Constants.ERROR_ENTER_ROOM_SERVER_VERIFY_FAILED) {

                    } else if (errorType == Constants.ERROR_ENTER_ROOM_UNKNOW) {

                    }
                    break;
            }
        }
    }
}
