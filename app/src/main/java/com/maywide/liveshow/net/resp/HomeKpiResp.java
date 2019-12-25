package com.maywide.liveshow.net.resp;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 首页指标返回
 */
public class HomeKpiResp implements MultiItemEntity {
    private String model;
    private String rank;
    private String boxId;

    private List<DataKpi> dataList;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public List<DataKpi> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataKpi> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemType() {
        if("1".equals(model)){
            return 1;
        }else {
            return 2;
        }
    }

    public static class DataKpi{
        private String title;
        private String num;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
