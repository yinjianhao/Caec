package com.me.caec.view.pagerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.caec.R;
import com.me.caec.activity.CartActivity;
import com.me.caec.activity.CommentActivity;
import com.me.caec.activity.OrderDetailCancelActivity;
import com.me.caec.activity.OrderDetailNormalActivity;
import com.me.caec.activity.OrderDetailPayActivity;
import com.me.caec.bean.OrderList;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.DpTransforUtils;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.ConfirmDialog;
import com.me.caec.utils.OrderUtils;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private String[] reasons = new String[]{"预算不足", "价格太高", "现在不想买了", "买其他商品了", "其它"};

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
        decimalFormat = new DecimalFormat("0.00");
//        srlOrderList = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_order_list);
        lvOrderList = (ListView) rootView.findViewById(R.id.lv_order_list);
        lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断哪个订单详情
                OrderList.DataBean dataBean = orderListData.get(position);
                String status = dataBean.getStatus();
                Intent i;
                if (status.equals("01") || status.equals("23")) {
                    i = new Intent(activity, OrderDetailPayActivity.class);
                    i.putExtra("orderId", dataBean.getId());
                    i.putExtra("buyAgain", view.findViewById(R.id.btn_buy_again).getVisibility() == View.VISIBLE);
                } else {
                    OrderList.DataBean.SubOrdersBean subOrder = dataBean.getSubOrders().get(0);
                    String subOrderStatus = subOrder.getStatus();

                    if (subOrderStatus.equals("09") || subOrderStatus.equals("11")) {
                        i = new Intent(activity, OrderDetailCancelActivity.class);
                        i.putExtra("buyAgain", view.findViewById(R.id.btn_buy_again).getVisibility() == View.VISIBLE);
                    } else {
                        i = new Intent(activity, OrderDetailNormalActivity.class);
                        i.putExtra("buyAgain", view.findViewById(R.id.btn_buy_again).getVisibility() == View.VISIBLE);
                        i.putExtra("cancel", view.findViewById(R.id.btn_cancel).getVisibility() == View.VISIBLE);
                        i.putExtra("cancelMoney", (boolean) view.findViewById(R.id.btn_cancel).getTag());
                        i.putExtra("confirm", view.findViewById(R.id.btn_confirm).getVisibility() == View.VISIBLE);
                        i.putExtra("comment", view.findViewById(R.id.btn_comment).getVisibility() == View.VISIBLE);
                    }

                    i.putExtra("orderId", subOrder.getId());

                    i.putExtra("orderType", subOrder.getType());
                    i.putExtra("payType", dataBean.getPayType());
                }

                activity.startActivity(i);
            }
        });
        lvOrderList.setEmptyView(rootView.findViewById(R.id.ll_empty));

        getOrderList();
    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 请求订单数据
     */
    private void getOrderList() {
        RequestParams params = new RequestParams(RequestUrl.ORDER_LIST_URL);
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
            viewHolder.tvTime.setTag(position);   //设置标记当前位置

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
                if ((subOrderStatus.equals("26") || subOrderStatus.equals("11")) && !hasCustomCar) {  //交易完成或已取消
                    viewHolder.btnBuyAgain.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnBuyAgain.setVisibility(View.GONE);
                }

                if (subOrderStatus.equals("26") && !isComment) {
                    viewHolder.btnComment.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnComment.setVisibility(View.GONE);
                }

                if (subOrderStatus.equals("30")) {  //已发货, 确认守候
                    viewHolder.btnConfirm.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnConfirm.setVisibility(View.GONE);
                }

                //是否可以取消
                //整车在已支付,待提车下,显示取消订单;精品未发货之前;定制车按规则计算;
                Boolean backMoney = false;   //是否退钱
                boolean isCancel = false;    //是否可以取消
                String type = subOrder.getType();

                //能否允许取消,退不退钱
                if (type.equals("1") && subOrderStatus.equals("02")) {  //精品发货之前(已支付)
                    isCancel = true;
                    backMoney = true;
                } else if (type.equals("2") && (subOrderStatus.equals("02") || subOrderStatus.equals("07"))) {
                    isCancel = true;
                    backMoney = true;
                } else {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    Date productTime = null;
                    Date sysTime = null;
                    Date receiverTime = null;

                    try {
                        productTime = format.parse(subOrder.getProducttime());
                        sysTime = format.parse(subOrder.getSysTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        viewHolder.btnCancel.setVisibility(View.GONE);
                    }

                    if (productTime != null && sysTime != null) {
                        if (sysTime.before(productTime) || sysTime.equals(productTime)) {
                            if (Integer.parseInt(subOrderStatus) < 7) {
                                isCancel = true;
                            }
                        } else {
                            if (Integer.parseInt(subOrderStatus) < 7) {
                                isCancel = true;
                                backMoney = true;
                            } else if (subOrderStatus.equals("07")) {
                                try {
                                    receiverTime = format.parse(subOrder.getReceiverTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (receiverTime != null && receiverTime.after(productTime)) {
                                    isCancel = true;
                                    backMoney = true;
                                }
                            }
                        }
                    }
                }

                viewHolder.btnCancel.setTag(backMoney); //标志,退不退钱
                if (isCancel) {
                    viewHolder.btnCancel.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnCancel.setVisibility(View.GONE);
                }
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

            svGoods.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            v.getParent().requestDisallowInterceptTouchEvent(true);
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//
//
//
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            break;
//                        default:
//                            break;
//                    }
                    return false;
                }
            });

            //再次购买
            btnBuyAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONArray jsonArray = new JSONArray();

                    int position = (int) tvTime.getTag();
                    List<OrderList.DataBean.SubOrdersBean> subOrders = orderListData.get(position).getSubOrders();
                    for (OrderList.DataBean.SubOrdersBean subOrder : subOrders) {
                        List<OrderList.DataBean.SubOrdersBean.GoodsBean> goods = subOrder.getGoods();
                        for (OrderList.DataBean.SubOrdersBean.GoodsBean good : goods) {
                            JSONObject json = new JSONObject();
                            json.put("id", good.getId());
                            json.put("count", good.getCount());
                            jsonArray.add(json);
                        }
                    }

                    RequestParams params = new RequestParams(RequestUrl.ADD_CART_URL);
                    params.addQueryStringParameter("token", PreferencesUtils.getString(activity, "token", ""));
                    params.addQueryStringParameter("goods", jsonArray.toString());

                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                org.json.JSONObject json = new org.json.JSONObject(result);
                                if (json.getInt("result") == 0) {
                                    Toast.makeText(activity, "加入购物车成功", Toast.LENGTH_SHORT).show();
                                    activity.startActivity(new Intent(activity, CartActivity.class));
                                } else {
                                    Toast.makeText(activity, "加入购物车失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(activity, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
            });

            //评论
            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvTime.getTag();
                    OrderList.DataBean.SubOrdersBean subOrder = orderListData.get(position).getSubOrders().get(0);
                    String subOrderId = subOrder.getId();

                    Intent i = new Intent(activity, CommentActivity.class);
                    i.putExtra("orderId", subOrderId);
                    activity.startActivity(i);
                }
            });

            //取消订单
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (int) tvTime.getTag();
                    final OrderList.DataBean order = orderListData.get(position);
                    String status = order.getStatus();
                    Boolean backMoney;
                    String orderId;

                    if (status.equals("01")) {
                        backMoney = true;
                        orderId = order.getId();
                    } else {
                        backMoney = (Boolean) btnCancel.getTag();
                        orderId = order.getSubOrders().get(0).getId();
                    }

                    final Boolean finalBackMoney = backMoney;
                    final String finalOrderId = orderId;

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setSingleChoiceItems(reasons, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            dialog.dismiss();

                            ConfirmDialog confirmDialog = new ConfirmDialog(activity);
                            String tip = "你确定要取消此订单吗?" + (finalBackMoney ? "" : "(不退钱哦)");
                            confirmDialog.setBody(tip);
                            confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                                @Override
                                public void confirm() {
                                    RequestParams params = new RequestParams(RequestUrl.CANCEL_ORDER_URL);
                                    params.addQueryStringParameter("token", PreferencesUtils.getString(activity, "token", ""));
                                    params.addQueryStringParameter("id", finalOrderId);
                                    params.addQueryStringParameter("reason", reasons[which]);

                                    x.http().post(params, new Callback.CommonCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            try {
                                                org.json.JSONObject json = new org.json.JSONObject(result);
                                                if (json.getInt("result") == 0) {
                                                    if (type == TYPE_ALL) {
                                                        order.setStatus("23");
                                                    } else {
                                                        orderListData.remove(position);
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(activity, "取消订单失败", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable ex, boolean isOnCallback) {
                                            Toast.makeText(activity, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(CancelledException cex) {

                                        }

                                        @Override
                                        public void onFinished() {

                                        }
                                    });
                                }

                                @Override
                                public void cancel() {

                                }
                            });
                            confirmDialog.show();
                        }
                    });
                    builder.show();
                }
            });

            //去付款
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvTime.getTag();
                    Toast.makeText(activity, "没办法付钱啊!", Toast.LENGTH_SHORT).show();
                }
            });

            //确认收货
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (int) tvTime.getTag();

                    final OrderList.DataBean.SubOrdersBean subOrder = orderListData.get(position).getSubOrders().get(0);
                    final String subOrderId = subOrder.getId();
                    ConfirmDialog dialog = new ConfirmDialog(activity);
                    dialog.setBody("你确定要收货吗?");
                    dialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                        @Override
                        public void confirm() {
                            RequestParams params = new RequestParams(RequestUrl.CONFIRM_RECEIPT_URL);
                            params.addQueryStringParameter("token", PreferencesUtils.getString(activity, "token", ""));
                            params.addQueryStringParameter("id", subOrderId);
                            x.http().post(params, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    try {
                                        org.json.JSONObject json = new org.json.JSONObject(result);
                                        if (json.getInt("result") == 0) {
                                            if (type == TYPE_ALL) {
                                                subOrder.setStatus("26");
                                                subOrder.getGoods().get(0).setIsAssess("N");//设置可以评论,1条即可
                                            } else {
                                                orderListData.remove(position);
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(activity, "确认收货失败", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {
                                    Toast.makeText(activity, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(CancelledException cex) {

                                }

                                @Override
                                public void onFinished() {

                                }
                            });
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                    dialog.show();
                }
            });
        }
    }
}
