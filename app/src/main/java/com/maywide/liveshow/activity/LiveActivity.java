package com.maywide.liveshow.activity;

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
import com.maywide.liveshow.utils.LiveShowReceiver;
import com.maywide.liveshow.widget.BroadCastDialog;
import com.maywide.liveshow.widget.ConfirmDialog;
import com.maywide.liveshow.widget.ShareDialog;
import com.wushuangtech.library.Constants;
import com.wushuangtech.wstechapi.model.VideoCanvas;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    TextView tvBroadcast;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //动态弹起布局
        addLayoutListener(lyLiveShow, lyBottom);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initView() {

        ivLiveIcon.setOnClickListener(this);
        tvBroad.setOnClickListener(this);
        ivBeauty.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivClose.setOnClickListener(this);
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
                BroadCastDialog broadCastDialog = BroadCastDialog.getInstance("公告栏", "大大大大大大哥哥哥哥");
                broadCastDialog.setOutCancel(true)
                        .setMargin(0);
                broadCastDialog.setOnLayOutClickListener(new BroadCastDialog.onLayOutClickListener() {
                    @Override
                    public void onLayOutClick() {

                    }
                });
                broadCastDialog.show(getSupportFragmentManager());
                break;
            //美颜
            case R.id.iv_beauty:
                //弹框确定
                ConfirmDialog beautyDialog = ConfirmDialog.newInstance(getString(R.string.dialog_open_beauty), getString(R.string.dialog_check_beauty));
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
                        finish();
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
     * 软键盘动态上移
     *
     * @param main
     * @param scroll
     */
    private void addLayoutListener(final View main, final View scroll) {

        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取main 在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rect);
                //获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                //屏幕高度
                int screenHeight = main.getRootView().getHeight();
                //不可见区域大于屏幕本身高度1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 3) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);

                    int scrollHeight = (location[1] + scroll.getHeight()) - rect.bottom;

                    if (scrollHeight != 0) {
                        //让界面整体上移键盘的高度
                        main.scrollTo(0, scrollHeight);
                    } else {
                        return;
                    }

                } else {
                    //不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }
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
                finish();
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

            }
        });
        //管理员
        shareDialog.setOnManageClickListener(new ShareDialog.onManageClickListener() {
            @Override
            public void onManageClick() {

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
}
