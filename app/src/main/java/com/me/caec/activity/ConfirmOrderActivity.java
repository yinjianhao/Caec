package com.me.caec.activity;

import android.content.Intent;
import android.content.res.ObbInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.caec.R;
import com.me.caec.bean.ConfirmOrder;
import com.me.caec.bean.ConfirmedOrder;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.DpTransforUtils;
import com.me.caec.utils.ImageUtils;
import com.me.caec.utils.NumberUtils;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.ll_good_content)
    private LinearLayout llGoodContent;

    @ViewInject(R.id.sv_body)
    private ScrollView svBody;

    @ViewInject(R.id.rl_footer)
    private RelativeLayout rlFooter;

    @ViewInject(R.id.tv_goods_price)
    private TextView tvGoodsPrice;

    @ViewInject(R.id.tv_freight)
    private TextView tvFreight;

    @ViewInject(R.id.tv_discount)
    private TextView tvDiscount;

    @ViewInject(R.id.tv_order_price)
    private TextView tvOrderPrice;

    @ViewInject(R.id.cb_check)
    private CheckBox cbCheck;

    @ViewInject(R.id.tv_total_price)
    private TextView tvTotalPrice;

    @ViewInject(R.id.btn_confirm)
    private Button btnConfirm;

    @ViewInject(R.id.tv_coupon)
    private TextView tvCoupon;

    @ViewInject(R.id.ll_coupon)
    private LinearLayout llCoupon;

    private final int TYPE_DISTRIBUTOR = 1;
    private final int TYPE_BUY = 2;
    private final int TYPE_ADDRESS = 3;
    private final int TYPE_INVOICE = 4;
    private final int TYPE_COUPON = 5;

    private ConfirmOrder.DataBean dataBean;

    private float orderTotalPrice = 0;  //订单总额(包含运费)

    private LinearLayout currentVIew;

    private Map<String, Distributor> distributors;

    private Map<String, BuyType> buyTypes;

    private String[] orderMsgs;

    private String receivingId = "";

    private int invoiceType = -1;

    private String invoiceCompony = "";

    private String partMsg = "";

    private String couponId = "";

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_confirm_order);
    }

    @Override
    public void render() {
        tvTitle.setText("确认订单");
        tvBack.setOnClickListener(this);
        llCoupon.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        Intent intent = getIntent();
        String params = intent.getStringExtra("params");
        getConfirmOrderList(params);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_coupon:
                chooseCoupon();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            default:
                break;
        }
    }

    private void confirm() {
        String result = checkOk();

        if (!result.isEmpty()) {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray cars = new JSONArray();
        JSONArray parts = new JSONArray();

        List<ConfirmOrder.DataBean.CarsBean> carsBeen = dataBean.getCars();
        for (int i = 0, size = carsBeen.size(); i < size; i++) {
            ConfirmOrder.DataBean.CarsBean carBeen = carsBeen.get(i);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("goodsId", carBeen.getId());
            jsonObject.put("goodsCount", carBeen.getCount());
            jsonObject.put("optionalIds", "[]");
            jsonObject.put("orderMsg", distributors.get(String.valueOf(i)));
            jsonObject.put("dealerId", orderMsgs[i] == null ? "" : orderMsgs[i]);

            BuyType buyType = buyTypes.get(String.valueOf(i));
            jsonObject.put("type", buyType.type);
            jsonObject.put("receiver", buyType.receiver);
            jsonObject.put("mobile", buyType.mobile);
            jsonObject.put("no", buyType.no);
            jsonObject.put("name", buyType.name);

            cars.add(jsonObject);
        }

        List<ConfirmOrder.DataBean.PartsBean> partsBeen = dataBean.getParts();
        for (int i = 0, size = partsBeen.size(); i < size; i++) {
            ConfirmOrder.DataBean.PartsBean partBean = partsBeen.get(i);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", partBean.getId());
            jsonObject.put("count", partBean.getId());

            parts.add(jsonObject);
        }

        JSONObject goods = new JSONObject();
        goods.put("goods", parts);
        goods.put("orderMsg", partMsg);

        Map<String, Object> map = new HashMap<>();
        map.put("from", 1);
        map.put("cars", cars.toString());
        map.put("goods", goods.toString());

        final JSONObject receipt = new JSONObject();

        if (invoiceType != -1) {
            receipt.put("type", 1);
            receipt.put("header", invoiceType);
            receipt.put("company", invoiceCompony);
        }
        map.put("receipt", receipt.toString());

        JSONObject receiving = new JSONObject();
        receiving.put("receivingId", receivingId);
        map.put("receiving", receiving.toString());

        if (couponId.isEmpty()) {
            map.put("coupon", "[]");
        } else {
            map.put("coupon", Arrays.toString(new String[]{couponId}));
        }

        BaseClient.post(this, RequestUrl.CONFIRM_ORDER_URL, map, ConfirmedOrder.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                ConfirmedOrder data = (ConfirmedOrder) result;
                if (data.getResult() == 0) {
                    Log.d("ConfirmOrderActivity", data.getData().getOrderId());
                } else {
                    Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private String checkOk() {
        int size = dataBean.getCars().size();

        for (int i = 0; i < size; i++) {
            if (distributors.get(String.valueOf(i)) == null) {
                return "请选择提车经销商";
            }
        }

        for (int i = 0; i < size; i++) {
            if (buyTypes.get(String.valueOf(i)) == null) {
                return "请填写提车人信息";
            }
        }

        if (receivingId == null || receivingId.isEmpty()) {
            return "请选择收货人信息";
        }

        if (!cbCheck.isChecked()) {
            return "请勾选同意长安商城服务条款";
        }

        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            switch (requestCode) {
                case TYPE_DISTRIBUTOR:
                    int id = extras.getInt("id");
                    String proName = extras.getString("provinceName");
                    int proId = extras.getInt("provinceId");
                    String cityName = extras.getString("cityName");
                    int cityId = extras.getInt("cityId");
                    String dealerName = extras.getString("dealerName");
                    int dealerId = extras.getInt("dealerId");
                    TextView tv = (TextView) currentVIew.findViewById(R.id.tv_distributor);
                    tv.setText(proName + " " + cityName + "\n" + dealerName);

                    distributors.put(currentVIew.getTag().toString(), new Distributor(id, dealerId, proId, cityId));
                    break;
                case TYPE_BUY:
                    int type = extras.getInt("type");
                    String receiver = extras.getString("receiver");
                    String mobile = extras.getString("mobile");
                    String no = extras.getString("no");
                    String name = extras.getString("name");

                    tv = (TextView) currentVIew.findViewById(R.id.tv_buy_type);
                    tv.setText(receiver + "\n" + mobile + "\n" + no);

                    buyTypes.put(currentVIew.getTag().toString(), new BuyType(type, receiver, mobile, no, name));
                    break;
                case TYPE_ADDRESS:
                    receivingId = extras.getString("receivingId");
                    receiver = extras.getString("receiver");
                    mobile = extras.getString("mobile");
                    String provinceName = extras.getString("provinceName");
                    cityName = extras.getString("cityName");
                    String areaName = extras.getString("areaName");
                    String address = extras.getString("address");
                    String zip = extras.getString("zip");

                    if (!receivingId.isEmpty()) {
                        tv = (TextView) currentVIew.findViewById(R.id.tv_receiver);
                        tv.setText(receiver + " 手机：" + mobile + "\n" + provinceName + cityName + areaName + address + "\n" + zip);
                    }
                    break;
                case TYPE_INVOICE:
                    invoiceType = extras.getInt("type");
                    invoiceCompony = extras.getString("companyName");

                    tv = (TextView) currentVIew.findViewById(R.id.tv_invoice);

                    if (invoiceType == 1) {
                        tv.setText("个人");
                    } else if (invoiceType == 2) {
                        tv.setText("公司" + "\n" + invoiceCompony);
                    } else {
                        tv.setText("不需要发票");
                    }
                    break;
                case TYPE_COUPON:
                    couponId = extras.getString("id");
                    float price = extras.getFloat("price");

                    if (!couponId.isEmpty()) {
                        tvCoupon.setTextColor(getResources().getColor(R.color.baseRed));
                        tvCoupon.setText("-¥" + NumberUtils.toFixed2(price));
                        String s = "¥" + NumberUtils.toFixed2(orderTotalPrice - price);
                        tvOrderPrice.setText(s);
                        tvTotalPrice.setText(s);
                    } else {
                        tvCoupon.setTextColor(getResources().getColor(R.color.color333));
                        tvCoupon.setText("未使用");
                        tvOrderPrice.setText("¥" + NumberUtils.toFixed2(orderTotalPrice));
                        tvTotalPrice.setText("¥" + NumberUtils.toFixed2(orderTotalPrice));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void chooseCoupon() {
        Intent i = new Intent(this, CouponActivity.class);
        i.putExtra("couponId", couponId);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", dataBean);
        i.putExtras(bundle);
        startActivityForResult(i, TYPE_COUPON);
    }

    /**
     * 获取确认订单列表
     */
    private void getConfirmOrderList(String params) {
        Map<String, Object> map = new HashMap<>();

        map.put("token", PreferencesUtils.getString(this, "token", ""));
        map.put("goods", params);

        BaseClient.post(this, RequestUrl.CONFIRM_LIST_URL, map, ConfirmOrder.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                ConfirmOrder data = (ConfirmOrder) result;

                if (data.getResult() == 0) {
                    dataBean = data.getData();
                    initConfirmList();
                } else {
                    Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 渲染列表
     */
    private void initConfirmList() {
        float goodsTotalPrice = 0;

        final List<ConfirmOrder.DataBean.CarsBean> cars = dataBean.getCars();

        //初始化
        distributors = new HashMap<>();
        buyTypes = new HashMap<>();
        orderMsgs = new String[cars.size()];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = DpTransforUtils.dp2px(this, 10);

        for (int i = 0, l = cars.size(); i < l; i++) {
            final ConfirmOrder.DataBean.CarsBean car = cars.get(i);
            View carView = View.inflate(this, R.layout.item_confirm_order_car, null);
            carView.setLayoutParams(params);
            LinearLayout llDistributor = (LinearLayout) carView.findViewById(R.id.ll_distributor);
            LinearLayout llBuyType = (LinearLayout) carView.findViewById(R.id.ll_buy_type);
            ImageView ivGood = (ImageView) carView.findViewById(R.id.iv_good);
            TextView tvGoodName = (TextView) carView.findViewById(R.id.tv_good_name);
            TextView tvGoodDesc = (TextView) carView.findViewById(R.id.tv_good_desc);
            TextView tvPrice = (TextView) carView.findViewById(R.id.tv_price);
            TextView tvOldPrice = (TextView) carView.findViewById(R.id.tv_old_price);
            TextView tvNum = (TextView) carView.findViewById(R.id.tv_num);
            TextView tvPriceCount = (TextView) carView.findViewById(R.id.tv_price_count);
            TextView tvDeposit = (TextView) carView.findViewById(R.id.tv_deposit);
            EditText etMsg = (EditText) carView.findViewById(R.id.et_msg);

            llDistributor.setTag(i);
            llDistributor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentVIew = (LinearLayout) v;
                    Intent i = new Intent(ConfirmOrderActivity.this, DistributorActivity.class);
                    Distributor distributor = distributors.get(v.getTag().toString());
                    i.putExtra("carId", car.getId());
                    if (distributor != null) {
                        i.putExtra("dealerId", distributor.dealerId);
                        i.putExtra("provinceId", distributor.provinceId);
                        i.putExtra("cityId", distributor.cityId);
                    }

                    startActivityForResult(i, TYPE_DISTRIBUTOR);
                }
            });

            llBuyType.setTag(i);
            llBuyType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentVIew = (LinearLayout) v;
                    Intent i = new Intent(ConfirmOrderActivity.this, BuyTypeActivity.class);

                    BuyType buyType = buyTypes.get(v.getTag().toString());
                    if (buyType != null) {
                        i.putExtra("receiver", buyType.receiver);
                        i.putExtra("type", buyType.type);
                        i.putExtra("name", buyType.name);
                        i.putExtra("no", buyType.no);
                        i.putExtra("mobile", buyType.mobile);
                    }
                    startActivityForResult(i, TYPE_BUY);
                }
            });

            llBuyType.setTag(i);
            final int finalI = i;
            etMsg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    orderMsgs[finalI] = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            x.image().bind(ivGood, car.getImg(), ImageUtils.getDefaultImageOptions());
            tvGoodName.setText(car.getName());
            tvGoodDesc.setText(car.getProp());
            tvPrice.setText("¥" + NumberUtils.toFixed2(car.getPrice()));
            tvOldPrice.setText("¥" + NumberUtils.toFixed2(car.getOriginalPrice()));
            tvOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            tvNum.setText("x" + car.getCount());

            tvPriceCount.setText("¥" + NumberUtils.toFixed2(car.getPrice()));
            tvDeposit.setText("¥" + NumberUtils.toFixed2(car.getPay()));

            goodsTotalPrice += car.getPay();

            llGoodContent.addView(carView);
        }

        List<ConfirmOrder.DataBean.PartsBean> parts = dataBean.getParts();

        View partView = View.inflate(this, R.layout.item_confirm_order_part, null);
        partView.setLayoutParams(params);

        LinearLayout llPartContent = (LinearLayout) partView.findViewById(R.id.ll_part_content);
        LinearLayout llReceiver = (LinearLayout) partView.findViewById(R.id.ll_receiver);
        LinearLayout llInvoice = (LinearLayout) partView.findViewById(R.id.ll_invoice);
        TextView tvPriceCount = (TextView) partView.findViewById(R.id.tv_price_count);
        TextView tvCarriage = (TextView) partView.findViewById(R.id.tv_carriage);
        EditText etMsg = (EditText) partView.findViewById(R.id.et_msg);

        llReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentVIew = (LinearLayout) v;
                Intent i = new Intent(ConfirmOrderActivity.this, ChooseAddressActivity.class);

                i.putExtra("receivingId", receivingId);

                startActivityForResult(i, TYPE_ADDRESS);
            }
        });

        llInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentVIew = (LinearLayout) v;
                Intent i = new Intent(ConfirmOrderActivity.this, InvoiceActivity.class);

                i.putExtra("type", invoiceType);
                i.putExtra("companyName", invoiceCompony);

                startActivityForResult(i, TYPE_INVOICE);
            }
        });

        etMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                partMsg = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        float totalPrice = 0;
        float carriage = 0;

        for (ConfirmOrder.DataBean.PartsBean part : parts) {
            View goodView = View.inflate(this, R.layout.item_order_detail_good, null);
            ImageView ivGood = (ImageView) goodView.findViewById(R.id.iv_good);
            TextView tvGoodName = (TextView) goodView.findViewById(R.id.tv_good_name);
            TextView tvGoodDesc = (TextView) goodView.findViewById(R.id.tv_good_desc);
            TextView tvPrice = (TextView) goodView.findViewById(R.id.tv_price);
            TextView tvOldPrice = (TextView) goodView.findViewById(R.id.tv_old_price);
            TextView tvNum = (TextView) goodView.findViewById(R.id.tv_num);

            x.image().bind(ivGood, part.getImg(), ImageUtils.getDefaultImageOptions());
            tvGoodName.setText(part.getName());
            tvGoodDesc.setText(part.getProp());
            tvPrice.setText("¥" + NumberUtils.toFixed2(part.getPrice()));
            tvOldPrice.setText("¥" + NumberUtils.toFixed2(part.getOriginalPrice()));
            tvOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            tvNum.setText("x" + part.getCount());

            totalPrice += part.getPrice();
            carriage += part.getCarriage();

            llPartContent.addView(goodView);
        }
        tvPriceCount.setText("¥" + NumberUtils.toFixed2(totalPrice + carriage));
        tvCarriage.setText("¥" + NumberUtils.toFixed2(carriage));
        llGoodContent.addView(partView);

        orderTotalPrice = goodsTotalPrice += totalPrice + carriage;

        tvGoodsPrice.setText("¥" + NumberUtils.toFixed2(goodsTotalPrice));
        tvFreight.setText("¥" + NumberUtils.toFixed2(carriage));
        tvDiscount.setText("¥0.00");
        tvOrderPrice.setText("¥" + NumberUtils.toFixed2(goodsTotalPrice));
        tvTotalPrice.setText("¥" + NumberUtils.toFixed2(goodsTotalPrice));
        btnConfirm.setText("提交订单(" + (cars.size() + parts.size()) + ")");

        svBody.setVisibility(View.VISIBLE);
        rlFooter.setVisibility(View.VISIBLE);
    }

    private class Distributor {
        public int id;
        public int dealerId;
        public int provinceId;
        public int cityId;

        public Distributor(int id, int dealerId, int provinceId, int cityId) {
            this.id = id;
            this.dealerId = dealerId;
            this.provinceId = provinceId;
            this.cityId = cityId;
        }
    }

    private class BuyType {
        public int type;
        public String receiver;
        public String mobile;
        public String no;
        public String name;

        public BuyType(int type, String receiver, String mobile, String no, String name) {
            this.type = type;
            this.receiver = receiver;
            this.mobile = mobile;
            this.no = no;
            this.name = name;
        }
    }
}
