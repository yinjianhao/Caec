package com.me.caec.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;
import com.me.caec.bean.ConfirmOrder;
import com.me.caec.fragment.CouponFragment;
import com.me.caec.fragment.UnCouponFragment;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class CouponActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.tl_tab)
    private TabLayout tlTab;

    @ViewInject(R.id.vp_pager)
    private ViewPager vpPager;

    private String[] title = new String[]{"可使用", "不可使用"};

    private Adapter adapter;

    private List<Fragment> fragmentList;

    private String couponId;    //选中

    private List<ConfirmOrder.DataBean.CouponBean> coupons;    //可用
    private List<ConfirmOrder.DataBean.CouponBean> unCoupons;  //不可用

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_coupon);
    }

    @Override
    public void render() {
        tvTitle.setText("代金券");
        tvBack.setOnClickListener(this);

        Intent intent = getIntent();
        couponId = intent.getStringExtra("couponId");
        ConfirmOrder.DataBean dataBean = (ConfirmOrder.DataBean) intent.getSerializableExtra("data");

        coupons = new ArrayList<>();
        unCoupons = new ArrayList<>();

        List<ConfirmOrder.DataBean.CouponBean> all = dataBean.getCoupon();
        for (ConfirmOrder.DataBean.CouponBean coupon : all) {
            if (coupon.isEnable()) {
                coupons.add(coupon);
            } else {
                unCoupons.add(coupon);
            }
        }

        fragmentList = new ArrayList<>();
        fragmentList.add(new CouponFragment());
        fragmentList.add(new UnCouponFragment());

        adapter = new Adapter(getSupportFragmentManager());
        vpPager.setAdapter(adapter);
        tlTab.setupWithViewPager(vpPager);
        tlTab.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ((CouponFragment)fragmentList.get(0)).goBack();
                break;
            default:
                break;
        }
    }

    private class Adapter extends FragmentPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("getItem", position + "");
            return fragmentList.get(position);
        }
    }

    public List<ConfirmOrder.DataBean.CouponBean> getCoupons() {
        return coupons;
    }

    public String getCouponId() {
        return couponId;
    }

    public List<ConfirmOrder.DataBean.CouponBean> getUnCoupons() {
        return unCoupons;
    }
}
