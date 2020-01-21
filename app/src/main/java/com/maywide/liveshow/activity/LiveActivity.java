package com.maywide.liveshow.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.maywide.liveshow.LocalConfig;
import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.net.req.LoginReq;
import com.maywide.liveshow.net.resp.LoginResp;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.LiveShowReceiver;
import com.maywide.liveshow.widget.BroadCastDialog;
import com.maywide.liveshow.widget.ConfirmDialog;
import com.maywide.liveshow.widget.InfoDialog;
import com.maywide.liveshow.widget.MarqueeTextView;
import com.maywide.liveshow.widget.ShareDialog;
import com.wushuangtech.library.Constants;
import com.wushuangtech.wstechapi.model.VideoCanvas;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    //是否美颜标志位
    private boolean isBeauty = false;

    private LiveShowReceiver liveShowReceiver;

    //主播个人信息
    private LoginResp.baseDetail baseDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        if (baseDetail!=null){
            tvId.setText(baseDetail.getId());
            tvName.setText(baseDetail.getNickname());
            tvBroadcast.setText(baseDetail.getNotice());
        }

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
                BroadCastDialog broadCastDialog = BroadCastDialog.getInstance("公告栏", baseDetail.getNotice());
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
                    }
                });
                confirmDialog.show(getSupportFragmentManager());
                break;
            case R.id.iv_more:
                showShareDialog();
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
        LoginReq loginReq = new LoginReq();

        loginReq.setToken(sharedPreferencesUtils.getString("token", ""));
        RetrofitClient
                .getInstance()
                .api(API.class)
                .stopLiveReq(loginReq)
                .enqueue(new Callback<ResponseObj<LoginResp>>() {
                    @Override
                    public void onResponse(Call<ResponseObj<LoginResp>> call, Response<ResponseObj<LoginResp>> response) {
//                        LoginResp resp = response.body().getData();
                        if ("0".equals(response.body().getCode())) {
                            mTTTEngine.leaveChannel();
                            finish();
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
        try {
            unregisterReceiver(liveShowReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //弹框确定
        ConfirmDialog confirmDialog = ConfirmDialog.newInstance(getString(R.string.dialog_exit_live), getString(R.string.dialog_check_live));
        confirmDialog.setOutCancel(false);
        confirmDialog.setMargin(60);
        confirmDialog.setOnSureClickListener(new ConfirmDialog.OnSureClickListener() {
            @Override
            public void onSureClik() {
                stopLiveReq();
            }
        });
        confirmDialog.show(getSupportFragmentManager());
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
    private void showShareDialog(){

        ShareDialog shareDialog = ShareDialog.newInstance();
        shareDialog.setOutCancel(true)
                .setMargin(0);
        //粉丝
        shareDialog.setOnFensClickListener(new ShareDialog.onFensClickListener() {
            @Override
            public void onFensClick() {
                Intent intent = new Intent();
                intent.setClass(LiveActivity.this,LinkPerActivity.class);
                intent.putExtra("showFragment","fens");
                startActivity(intent);
            }
        });
        //管理员
        shareDialog.setOnManageClickListener(new ShareDialog.onManageClickListener() {
            @Override
            public void onManageClick() {
                Intent intent = new Intent();
                intent.setClass(LiveActivity.this,LinkPerActivity.class);
                intent.putExtra("showFragment","manage");
                startActivity(intent);
            }
        });
        //分享链接
        shareDialog.setOnShareClickListener(new ShareDialog.onShareClickListener() {
            @Override
            public void onShareClick() {

            }
        });
        shareDialog.show(getSupportFragmentManager());
    }

    /**
     * 个人信息弹框
     */
    private void showInfoDialog(){
        InfoDialog infoDialog = InfoDialog.newInstance(baseDetail);
        infoDialog.setOutCancel(true)
                .setMargin(27);

        infoDialog.show(getSupportFragmentManager());
    }
}
