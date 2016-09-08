package com.me.caec.bean;

import java.util.List;

/**
 * 订单详情(未付款,已关闭)
 *
 * Created by yin on 2016/9/8.
 */
public class OrderDetailPay {

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
        private String sysTime;
        private String id;
        private String time;
        private String reason;
        private String status;
        private int freight;
        private double pay;
        private PartsBean parts;
        private String endTime;
        private double cost;
        private double discount;

        private List<CarsBean> cars;

        public String getSysTime() {
            return sysTime;
        }

        public void setSysTime(String sysTime) {
            this.sysTime = sysTime;
        }

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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public PartsBean getParts() {
            return parts;
        }

        public void setParts(PartsBean parts) {
            this.parts = parts;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public List<CarsBean> getCars() {
            return cars;
        }

        public void setCars(List<CarsBean> cars) {
            this.cars = cars;
        }

        public static class PartsBean {

            private ReceiptBean receipt;
            private ReceivingBean receiving;
            private String orderId;
            private List<PartInfoBean> data;

            public ReceiptBean getReceipt() {
                return receipt;
            }

            public void setReceipt(ReceiptBean receipt) {
                this.receipt = receipt;
            }

            public ReceivingBean getReceiving() {
                return receiving;
            }

            public void setReceiving(ReceivingBean receiving) {
                this.receiving = receiving;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public List<PartInfoBean> getData() {
                return data;
            }

            public void setData(List<PartInfoBean> data) {
                this.data = data;
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

            public static class ReceivingBean {
                private String zip;
                private String receiver;
                private String address;
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

            public static class PartInfoBean {
                private int id;
                private int pId;
                private double price;
                private double originalPrice;
                private int count;
                private String name;
                private String prop;
                private String img;
                private String url;

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

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getProp() {
                    return prop;
                }

                public void setProp(String prop) {
                    this.prop = prop;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

        public static class CarsBean {
            private int provinceId;
            private int dealerId;
            private String storeName;
            private ReceiverBean receiver;
            private int count;
            private int cityId;
            private String img;
            private String prop;
            private String storeAddress;
            private String dealerName;
            private String type;
            private String storePhone;
            private double cost;
            private String url;
            private int id;
            private int pId;
            private double price;
            private double originalPrice;
            private String name;
            private double pay;
            private int storeId;

            public int getProvinceId() {
                return provinceId;
            }

            public void setProvinceId(int provinceId) {
                this.provinceId = provinceId;
            }

            public int getDealerId() {
                return dealerId;
            }

            public void setDealerId(int dealerId) {
                this.dealerId = dealerId;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public ReceiverBean getReceiver() {
                return receiver;
            }

            public void setReceiver(ReceiverBean receiver) {
                this.receiver = receiver;
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

            public String getStoreAddress() {
                return storeAddress;
            }

            public void setStoreAddress(String storeAddress) {
                this.storeAddress = storeAddress;
            }

            public String getDealerName() {
                return dealerName;
            }

            public void setDealerName(String dealerName) {
                this.dealerName = dealerName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getStorePhone() {
                return storePhone;
            }

            public void setStorePhone(String storePhone) {
                this.storePhone = storePhone;
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

            public int getStoreId() {
                return storeId;
            }

            public void setStoreId(int storeId) {
                this.storeId = storeId;
            }

            public static class ReceiverBean {
                private String zip;
                private String receiver;
                private String address;
                private String no;
                private String name;
                private int type;
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

                public int getType() {
                    return type;
                }

                public void setType(int type) {
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
        }
    }
}
