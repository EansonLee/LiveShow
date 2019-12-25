package com.maywide.liveshow.net.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heyongbiao-pc on 2018/11/19.
 */

public class HomeNewsResp extends ResponseList implements Serializable {

    private int pageIndex;
    private int pageSize;
    //总数
    private int count;
    public List<NewsDetail> pageList;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getConut() {
        return count;
    }

    public void setConut(int conut) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<NewsDetail> getPageList() {
        return pageList;
    }

    public void setPageList(List<NewsDetail> pageList) {
        this.pageList = pageList;
    }

    public static class NewsDetail implements Serializable {

        private String title;
        private String author;
        private String publishDate;
        private String imgUrl;
        private String pageUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }
    }
}
