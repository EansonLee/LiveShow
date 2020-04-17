package com.maywide.liveshow.net.resp;

import java.io.Serializable;

/**
 * Created by user on 2018/11/14.
 */

public class LoginResp implements Serializable{

	private String token;
    //主播基本信息
    private baseDetail anchor;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public baseDetail getAnchor() {
        return anchor;
    }

    public void setAnchor(baseDetail anchor) {
        this.anchor = anchor;
    }

    public static class baseDetail implements Serializable{
        //主播ID
        private String id;
        //用户ID
        private long user_id;
        //主播号/房间号
        private String anchor_code;
        //当前直播ID
        private String live_record_id;
        //直播间公告
        private String notice;
        //主播昵称
        private String nickname;
        //直播间名称
        private String live_broadcast_name;
        //主播默认背景图
        private String bg_picture;
        //粉丝数
        private String fans_count;
        //关注数
        private String follow_count;
        //头像
        private String headimgurl;
        //性别 1-男 2-女
        private int sex;
        //推流url
        private String pushUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getAnchor_code() {
            return anchor_code;
        }

        public void setAnchor_code(String anchor_code) {
            this.anchor_code = anchor_code;
        }

        public String getLive_record_id() {
            return live_record_id;
        }

        public void setLive_record_id(String live_record_id) {
            this.live_record_id = live_record_id;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLive_broadcast_name() {
            return live_broadcast_name;
        }

        public void setLive_broadcast_name(String live_broadcast_name) {
            this.live_broadcast_name = live_broadcast_name;
        }

        public String getBg_picture() {
            return bg_picture;
        }

        public void setBg_picture(String bg_picture) {
            this.bg_picture = bg_picture;
        }

        public String getFans_count() {
            return fans_count;
        }

        public void setFans_count(String fans_count) {
            this.fans_count = fans_count;
        }

        public String getFollow_count() {
            return follow_count;
        }

        public void setFollow_count(String follow_count) {
            this.follow_count = follow_count;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getPushUrl() {
            return pushUrl;
        }

        public void setPushUrl(String pushUrl) {
            this.pushUrl = pushUrl;
        }
    }
}
