package com.maywide.liveshow.net.req;

/**
 * Created by liyizhen on 2020/1/20.
 */

public class UpGradeReq extends BaseReq{
    //粉丝用户ID
    private String setted_user_id;
    //0-设置房管，1-取消房管
    private int is_cancel;

    public String getSetted_user_id() {
        return setted_user_id;
    }

    public void setSetted_user_id(String setted_user_id) {
        this.setted_user_id = setted_user_id;
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }
}
