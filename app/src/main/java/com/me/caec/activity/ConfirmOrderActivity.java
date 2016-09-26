package com.me.caec.activity;

import android.content.Intent;
import android.content.res.ObbInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.bean.ConfirmOrder;
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

    private final int TYPE_DISTRIBUTOR = 1;
    private final int TYPE_BUY = 2;
    private final int TYPE_ADDRESS = 3;
    private final int TYPE_RECEIPT = 4;
    private final int TYPE_COUPON = 5;

    private final int TAG_DISTRIBUTOR = 1;

    private ConfirmOrder.DataBean dataBean;

    private float orderTotalPrice = 0;  //订单总额(包含运费)

    private LinearLayout currentVIew;

    private Map<String, Distributor> distributors;

    private Map<String, BuyType> buyTypes;

    private String[] orderMsgs;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_confirm_order);
    }

    @Override
    public void render() {
        tvTitle.setText("确认订单");
        tvBack.setOnClickListener(this);

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
            default:
                break;
        }
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
                    break;
                case TYPE_RECEIPT:
                    break;
                case TYPE_COUPON:
                    break;
                default:
                    break;
            }
        }
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
        TextView tvPriceCount = (TextView) partView.findViewById(R.id.tv_price_count);
        TextView tvCarriage = (TextView) partView.findViewById(R.id.tv_carriage);

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
