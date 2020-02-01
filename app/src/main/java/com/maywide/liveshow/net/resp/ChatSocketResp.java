package com.maywide.liveshow.net.resp;

import java.io.Serializable;

public class ChatSocketResp implements Serializable {

    //昵称
    private String name;
    //用户id
    private String user_id;
    //时间
    private String time;
    //头像
    private String avatar;
    //消息内容
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
