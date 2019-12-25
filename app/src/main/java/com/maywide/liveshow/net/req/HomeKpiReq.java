package com.maywide.liveshow.net.req;

public class HomeKpiReq extends BaseReq {
    //1常用指标 2其他指标
    private String indexType;

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }
}
