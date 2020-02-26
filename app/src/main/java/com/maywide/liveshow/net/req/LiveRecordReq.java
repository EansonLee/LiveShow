package com.maywide.liveshow.net.req;

public class LiveRecordReq extends BaseReq{

    //要录制的直播流的拉流 URL
    private String url;
    //开始录制 start 或结束录制 stop
    private String action;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
