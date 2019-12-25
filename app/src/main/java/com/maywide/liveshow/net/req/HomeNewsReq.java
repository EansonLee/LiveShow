package com.maywide.liveshow.net.req;

/**
 * Created by heyongbiao-pc on 2018/11/19.
 */

public class HomeNewsReq extends BaseReq {

    private int pageIndex;
    private int pageSize;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
