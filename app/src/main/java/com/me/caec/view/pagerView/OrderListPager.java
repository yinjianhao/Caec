package com.me.caec.view.pagerView;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.me.caec.R;
import com.me.caec.bean.OrderList;
import com.me.caec.globle.Client;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.OrderUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Arrays;
import java.util.List;

/**
 * 订单列表
 * <p/>
 * Created by yin on 2016/9/5.
 */
public class OrderListPager {

    //全部
    public static final int TYPE_ALL = 0;
    //未付款
    public static final int TYPE_UNPAID = 1;
    //待提车
    public static final int TYPE_UNCAR = 2;
    //待收货
    public static final int TYPE_UNRECEIVED = 3;
    //待评价
    public static final int TYPE_UNCOMMENT = 4;
    //售后
    public static final int TYPE_SERVICE = 5;

    private Activity activity;

    private View rootView;

    //订单类型
    private int type;

    private SwipeRefreshLayout srlOrderList;

    private ListView lvOrderList;

    private Adapter adapter;

    //订单数据
    private List<OrderList.DataBean> orderListData;

    /**
     * @param activity 所属活动
     * @param type     订单类型
     */
    public OrderListPager(Activity activity, int type) {
        this.activity = activity;
        this.type = type;
        rootView = initView();
    }

    public View initView() {
        return View.inflate(activity, R.layout.pager_order_list, null);
    }

    public void initData() {
        srlOrderList = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_order_list);
        lvOrderList = (ListView) rootView.findViewById(R.id.lv_order_list);

        getOrderList();
    }

    public View getRootView() {
        return rootView;
    }

    /**
     *
     */
    private void getOrderList() {
        RequestParams params = new RequestParams(Client.ORDER_LIST_URL);
        params.addBodyParameter("token", PreferencesUtils.getString(activity, "token", ""));
        params.addBodyParameter("pageSize", "20");
        params.addBodyParameter("pageIndex", "1");
        params.addBodyParameter("status", Arrays.toString(type2status()));

        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                OrderList orderList = JSON.parseObject(string, OrderList.class);
                if (orderList.getResult() == 0) {
                    orderListData = orderList.getData();
                    if (adapter == null) {
                        adapter = new Adapter();
                        lvOrderList.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(activity, "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(activity, "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 订单类型转化为请求数据的订单状态
     *
     * @return
     */
    private String[] type2status() {
        String[] status;
        switch (type) {
            case TYPE_UNPAID:
                status = new String[]{"01"};
                break;
            case TYPE_UNCAR:
                status = new String[]{"07", "10"};
                break;
            case TYPE_UNRECEIVED:
                status = new String[]{"30"};
                break;
            case TYPE_UNCOMMENT:
                status = new String[]{"26"};
                break;
            case TYPE_SERVICE:
                status = new String[]{"09", "11"};
                break;
            case TYPE_ALL:
            default:
                status = new String[]{};
                break;
        }

        return status;
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderListData.size();
        }

        @Override
        public OrderList.DataBean getItem(int position) {
            return orderListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(activity, R.layout.listview_item_order_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //订单逻辑
            OrderList.DataBean dataBean = getItem(position);
            String status = dataBean.getStatus();

            viewHolder.tvTime.setText(dataBean.getTime());

            //未付款或已关闭(此时未拆单)
            if (status.equals("01") || status.equals("23")) {
                viewHolder.tvStatus.setText(OrderUtils.status2StatusName(status));
                viewHolder.tvStatus.setTextColor(OrderUtils.status2TextColor(status));
            } else {
                List<OrderList.DataBean.SubOrdersBean> subOrdersBean = dataBean.getSubOrders(); //子订单

            }

            viewHolder.tvStatus.setText(OrderUtils.status2StatusName(dataBean.getStatus()));
            viewHolder.tvStatus.setTextColor(OrderUtils.status2TextColor(dataBean.getStatus()));

            return convertView;
        }
    }

    private class ViewHolder {

        public TextView tvTime;
        public TextView tvStatus;
        public LinearLayout llGoods;
        public TextView tvName;
        public TextView tvGoodsCount;
        public TextView tvTotalPaid;
        public TextView tvRealPaid;
        public Button btnBuyAgain;
        public Button btnComment;
        public Button btnCancel;
        public Button btnPay;
        public Button btnConfirm;

        public ViewHolder(View view) {
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
            llGoods = (LinearLayout) view.findViewById(R.id.ll_goods);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvGoodsCount = (TextView) view.findViewById(R.id.tv_goods_count);
            tvTotalPaid = (TextView) view.findViewById(R.id.tv_total_paid);
            tvRealPaid = (TextView) view.findViewById(R.id.tv_real_paid);
            btnBuyAgain = (Button) view.findViewById(R.id.btn_buy_again);
            btnComment = (Button) view.findViewById(R.id.btn_comment);
            btnCancel = (Button) view.findViewById(R.id.btn_cancel);
            btnPay = (Button) view.findViewById(R.id.btn_pay);
            btnConfirm = (Button) view.findViewById(R.id.btn_confirm);

            btnBuyAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
