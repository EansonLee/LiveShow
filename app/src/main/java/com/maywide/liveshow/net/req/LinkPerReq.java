package com.maywide.liveshow.net.req;

/**
 * Created by liyizhen on 2020/1/19.
 */

public class LinkPerReq extends BaseReq{
    //页码
    private int page;
    //每页数据量
    private int pageSize;
    //type 1-房管 0-粉丝
    private String type;
    //如果是主播就不用传，否则必填(主播号)
    private String anchor_code;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnchor_code() {
        return anchor_code;
    }

    public void setAnchor_code(String anchor_code) {
        this.anchor_code = anchor_code;
    }
}
