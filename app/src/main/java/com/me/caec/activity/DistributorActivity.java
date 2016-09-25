package com.me.caec.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.bean.Distributor;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributorActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.lv_list)
    private ListView lvList;

    @ViewInject(R.id.btn_confirm)
    private Button btnConfirm;

    private List<Distributor.DataBean> dataBeanList;

    private Adapter adapter;

    private int id;
    private int dealerId;
    private int provinceId;
    private int cityId;
    private int carId;

    private int checkedPosition = -1;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_distributor);
    }

    @Override
    public void render() {
        tvTitle.setText("选择经销商");
        tvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        dealerId = intent.getIntExtra("id", -1);
        provinceId = intent.getIntExtra("id", 23);
        cityId = intent.getIntExtra("cityId", 268);
        carId = intent.getIntExtra("carId", -1);

        adapter = new Adapter();
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkedPosition = position;
                adapter.notifyDataSetChanged();
            }
        });

        getDistributorList();
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
            case R.id.btn_confirm:
                confirm();
                break;
            default:
                break;
        }
    }

    private void confirm() {
        Distributor.DataBean dataBean = dataBeanList.get(checkedPosition);

        Intent intent = new Intent();
        intent.putExtra("id", dataBean.getId());
        intent.putExtra("dealerId", dataBean.getDealerId());
        intent.putExtra("dealerName", dataBean.getName());
        intent.putExtra("provinceId", provinceId);
        intent.putExtra("provinceName", "重庆");
        intent.putExtra("cityId", cityId);
        intent.putExtra("cityName", "重庆市");
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 获取经销商列表
     */
    private void getDistributorList() {
        Map<String, String> map = new HashMap<>();
        map.put("cityId", String.valueOf(cityId));
        map.put("carId", String.valueOf(carId));

        BaseClient.get(this, RequestUrl.DISTRIBUTOR_LIST_URL, map, Distributor.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                Distributor data = (Distributor) result;

                if (data.getResult() == 0) {
                    dataBeanList = data.getData();
                    adapter.notifyDataSetChanged();
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

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataBeanList == null ? 0 : dataBeanList.size();
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
                convertView = View.inflate(DistributorActivity.this, R.layout.listview_item_distributor_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Distributor.DataBean dataBean = dataBeanList.get(position);

            if(checkedPosition == position) {
                viewHolder.cbCheck.setChecked(true);
            } else {
                viewHolder.cbCheck.setChecked(false);
            }

            viewHolder.tvName.setText(dataBean.getName());
            viewHolder.tvAddress.setText("地址:" + dataBean.getAddress());
            viewHolder.tvTel.setText("电话:" + dataBean.getTel());
            return convertView;
        }
    }

    private class ViewHolder {

        public CheckBox cbCheck;
        public TextView tvName;
        public TextView tvAddress;
        public TextView tvTel;

        public ViewHolder(View view) {
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvTel = (TextView) view.findViewById(R.id.tv_tel);
        }
    }
}
