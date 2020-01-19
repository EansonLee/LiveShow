package com.maywide.liveshow.net.req;

/**
 * Created by liyizhen on 2020/1/19.
 */

public class LinkPerReq extends BaseReq{
    //页码
    private int page;
    //每页数据量
    private int pageSize;

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
}
