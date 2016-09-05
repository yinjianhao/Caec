package com.me.caec.view;

import com.me.caec.R;

import java.util.Arrays;

/**
 * 订单相关操作
 * <p/>
 * Created by yin on 2016/9/5.
 */
public class OrderUtils {

    /**
     * 状态转换成对应的名字
     *
     * @param status
     * @return
     */
    public static String status2StatusName(String status) {
        String statusName = "";

        switch (status) {
            case "01":
                statusName = "待付款";
                break;
            case "02":
                statusName = "已支付";
                break;
            case "03":
                statusName = "生产中";
                break;
            case "04":
                statusName = "生产已完成";
                break;
            case "06":
                statusName = "已出库";
                break;
            case "07":
                statusName = "待提车";
                break;
            case "09":
                statusName = "待审核";
                break;
            case "10":
                statusName = "取消审核未通过";
                break;
            case "11":
                statusName = "已取消";
                break;
            case "23":
                statusName = "已关闭";
                break;
            case "26":
                statusName = "交易完成";
                break;
            case "30":
                statusName = "已发货";
                break;
        }
        return statusName;
    }

    /**
     * 根据状态获取文字颜色
     * @param status
     * @return
     */
    public static int status2TextColor(String status) {
        int color = R.color.baseRed;

        String[] s = new String[]{"11", "23", "26"};
        if (Arrays.binarySearch(s, status) > 0) {
            color = R.color.color333;
        }

        return color;
    }
}
