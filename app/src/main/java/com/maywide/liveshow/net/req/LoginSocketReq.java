package com.maywide.liveshow.net.req;

/**
 * 登录
 */
public class LoginSocketReq {
    //直播号
    private String anchor_code;
    //用户id
    private long user_id;
    //用户名
    private String name;
    //头像
    private String avatar;
    //是否主播 1-主播（否则不传）
    private int is_anchor;

    public String getAnchor_code() {
        return anchor_code;
    }

    public void setAnchor_code(String anchor_code) {
        this.anchor_code = anchor_code;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIs_anchor() {
        return is_anchor;
    }

    public void setIs_anchor(int is_anchor) {
        this.is_anchor = is_anchor;
    }
}
