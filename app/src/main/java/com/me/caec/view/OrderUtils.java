package com.me.caec.view;

import com.me.caec.R;
import com.me.caec.bean.OrderList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     *
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

    /**
     * 除了未付款和已关闭订单(未拆分),其他状态的订单需要根据子订单拆分显示
     * 遍历订单数据,取出符合要求的子订单,组成新的订单数据(没个订单下只会有一个子订单了)
     *
     * @param dataBean
     * @return
     */
    public static List<OrderList.DataBean> processOrderListData(List<OrderList.DataBean> dataBean) {

        //遍历主订单
        for (int i = 0, l = dataBean.size(); i < l; i++) {
            OrderList.DataBean order = dataBean.get(i);
            //未付款和已关闭,跳过
            if (order.getStatus().equals("01") || order.getStatus().equals("23")) {
                continue;
            }

            //新建主订单,将子订单拼接起来
            List<OrderList.DataBean.SubOrdersBean> subOrders = order.getSubOrders();

            if (subOrders.size() > 1) {
                int currentI = i;

                for (OrderList.DataBean.SubOrdersBean subOrder : subOrders) {
                    OrderList.DataBean newOrder = new OrderList.DataBean();
                    newOrder.setCost(order.getCost());
                    newOrder.setId(order.getId());
                    newOrder.setPayType(order.getPayType());
                    newOrder.setStatus(order.getStatus());
                    newOrder.setTime(order.getTime());

                    List<OrderList.DataBean.SubOrdersBean> newSubOrders = new ArrayList<>();
                    newSubOrders.add(subOrder);
                    newOrder.setSubOrders(newSubOrders);


                    //将新增订单按顺序加入列表当中
                    i++;
                    dataBean.add(i, newOrder);
                }

                //删除原有订单
                dataBean.remove(currentI);
                i--;
            }
        }

        return dataBean;
    }
}
