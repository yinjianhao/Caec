package com.me.caec.bean;

/**
 * rsa模和指数
 *
 * Created by yin on 2016/9/13.
 */
public class RSA {

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
        private String exp;
        private String mod;

        public String getExp() {
            return exp;
        }

        public void setExp(String exp) {
            this.exp = exp;
        }

        public String getMod() {
            return mod;
        }

        public void setMod(String mod) {
            this.mod = mod;
        }
    }
}
