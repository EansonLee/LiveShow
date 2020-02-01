package com.maywide.liveshow.net.resp;

/**
 * 公告websockete
 */
public class NoticeSocketResp {
    //公告内容
    private String content;
    //重复次数
    private int repeat_count;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRepeat_count() {
        return repeat_count;
    }

    public void setRepeat_count(int repeat_count) {
        this.repeat_count = repeat_count;
    }
}