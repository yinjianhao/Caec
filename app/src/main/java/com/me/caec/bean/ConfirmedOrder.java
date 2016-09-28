package com.me.caec.bean;

/**
 * 提交订单(生成订单)
 *
 * @auther yjh
 * @date 2016/9/28
 */

public class ConfirmedOrder {

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
        private float pay;
        private String orderId;

        public float getPay() {
            return pay;
        }

        public void setPay(float pay) {
            this.pay = pay;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
