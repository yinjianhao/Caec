package com.me.caec.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.me.caec.R;
import com.me.caec.activity.CouponActivity;
import com.me.caec.bean.ConfirmOrder;
import com.me.caec.utils.NumberUtils;

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
        unCoupons = ((CouponActivity) getActivity()).getUnCoupons();
        lvList.setEmptyView(tvEmpty);
        adapter = new Adapter();
        lvList.setAdapter(adapter);
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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.listview_item_coupon, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ConfirmOrder.DataBean.CouponBean coupon = unCoupons.get(position);

            viewHolder.tvTitle.setText(coupon.getTypeName());
            viewHolder.tvPrice.setText(NumberUtils.toFixed2(coupon.getPrice()));
            viewHolder.tvTime.setText(coupon.getStartDate().substring(0, 10) + "-" + coupon.getEndDate().substring(0, 10));
            viewHolder.tvApply.setText("使用范围:" + coupon.getDesc());

            return convertView;
        }
    }

    private class ViewHolder {

        public CheckBox cbCheck;
        public TextView tvTitle;
        public TextView tvPrice;
        public TextView tvTime;
        public TextView tvApply;
        public LinearLayout llApply;
        public ImageView ivDown;

        public ViewHolder(View view) {
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvApply = (TextView) view.findViewById(R.id.tv_apply);
            llApply = (LinearLayout) view.findViewById(R.id.ll_apply);
            ivDown = (ImageView) view.findViewById(R.id.iv_down);

            llApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvApply.getVisibility() == View.GONE) {
                        tvApply.setVisibility(View.VISIBLE);
                        ivDown.setImageResource(R.drawable.ic_action_up_item);
                    } else {
                        tvApply.setVisibility(View.GONE);
                        ivDown.setImageResource(R.drawable.ic_action_down_item);
                    }
                }
            });
        }
    }
}
