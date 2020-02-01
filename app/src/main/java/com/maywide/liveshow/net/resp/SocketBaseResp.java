package com.maywide.liveshow.net.resp;

import java.io.Serializable;

/**
 * websocket返回
 * @param <T>
 */
public class SocketBaseResp<T> implements Serializable {

    //类型
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
