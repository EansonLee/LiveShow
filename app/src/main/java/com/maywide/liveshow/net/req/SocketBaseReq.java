package com.maywide.liveshow.net.req;

import java.io.Serializable;

public class SocketBaseReq<T> implements Serializable {
    //类型判断
    private String type;
    //具体数据
    private T data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
