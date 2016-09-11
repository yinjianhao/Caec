package com.me.caec.bean;

import java.util.List;

/**
 * 订单详情
 *
 * Created by yin on 2016/9/8.
 */
public class OrderDetail {

    private int result;
    private DataBean data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private ReceiverBean receiver;
        private String status;
        private ReceiptBean receipt;
        private String type;
        private double cost;
        private String payTime;
        private double discount;
        private String id;
        private String consignor;
        private String time;
        private String isAssess;
        private int freight;
        private double pay;
        private String payType;
        private String delivery;
        private LogisticsBean logistics;
        private List<GoodsBean> goods;

        public ReceiverBean getReceiver() {
            return receiver;
        }

        public void setReceiver(ReceiverBean receiver) {
            this.receiver = receiver;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ReceiptBean getReceipt() {
            return receipt;
        }

        public void setReceipt(ReceiptBean receipt) {
            this.receipt = receipt;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getConsignor() {
            return consignor;
        }

        public void setConsignor(String consignor) {
            this.consignor = consignor;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIsAssess() {
            return isAssess;
        }

        public void setIsAssess(String isAssess) {
            this.isAssess = isAssess;
        }

        public int getFreight() {
            return freight;
        }

        public void setFreight(int freight) {
            this.freight = freight;
        }

        public double getPay() {
            return pay;
        }

        public void setPay(double pay) {
            this.pay = pay;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public LogisticsBean getLogistics() {
            return logistics;
        }

        public void setLogistics(LogisticsBean logistics) {
            this.logistics = logistics;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class ReceiverBean {
            private String zip;
            private String receiver;
            private String address;
            private String no;
            private String name;
            private String type;
            private String orderMsg;
            private String mobile;

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getOrderMsg() {
                return orderMsg;
            }

            public void setOrderMsg(String orderMsg) {
                this.orderMsg = orderMsg;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }

        public static class ReceiptBean {
            private String company;
            private String type;
            private String header;

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getHeader() {
                return header;
            }

            public void setHeader(String header) {
                this.header = header;
            }
        }

        public static class LogisticsBean {
            private String number;
            private String express;
            private List<ListBean> list;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getExpress() {
                return express;
            }

            public void setExpress(String express) {
                this.express = express;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                private String time;
                private String operator;
                private String info;

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getOperator() {
                    return operator;
                }

                public void setOperator(String operator) {
                    this.operator = operator;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }
            }
        }

        public static class GoodsBean {
            private int provinceId;
            private String dealerId;
            private int count;
            private int cityId;
            private String img;
            private String prop;
            private String dealerName;
            private String code;
            private String type;
            private double cost;
            private String url;
            private int id;
            private int pId;
            private double price;
            private double originalPrice;
            private String isAssess;
            private String name;
            private double pay;

            public int getProvinceId() {
                return provinceId;
            }

            public void setProvinceId(int provinceId) {
                this.provinceId = provinceId;
            }

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

            public int getCityId() {
                return cityId;
            }

            public void setCityId(int cityId) {
                this.cityId = cityId;
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

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public double getCost() {
                return cost;
            }

            public void setCost(double cost) {
                this.cost = cost;
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

            public int getPId() {
                return pId;
            }

            public void setPId(int pId) {
                this.pId = pId;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(double originalPrice) {
                this.originalPrice = originalPrice;
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

            public double getPay() {
                return pay;
            }

            public void setPay(double pay) {
                this.pay = pay;
            }
        }
    }
}
