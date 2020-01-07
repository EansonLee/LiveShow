package com.maywide.liveshow.utils;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.SurfaceView;
import android.widget.TextView;

import com.maywide.liveshow.Handler.MyTTTRtcEngineEventHandler;
import com.maywide.liveshow.LocalConfig;
import com.maywide.liveshow.LocalConstans;
import com.maywide.liveshow.R;
import com.maywide.liveshow.bean.EnterUserInfo;
import com.maywide.liveshow.bean.JniObjs;
import com.wushuangtech.library.Constants;
import com.wushuangtech.wstechapi.TTTRtcEngine;
import com.wushuangtech.wstechapi.model.VideoCanvas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.wushuangtech.library.Constants.CLIENT_ROLE_ANCHOR;
import static com.wushuangtech.library.Constants.CLIENT_ROLE_AUDIENCE;

public class LiveShowReceiver extends BroadcastReceiver {

    private long mAnchorId = -1;

    private ProgressDialog progressDialog;
    private TTTRtcEngine mTTTEngine;

    public LiveShowReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (MyTTTRtcEngineEventHandler.TAG.equals(action)) {
            JniObjs mJniObjs = intent.getParcelableExtra(MyTTTRtcEngineEventHandler.MSG_TAG);
//            Log.d("UI onReceive callBack... mJniType : " + mJniObjs.mJniType);
            switch (mJniObjs.mJniType) {
                case LocalConstans.CALL_BACK_ON_USER_KICK:
                    String message = "";
                    int errorType = mJniObjs.mErrorType;
                    if (errorType == Constants.ERROR_KICK_BY_HOST) {
                        message = context.getResources().getString(R.string.ttt_error_exit_kicked);
                    } else if (errorType == Constants.ERROR_KICK_BY_PUSHRTMPFAILED) {
                        message = context.getResources().getString(R.string.ttt_error_exit_push_rtmp_failed);
                    } else if (errorType == Constants.ERROR_KICK_BY_SERVEROVERLOAD) {
                        message = context.getResources().getString(R.string.ttt_error_exit_server_overload);
                    } else if (errorType == Constants.ERROR_KICK_BY_MASTER_EXIT) {
                        message = context.getResources().getString(R.string.ttt_error_exit_anchor_exited);
                    } else if (errorType == Constants.ERROR_KICK_BY_RELOGIN) {
                        message = context.getResources().getString(R.string.ttt_error_exit_relogin);
                    } else if (errorType == Constants.ERROR_KICK_BY_NEWCHAIRENTER) {
                        message = context.getResources().getString(R.string.ttt_error_exit_other_anchor_enter);
                    } else if (errorType == Constants.ERROR_KICK_BY_NOAUDIODATA) {
                        message = context.getResources().getString(R.string.ttt_error_exit_noaudio_upload);
                    } else if (errorType == Constants.ERROR_KICK_BY_NOVIDEODATA) {
                        message = context.getResources().getString(R.string.ttt_error_exit_novideo_upload);
                    } else if (errorType == Constants.ERROR_TOKEN_EXPIRED) {
                        message = context.getResources().getString(R.string.ttt_error_exit_token_expired);
                    }
                    progressDialog.setMessage(message);
                    break;
                case LocalConstans.CALL_BACK_ON_CONNECTLOST:
                    progressDialog.setMessage(context.getResources().getString(R.string.net_dialog_err));
                    break;
                case LocalConstans.CALL_BACK_ON_USER_JOIN: // 接收到用户加入频道
                    long uid = mJniObjs.mUid;
                    int identity = mJniObjs.mIdentity;
                    if (identity == CLIENT_ROLE_ANCHOR) {
                        mAnchorId = uid;
                    }
                    // 如果自己角色是观众
                    if (LocalConfig.mLocalRole == CLIENT_ROLE_AUDIENCE) {
                        if (identity == CLIENT_ROLE_ANCHOR) { // 如果用户角色为主播，打开视频
                            if (!mHasLocalView) {
                                mHasLocalView = true;
                                SurfaceView mSurfaceView = mTTTEngine.CreateRendererView(MainActivity.this);
                                mTTTEngine.setupRemoteVideo(new VideoCanvas(uid, Constants.RENDER_MODE_HIDDEN, mSurfaceView));
                                ((ConstraintLayout) findViewById(R.id.local_view_layout)).addView(mSurfaceView);
                            }
                        } else {
                            mWindowManager.add(LocalConfig.mLocalUserID, uid, getRequestedOrientation());
                        }
                    } else if (LocalConfig.mLocalRole == CLIENT_ROLE_ANCHOR) {
                        EnterUserInfo userInfo = new EnterUserInfo(uid, identity);
                        mWindowManager.addAndSendSei(LocalConfig.mLocalUserID, userInfo);
                    }
                    break;
                case LocalConstans.CALL_BACK_ON_USER_OFFLINE:
                    long offLineUserID = mJniObjs.mUid;
                    mWindowManager.removeAndSendSei(LocalConfig.mLocalUserID, offLineUserID);
                    break;
                case LocalConstans.CALL_BACK_ON_SEI:
                    TreeSet<EnterUserInfo> mInfos = new TreeSet<>();
                    try {
                        JSONObject jsonObject = new JSONObject(mJniObjs.mSEI);
                        JSONArray jsonArray = jsonObject.getJSONArray("pos");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject2 = (JSONObject) jsonArray.get(i);
                            String devid = jsonobject2.getString("id");
                            float x = Float.valueOf(jsonobject2.getString("x"));
                            float y = Float.valueOf(jsonobject2.getString("y"));

                            long userId;
                            int index = devid.indexOf(":");
                            if (index > 0) {
                                userId = Long.parseLong(devid.substring(0, index));
                            } else {
                                userId = Long.parseLong(devid);
                            }
//                            MyLog.d("CALL_BACK_ON_SEI", "parse user id : " + userId);
                            if (userId != mAnchorId) {
                                EnterUserInfo temp = new EnterUserInfo(userId, Constants.CLIENT_ROLE_BROADCASTER);
                                temp.setXYLocation(x, y);
                                mInfos.add(temp);
                            } else {
                                if (!mHasLocalView) {
                                    mHasLocalView = true;
                                    SurfaceView mSurfaceView = mTTTEngine.CreateRendererView(MainActivity.this);
                                    mTTTEngine.setupRemoteVideo(new VideoCanvas(userId, Constants.RENDER_MODE_HIDDEN, mSurfaceView));
                                    ((ConstraintLayout) findViewById(R.id.local_view_layout)).addView(mSurfaceView);
                                }
                            }

                        }
                    } catch (JSONException e) {
//                        MyLog.d("CALL_BACK_ON_SEI", "parse xml error : " + e.getLocalizedMessage());
                    }

                    int count = 0;
                    for (EnterUserInfo temp : mInfos) {
                        temp.mShowIndex = count;
                        count++;
                    }

                    for (EnterUserInfo next : mInfos) {
//                        MyLog.d("CALL_BACK_ON_SEI", "user list : " + next.getId() + " | index : " + next.mShowIndex);
                        mWindowManager.add(LocalConfig.mLocalUserID, next.getId(), getRequestedOrientation(), next.mShowIndex);
                    }

                    synchronized (obj) {
                        if (mUserMutes.size() > 0) {
                            Set<Map.Entry<Long, Boolean>> entries = mUserMutes.entrySet();
                            for (Map.Entry<Long, Boolean> next : entries) {
                                mWindowManager.muteAudio(next.getKey(), next.getValue());
                            }
                        }
                        mUserMutes.clear();
                        mIsReceiveSei = true;
                    }
                    break;
                case LocalConstans.CALL_BACK_ON_REMOTE_AUDIO_STATE:
                    if (mJniObjs.mRemoteAudioStats.getUid() != mAnchorId) {
                        String audioString = getResources().getString(R.string.ttt_audio_downspeed);
                        String audioResult = String.format(audioString, String.valueOf(mJniObjs.mRemoteAudioStats.getReceivedBitrate()));
                        mWindowManager.updateAudioBitrate(mJniObjs.mRemoteAudioStats.getUid(), audioResult);
                    } else
                        setTextViewContent(mAudioSpeedShow, R.string.ttt_audio_downspeed, String.valueOf(mJniObjs.mRemoteAudioStats.getReceivedBitrate()));
                    break;
                case LocalConstans.CALL_BACK_ON_REMOTE_VIDEO_STATE:
                    if (mJniObjs.mRemoteVideoStats.getUid() != mAnchorId) {
                        String videoString = getResources().getString(R.string.ttt_video_downspeed);
                        String videoResult = String.format(videoString, String.valueOf(mJniObjs.mRemoteVideoStats.getReceivedBitrate()));
                        mWindowManager.updateVideoBitrate(mJniObjs.mRemoteVideoStats.getUid(), videoResult);
                    } else
                        setTextViewContent(mVideoSpeedShow, R.string.ttt_video_downspeed, String.valueOf(mJniObjs.mRemoteVideoStats.getReceivedBitrate()));
                    break;
                case LocalConstans.CALL_BACK_ON_LOCAL_AUDIO_STATE:
                    if (LocalConfig.mLocalRole == CLIENT_ROLE_ANCHOR)
                        setTextViewContent(mAudioSpeedShow, R.string.ttt_audio_upspeed, String.valueOf(mJniObjs.mLocalAudioStats.getSentBitrate()));
                    else {
                        String localAudioString = getResources().getString(R.string.ttt_audio_upspeed);
                        String localAudioResult = String.format(localAudioString, String.valueOf(mJniObjs.mLocalAudioStats.getSentBitrate()));
                        mWindowManager.updateAudioBitrate(LocalConfig.mLocalUserID, localAudioResult);
                    }
                    break;
                case LocalConstans.CALL_BACK_ON_LOCAL_VIDEO_STATE:
                    if (LocalConfig.mLocalRole == CLIENT_ROLE_ANCHOR) {
                        mFpsSpeedShow.setText("FPS-" + mJniObjs.mLocalVideoStats.getSentFrameRate());
                        setTextViewContent(mVideoSpeedShow, R.string.ttt_video_upspeed, String.valueOf(mJniObjs.mLocalVideoStats.getSentBitrate()));
                    } else {
                        String localVideoString = getResources().getString(R.string.ttt_video_upspeed);
                        String localVideoResult = String.format(localVideoString, String.valueOf(mJniObjs.mLocalVideoStats.getSentBitrate()));
                        mWindowManager.updateVideoBitrate(LocalConfig.mLocalUserID, localVideoResult);
                    }
                    break;
                case LocalConstans.CALL_BACK_ON_MUTE_AUDIO:
                    long muteUid = mJniObjs.mUid;
                    boolean mIsMuteAuido = mJniObjs.mIsDisableAudio;
                    MyLog.i("OnRemoteAudioMuted CALL_BACK_ON_MUTE_AUDIO start! .... " + mJniObjs.mUid
                            + " | mIsMuteAuido : " + mIsMuteAuido);
                    if (muteUid == mAnchorId) {
//                            mIsMute = mIsMuteAuido;
//                            if (mIsHeadset)
//                                mAudioChannel.setImageResource(mIsMuteAuido ? R.drawable.mainly_btn_muted_headset_selector : R.drawable.mainly_btn_headset_selector);
//                            else
//                                mAudioChannel.setImageResource(mIsMuteAuido ? R.drawable.mainly_btn_mute_speaker_selector : R.drawable.mainly_btn_speaker_selector);
                    } else {
                        if (LocalConfig.mLocalRole != Constants.CLIENT_ROLE_ANCHOR) {
                            if (mIsReceiveSei) {
                                mWindowManager.muteAudio(muteUid, mIsMuteAuido);
                            } else {
                                mUserMutes.put(muteUid, mIsMuteAuido);
                            }
                        } else {
                            mWindowManager.muteAudio(muteUid, mIsMuteAuido);
                        }
                    }
                    break;

                case LocalConstans.CALL_BACK_ON_AUDIO_ROUTE:
                    int mAudioRoute = mJniObjs.mAudioRoute;
                    if (mAudioRoute == Constants.AUDIO_ROUTE_SPEAKER || mAudioRoute == Constants.AUDIO_ROUTE_HEADPHONE) {
                        mIsHeadset = false;
                        mAudioChannel.setImageResource(mIsMute ? R.drawable.mainly_btn_mute_speaker_selector : R.drawable.mainly_btn_speaker_selector);
                    } else {
                        mIsHeadset = true;
                        mAudioChannel.setImageResource(mIsMute ? R.drawable.mainly_btn_muted_headset_selector : R.drawable.mainly_btn_headset_selector);
                    }
                    break;
                case LocalConstans.CALL_BACK_ON_PHONE_LISTENER_COME:
                    mIsPhoneComing = true;
                    mIsSpeaker = mTTTEngine.isSpeakerphoneEnabled();
                    if (mIsSpeaker) {
                        mTTTEngine.setEnableSpeakerphone(false);
                    }

                    if (!mIsMute) {
                        mTTTEngine.muteLocalAudioStream(true);
                    }
                    mTTTEngine.muteAllRemoteAudioStreams(true);
                    break;
                case LocalConstans.CALL_BACK_ON_PHONE_LISTENER_IDLE:
                    if (mIsPhoneComing) {
                        if (mIsSpeaker) {
                            mTTTEngine.setEnableSpeakerphone(true);
                        }

                        if (!mIsMute) {
                            mTTTEngine.muteLocalAudioStream(false);
                        }
                        mTTTEngine.muteAllRemoteAudioStreams(false);
                        mIsPhoneComing = false;
                    }
                    break;
                case LocalConstans.CALL_BACK_ON_AUDIO_VOLUME_INDICATION:
                    if (mIsMute) return;
                    int volumeLevel = mJniObjs.mAudioLevel;
                    if (mJniObjs.mUid == LocalConfig.mLocalUserID) {
                        if (mIsHeadset) {
                            if (volumeLevel >= 0 && volumeLevel <= 3) {
                                mAudioChannel.setImageResource(R.drawable.mainly_btn_headset_small_selector);
                            } else if (volumeLevel > 3 && volumeLevel <= 6) {
                                mAudioChannel.setImageResource(R.drawable.mainly_btn_headset_middle_selector);
                            } else if (volumeLevel > 6 && volumeLevel <= 9) {
                                mAudioChannel.setImageResource(R.drawable.mainly_btn_headset_big_selector);
                            }
                        } else {
                            if (volumeLevel >= 0 && volumeLevel <= 3) {
                                mAudioChannel.setImageResource(R.drawable.mainly_btn_speaker_small_selector);
                            } else if (volumeLevel > 3 && volumeLevel <= 6) {
                                mAudioChannel.setImageResource(R.drawable.mainly_btn_speaker_middle_selector);
                            } else if (volumeLevel > 6 && volumeLevel <= 9) {
                                mAudioChannel.setImageResource(R.drawable.mainly_btn_speaker_big_selector);
                            }
                        }
                    } else {
                        mWindowManager.updateSpeakState(mJniObjs.mUid, mJniObjs.mAudioLevel);
                    }
                    break;
            }
        }
    }

}
