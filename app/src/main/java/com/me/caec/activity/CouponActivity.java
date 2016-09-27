package com.me.caec.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;

import org.xutils.view.annotation.ViewInject;

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

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_coupon);
    }

    @Override
    public void render() {
        tvTitle.setText("代金券");
        tvBack.setOnClickListener(this);

        adapter = new Adapter();
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
