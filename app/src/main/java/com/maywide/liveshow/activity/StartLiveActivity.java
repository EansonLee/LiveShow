package com.maywide.liveshow.activity;


import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseAcitivity;

import com.maywide.liveshow.net.req.FluReq;
import com.maywide.liveshow.net.req.LiveBroadCastReq;
import com.maywide.liveshow.net.req.LoginReq;

import com.maywide.liveshow.net.resp.FluResp;
import com.maywide.liveshow.net.resp.LiveRecordResp;
import com.maywide.liveshow.net.resp.LoginResp;
import com.maywide.liveshow.net.resp.ResponseList;
import com.maywide.liveshow.net.resp.ResponseObj;
import com.maywide.liveshow.net.retrofit.API;
import com.maywide.liveshow.net.retrofit.RetrofitClient;
import com.maywide.liveshow.utils.EnterLiveRoomReceiver;
import com.maywide.liveshow.utils.UpLoadUtils;
import com.maywide.liveshow.widget.ConfirmDialog;
import com.netease.LSMediaCapture.lsMediaCapture;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @BindView(R.id.et_title)
    EditText etTitle;

    //房间名称
    private String roomNum;
    //推流地址
    private String mPushUrl;
    //传给后台地址
    private String sendUrl;
    //图片地址
    private String photoPath;
    //本地图片文件名
    private String fileName;
    //拉流地址
    private String pullUrl;

    //主播个人信息
    private LoginResp.baseDetail baseDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

//        addLayoutListener(lyStartLive, tvStartLive);
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

        baseDetail = (LoginResp.baseDetail) getIntent().getSerializableExtra("infoData");
        if (null != baseDetail) {
            //获取主播背景图
            photoPath = baseDetail.getBg_picture();
            //获取用户id
//            LocalConfig.mLocalUserID = baseDetail.getUser_id();
            //获取房间号
            roomNum = baseDetail.getAnchor_code();
            //推流地址
//            mPushUrl = "rtmp://push.agegeage.hqcqz1.cn/live/" + roomNum;
//
//            sendUrl = "http://m3u8.agegeage.hqcqz1.cn/live/" + roomNum + "/playlist.m3u8";
//
//            pullUrl = "rtmp://pull.agegeage.hqcqz1.cn/live/" + roomNum;
            getFlUrl();
        }

//        if (!TextUtils.isEmpty(photoPath)) {
//
//            //todo 显示在＋号
//            Glide.with(StartLiveActivity.this)
//                    .load(photoPath)
//                    .error(R.mipmap.load_err)
//                    .into(ivAdd);
//        }

    }

    @Override
    protected View getStatusBarView() {
        return statusBar;
    }

    @Override
    protected View getNetErrView() {
        return lyShare;
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
            //开始直播
            case R.id.tv_start_live:
                showStarLiveDialog();
                break;
        }
    }


    /**
     * 开始直播请求
     */
    private void liveBroadCastReq() {
        //判断标题
        if (TextUtils.isEmpty(etTitle.getText().toString())) {
            dismissProgressDialog();
            showToast("请输入直播标题");
            return;
        }
        //判断直播背景图
        if (TextUtils.isEmpty(photoPath)) {
            dismissProgressDialog();
            showToast("请选择直播背景图");
            return;
        }
        //判断推流url
        if (TextUtils.isEmpty(mPushUrl)) {
            dismissProgressDialog();
            showToast("请设置推流地址");
            return;
        }
        LiveBroadCastReq liveBroadCastReq = new LiveBroadCastReq();
        liveBroadCastReq.setToken(sharedPreferencesUtils.getString("token", ""));

        //判断是否选择本地图片
        if (!TextUtils.isEmpty(fileName)) {
            String sendImgUrl = "http://images.fensemall.com/" + fileName;
            liveBroadCastReq.setPicture(sendImgUrl);
        } else {
            liveBroadCastReq.setPicture(photoPath);
        }
        liveBroadCastReq.setTitle(etTitle.getText().toString());
        liveBroadCastReq.setUrl(sendUrl);
        liveBroadCastReq.setVideo_url(mPushUrl);

        RetrofitClient
                .getInstance()
                .api(API.class)
                .liveBroadCastReq(liveBroadCastReq)
                .enqueue(new Callback<ResponseObj<LoginResp>>() {
                    @Override
                    public void onResponse(Call<ResponseObj<LoginResp>> call, Response<ResponseObj<LoginResp>> response) {
                        if (response.body() == null) {
                            return;
                        }
                        LoginResp resp = response.body().getData();
                        if ("0".equals(response.body().getCode()) && null != resp) {
                            Intent intent = new Intent();
                            intent.setClass(StartLiveActivity.this, LiveActivity.class);
                            intent.putExtra("infoData", baseDetail);
                            StartLiveActivity.this.startActivity(intent);
                            StartLiveActivity.this.finish();
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
     * 退出直播
     */
    private void stopLiveReq() {
        LiveBroadCastReq liveBroadCastReq = new LiveBroadCastReq();

        liveBroadCastReq.setToken(sharedPreferencesUtils.getString("token", ""));
        liveBroadCastReq.setVideo_url("888");
        RetrofitClient
                .getInstance()
                .api(API.class)
                .stopLiveReq(liveBroadCastReq)
                .enqueue(new Callback<ResponseObj<LoginResp>>() {
                    @Override
                    public void onResponse(Call<ResponseObj<LoginResp>> call, Response<ResponseObj<LoginResp>> response) {
                        if ("0".equals(response.body().getCode())) {
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

    /**
     * 获取推流/拉流url
     */
    private void getFlUrl(){
        FluReq fluReq = new FluReq();

        fluReq.setName(roomNum);
        fluReq.setType(0);
        //Headers
        Map<String,String> headers = new HashMap<>();
        headers.put("appKey","");
        headers.put("appSecret","");
        headers.put("nonce","1");
        headers.put("curTime",String.valueOf((new Date()).getTime() / 1000L));
//        headers.put("checkSum",CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime));

        RetrofitClient
                .getInstance("https://vcloud.163.com/app/",headers)
                .api(API.class)
                .getFlUrl(fluReq)
                .enqueue(new Callback<FluResp>() {
                    @Override
                    public void onResponse(Call<FluResp> call, Response<FluResp> response) {
                        if (200==(response.body().getCode())) {
                            FluResp fluResp = response.body();
                            pullUrl = fluResp.getRtmpPullUrl();
                            sendUrl = fluResp.getRtmpPullUrl();
                            mPushUrl = fluResp.getPushUrl();
                        } else {
                            showToast(response.body().getMsg());
                        }
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<FluResp> call, Throwable t) {
                        showToast(getString(R.string.net_err));
                        dismissProgressDialog();
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
//                getRecordUrlReq(pullUrl);
                liveBroadCastReq();
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
//                int beautyCode = mTTTEngine.setBeautyFaceStatus(true, 0.5f, 0.5f);
//                if (0 == beautyCode) {
//                    showToast("美颜开启成功");
//                }
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
                stopLiveReq();
//                finish();
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
        photoPath = cursor.getString(column_index);
        //获取文件名
        fileName = getFileName(photoPath);
        //上传七牛云
        UpLoadUtils.uploadPic(photoPath, fileName);
    }


    /**
     * huoqu
     * 根据文件路径获取文件名
     *
     * @param pathandname
     * @return
     */
    public String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }
}
