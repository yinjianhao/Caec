package com.me.caec.fragment;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.bean.BaseBean;
import com.me.caec.bean.CartList;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.ImageUtils;
import com.me.caec.utils.NumberUtils;
import com.me.caec.utils.PreferencesUtils;
import com.me.caec.view.CartDialog;
import com.me.caec.view.ConfirmDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车
 * Created by yin on 2016/8/29.
 */

@ContentView(R.layout.fragment_cart)
public class CartFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.tv_edit)
    private TextView tvEdit;

    @ViewInject(R.id.lv_cart)
    private ListView lvCart;

    @ViewInject(R.id.cb_check_all)
    private CheckBox cbCheckAll;

    @ViewInject(R.id.tv_price)
    private TextView tvPrice;

    @ViewInject(R.id.btn_settlement)
    private Button btnSettlement;

    //是否是编辑状态
    private boolean isEdit = false;
    private List<CartList.DataBean> cartList;
    private Adapter adapter;

    //当前选中的
    private List<Integer> CheckedList = new ArrayList<>();

    @Override
    public void initData() {
        tvEdit.setOnClickListener(this);

        getCartList();

        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                ConfirmDialog dialog = new ConfirmDialog(getActivity());
                dialog.setBody("你确定要删除此商品吗?");
                dialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void confirm() {
                        deleteCart(position);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                dialog.show();
                return false;
            }
        });

        cbCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckedList.clear();
                if (cbCheckAll.isChecked()) {
                    for (int i = 0, l = adapter.getCount(); i < l; i++) {
                        CheckedList.add(i);
                    }
                }
                adapter.notifyDataSetChanged();
                countPrice();
            }
        });
    }

    /**
     * 删除购物车
     */
    private void deleteCart(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", PreferencesUtils.getString(getActivity(), "token", ""));
        String[] ids = new String[]{cartList.get(position).getCartItemId()};
        map.put("cartItemIds", Arrays.toString(ids));

        BaseClient.post(getActivity(), RequestUrl.CART_DELETE_URL, map, BaseBean.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                BaseBean data = (BaseBean) result;

                if (data.getResult() == 0) {
                    cartList.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
     * 获取购物车列表
     */
    private void getCartList() {

        Map<String, String> map = new HashMap<>();
        map.put("token", PreferencesUtils.getString(getActivity(), "token", ""));
        map.put("pageIndex", "1");
        map.put("pageSize", "20");

        BaseClient.get(getActivity(), RequestUrl.CART_LIST_URL, map, CartList.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                CartList data = (CartList) result;

                if (data.getResult() == 0) {
                    cartList = data.getData();
                    if (adapter == null) {
                        adapter = new Adapter();
                        lvCart.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit:

                break;
            default:
                break;
        }
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cartList.size();
        }

        @Override
        public CartList.DataBean getItem(int position) {
            return cartList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.listview_item_cart_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            CartList.DataBean dataBean = getItem(position);

            viewHolder.position = position;   //设置当前位置
            viewHolder.cbCheck.setChecked(CheckedList.contains(position));
            x.image().bind(viewHolder.ivGood, dataBean.getImg(), ImageUtils.getDefaultImageOptions());
            viewHolder.tvGood.setText(dataBean.getName());
            viewHolder.tvDesc.setText(dataBean.getProp());
            viewHolder.tvNum.setText(String.valueOf(dataBean.getCount()));
            viewHolder.tvPrice.setText("¥" + NumberUtils.toFixed2(dataBean.getPrice()));
            viewHolder.tvOriginalPrice.setText("¥" + NumberUtils.toFixed2(dataBean.getOriginalPrice()));
            viewHolder.tvOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            viewHolder.tvOriginalPrice.setTag(position);

            viewHolder.tvPlus.setEnabled(dataBean.getCount() == 1);
            viewHolder.tvAdd.setEnabled(dataBean.getCount() >= dataBean.getStock());

            return convertView;
        }
    }

    private class ViewHolder {

        int position;

        RelativeLayout rlCheck;
        CheckBox cbCheck;
        ImageView ivGood;
        TextView tvGood;
        TextView tvDesc;
        TextView tvPrice;
        TextView tvOriginalPrice;
        TextView tvPlus;
        TextView tvNum;
        TextView tvAdd;

        ViewHolder(View view) {
            rlCheck = (RelativeLayout) view.findViewById(R.id.rl_check);
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
            ivGood = (ImageView) view.findViewById(R.id.iv_good);
            tvGood = (TextView) view.findViewById(R.id.tv_good);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvOriginalPrice = (TextView) view.findViewById(R.id.tv_originalPrice);
            tvPlus = (TextView) view.findViewById(R.id.tv_plus);
            tvNum = (TextView) view.findViewById(R.id.tv_num);
            tvAdd = (TextView) view.findViewById(R.id.tv_add);

            rlCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbCheck.isChecked()) {
                        CheckedList.remove((Integer) position);
                        cbCheck.setChecked(false);
                    } else {
                        CheckedList.add(position);
                        cbCheck.setChecked(true);
                    }

                    //是否全选
                    cbCheckAll.setChecked(CheckedList.size() == adapter.getCount());

                    countPrice();  //计算总价
                }
            });

            tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvOriginalPrice.getTag();
                    CartList.DataBean dataBean = cartList.get(position);

                    int count = dataBean.getCount();
                    int stock = dataBean.getStock();


                }
            });

            tvNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvOriginalPrice.getTag();

                    int num = Integer.parseInt(tvNum.getText().toString());

                    Log.d("CartFragment", String.valueOf(position));

                    CartDialog cartDialog = new CartDialog(getActivity());
                    cartDialog.show();
                }
            });

            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tvOriginalPrice.getTag();

                    int num = Integer.parseInt(tvNum.getText().toString());

                    Log.d("CartFragment", String.valueOf(position));
                }
            });
        }
    }

    /**
     * 计算总价
     */
    private void countPrice() {

        float price = 0;

        for (int position : CheckedList) {
            CartList.DataBean dataBean = cartList.get(position);
            price += dataBean.getCount() * dataBean.getPrice();
        }

        btnSettlement.setText("去结算(" + CheckedList.size() + ")");
        tvPrice.setText(NumberUtils.toFixed2(price));
    }
}
