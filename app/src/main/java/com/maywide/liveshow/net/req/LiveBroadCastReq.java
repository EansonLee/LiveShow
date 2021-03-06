package com.maywide.liveshow.net.req;

/**
 * Created by liyizhen on 2020/1/17.
 */

public class LiveBroadCastReq extends BaseReq{
    //推流URL
    private String url;
    //直播主题
    private String title;
    //直播背景图
    private String picture;
    //录播url
    private String video_url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
