package com.maywide.liveshow.net.resp;

import java.util.HashMap;
import java.util.List;

public class DetailBoxKpiResp {

    private String staDate;

    private List<Title> titleList;

    private List<HashMap<String,String>> dataList;

    public String getStaDate() {
        return staDate;
    }

    public void setStaDate(String staDate) {
        this.staDate = staDate;
    }

    public List<Title> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<Title> titleList) {
        this.titleList = titleList;
    }

    public List<HashMap<String,String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<HashMap<String,String>> dataList) {
        this.dataList = dataList;
    }

    public static class Title {
        private String titleName;
        private String id;

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
