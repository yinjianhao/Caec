package com.me.caec.bean;

import java.util.List;

/**
 * @auther yjh
 * @date 2016/9/25
 */

public class Distributor {

    private int result;

    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private int dealerId;
        private String scope;
        private String desc;
        private String address;
        private String tel;
        private String name;
        private int score;

        private List<WgsBean> wgs;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDealerId() {
            return dealerId;
        }

        public void setDealerId(int dealerId) {
            this.dealerId = dealerId;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public List<WgsBean> getWgs() {
            return wgs;
        }

        public void setWgs(List<WgsBean> wgs) {
            this.wgs = wgs;
        }

        public static class WgsBean {
            private String lng;
            private String lat;

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }
        }
    }
}
