package com.maywide.liveshow.net.resp;

public class LiveRecordResp {
    //接口回调
    private String msg;

    //录制的任务id
    private int id;
    // 录制任务当前的状态(1-录制中,2-已停⽌止 (未收到回调数据),3-已停⽌止 (已收到回调数据),5-录制失败,6-定时录制)
    private int status;
    //录播内容
    private Detail callbacks;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Detail getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(Detail callbacks) {
        this.callbacks = callbacks;
    }

    static class Detail{
        //录播地址
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
