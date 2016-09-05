package com.me.caec.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;
import com.me.caec.view.pagerView.OrderListPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.tl_tab)
    private TabLayout tlTab;

    @ViewInject(R.id.vp_pager)
    private ViewPager vpPager;

    private String[] title = new String[]{"全部", "待付款", "待提车", "待收货", "待评价", "售后/退款"};

    private Adapter adapter;

    private OrderListPager allPager;
    private OrderListPager unpaidPager;
    private OrderListPager uncarPager;
    private OrderListPager unreceivedPager;
    private OrderListPager uncommentPager;
    private OrderListPager serviecePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("订单列表");
        tvBack.setOnClickListener(this);

        adapter = new Adapter();
        vpPager.setAdapter(adapter);
        tlTab.setupWithViewPager(vpPager);
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

    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            switch (position) {
                case 0:
                    if (allPager == null) {
                        allPager = new OrderListPager(OrderListActivity.this, OrderListPager.TYPE_ALL);
                        allPager.initData();
                    }
                    view = allPager.getRootView();
                    break;
                case 1:
                    if (unpaidPager == null) {
                        unpaidPager = new OrderListPager(OrderListActivity.this, OrderListPager.TYPE_UNPAID);
                        unpaidPager.initData();
                    }
                    view = unpaidPager.getRootView();
                    break;
                case 2:
                    if (uncarPager == null) {
                        uncarPager = new OrderListPager(OrderListActivity.this, OrderListPager.TYPE_UNCAR);
                        uncarPager.initData();
                    }
                    view = uncarPager.getRootView();
                    break;
                case 3:
                    if (unreceivedPager == null) {
                        unreceivedPager = new OrderListPager(OrderListActivity.this, OrderListPager.TYPE_UNRECEIVED);
                        unreceivedPager.initData();
                    }
                    view = unreceivedPager.getRootView();
                    break;
                case 4:
                    if (uncommentPager == null) {
                        uncommentPager = new OrderListPager(OrderListActivity.this, OrderListPager.TYPE_UNCOMMENT);
                        uncommentPager.initData();
                    }
                    view = uncommentPager.getRootView();
                    break;
                case 5:
                    if (serviecePager == null) {
                        serviecePager = new OrderListPager(OrderListActivity.this, OrderListPager.TYPE_SERVICE);
                        serviecePager.initData();
                    }
                    view = serviecePager.getRootView();
                    break;
                default:
                    break;
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
