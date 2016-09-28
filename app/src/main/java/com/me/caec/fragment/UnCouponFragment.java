package com.me.caec.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.me.caec.R;
import com.me.caec.activity.CouponActivity;
import com.me.caec.bean.ConfirmOrder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 不可用优惠卷
 * Created by yin on 2016/8/29.
 */

@ContentView(R.layout.fragment_uncoupon)
public class UnCouponFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView lvList;

    @ViewInject(R.id.tv_empty)
    private TextView tvEmpty;

    private List<ConfirmOrder.DataBean.CouponBean> unCoupons;

    private Adapter adapter;

    @Override
    public void initData() {
//        unCoupons = ((CouponActivity) getActivity()).getUnCoupons();
//        lvList.setEmptyView(tvEmpty);
//        adapter = new Adapter();
//        lvList.setAdapter(adapter);
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
