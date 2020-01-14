package com.maywide.liveshow.net.resp;

/**
 * Created by liyizhen on 2020/1/14.
 */

public class FansResp {
    //粉丝类型
    private String type;
    //粉丝头像
    private String avatar;
    //粉丝名称
    private String name;
    //用户ID
    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
