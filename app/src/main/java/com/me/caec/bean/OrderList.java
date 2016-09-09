package com.me.caec.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单列表
 * <p/>
 * Created by yin on 2016/9/5.
 */
public class OrderList {

    private int total;
    private int result;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

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

    public static class DataBean implements Serializable {
        private String id;
        private String time;
        private String status;
        private String payType;
        private float cost;

        private List<SubOrdersBean> subOrders;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public float getCost() {
            return cost;
        }

        public void setCost(float cost) {
            this.cost = cost;
        }

        public List<SubOrdersBean> getSubOrders() {
            return subOrders;
        }

        public void setSubOrders(List<SubOrdersBean> subOrders) {
            this.subOrders = subOrders;
        }

        public static class SubOrdersBean implements Serializable {
            private String receiver;
            private String status;
            private String returntime;
            private String type;
            private float cost;
            private String id;
            private String sysTime;
            private String time;
            private String payStatus;
            private String rtime;
            private int receiptStatus;
            private int day;
            private String producttime;
            private String receiverTime;

            private List<GoodsBean> goods;

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getReturntime() {
                return returntime;
            }

            public void setReturntime(String returntime) {
                this.returntime = returntime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public float getCost() {
                return cost;
            }

            public void setCost(float cost) {
                this.cost = cost;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSysTime() {
                return sysTime;
            }

            public void setSysTime(String sysTime) {
                this.sysTime = sysTime;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getPayStatus() {
                return payStatus;
            }

            public void setPayStatus(String payStatus) {
                this.payStatus = payStatus;
            }

            public String getRtime() {
                return rtime;
            }

            public void setRtime(String rtime) {
                this.rtime = rtime;
            }

            public int getReceiptStatus() {
                return receiptStatus;
            }

            public void setReceiptStatus(int receiptStatus) {
                this.receiptStatus = receiptStatus;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public String getProducttime() {
                return producttime;
            }

            public void setProducttime(String producttime) {
                this.producttime = producttime;
            }

            public String getReceiverTime() {
                return receiverTime;
            }

            public void setReceiverTime(String receiverTime) {
                this.receiverTime = receiverTime;
            }

            public List<GoodsBean> getGoods() {
                return goods;
            }

            public void setGoods(List<GoodsBean> goods) {
                this.goods = goods;
            }

            public static class GoodsBean implements Serializable {
                private String dealerId;
                private int count;
                private String img;
                private String prop;
                private String dealerName;
                private String url;
                private int id;
                private float price;
                private String isAssess;
                private String name;

                public String getDealerId() {
                    return dealerId;
                }

                public void setDealerId(String dealerId) {
                    this.dealerId = dealerId;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getProp() {
                    return prop;
                }

                public void setProp(String prop) {
                    this.prop = prop;
                }

                public String getDealerName() {
                    return dealerName;
                }

                public void setDealerName(String dealerName) {
                    this.dealerName = dealerName;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public float getPrice() {
                    return price;
                }

                public void setPrice(float price) {
                    this.price = price;
                }

                public String getIsAssess() {
                    return isAssess;
                }

                public void setIsAssess(String isAssess) {
                    this.isAssess = isAssess;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
