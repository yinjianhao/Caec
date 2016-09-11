package com.me.caec.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.caec.R;
import com.me.caec.bean.OrderDetail;
import com.me.caec.bean.OrderDetailPay;
import com.me.caec.bean.OrderList;
import com.me.caec.globle.Client;
import com.me.caec.utils.ImageUtils;
import com.me.caec.utils.OrderUtils;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.ConfirmDialog;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 未支付和已关闭(订单未拆分)
 */
public class OrderDetailPayActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.sv_body)
    private ScrollView svBody;

    @ViewInject(R.id.ll_btns)
    private LinearLayout llBtns;

    @ViewInject(R.id.ll_order_body)
    private LinearLayout llOrderBody;

    @ViewInject(R.id.btn_buy_again)
    public Button btnBuyAgain;

    @ViewInject(R.id.btn_cancel)
    public Button btnCancel;

    @ViewInject(R.id.btn_pay)
    public Button btnPay;

    @ViewInject(R.id.tv_id)
    private TextView tvId;

    @ViewInject(R.id.tv_status)
    private TextView tvStatus;

    @ViewInject(R.id.tv_down)
    private TextView tvDown;

    @ViewInject(R.id.tv_down2)
    private TextView tvDown2;

    @ViewInject(R.id.tv_goods_price)
    private TextView tvGoodsPrice;

    @ViewInject(R.id.tv_freight)
    private TextView tvFreight;

    @ViewInject(R.id.tv_discount)
    private TextView tvDiscount;

    @ViewInject(R.id.tv_order_price)
    private TextView tvOrderPrice;

    @ViewInject(R.id.tv_real_pay)
    private TextView tvRealPay;

    @ViewInject(R.id.tv_order_time)
    private TextView tvOrderTime;

    //转化2位小数
    private DecimalFormat decimalFormat;

    private String[] reasons = new String[]{"预算不足", "价格太高", "现在不想买了", "买其他商品了", "其它"};

    private String orderId;
    private Boolean buyAgain;

    private OrderDetailPay.DataBean orderDetailPay;
    private long countTime;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    countTime--;
                    if (countTime > 0) {
                        String time = processTime();
                        tvDown.setText(time);
                        tvDown2.setText(time);
                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        //取消订单
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_pay);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("订单详情");
        tvBack.setOnClickListener(this);

        decimalFormat = new DecimalFormat("0.00");
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        buyAgain = intent.getBooleanExtra("buyAgain", false);

        getOrderDetail();
    }

    /**
     * 获取订单详情信息
     */
    private void getOrderDetail() {
        RequestParams params = new RequestParams(Client.ORDER_DETAIL_PAY_URL);
        params.addQueryStringParameter("token", PreferencesUtils.getString(this, "token", ""));
        params.addQueryStringParameter("id", orderId);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                OrderDetailPay data = JSON.parseObject(result, OrderDetailPay.class);

                if (data.getResult() == 0) {
                    orderDetailPay = data.getData();
                    processView();
                } else {
                    Toast.makeText(OrderDetailPayActivity.this, "获取订单详情失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(OrderDetailPayActivity.this, "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processView() {
        tvId.setText("订单号:   " + orderId);
        tvStatus.setText("订单状态:   " + OrderUtils.status2StatusName(orderDetailPay.getStatus()));

        List<OrderDetailPay.DataBean.CarsBean> cars = orderDetailPay.getCars();
        for (OrderDetailPay.DataBean.CarsBean car : cars) {
            View view = View.inflate(this, R.layout.item_order_detail_pay_good, null);

            TextView tvStoreName = (TextView) view.findViewById(R.id.tv_store_name);
            TextView tvStoreAddress = (TextView) view.findViewById(R.id.tv_store_address);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
            RelativeLayout rlReason = (RelativeLayout) view.findViewById(R.id.rl_reason);
            TextView tvReason = (TextView) view.findViewById(R.id.tv_reason);
            RelativeLayout rlMessage = (RelativeLayout) view.findViewById(R.id.rl_message);
            TextView tvMessage = (TextView) view.findViewById(R.id.tv_message);
            LinearLayout llGoodsBody = (LinearLayout) view.findViewById(R.id.ll_goods_body);

            tvStoreName.setText(car.getStoreName());
            tvStoreAddress.setText(car.getStoreAddress());
            tvName.setText(car.getReceiver().getReceiver() + "  " + car.getReceiver().getMobile());
            tvAddress.setVisibility(View.GONE);

            if (orderDetailPay.getStatus().equals("23")) {
                tvReason.setText(orderDetailPay.getReason());
            } else {
                rlReason.setVisibility(View.GONE);
            }

            if (car.getReceiver().getOrderMsg().isEmpty()) {
                rlMessage.setVisibility(View.GONE);
            } else {
                tvMessage.setText(car.getReceiver().getOrderMsg());
            }

            View goodView = View.inflate(this, R.layout.item_order_detail_good, null);
            ImageView ivGood = (ImageView) goodView.findViewById(R.id.iv_good);
            TextView tvGoodName = (TextView) goodView.findViewById(R.id.tv_good_name);
            TextView tvGoodDesc = (TextView) goodView.findViewById(R.id.tv_good_desc);

            x.image().bind(ivGood, car.getImg(), ImageUtils.getDefaultImageOptions());
            tvGoodName.setText(car.getName());
            tvGoodDesc.setText(car.getProp());
            llGoodsBody.addView(goodView);
            llOrderBody.addView(view);
        }

        OrderDetailPay.DataBean.PartsBean parts = orderDetailPay.getParts();

        View view = View.inflate(this, R.layout.item_order_detail_pay_good, null);

        RelativeLayout rlLocation = (RelativeLayout) view.findViewById(R.id.rl_location);
        TextView tvStoreName = (TextView) view.findViewById(R.id.tv_store_name);
        TextView tvStoreAddress = (TextView) view.findViewById(R.id.tv_store_address);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
        RelativeLayout rlReason = (RelativeLayout) view.findViewById(R.id.rl_reason);
        TextView tvReason = (TextView) view.findViewById(R.id.tv_reason);
        RelativeLayout rlMessage = (RelativeLayout) view.findViewById(R.id.rl_message);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_message);
        LinearLayout llGoodsBody = (LinearLayout) view.findViewById(R.id.ll_goods_body);

        rlLocation.setVisibility(View.GONE);
        tvName.setText(parts.getReceiving().getReceiver() + "  " + parts.getReceiving().getMobile());
        tvAddress.setText(parts.getReceiving().getAddress());

        if (orderDetailPay.getStatus().equals("23")) {
            tvReason.setText(orderDetailPay.getReason());
        } else {
            rlReason.setVisibility(View.GONE);
        }

        if (parts.getReceiving().getOrderMsg().isEmpty()) {
            rlMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setText(parts.getReceiving().getOrderMsg());
        }

        List<OrderDetailPay.DataBean.PartsBean.PartInfoBean> part = parts.getData();

        for (OrderDetailPay.DataBean.PartsBean.PartInfoBean partItem : part) {
            View partView = View.inflate(this, R.layout.item_order_detail_good, null);
            ImageView ivGood = (ImageView) partView.findViewById(R.id.iv_good);
            TextView tvGoodName = (TextView) partView.findViewById(R.id.tv_good_name);
            TextView tvGoodDesc = (TextView) partView.findViewById(R.id.tv_good_desc);

            x.image().bind(ivGood, partItem.getImg(), ImageUtils.getDefaultImageOptions());
            tvGoodName.setText(partItem.getName());
            tvGoodDesc.setText(partItem.getProp());
            llGoodsBody.addView(partView);
        }
        llOrderBody.addView(view);

        tvGoodsPrice.setText("¥" + decimalFormat.format(orderDetailPay.getCost()));
        tvFreight.setText("¥" + decimalFormat.format(orderDetailPay.getFreight()));
        tvDiscount.setText("¥" + decimalFormat.format(orderDetailPay.getDiscount()));
        tvOrderPrice.setText("¥" + decimalFormat.format(orderDetailPay.getPay()));
        tvRealPay.setText("¥" + decimalFormat.format(orderDetailPay.getPay()));
        tvOrderTime.setText("¥" + orderDetailPay.getTime());

        setButton();
        svBody.setVisibility(View.VISIBLE);

        countDown();  //开始倒计时
    }

    /**
     * 倒计时
     */
    private void countDown() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        try {
            Date systemTime = format.parse(orderDetailPay.getSysTime());
            Date endTime = format.parse(orderDetailPay.getEndTime());

            countTime = endTime.getTime() - systemTime.getTime();

            handler.sendEmptyMessageDelayed(1, 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String processTime() {
        long l = countTime / 1000 / 60 / 60;
        long l1 = countTime / 1000 / 60 % 60;
        long l2 = countTime / 1000 % 60;

        return "付款剩余" + l + "时" + l1 + "分" + l2 + "秒";
    }

    private void setButton() {
        if (orderDetailPay.getStatus().equals("01")) {
            llBtns.setVisibility(View.VISIBLE);
            btnBuyAgain.setVisibility(View.GONE);
            btnPay.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        } else {
            if (!buyAgain) {
                llBtns.setVisibility(View.GONE);
            } else {
                tvDown.setVisibility(View.GONE);
                tvDown2.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_pay:
                Toast.makeText(this, "没办法付钱啊!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * 关闭订单
     */
    private void cancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(reasons, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                dialog.dismiss();

                ConfirmDialog confirmDialog = new ConfirmDialog(OrderDetailPayActivity.this);
                confirmDialog.setBody("你确定要取消此订单吗?");
                confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void confirm() {
                        RequestParams params = new RequestParams(Client.CANCEL_ORDER_URL);
                        params.addQueryStringParameter("token", PreferencesUtils.getString(OrderDetailPayActivity.this, "token", ""));
                        params.addQueryStringParameter("id", orderId);
                        params.addQueryStringParameter("reason", reasons[which]);

                        x.http().post(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    org.json.JSONObject json = new org.json.JSONObject(result);
                                    if (json.getInt("result") == 0) {
                                        Toast.makeText(OrderDetailPayActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                                        goListWithUpdate();
                                    } else {
                                        Toast.makeText(OrderDetailPayActivity.this, "取消订单失败", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Toast.makeText(OrderDetailPayActivity.this, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
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

    /**
     * 返回订单列表,并更新列表
     */
    private void goListWithUpdate() {
        Intent i = new Intent(this, OrderListActivity.class);
        i.putExtra("update", true);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }
}
