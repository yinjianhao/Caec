package com.me.caec.view.pagerView;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.me.caec.R;
import com.me.caec.bean.OrderList;
import com.me.caec.globle.Client;
import com.me.caec.utils.DpTransforUtils;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.OrderUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 订单列表
 * <p>
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

    //转化2位小数
    private DecimalFormat decimalFormat;

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
        decimalFormat = new DecimalFormat("#.00");
    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 请求订单数据
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

                    //处理订单数据
                    orderListData = OrderUtils.processOrderListData(orderListData);

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
            List<ImageView> imageViews = new ArrayList<>();  //保存商品的图片view
            Boolean hasCustomCar = false;  //订单中是否有定制车
            Boolean isComment = true;     //是否已经评论

            OrderList.DataBean dataBean = getItem(position);
            String status = dataBean.getStatus();

            viewHolder.tvTime.setText(dataBean.getTime());

            //未付款或已关闭(此时未拆单)
            if (status.equals("01") || status.equals("23")) {
                viewHolder.tvStatus.setText(OrderUtils.status2StatusName(status));
                viewHolder.tvStatus.setTextColor(ContextCompat.getColor(
                        activity, OrderUtils.status2TextColor(status)));

                viewHolder.tvTotalPaid.setText("合计:¥" + decimalFormat.format(dataBean.getCost()));
                viewHolder.tvRealPaid.setText("实付:¥" + decimalFormat.format(dataBean.getCost()));

                List<OrderList.DataBean.SubOrdersBean> subOrders = dataBean.getSubOrders();
                for (int i = 0, l = subOrders.size(); i < l; i++) {
                    OrderList.DataBean.SubOrdersBean subOrder = subOrders.get(i);

                    if (subOrder.getType().equals("3")) {  //1精品 2整车 3定制车
                        hasCustomCar = true;
                    }

                    List<OrderList.DataBean.SubOrdersBean.GoodsBean> goods = subOrder.getGoods();
                    for (OrderList.DataBean.SubOrdersBean.GoodsBean good : goods) {

                        ImageView iv = new ImageView(activity);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                DpTransforUtils.dp2px(activity, 60), DpTransforUtils.dp2px(activity, 60));
                        params.setMargins(0, 0, DpTransforUtils.dp2px(activity, 10), 0);
                        iv.setLayoutParams(params);
                        iv.setBackgroundResource(R.drawable.border_image);

                        ImageOptions.Builder builder = new ImageOptions.Builder();
                        builder.setLoadingDrawableId(R.drawable.placeholder_200_200);
                        ImageOptions options = builder.build();
                        x.image().bind(iv, good.getImg(), options);
                        imageViews.add(iv);
                    }
                }

                //大于一个
                if (imageViews.size() > 1) {
                    viewHolder.llGoods.removeAllViews();
                    for (int i = 0, l = imageViews.size(); i < l; i++) {
                        viewHolder.llGoods.addView(imageViews.get(i));
                    }
                    viewHolder.llGoodOne.setVisibility(View.GONE);
                    viewHolder.svGoods.setVisibility(View.VISIBLE);
                } else {
                    OrderList.DataBean.SubOrdersBean.GoodsBean goodOne = dataBean.getSubOrders().get(0).getGoods().get(0);
                    viewHolder.tvName.setText(goodOne.getName());
                    ImageOptions.Builder builder = new ImageOptions.Builder();
                    builder.setLoadingDrawableId(R.drawable.placeholder_200_200);
                    ImageOptions options = builder.build();
                    x.image().bind(viewHolder.ivGoodOne, goodOne.getImg(), options);
                    viewHolder.llGoodOne.setVisibility(View.VISIBLE);
                    viewHolder.svGoods.setVisibility(View.GONE);
                }

                viewHolder.tvGoodsCount.setText("总共" + imageViews.size() + "件商品");

                //未付款, 取消订单和去付款
                if (status.equals("01")) {
                    viewHolder.btnCancel.setVisibility(View.VISIBLE);
                    viewHolder.btnPay.setVisibility(View.VISIBLE);
                    viewHolder.btnBuyAgain.setVisibility(View.GONE);
                    viewHolder.btnComment.setVisibility(View.GONE);
                    viewHolder.btnConfirm.setVisibility(View.GONE);
                } else {
                    //已关闭, 再次购买(无定制车时)
                    if (hasCustomCar) {
                        viewHolder.btnBuyAgain.setVisibility(View.GONE);
                    } else {
                        viewHolder.btnBuyAgain.setVisibility(View.VISIBLE);
                    }
                    viewHolder.btnPay.setVisibility(View.GONE);
                    viewHolder.btnCancel.setVisibility(View.GONE);
                    viewHolder.btnComment.setVisibility(View.GONE);
                    viewHolder.btnConfirm.setVisibility(View.GONE);
                }
            } else {
                //其他状态,订单已拆分
                //数据已处理,每个订单中只有一个子订单
                OrderList.DataBean.SubOrdersBean subOrder = dataBean.getSubOrders().get(0);

                if (subOrder.getType().equals("3")) {  //1精品 2整车 3定制车
                    hasCustomCar = true;
                }

                viewHolder.tvStatus.setText(OrderUtils.status2StatusName(subOrder.getStatus()));
                viewHolder.tvStatus.setTextColor(ContextCompat.getColor(
                        activity, OrderUtils.status2TextColor(subOrder.getStatus())));
                viewHolder.tvTotalPaid.setText("合计:¥" + decimalFormat.format(subOrder.getCost()));
                viewHolder.tvRealPaid.setText("实付:¥" + decimalFormat.format(subOrder.getCost()));

                List<OrderList.DataBean.SubOrdersBean.GoodsBean> goods = subOrder.getGoods();


                for (OrderList.DataBean.SubOrdersBean.GoodsBean good : goods) {

                    if (isComment && good.getIsAssess().equals("N")) {   //没有评论时
                        isComment = false;
                    }

                    ImageView iv = new ImageView(activity);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            DpTransforUtils.dp2px(activity, 60), DpTransforUtils.dp2px(activity, 60));
                    params.setMargins(0, 0, DpTransforUtils.dp2px(activity, 10), 0);
                    iv.setLayoutParams(params);
                    iv.setBackgroundResource(R.drawable.border_image);

                    ImageOptions.Builder builder = new ImageOptions.Builder();
                    builder.setLoadingDrawableId(R.drawable.placeholder_200_200);
                    ImageOptions options = builder.build();
                    x.image().bind(iv, good.getImg(), options);
                    imageViews.add(iv);
                }

                //大于一个
                if (imageViews.size() > 1) {
                    viewHolder.llGoods.removeAllViews();
                    for (int i = 0, l = imageViews.size(); i < l; i++) {
                        viewHolder.llGoods.addView(imageViews.get(i));
                    }
                    viewHolder.llGoodOne.setVisibility(View.GONE);
                    viewHolder.svGoods.setVisibility(View.VISIBLE);
                } else {
                    OrderList.DataBean.SubOrdersBean.GoodsBean goodOne = goods.get(0);
                    viewHolder.tvName.setText(goodOne.getName());
                    ImageOptions.Builder builder = new ImageOptions.Builder();
                    builder.setLoadingDrawableId(R.drawable.placeholder_200_200);
                    ImageOptions options = builder.build();
                    x.image().bind(viewHolder.ivGoodOne, goodOne.getImg(), options);
                    viewHolder.llGoodOne.setVisibility(View.VISIBLE);
                    viewHolder.svGoods.setVisibility(View.GONE);
                }

                viewHolder.tvGoodsCount.setText("总共" + imageViews.size() + "件商品");

                String subOrderStatus = subOrder.getStatus();

                viewHolder.btnPay.setVisibility(View.GONE);//不能付款了
                if ((subOrderStatus.equals("23") || subOrderStatus.equals("11")) && !hasCustomCar) {  //交易完成或已取消
                    viewHolder.btnBuyAgain.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnBuyAgain.setVisibility(View.GONE);
                }

                if (subOrderStatus.equals("23") && !isComment) {
                    viewHolder.btnComment.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnComment.setVisibility(View.GONE);
                }

                if (subOrderStatus.equals("30")) {  //已发货
                    viewHolder.btnConfirm.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnConfirm.setVisibility(View.GONE);
                }

                viewHolder.btnCancel.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    private class ViewHolder {

        public TextView tvTime;
        public TextView tvStatus;
        public LinearLayout llGoodOne;
        public ImageView ivGoodOne;
        public LinearLayout llGoods;
        public HorizontalScrollView svGoods;
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
            llGoodOne = (LinearLayout) view.findViewById(R.id.ll_good_one);
            ivGoodOne = (ImageView) view.findViewById(R.id.iv_good_one);
            llGoods = (LinearLayout) view.findViewById(R.id.ll_goods);
            svGoods = (HorizontalScrollView) view.findViewById(R.id.sv_goods);
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
