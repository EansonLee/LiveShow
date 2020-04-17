package com.maywide.liveshow.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.maywide.liveshow.R;
import com.maywide.liveshow.adapter.TalkAdapter;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.base.MyApplication;
import com.maywide.liveshow.bean.PublishParam;
import com.maywide.liveshow.net.req.ChatSocketReq;
import com.maywide.liveshow.net.req.LiveBroadCastReq;
import com.maywide.liveshow.net.req.LiveRecordReq;
import com.maywide.liveshow.net.req.LoginReq;
import com.maywide.liveshow.net.req.NoticeReq;
import com.maywide.liveshow.net.req.SocketBaseReq;
import com.maywide.liveshow.net.resp.ChatSocketResp;
import com.maywide.liveshow.net.resp.LiveRecordResp;
import com.maywide.liveshow.net.resp.LoginResp;
import com.maywide.liveshow.net.resp.ResponseList;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.resp.SocketBaseResp;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.ChannelChangReceiver;
import com.maywide.liveshow.widget.BroadCastDialog;
import com.maywide.liveshow.widget.ConfirmDialog;
import com.maywide.liveshow.widget.EditConfirmDialog;
import com.maywide.liveshow.widget.InfoDialog;
import com.maywide.liveshow.widget.MarqueeTextView;
import com.maywide.liveshow.widget.ShareDialog;
import com.netease.LSMediaCapture.Statistics;
import com.netease.LSMediaCapture.lsLogUtil;
import com.netease.LSMediaCapture.lsMediaCapture;
import com.netease.LSMediaCapture.lsMessageHandler;
import com.netease.vcloud.video.effect.VideoEffect;
import com.netease.vcloud.video.render.NeteaseView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maywide.liveshow.base.MyApplication.jWebSClientService;
import static com.netease.LSMediaCapture.lsMediaCapture.StreamType.AUDIO;

/**
 * 直播界面
 */
public class LiveActivity extends BaseAcitivity implements View.OnClickListener, lsMessageHandler {
    //直播界面
    @BindView(R.id.ly_live_show)
    NeteaseView lyLiveShow;
    //头像
    @BindView(R.id.iv_live_icon)
    RoundedImageView ivLiveIcon;
    //昵称
    @BindView(R.id.tv_name)
    TextView tvName;
    //主播id
    @BindView(R.id.tv_id)
    TextView tvId;
    //公告栏
    @BindView(R.id.tv_broad)
    TextView tvBroad;
    //星钻数量
    @BindView(R.id.tv_star)
    TextView ivStar;
    //星钻布局
    @BindView(R.id.ly_star)
    LinearLayout lyStar;
    //守护布局
    @BindView(R.id.ly_protect)
    LinearLayout lyProtect;
    //滚动公告
    @BindView(R.id.tv_broadcast)
    MarqueeTextView tvBroadcast;
    //聊天recycleView
    @BindView(R.id.rcv_talk)
    RecyclerView rcvTalk;
    //底层布局
    @BindView(R.id.ly_bottom)
    LinearLayout lyBottom;
    //聊天键
    @BindView(R.id.et_talk)
    EditText etTalk;
    //美颜
    @BindView(R.id.iv_beauty)
    ImageView ivBeauty;
    //拍照
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    //分享
    @BindView(R.id.iv_share)
    ImageView ivShare;
    //关闭
    @BindView(R.id.iv_close)
    ImageView ivClose;
    //更多
    @BindView(R.id.iv_more)
    ImageView ivMore;
    //发送
//    @BindView(R.id.tv_send)
//    TextView tvSend;
    //是否美颜标志位
    private boolean isBeauty = false;

    //网易云SDK
    private lsMediaCapture mLSMediaCapture = null;
    private lsMediaCapture.LiveStreamingPara mLiveStreamingPara;

    //聊天
    private TalkAdapter talkAdapter;
    private List<ChatSocketResp> chatList = new ArrayList<>();

    //主播个人信息
    private LoginResp.baseDetail baseDetail;

    // 点击退出时记录时间
    private long firstTime = 0;

    //公告栏弹框
    private BroadCastDialog broadCastDialog;

    //推流url
    private String pushUrl;
    //传给后台地址
    private String sendUrl;
    //房间名称
    private String roomNum;
    //拉流地址
    private String pullUrl;

    //通过线程监听直播
    private Thread mThread;
    private Handler mHandler;
    //状态变量
    private boolean m_liveStreamingOn = false;
    private boolean m_liveStreamingInitFinished = false;
    private long mLastAudioProcessErrorAlertTime = 0;
    private long mLastVideoProcessErrorAlertTime = 0;
    private Intent mIntentLiveStreamingStopFinished = new Intent("LiveStreamingStopFinished");

    private File mMP3AppFileDirectory = null;
    private String mMixAudioFilePath = null;

    private MsgReceiver msgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        //动态弹起布局
//        addLayoutListener(lyLiveShow, lyBottom);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initView() {

        baseDetail = (LoginResp.baseDetail) getIntent().getSerializableExtra("infoData");
        if (baseDetail != null) {
            tvId.setText(baseDetail.getId());
            tvName.setText(baseDetail.getNickname());
            tvBroadcast.setText(baseDetail.getNotice());

            //推流地址
            pushUrl = baseDetail.getPushUrl() + roomNum;

            sendUrl = baseDetail.getPushUrl() + roomNum;

            pullUrl = baseDetail.getPushUrl() + roomNum;

//            if (TextUtils.isEmpty(roomNum)){
//                //开始录播
//                liveRecordReq(pullUrl,"start");
//            }
        }

        EventBus.getDefault().register(this);

        talkAdapter = new TalkAdapter(chatList);
        rcvTalk.setLayoutManager(new LinearLayoutManager(this));
        rcvTalk.setAdapter(talkAdapter);

        etTalk.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String sendData = etTalk.getText().toString();
                    if (TextUtils.isEmpty(sendData)) {
                        showToast("发送消息不能为空哦~");
                        return true;
                    }
                    if (MyApplication.client != null && MyApplication.client.isOpen()) {
                        ChatSocketReq chatSocketReq = new ChatSocketReq();
                        chatSocketReq.setContent(sendData);
                        SocketBaseReq<ChatSocketReq> socketBaseReq = new SocketBaseReq<>();
                        socketBaseReq.setData(chatSocketReq);
                        socketBaseReq.setType("chat");
                        String msg = new Gson().toJson(socketBaseReq);
                        jWebSClientService.sendMsg(msg);
                        etTalk.setText("");
                    }
                }
                return true;
            }
        });

        ivLiveIcon.setOnClickListener(this);
        tvBroad.setOnClickListener(this);
        ivBeauty.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        lyStar.setOnClickListener(this);
        lyProtect.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("AudioMix");
        registerReceiver(msgReceiver, intentFilter);

        staticHandler();

        //直播参数设置
        PublishParam publishParam = new PublishParam();
        publishParam.setStreamType(lsMediaCapture.StreamType.AV);
        publishParam.setFormatType(lsMediaCapture.FormatType.RTMP);
        //直播录制地址
        publishParam.setRecordPath(pushUrl);
        //设置视频清晰度
        publishParam.setVideoQuality(lsMediaCapture.VideoQuality.SUPER);

        //1、创建直播实例
        lsMediaCapture.LsMediaCapturePara lsMediaCapturePara = new lsMediaCapture.LsMediaCapturePara();
        lsMediaCapturePara.setContext(getApplicationContext()); //设置SDK上下文（建议使用ApplicationContext）
        lsMediaCapturePara.setMessageHandler(this); //设置SDK消息回调
        lsMediaCapturePara.setLogLevel(lsLogUtil.LogLevel.INFO); //日志级别
//        lsMediaCapturePara.setUploadLog(publishParam.uploadLog);//是否上传SDK日志
        mLSMediaCapture = new lsMediaCapture(lsMediaCapturePara);

        //2、设置直播参数
        mLiveStreamingPara = new lsMediaCapture.LiveStreamingPara();
        mLiveStreamingPara.setStreamType(publishParam.getStreamType()); // 推流类型 AV、AUDIO、VIDEO
        mLiveStreamingPara.setFormatType(publishParam.getFormatType()); // 推流格式 RTMP、MP4、RTMP_AND_MP4
        mLiveStreamingPara.setRecordPath(publishParam.getRecordPath());//formatType 为 MP4 或 RTMP_AND_MP4 时有效
        mLiveStreamingPara.setQosOn(true);


        lsMediaCapture.VideoQuality videoQuality = publishParam.getVideoQuality(); //视频模板（SUPER_HIGH 1280*720、SUPER 960*540、HIGH 640*480、MEDIUM 480*360、LOW 352*288）
        mLSMediaCapture.startVideoPreview(lyLiveShow, true, true, videoQuality, false);

        //滤镜类型
        publishParam.setFilterType(VideoEffect.FilterType.clean);

        mLSMediaCapture.setBeautyLevel(5); //磨皮强度为5,共5档，0为关闭
        mLSMediaCapture.setFilterStrength(0.5f); //滤镜强度
        mLSMediaCapture.setFilterType(publishParam.getFilterType());

        if (mThread != null) {
            showToast("正在开启直播，请稍后。。。");
            return;
        }
        showToast("初始化中。。。");
        mThread = new Thread() {
            @Override
            public void run() {
                //正常网络下initLiveStream 1、2s就可完成，当网络很差时initLiveStream可能会消耗5-10s，因此另起线程防止UI卡住
                if (!startAV()) {
                    showToast("直播开启失败，请仔细检查推流地址, 正在退出当前界面。。。");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LiveActivity.this.finish();
                        }
                    }, 5000);
                }
                mThread = null;
            }
        };
        mThread.start();

        if (mLSMediaCapture != null && m_liveStreamingOn) {
            if (mLiveStreamingPara.getStreamType() != AUDIO) {
                //关闭推流固定图像，正常推流
                mLSMediaCapture.resumeVideoEncode();
            } else {
                //关闭推流静音帧
                mLSMediaCapture.resumeAudioEncode();
            }
        }

    }

    //开始直播
    private boolean startAV() {
        //6、初始化直播
        m_liveStreamingInitFinished = mLSMediaCapture.initLiveStream(mLiveStreamingPara, pushUrl);
        if (mLSMediaCapture != null && m_liveStreamingInitFinished) {
            //7、开始直播
            mLSMediaCapture.startLiveStreaming();
            m_liveStreamingOn = true;
            return true;
        }
        return m_liveStreamingInitFinished;
    }

    public void staticHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected View getStatusBarView() {
        return null;
    }

    @Override
    protected View getNetErrView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //头像
            case R.id.iv_live_icon:
                showInfoDialog();
                break;
            //星钻
            case R.id.ly_star:

                break;
            //守护
            case R.id.ly_protect:

                break;
            //拍照
            case R.id.iv_photo:

                break;
            //分享
            case R.id.iv_share:

                break;
            //公告栏
            case R.id.tv_broad:
                broadCastDialog = BroadCastDialog.getInstance("公告栏", baseDetail.getNotice());
                broadCastDialog.setOutCancel(true)
                        .setMargin(0);
                broadCastDialog.show(getSupportFragmentManager());
                break;
            //美颜
            case R.id.iv_beauty:
                //如果没开美颜提示开美颜
                if (!isBeauty) {
                    showBeautyDialog(getString(R.string.dialog_open_beauty), getString(R.string.dialog_check_beauty));
                } else {
                    //开美颜提示关闭美颜
                    showBeautyDialog(getString(R.string.dialog_close_beauty), getString(R.string.dialog_check_close_beauty));
                }
                break;
            case R.id.iv_close:
                //弹框确定
                ConfirmDialog confirmDialog = ConfirmDialog.newInstance(getString(R.string.dialog_exit_live), getString(R.string.dialog_check_live));
                confirmDialog.setOutCancel(false);
                confirmDialog.setMargin(60);
                confirmDialog.setOnSureClickListener(new ConfirmDialog.OnSureClickListener() {
                    @Override
                    public void onSureClik() {
                        stopLiveReq();
//                        liveRecordReq(pullUrl,"stop");
//                        getRecordUrlReq(pullUrl);
                    }
                });
                confirmDialog.show(getSupportFragmentManager());
                break;
            case R.id.iv_more:
                //右上角三个点
                showShareDialog();
                break;
            case R.id.tv_broadcast:
                //公告
                String notice = tvBroadcast.getText().toString();
                showNotictDialog(notice);
                break;
        }
    }

    /**
     * 退出直播
     */
    private void stopLiveReq() {

        mLSMediaCapture.stopVideoPreview();
        mLSMediaCapture.destroyVideoPreview();

        LiveBroadCastReq liveBroadCastReq = new LiveBroadCastReq();

        liveBroadCastReq.setToken(sharedPreferencesUtils.getString("token", ""));
//        liveBroadCastReq.setVideo_url(recordUrl);

        RetrofitClient
                .getInstance()
                .api(API.class)
                .stopLiveReq(liveBroadCastReq)
                .enqueue(new Callback<ResponseObj<LoginResp>>() {
                    @Override
                    public void onResponse(Call<ResponseObj<LoginResp>> call, Response<ResponseObj<LoginResp>> response) {
//                        LoginResp resp = response.body().getData();
                        if ("0".equals(response.body().getCode())) {

                            finish();
                            if (EventBus.getDefault().isRegistered(this)) {
                                EventBus.getDefault().unregister(this);
                            }
                        } else {
                            showToast(response.body().getMsg());
                        }
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<ResponseObj<LoginResp>> call, Throwable t) {
                        showToast(getString(R.string.net_err));
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 修改直播间公告
     *
     * @param notice 新的公告
     */
    private void updateNoticeReq(final String notice) {
        NoticeReq noticeReq = new NoticeReq();
        noticeReq.setToken(sharedPreferencesUtils.getString("token", ""));
        noticeReq.setName(notice);
        RetrofitClient.getInstance()
                .api(API.class)
                .updateNoticeReq(noticeReq)
                .enqueue(new Callback<ResponseObj<LoginResp>>() {
                    @Override
                    public void onResponse(Call<ResponseObj<LoginResp>> call, Response<ResponseObj<LoginResp>> response) {
                        if ("0".equals(response.body().getCode())) {
                            tvBroadcast.setText(notice);
                            broadCastDialog = BroadCastDialog.getInstance("公告栏", notice);
                            showToast("公告修改成功");
                        } else {
                            showToast(response.body().getMsg());
                        }
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<ResponseObj<LoginResp>> call, Throwable t) {
                        showToast(getString(R.string.net_err));
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    protected void onPause() {
        if (mLSMediaCapture != null) {
            if (m_liveStreamingOn) {
                if (mLiveStreamingPara.getStreamType() != AUDIO) {
                    //推最后一帧图像
                    mLSMediaCapture.backgroundVideoEncode();
                } else {
                    //推静音帧
                    mLSMediaCapture.backgroundAudioEncode();
                }
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        //解绑广播
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        //伴音相关Receiver取消注册
        unregisterReceiver(msgReceiver);
        if (mLSMediaCapture != null) {
            mLSMediaCapture.stopLiveStreaming();
        }
        mLSMediaCapture.stopVideoPreview();
        mLSMediaCapture.destroyVideoPreview();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        long secondTime = System.currentTimeMillis();
        // 如果两次按键时间间隔大于1000毫秒，则不退出
        if (secondTime - firstTime > 1000) {
            showToast("再按一次退出应用");
            firstTime = secondTime;// 更新firstTime
        } else {
            stopLiveReq();
//            liveRecordReq(pullUrl,"stop");
//            getRecordUrlReq(pullUrl);
        }
    }

    /**
     * 聊天
     *
     * @param data
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String data) {
        String type = null;
        String chatData = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            type = jsonObject.getString("type");
            chatData = jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("chat") || type.equals("login")) {
                ChatSocketResp chatResp = new Gson().fromJson(chatData, ChatSocketResp.class);
                if (type.equals("login")) {
                    chatResp.setContent("进入房间");
                }
                chatList.add(chatResp);
                talkAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 美颜弹框
     *
     * @param title
     * @param msg
     */
    private void showBeautyDialog(String title, String msg) {
        ConfirmDialog beautyDialog = ConfirmDialog.newInstance(title, msg);
        beautyDialog.setOutCancel(false);
        beautyDialog.setMargin(70);
        beautyDialog.setOnSureClickListener(new ConfirmDialog.OnSureClickListener() {
            @Override
            public void onSureClik() {
                if (!isBeauty) {
                    mLSMediaCapture.setBeautyLevel(5); //磨皮强度为5,共5档，0为关闭
                    mLSMediaCapture.setFilterStrength(0.5f); //滤镜强度
                    isBeauty = true;
                    showToast("美颜开启成功");
                } else {
                    mLSMediaCapture.setBeautyLevel(0); //磨皮强度为5,共5档，0为关闭
                    isBeauty = false;
                    showToast("美颜关闭成功");
                }
            }
        });
        beautyDialog.show(getSupportFragmentManager());
    }

    /**
     * 分享弹框
     */
    private void showShareDialog() {

        ShareDialog shareDialog = ShareDialog.newInstance();
        shareDialog.setOutCancel(true)
                .setMargin(0);
        //粉丝
        shareDialog.setOnFensClickListener(new ShareDialog.onFensClickListener() {
            @Override
            public void onFensClick() {
                Intent intent = new Intent();
                intent.setClass(LiveActivity.this, LinkPerActivity.class);
                intent.putExtra("showFragment", "fens");
                startActivity(intent);
            }
        });
        //管理员
        shareDialog.setOnManageClickListener(new ShareDialog.onManageClickListener() {
            @Override
            public void onManageClick() {
                Intent intent = new Intent();
                intent.setClass(LiveActivity.this, LinkPerActivity.class);
                intent.putExtra("showFragment", "manage");
                startActivity(intent);
            }
        });
        //分享链接
        shareDialog.setOnShareClickListener(new ShareDialog.onShareClickListener() {
            @Override
            public void onShareClick() {

            }
        });
        //退出登录
        shareDialog.setOnLogoutClickListener(new ShareDialog.onLogoutClickListener() {
            @Override
            public void onLogoutClick() {
                Intent intent = new Intent();
                intent.setClass(LiveActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        shareDialog.show(getSupportFragmentManager());
    }

    /**
     * 个人信息弹框
     */
    private void showInfoDialog() {
        InfoDialog infoDialog = InfoDialog.newInstance(baseDetail);
        infoDialog.setOutCancel(true)
                .setMargin(27);
        infoDialog.show(getSupportFragmentManager());
    }

    /**
     * 修改公告弹框
     *
     * @param notice 原来的公告
     */
    private void showNotictDialog(String notice) {
        EditConfirmDialog editConfirmDialog = EditConfirmDialog.newInstance(getString(R.string.notice_title), notice);
        editConfirmDialog.setOutCancel(true);
        editConfirmDialog.setMargin(40);
        editConfirmDialog.setOnSureClickListener(new EditConfirmDialog.OnSureClickListener() {
            @Override
            public void onSureClik(String notice) {
                if (TextUtils.isEmpty(notice)) {
                    showToast("新公告不能为空哦");
                } else {
                    updateNoticeReq(notice);
                }
            }
        });
        editConfirmDialog.show(getSupportFragmentManager());
    }

    @Override
    public void handleMessage(int msg, Object object) {
        switch (msg) {
            case MSG_INIT_LIVESTREAMING_OUTFILE_ERROR://初始化直播出错
            case MSG_INIT_LIVESTREAMING_VIDEO_ERROR:
            case MSG_INIT_LIVESTREAMING_AUDIO_ERROR: {
                showToast("初始化直播出错，正在退出当前界面");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LiveActivity.this.finish();
                    }
                }, 3000);
                break;
            }
            case MSG_START_LIVESTREAMING_ERROR://开始直播出错
            {
                showToast("开始直播出错：" + object);
                break;
            }
            case MSG_STOP_LIVESTREAMING_ERROR://停止直播出错
            {
                if (m_liveStreamingOn) {
                    showToast("MSG_STOP_LIVESTREAMING_ERROR  停止直播出错");
                }
                break;
            }
            case MSG_AUDIO_PROCESS_ERROR://音频处理出错
            {
                if (m_liveStreamingOn && System.currentTimeMillis() - mLastAudioProcessErrorAlertTime >= 10000) {
                    showToast("音频处理出错");
                    mLastAudioProcessErrorAlertTime = System.currentTimeMillis();
                }

                break;
            }
            case MSG_VIDEO_PROCESS_ERROR://视频处理出错
            {
                if (m_liveStreamingOn && System.currentTimeMillis() - mLastVideoProcessErrorAlertTime >= 10000) {
                    showToast("视频处理出错");
                    mLastVideoProcessErrorAlertTime = System.currentTimeMillis();
                }
                break;
            }
            case MSG_START_PREVIEW_ERROR://视频预览出错，可能是获取不到camera的使用权限
            {
                Log.i("---", "test: in handleMessage, MSG_START_PREVIEW_ERROR");
                showToast("无法打开相机，可能没有相关的权限或者自定义分辨率不支持");
                break;
            }
            case MSG_AUDIO_RECORD_ERROR://音频采集出错，获取不到麦克风的使用权限
            {
                showToast("无法开启；录音，可能没有相关的权限");
                Log.i("---", "test: in handleMessage, MSG_AUDIO_RECORD_ERROR");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LiveActivity.this.finish();
                    }
                }, 3000);
                break;
            }
            case MSG_RTMP_URL_ERROR://断网消息
            {
                Log.i("---", "test: in handleMessage, MSG_RTMP_URL_ERROR");
                showToast("MSG_RTMP_URL_ERROR，推流已停止,正在退出当前界面");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LiveActivity.this.finish();
                    }
                }, 3000);
                break;
            }
            case MSG_URL_NOT_AUTH://直播URL非法，URL格式不符合视频云要求
            {
                showToast("MSG_URL_NOT_AUTH  直播地址不合法");
                break;
            }
            case MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR://音频采集参数不支持
            {
                Log.i("---", "test: in handleMessage, MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR");
                break;
            }
            case MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR://音频参数不支持
            {
                Log.i("---", "test: in handleMessage, MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR");
                break;
            }
            case MSG_NEW_AUDIORECORD_INSTANCE_ERROR://音频实例初始化出错
            {
                Log.i("---", "test: in handleMessage, MSG_NEW_AUDIORECORD_INSTANCE_ERROR");
                break;
            }
            case MSG_AUDIO_START_RECORDING_ERROR://音频采集出错
            {
                Log.i("---", "test: in handleMessage, MSG_AUDIO_START_RECORDING_ERROR");
                break;
            }
            case MSG_QOS_TO_STOP_LIVESTREAMING://网络QoS极差，视频码率档次降到最低
            {
                showToast("MSG_QOS_TO_STOP_LIVESTREAMING");
                Log.i("---", "test: in handleMessage, MSG_QOS_TO_STOP_LIVESTREAMING");
                break;
            }
            case MSG_CAMERA_NOT_SUPPORT_FLASH:
                showToast("不支持闪光灯");
                break;
            case MSG_START_PREVIEW_FINISHED://camera采集预览完成
            {
                Log.i("---", "test: MSG_START_PREVIEW_FINISHED");
                break;
            }
            case MSG_START_LIVESTREAMING_FINISHED://开始直播完成
            {
                Log.i("---", "test: MSG_START_LIVESTREAMING_FINISHED");
                showToast("直播开始");
                m_liveStreamingOn = true;
                break;
            }
            case MSG_STOP_LIVESTREAMING_FINISHED://停止直播完成
            {
                Log.i("---", "test: MSG_STOP_LIVESTREAMING_FINISHED");
                showToast("停止直播已完成");
                m_liveStreamingOn = false;
                {
                    mIntentLiveStreamingStopFinished.putExtra("LiveStreamingStopFinished", 1);
                    sendBroadcast(mIntentLiveStreamingStopFinished);
                }

                break;
            }
            case MSG_STOP_VIDEO_CAPTURE_FINISHED: {
                Log.i("---", "test: in handleMessage: MSG_STOP_VIDEO_CAPTURE_FINISHED");
                break;
            }
            case MSG_STOP_AUDIO_CAPTURE_FINISHED: {
                Log.i("---", "test: in handleMessage: MSG_STOP_AUDIO_CAPTURE_FINISHED");
                break;
            }
            case MSG_SWITCH_CAMERA_FINISHED://切换摄像头完成
            {
                showToast("相机切换成功");
                break;
            }
            case MSG_GET_STATICS_INFO://获取统计信息的反馈消息
            {

                Message message = Message.obtain(mHandler, MSG_GET_STATICS_INFO);
                Statistics statistics = (Statistics) object;

                Bundle bundle = new Bundle();
                bundle.putInt("FR", statistics.videoEncodeFrameRate);
                bundle.putInt("VBR", statistics.videoRealSendBitRate);
                bundle.putInt("ABR", statistics.audioRealSendBitRate);
                bundle.putInt("TBR", statistics.totalRealSendBitRate);
                bundle.putInt("networkLevel", statistics.networkLevel);
                bundle.putString("resolution", statistics.videoEncodeWidth + " x " + statistics.videoEncodeHeight);
                message.setData(bundle);
//				  Log.i(TAG, "test: audio : " + statistics.audioEncodeBitRate + "  video: " + statistics.videoEncodeBitRate + "  total: " + statistics.totalRealSendBitRate);

                if (mHandler != null) {
                    mHandler.sendMessage(message);
                }
                break;
            }
            case MSG_BAD_NETWORK_DETECT://如果连续一段时间（10s）实际推流数据为0，会反馈这个错误消息
            {
                showToast("MSG_BAD_NETWORK_DETECT");
                //Log.i(TAG, "test: in handleMessage, MSG_BAD_NETWORK_DETECT");
                break;
            }
            case MSG_URL_FORMAT_NOT_RIGHT://推流url格式不正确
            {
                //Log.i(TAG, "test: in handleMessage, MSG_URL_FORMAT_NOT_RIGHT");
                showToast("MSG_URL_FORMAT_NOT_RIGHT");
                break;
            }
            case MSG_URL_IS_EMPTY://推流url为空
            {
                //Log.i(TAG, "test: in handleMessage, MSG_URL_IS_EMPTY");
                break;
            }

            case MSG_SPEED_CALC_SUCCESS:
            case MSG_SPEED_CALC_FAIL:
                Message message = Message.obtain(mHandler, msg);
                message.obj = object;
                mHandler.sendMessage(message);
                break;

            default:
                break;
        }
    }

    //用于接收Service发送的消息，伴音开关
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int audioMixMsg = intent.getIntExtra("AudioMixMSG", 0);
            mMixAudioFilePath = mMP3AppFileDirectory.toString() + "/" + intent.getStringExtra("AudioMixFilePathMSG");

            //伴音开关的控制
            if (audioMixMsg == 1) {
                if (mMixAudioFilePath.isEmpty())
                    return;

                if (mLSMediaCapture != null) {
                    mLSMediaCapture.startPlayMusic(mMixAudioFilePath, false);
                }
            } else if (audioMixMsg == 2) {
                if (mLSMediaCapture != null) {
                    mLSMediaCapture.resumePlayMusic();
                }
            } else if (audioMixMsg == 3) {
                if (mLSMediaCapture != null) {
                    mLSMediaCapture.pausePlayMusic();
                }
            } else if (audioMixMsg == 4) {
                if (mLSMediaCapture != null) {
                    mLSMediaCapture.stopPlayMusic();
                }
            }
        }
    }
}
