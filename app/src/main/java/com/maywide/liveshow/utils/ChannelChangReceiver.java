package com.maywide.liveshow.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.maywide.liveshow.adapter.TalkAdapter;
import com.maywide.liveshow.net.resp.ChatSocketResp;
import com.maywide.liveshow.net.resp.SocketBaseResp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * socket长链接测试
 * Created by liyizhen on 2019/12/27.
 */

public class ChannelChangReceiver extends BroadcastReceiver {

    private TalkAdapter talkAdapter;
    private List<ChatSocketResp> chatList = new ArrayList<>();

    public ChannelChangReceiver() {

    }

//    public ChannelChangReceiver(List<ChatSocketResp> chatList, TalkAdapter talkAdapter) {
//        this.chatList = chatList;
//        this.talkAdapter = talkAdapter;
//    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    String chatData = msg.getData().getString("chatData");
//                    ChatSocketResp chatResp = new Gson().fromJson(chatData, ChatSocketResp.class);
//                    chatList.add(chatResp);
//                    talkAdapter.notifyDataSetChanged();
//                    break;
//                case 1:
//
//                    break;
//            }
//        }
//    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("message");
        Log.e("---", data);
//        String type = null;
//        String chatData = null;
//        try {
//            JSONObject jsonObject = new JSONObject(data);
//            type = jsonObject.getString("type");
//            chatData = jsonObject.getString("data");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (!TextUtils.isEmpty(type)) {
//            switch (type) {
//                case "chat":
//                    Message msg = handler.obtainMessage();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("chatData", chatData);
//                    msg.what = 0;
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);
//                    break;
//                case "ping":
//                    handler.sendEmptyMessage(1);
//                    break;
//            }
//        }
    }
}
