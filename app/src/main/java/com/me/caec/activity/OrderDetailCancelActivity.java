package com.me.caec.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.me.caec.bean.OrderDetailCancel;
import com.me.caec.bean.OrderList;
import com.me.caec.globle.Client;
import com.me.caec.utils.OrderUtils;
import com.me.caec.utils.PreferencesUtils;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 待审核或已取消
 */

public class OrderDetailCancelActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.sv_body)
    private ScrollView svBody;

    @ViewInject(R.id.ll_btns)
    private LinearLayout llBtns;

    @ViewInject(R.id.ll_goods_body)
    private LinearLayout llGoodsBody;

    @ViewInject(R.id.btn_buy_again)
    public Button btnBuyAgain;

    @ViewInject(R.id.btn_comment)
    public Button btnComment;

    @ViewInject(R.id.btn_cancel)
    public Button btnCancel;

    @ViewInject(R.id.btn_confirm)
    public Button btnConfirm;

    @ViewInject(R.id.tv_id)
    private TextView tvId;

    @ViewInject(R.id.tv_status)
    private TextView tvStatus;

    @ViewInject(R.id.tv_down)
    private TextView tvDown;

    @ViewInject(R.id.tv_logistics)
    private TextView tvLogistics;

    @ViewInject(R.id.tv_time)
    private TextView tvTime;

    @ViewInject(R.id.tv_name)
    private TextView tvName;

    @ViewInject(R.id.tv_address)
    private TextView tvAddress;

    @ViewInject(R.id.rl_reason)
    private RelativeLayout rlReason;

    @ViewInject(R.id.tv_reason)
    private TextView tvReason;

    @ViewInject(R.id.rl_message)
    private RelativeLayout rlMessage;

    @ViewInject(R.id.tv_message)
    private TextView tvMessage;

    @ViewInject(R.id.tv_money)
    private TextView tvMoney;

    @ViewInject(R.id.tv_payment_time)
    private TextView tvPaymentTime;

    @ViewInject(R.id.tv_pay_type)
    private TextView tvPayType;

    @ViewInject(R.id.ll_car_code)
    private LinearLayout llCarCode;

    @ViewInject(R.id.tv_goods_price)
    private TextView tvGoodsPrice;

    @ViewInject(R.id.tv_code)
    private TextView tvCode;

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

    private String orderId;
    private Boolean buyAgain;
    private String orderType;
    private String payType;

    private JSONArray goods;  //存储商品id
    private OrderDetailCancel.DataBean orderDetailCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_normal);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("订单详情");
        tvBack.setOnClickListener(this);

        decimalFormat = new DecimalFormat("0.00");
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        orderType = intent.getStringExtra("orderType");
        payType = intent.getStringExtra("payType");
        buyAgain = intent.getBooleanExtra("buyAgain", false);

        getOrderDetail();
    }

    /**
     * 获取订单详情数据
     */
    private void getOrderDetail() {
        RequestParams params = new RequestParams(Client.ORDER_DETAIL_CANCEL_URL);
        params.addQueryStringParameter("token", PreferencesUtils.getString(this, "token", ""));
        params.addQueryStringParameter("id", orderId);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                OrderDetailCancel data = JSON.parseObject(result, OrderDetailCancel.class);
                if (data.getResult() == 0) {
                    orderDetailCancel = data.getData();
                    processView();
                } else {
                    Toast.makeText(OrderDetailCancelActivity.this, "获取订单详情失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(OrderDetailCancelActivity.this, "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
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
     * 填充数据
     */
    private void processView() {

        tvId.setText("订单号:   " + orderId);
        tvStatus.setText("订单状态:   " + OrderUtils.status2StatusName(orderDetailCancel.getStatus()));
        tvDown.setVisibility(View.GONE);

        tvLogistics.setText(orderDetailCancel.getLogistics().getList().get(0).getInfo());
        tvTime.setText(orderDetailCancel.getLogistics().getList().get(0).getTime());
        tvName.setText(orderDetailCancel.getReceiver().getReceiver() + "  " + orderDetailCancel.getReceiver().getMobile());

        String address = orderDetailCancel.getReceiver().getAddress();
        if (address.isEmpty()) {
            tvAddress.setVisibility(View.GONE);
        } else {
            tvAddress.setText(address);
        }

        String reason = orderDetailCancel.getReason();
        if (reason.isEmpty()) {
            rlReason.setVisibility(View.GONE);
        } else {
            tvReason.setText(reason);
        }

        String message = orderDetailCancel.getReceiver().getOrderMsg();
        if (message.isEmpty()) {
            rlMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setText(message);
        }

        tvMoney.setText("支付金额:¥" + decimalFormat.format(orderDetailCancel.getPay()));
        tvPaymentTime.setText(orderDetailCancel.getPayTime());
        tvPayType.setText(OrderUtils.payType2PayTypeName(payType));

        if (!orderType.equals("1")) {
            getCarCode();  //获取提车验证码
        } else {
            llCarCode.setVisibility(View.GONE);
        }

        List<OrderDetailCancel.DataBean.GoodsBean> goodsBean = orderDetailCancel.getGoods();
        int l = goodsBean.size();
        goods = new JSONArray();
        for (int i = 0; i < l; i++) {
            OrderDetailCancel.DataBean.GoodsBean good = goodsBean.get(i);

            //保存商品信息,用于再次购买
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", good.getId());
            jsonObject.put("count", good.getCount());
            goods.add(jsonObject);

            View view = View.inflate(this, R.layout.item_order_detail_good, null);
            ImageView ivGood = (ImageView) view.findViewById(R.id.iv_good);
            ImageOptions.Builder builder = new ImageOptions.Builder();
            builder.setLoadingDrawableId(R.drawable.placeholder_200_200);
            ImageOptions op = builder.build();
            x.image().bind(ivGood, good.getImg(), op);
            ((TextView) view.findViewById(R.id.tv_good_name)).setText(good.getName());
            ((TextView) view.findViewById(R.id.tv_good_desc)).setText(good.getProp());
            ((TextView) view.findViewById(R.id.tv_price)).setText("¥" + decimalFormat.format(good.getPrice()));
            TextView tvOldPrice = ((TextView) view.findViewById(R.id.tv_old_price));
            tvOldPrice.setText("¥" + decimalFormat.format(good.getOriginalPrice()));
            tvOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            ((TextView) view.findViewById(R.id.tv_num)).setText("x" + good.getCount());

            llGoodsBody.addView(view);
        }
        tvGoodsPrice.setText("¥" + decimalFormat.format(orderDetailCancel.getCost()));
        tvFreight.setText("¥" + decimalFormat.format(orderDetailCancel.getFreight()));
        tvDiscount.setText("¥" + decimalFormat.format(orderDetailCancel.getDiscount()));
        tvOrderPrice.setText("¥" + decimalFormat.format(orderDetailCancel.getPay()));
        tvRealPay.setText("¥" + decimalFormat.format(orderDetailCancel.getPay()));
        tvOrderTime.setText("¥" + orderDetailCancel.getPayTime());

        setButton();
        svBody.setVisibility(View.VISIBLE);
    }

    /**
     * 获取提车验证码
     */
    private void getCarCode() {
        RequestParams params = new RequestParams(Client.CAR_CODE_URL);
        params.addQueryStringParameter("token", PreferencesUtils.getString(this, "token", ""));
        params.addQueryStringParameter("orderId", orderId);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(result);
                    if (jsonObject.getInt("result") == 0) {
                        tvCode.setText(jsonObject.getString("data"));
                    } else {
                        llCarCode.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(OrderDetailCancelActivity.this, "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
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
     * 设置操作按钮
     */
    private void setButton() {
        //设置按钮
        if (buyAgain) {
            llBtns.setVisibility(View.VISIBLE);
//            btnBuyAgain.setVisibility(View.VISIBLE);
            btnComment.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);

            btnBuyAgain.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_buy_again:
                buyAgain();
            default:
                break;
        }
    }

    /**
     * 再次购买
     */
    private void buyAgain() {

        RequestParams params = new RequestParams(Client.ADD_CART_URL);
        params.addQueryStringParameter("token", PreferencesUtils.getString(this, "token", ""));
        params.addQueryStringParameter("goods", goods.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    org.json.JSONObject json = new org.json.JSONObject(result);
                    if (json.getInt("result") == 0) {
                        Toast.makeText(OrderDetailCancelActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderDetailCancelActivity.this, CartActivity.class));
                    } else {
                        Toast.makeText(OrderDetailCancelActivity.this, "加入购物车失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(OrderDetailCancelActivity.this, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
