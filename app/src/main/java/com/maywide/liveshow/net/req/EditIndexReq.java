package com.maywide.liveshow.net.req;

/**
 *
 */
public class EditIndexReq extends BaseReq{
    //常用box ID字符串，多个用逗号隔开
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
