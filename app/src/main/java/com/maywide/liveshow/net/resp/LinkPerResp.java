package com.maywide.liveshow.net.resp;

/**
 * 房管或粉丝列表返回数据
 * Created by liyizhen on 2020/1/19.
 */

public class LinkPerResp extends ResponseList{
    //粉丝类型
    private int type;
    //粉丝头像
    private String avatar;
    //粉丝名称
    private String name;
    //用户ID
    private String id;
    //


    public int getType() {
        return type;
    }

    public void setType(int type) {
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
