package com.maywide.liveshow.net.req;

/**
 */
public class DetailBoxKpiReq extends BaseReq {

    private String city;
    private String boxId;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }
}
