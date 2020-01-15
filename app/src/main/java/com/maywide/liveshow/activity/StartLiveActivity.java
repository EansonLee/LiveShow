package com.maywide.liveshow.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maywide.liveshow.Handler.MyTTTRtcEngineEventHandler;
import com.maywide.liveshow.LocalConfig;
import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.bean.MyPermissionBean;
import com.maywide.liveshow.utils.EnterLiveRoomReceiver;
import com.maywide.liveshow.utils.MyPermissionManager;
import com.maywide.liveshow.utils.UpLoadUtils;
import com.maywide.liveshow.widget.ConfirmDialog;
import com.qiniu.android.http.ResponseInfo;
import com.wushuangtech.library.Constants;
import com.wushuangtech.wstechapi.model.PublisherConfiguration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wushuangtech.library.Constants.CLIENT_ROLE_ANCHOR;

/**
 * 开始直播（准备界面）
 */
public class StartLiveActivity extends BaseAcitivity implements View.OnClickListener {

    @BindView(R.id.ly_start_live)
    LinearLayout lyStartLive;
    @BindView(R.id.view_status_bar)
    View statusBar;
    @BindView(R.id.iv_locate)
    ImageView ivLocate;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.ly_share)
    LinearLayout lyShare;
    @BindView(R.id.ly_beauty)
    LinearLayout lyBeauty;
    @BindView(R.id.tv_start_live)
    TextView tvStartLive;

    //房间名称
    private String roomName = "123";

    //是否进入直播间标志位
    private boolean isLoging;
    //直播前广播消息用于接收sdk是否可以进入直播,
    private EnterLiveRoomReceiver enterLiveRoomReceiver;


    //权限
    private MyPermissionManager mMyPermissionManager;
    ArrayList<MyPermissionBean> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        addLayoutListener(lyStartLive, tvStartLive);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start_live;
    }

    @Override
    protected void initView() {
        ivLocate.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        lyShare.setOnClickListener(this);
        lyBeauty.setOnClickListener(this);
        tvStartLive.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        //权限设置
        mPermissionList.add(new MyPermissionBean(Manifest.permission.WRITE_EXTERNAL_STORAGE, getResources().getString(R.string.permission_write_external_storage)));
        mPermissionList.add(new MyPermissionBean(Manifest.permission.RECORD_AUDIO, getResources().getString(R.string.permission_record_audio)));
        mPermissionList.add(new MyPermissionBean(Manifest.permission.CAMERA, getResources().getString(R.string.permission_camera)));
        mPermissionList.add(new MyPermissionBean(Manifest.permission.READ_PHONE_STATE, getResources().getString(R.string.permission_read_phone_state)));
        //如果获得权限
        if (checkPermission()) {
            // ***注册广播，接收 SDK 的回调信令*** 重要操作!加TODO高亮
            enterLiveRoomReceiver = new EnterLiveRoomReceiver(getProgressDialog(), isLoging, this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(MyTTTRtcEngineEventHandler.TAG);
            registerReceiver(enterLiveRoomReceiver, filter);
        }

    }

    @Override
    protected View getStatusBarView() {
        return statusBar;
    }

    @Override
    protected View getNetErrView() {
        return lyShare;
    }

    /**
     * 检查所需权限
     */
    private boolean checkPermission() {
        mMyPermissionManager = new MyPermissionManager(this, new MyPermissionManager.PermissionUtilsInter() {
            @Override
            public List<MyPermissionBean> getApplyPermissions() {
                return mPermissionList;
            }

            @Override
            public AlertDialog.Builder getTipAlertDialog() {
                return null;
            }

            @Override
            public Dialog getTipDialog() {
                return null;
            }

            @Override
            public AlertDialog.Builder getTipAppSettingAlertDialog() {
                return null;
            }

            @Override
            public Dialog getTipAppSettingDialog() {
                return null;
            }
        });
        boolean isOk = mMyPermissionManager.checkPermission();
        return isOk;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择本地照片
            case R.id.iv_add:
                seleteLocalPhoto();
                break;
            //定位
            case R.id.iv_locate:

                break;
            //拍照
            case R.id.iv_photo:

                break;
            //关闭
            case R.id.iv_close:
                showCloseDialog();
                break;
            //分享
            case R.id.ly_share:

                break;
            //美颜
            case R.id.ly_beauty:
                showBeautyDialog();
                break;
            //开始直
            case R.id.tv_start_live:
                showStarLiveDialog();
                break;
        }
    }

    /**
     * 进房间前的设置
     */
    private void mustConfigSdk() {
        // 创建 SDK 实例对象，请看 MainApplication 类。

        /*
         * 1.设置频道模式，SDK 默认就是 CHANNEL_PROFILE_COMMUNICATION(通信模式)，这里需要显式调用设置为 CHANNEL_PROFILE_LIVE_BROADCASTING(直播模式)。
         * 注意:该接口是全局接口，离开频道后状态不会清除，所以在模式需要发生变化时调用即可，无需每次加入频道都设置。Demo在这里设置是为了简化代码。
         */
        mTTTEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING); // 必须设置的 API
        /*
         * 2.设置角色身份，CHANNEL_PROFILE_LIVE_BROADCASTING 模式下可以设置三种角色
         * CLIENT_ROLE_ANCHOR(主播) ：频道的创建者，只有主播可以创建频道，创建成功后，其他角色的用户才能加入频道。
         * CLIENT_ROLE_BROADCASTER(副播) ：默认可以收发音视频流。
         * CLIENT_ROLE_AUDIENCE(观众) ：默认音视频流只收不发。
         *
         * SDK 默认是 CLIENT_ROLE_BROADCASTER 角色，Demo 不展示观众角色。
         * 注意:该接口是全局接口，离开频道后状态不会清除，所以在角色需要发生变化时调用即可，无需每次加入频道都设置。Demo在这里设置是为了简化代码。
         */
        //设置角色身份
        int mRole = CLIENT_ROLE_ANCHOR;
        //将角色身份设置到本地
        LocalConfig.mLocalRole = mRole;
        mTTTEngine.setClientRole(LocalConfig.mLocalRole); // 必须设置的 API
        // 3.启用视频模块功能
        mTTTEngine.enableVideo(); // 必须设置的 API
        // 关闭美颜
        mTTTEngine.setBeautyFaceStatus(false, 0.5f, 0.5f);
        // 4.设置推流地址，只有主播角色的用户设置有效。该推流地址仅供Demo运行演示使用，不可在正式环境中使用。
        // 必须设置的 API
        if (LocalConfig.mLocalRole == CLIENT_ROLE_ANCHOR) {
            String mPushUrlPrefix = "rtmp://push.3ttest.cn/sdk2/";
            String mPushUrl;
//            if (mEncodeType == 0) {
            mPushUrl = mPushUrlPrefix + roomName; // H264视频推流格式，默认使用即可
//            } else {
//                mPushUrl = mPushUrlPrefix + mRoomName + "?trans=1"; //H265视频推流格式
//            }
            PublisherConfiguration mPublisherConfiguration = new PublisherConfiguration();
            mPublisherConfiguration.setPushUrl(mPushUrl);
            mTTTEngine.configPublisher(mPublisherConfiguration);
        }
    }

    /**
     * 进房间的设置
     */
    private void optConfigSdk() {
        // 1.设置音频编码参数，SDK 默认为 ISAC 音频编码格式，32kbps 音频码率，适用于通话；高音质选用 AAC 格式编码，码率设置为96kbps。
        //  可选操作的 API
        //高音质
        mTTTEngine.setPreferAudioCodec(Constants.TTT_AUDIO_CODEC_AAC, 96, 1);
        //一般音质
        mTTTEngine.setPreferAudioCodec(Constants.TTT_AUDIO_CODEC_ISAC, 32, 1);
        // 2.设置视频编码参数，SDK 默认为 360P 质量等级。
        // 可选操作的 API
        mTTTEngine.setVideoProfile(Constants.TTTRTC_VIDEOPROFILE_1080P, false);
        mTTTEngine.joinChannel("", roomName, LocalConfig.mLocalUserID, true, true);
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

    /**
     * 开始直播弹框
     */
    private void showStarLiveDialog() {
        //弹框确定
        ConfirmDialog liveDialog = ConfirmDialog.newInstance(getString(R.string.dialog_live_open), getString(R.string.dialog_check_live_open));
        liveDialog.setOutCancel(false);
        liveDialog.setMargin(70);
        liveDialog.setOnSureClickListener(new ConfirmDialog.OnSureClickListener() {
            @Override
            public void onSureClik() {
                showProgressDialog("正在进入房间");
                if (isLoging) {
                    return;
                }
                isLoging = true;
                mustConfigSdk();
                optConfigSdk();
            }
        });
        liveDialog.show(getSupportFragmentManager());
    }

    /**
     * 美颜弹框
     */
    private void showBeautyDialog() {
        //弹框确定
        ConfirmDialog beautyDialog = ConfirmDialog.newInstance(getString(R.string.dialog_open_beauty), getString(R.string.dialog_check_beauty));
        beautyDialog.setOutCancel(false);
        beautyDialog.setMargin(70);
        beautyDialog.setOnSureClickListener(new ConfirmDialog.OnSureClickListener() {
            @Override
            public void onSureClik() {
                int beautyCode = mTTTEngine.setBeautyFaceStatus(true, 0.5f, 0.5f);
                if (0 == beautyCode) {
                    showToast("美颜开启成功");
                }
            }
        });
        beautyDialog.show(getSupportFragmentManager());
    }

    /**
     * 关闭弹框
     */
    private void showCloseDialog() {
        //弹框确定
        ConfirmDialog confirmDialog = ConfirmDialog.newInstance(getString(R.string.dialog_exit_live), getString(R.string.dialog_check_live));
        confirmDialog.setOutCancel(false);
        confirmDialog.setMargin(70);
        confirmDialog.setOnSureClickListener(new ConfirmDialog.OnSureClickListener() {
            @Override
            public void onSureClik() {
                finish();
            }
        });
        confirmDialog.show(getSupportFragmentManager());
    }

    /**
     * 跳转本地相册获取照片
     */
    private void seleteLocalPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                ivAdd.setImageURI(uri);
                upLoadImage(uri);
            }
        }
    }

    /**
     * 将本地图片上传到七牛云
     *
     * @param uri
     */
    private void upLoadImage(Uri uri) {
        //将URI转换为路径：
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        //这个是获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        // 最后根据索引值获取图片路径
        String photoPath = cursor.getString(column_index);
        //获取文件名
        String fileName = getFileName(photoPath);
        //上传七牛云
        UpLoadUtils.init();
        UpLoadUtils.uploadPic(photoPath, fileName, new UpLoadUtils.UploadCallBack() {
            @Override
            public void success(String url) {
                Log.e("---", "图片地址-" + url);
            }

            @Override
            public void fail(String key, ResponseInfo info) {

            }
        });
    }


    /**
     * huoqu
     * 根据文件路径获取文件名
     * @param pathandname
     * @return
     */
    public String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if (start!=-1 && end!=-1) {
            return pathandname.substring(start+1, end);
        }
        else {
            return null;
        }
    }

}
