package com.maywide.liveshow.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
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
import com.maywide.liveshow.LocalConfig;
import com.maywide.liveshow.R;
import com.maywide.liveshow.adapter.TalkAdapter;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.base.MyApplication;
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
import com.maywide.liveshow.utils.LiveShowReceiver;
import com.maywide.liveshow.widget.BroadCastDialog;
import com.maywide.liveshow.widget.ConfirmDialog;
import com.maywide.liveshow.widget.EditConfirmDialog;
import com.maywide.liveshow.widget.InfoDialog;
import com.maywide.liveshow.widget.MarqueeTextView;
import com.maywide.liveshow.widget.ShareDialog;
import com.wushuangtech.library.Constants;
import com.wushuangtech.wstechapi.model.VideoCanvas;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maywide.liveshow.base.MyApplication.channelChangReceiver;
import static com.maywide.liveshow.base.MyApplication.jWebSClientService;
import static com.maywide.liveshow.base.MyApplication.serviceConnection;
import static com.wushuangtech.library.Constants.CLIENT_ROLE_ANCHOR;

/**
 * 直播界面
 */
public class LiveActivity extends BaseAcitivity implements View.OnClickListener {
    //直播界面
    @BindView(R.id.ly_live_show)
    LinearLayout lyLiveShow;
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

    private LiveShowReceiver liveShowReceiver;

    //聊天
    private TalkAdapter talkAdapter;
    private List<ChatSocketResp> chatList = new ArrayList<>();

    //主播个人信息
    private LoginResp.baseDetail baseDetail;

    // 点击退出时记录时间
    private long firstTime = 0;

    //公告栏弹框
    private BroadCastDialog broadCastDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
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

        // 如果角色是主播，打开自己的本地视频
        if (LocalConfig.mLocalRole == CLIENT_ROLE_ANCHOR) {
            // 打开本地预览视频，并开始推流
            // 创建 SurfaceView
            SurfaceView mSurfaceView = mTTTEngine.CreateRendererView(this);
            // 配置 SurfaceView
            mTTTEngine.setupLocalVideo(new VideoCanvas(0, Constants.RENDER_MODE_HIDDEN, mSurfaceView), getRequestedOrientation());
            lyLiveShow.addView(mSurfaceView);
            // 开始预览
            mTTTEngine.startPreview();
        }
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
     * 直播广播注册
     */
    private void initEngine() {
//        liveShowReceiver = new LiveShowReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(MyTTTRtcEngineEventHandler.TAG);
//        registerReceiver(liveShowReceiver, filter);
//        ((MyApplication) getApplicationContext()).mMyTTTRtcEngineEventHandler.setIsSaveCallBack(false);
    }

    /**
     * 退出直播
     */
    private void stopLiveReq() {
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
    protected void onDestroy() {

        //解绑广播
//        unregisterReceiver(liveShowReceiver);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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
        if (!TextUtils.isEmpty(type) && type.equals("chat")) {
            ChatSocketResp chatResp = new Gson().fromJson(chatData, ChatSocketResp.class);
            chatList.add(chatResp);
            talkAdapter.notifyDataSetChanged();
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
                    int beautyCode = mTTTEngine.setBeautyFaceStatus(true, 0.5f, 0.5f);
                    if (0 == beautyCode) {
                        isBeauty = true;
                        showToast("美颜开启成功");
                    } else {
                        showToast("美颜开启失败");
                    }
                } else {
                    int beautyCode = mTTTEngine.setBeautyFaceStatus(false, 0.5f, 0.5f);
                    if (0 == beautyCode) {
                        isBeauty = false;
                        showToast("美颜关闭成功");
                    } else {
                        showToast("美颜开启失败");
                    }
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
}
