package com.me.caec.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 确认订单列表
 * <p>
 * Created by yin on 2016/9/23.
 */

public class ConfirmOrder {

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

    public static class DataBean implements Serializable{

        private ReceiptBean receipt;

        private UserBean user;

        private List<CarsBean> cars;

        private List<PartsBean> parts;

        private List<CouponBean> coupon;

        private List<ReceivingBean> receiving;

        public ReceiptBean getReceipt() {
            return receipt;
        }

        public void setReceipt(ReceiptBean receipt) {
            this.receipt = receipt;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<CarsBean> getCars() {
            return cars;
        }

        public void setCars(List<CarsBean> cars) {
            this.cars = cars;
        }

        public List<PartsBean> getParts() {
            return parts;
        }

        public void setParts(List<PartsBean> parts) {
            this.parts = parts;
        }

        public List<CouponBean> getCoupon() {
            return coupon;
        }

        public void setCoupon(List<CouponBean> coupon) {
            this.coupon = coupon;
        }

        public List<ReceivingBean> getReceiving() {
            return receiving;
        }

        public void setReceiving(List<ReceivingBean> receiving) {
            this.receiving = receiving;
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

        public static class UserBean {
            private String nickName;
            private String mobile;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }

        public static class CarsBean {
            private String optionalInfo;
            private int id;
            private float price;
            private int count;
            private float originalPrice;
            private String name;
            private String prop;
            private String img;
            private float pay;
            private String url;

            public String getOptionalInfo() {
                return optionalInfo;
            }

            public void setOptionalInfo(String optionalInfo) {
                this.optionalInfo = optionalInfo;
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

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public float getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(float originalPrice) {
                this.originalPrice = originalPrice;
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

            public float getPay() {
                return pay;
            }

            public void setPay(float pay) {
                this.pay = pay;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class PartsBean {
            private int id;
            private float price;
            private int count;
            private float originalPrice;
            private int carriage;
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

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public float getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(float originalPrice) {
                this.originalPrice = originalPrice;
            }

            public int getCarriage() {
                return carriage;
            }

            public void setCarriage(int carriage) {
                this.carriage = carriage;
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

        public static class CouponBean implements Serializable {
            private String typeName;
            private String id;
            private String startDate;
            private String title;
            private String desc;
            private float price;
            private String rule;
            private boolean enable;
            private String endDate;
            private String code;
            private String type;

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }

            public boolean isEnable() {
                return enable;
            }

            public void setEnable(boolean enable) {
                this.enable = enable;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
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
        }

        public static class ReceivingBean {
            private int provinceId;
            private String id;
            private String zip;
            private String areaName;
            private String receiver;
            private int cityId;
            private String address;
            private String cityName;
            private String provinceName;
            private String isDeafault;
            private int areaId;
            private String mobile;

            public int getProvinceId() {
                return provinceId;
            }

            public void setProvinceId(int provinceId) {
                this.provinceId = provinceId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }

            public int getCityId() {
                return cityId;
            }

            public void setCityId(int cityId) {
                this.cityId = cityId;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getProvinceName() {
                return provinceName;
            }

            public void setProvinceName(String provinceName) {
                this.provinceName = provinceName;
            }

            public String getIsDeafault() {
                return isDeafault;
            }

            public void setIsDeafault(String isDeafault) {
                this.isDeafault = isDeafault;
            }

            public int getAreaId() {
                return areaId;
            }

            public void setAreaId(int areaId) {
                this.areaId = areaId;
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
