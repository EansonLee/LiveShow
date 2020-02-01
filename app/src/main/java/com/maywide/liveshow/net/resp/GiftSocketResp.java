package com.maywide.liveshow.net.resp;

/**
 * 送礼物
 */
public class GiftSocketResp {

    //礼物id
    private String gift_id;
    //礼物名称
    private String gift_name;
    //礼物图标
    private String icon;
    //礼物数量
    private String count;

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}