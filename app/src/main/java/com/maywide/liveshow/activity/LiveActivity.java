package com.maywide.liveshow.activity;

import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.maywide.liveshow.Handler.MyTTTRtcEngineEventHandler;
import com.maywide.liveshow.R;
import com.maywide.liveshow.base.BaseAcitivity;
import com.maywide.liveshow.base.MyApplication;
import com.maywide.liveshow.utils.EnterLiveRoomReceiver;
import com.maywide.liveshow.utils.LiveShowReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播界面
 */
public class LiveActivity extends BaseAcitivity implements View.OnClickListener {
    //直播界面
    @BindView(R.id.ly_live)
    RelativeLayout lyLive;
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

    private LiveShowReceiver liveShowReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //动态弹起布局
        addLayoutListener(lyLive, lyBottom);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

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
            //拍照
            case R.id.iv_photo:

                break;
            //分享
            case R.id.iv_share:

                break;
            //美颜
            case R.id.iv_beauty:

                break;
            case R.id.iv_close:
                finish();
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
}
