package com.me.caec.bean;

import java.util.List;

/**
 * 购物车列表
 * <p/>
 * Created by yin on 2016/9/12.
 */
public class CartList {


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

    public static class DataBean {
        private String dealerId;
        private String storeName;
        private int count;
        private int status;
        private String img;
        private String prop;
        private String dealerName;
        private String type;
        private String url;
        private String id;
        private int pId;
        private int stock;
        private float price;
        private float originalPrice;
        private String cartItemId;
        private String name;
        private String storeId;
        private double opt;
        private List<?> optionalInfo;

        public String getDealerId() {
            return dealerId;
        }

        public void setDealerId(String dealerId) {
            this.dealerId = dealerId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPId() {
            return pId;
        }

        public void setPId(int pId) {
            this.pId = pId;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(float originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getCartItemId() {
            return cartItemId;
        }

        public void setCartItemId(String cartItemId) {
            this.cartItemId = cartItemId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public double getOpt() {
            return opt;
        }

        public void setOpt(double opt) {
            this.opt = opt;
        }

        public List<?> getOptionalInfo() {
            return optionalInfo;
        }

        public void setOptionalInfo(List<?> optionalInfo) {
            this.optionalInfo = optionalInfo;
        }
    }
}
