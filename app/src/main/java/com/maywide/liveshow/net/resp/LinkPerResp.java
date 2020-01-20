package com.maywide.liveshow.net.resp;

import java.util.List;

/**
 * 房管或粉丝列表返回数据
 * Created by liyizhen on 2020/1/19.
 */

public class LinkPerResp extends ResponseObj {
    //数据总数
    private int count;
    //总页数
    private int pageCount;
    //每页数据量
    private int pageSize;
    //当前页数据量
    private int currentCount;
    //当前页数
    private int page;

    private List<perDetail>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<perDetail> getList() {
        return list;
    }

    public void setList(List<perDetail> list) {
        this.list = list;
    }

    public class perDetail {

        //粉丝头像
        private String avatar;
        //粉丝名称
        private String name;
        //用户ID
        private String id;

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
}
