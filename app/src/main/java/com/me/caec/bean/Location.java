package com.me.caec.bean;

import java.util.List;

/**
 * 省市区
 *
 * @auther yjh
 * @date 2016/9/1
 */
public class Location {

    private String result;

    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int areaid;
        private String areaname;

        private List<CityBean> al;

        public int getAreaid() {
            return areaid;
        }

        public void setAreaid(int areaid) {
            this.areaid = areaid;
        }

        public String getAreaname() {
            return areaname;
        }

        public void setAreaname(String areaname) {
            this.areaname = areaname;
        }

        public List<CityBean> getAl() {
            return al;
        }

        public void setAl(List<CityBean> al) {
            this.al = al;
        }

        public static class CityBean {
            private int areaid;
            private String areaname;

            private List<AreaBean> area;

            public int getAreaid() {
                return areaid;
            }

            public void setAreaid(int areaid) {
                this.areaid = areaid;
            }

            public String getAreaname() {
                return areaname;
            }

            public void setAreaname(String areaname) {
                this.areaname = areaname;
            }

            public List<AreaBean> getAl() {
                return area;
            }

            public void setAl(List<AreaBean> al) {
                this.area = al;
            }

            public static class AreaBean {
                private int areaid;
                private String areaname;

                public int getAreaid() {
                    return areaid;
                }

                public void setAreaid(int areaid) {
                    this.areaid = areaid;
                }

                public String getAreaname() {
                    return areaname;
                }

                public void setAreaname(String areaname) {
                    this.areaname = areaname;
                }
            }
        }
    }
}
