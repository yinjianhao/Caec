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
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.bean.AddressList;
import com.me.caec.bean.Location;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.LocationUtils;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseAddressActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.lv_address)
    private ListView lvAddress;

    @ViewInject(R.id.btn_new)
    private Button btnNew;

    private String receivingId;

    //地址列表数据
    private List<AddressList.DataBean> addressList;
    private Adapter adapter;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_choose_address);
    }

    @Override
    public void render() {
        tvTitle.setText("选择收货地址");
        tvBack.setOnClickListener(this);
        btnNew.setOnClickListener(this);

        final Intent intent = getIntent();
        receivingId = intent.getStringExtra("receivingId");

        adapter = new Adapter();
        lvAddress.setAdapter(adapter);
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressList.DataBean dataBean = addressList.get(position);

                Intent i = new Intent();
                i.putExtra("receivingId", dataBean.getId());
                i.putExtra("receiver", dataBean.getReceiver());
                i.putExtra("mobile", dataBean.getMobile());
                i.putExtra("provinceName", dataBean.getProvinceName());
                i.putExtra("cityName", dataBean.getCityName());
                i.putExtra("areaName", dataBean.getAreaName());
                i.putExtra("address", dataBean.getAddress());
                i.putExtra("zip", dataBean.getZip());
                setResult(RESULT_OK, i);
                finish();
            }
        });

        getAddressList();
    }

    /**
     * 获取地址列表
     */
    private void getAddressList() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", PreferencesUtils.getString(this, "token", ""));

        BaseClient.post(this, RequestUrl.ADDRESS_LIST_URL, map, AddressList.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                AddressList data = (AddressList) result;
                if (data.getResult() == 0) {
                    addressList = data.getData();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onShow() {

    }

    @Override
    public void reloadData() {
        getAddressList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_new:
                Intent intent = new Intent(this, CreateAddressActivity.class);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return addressList == null ? 0 : addressList.size();
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
                convertView = View.inflate(ChooseAddressActivity.this, R.layout.listview_item_chooseaddress_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            AddressList.DataBean dataBean = addressList.get(position);
            viewHolder.tvReceiver.setText(dataBean.getReceiver());
            viewHolder.tvPhone.setText(dataBean.getMobile());
            if (dataBean.isIsDeafault()) {
                viewHolder.tvDefault.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvDefault.setVisibility(View.GONE);
            }

            if (receivingId == null || receivingId.equals("")) {
                if (dataBean.isIsDeafault()) {
                    viewHolder.cbCheck.setChecked(true);
                } else {
                    viewHolder.cbCheck.setChecked(false);
                }
            } else {
                if (dataBean.getId().equals(receivingId)) {
                    viewHolder.cbCheck.setChecked(true);
                } else {
                    viewHolder.cbCheck.setChecked(false);
                }
            }

            List<Location.DataBean> location = LocationUtils.getLocation(ChooseAddressActivity.this);

            if (dataBean.getProvinceName().isEmpty()) {
                String provinceName = LocationUtils.findProvinceNameWithId(location, dataBean.getProvinceId());
                dataBean.setProvinceName(provinceName);
            }

            if (dataBean.getCityName().isEmpty()) {
                String cityName = LocationUtils.findCityNameWithId(location, dataBean.getCityId());
                dataBean.setCityName(cityName);
            }

            if (dataBean.getAreaName().isEmpty()) {
                String areaName = LocationUtils.findAreaNameWithId(location, dataBean.getAreaId());
                dataBean.setAreaName(areaName);
            }

            String address = dataBean.getProvinceName() +
                    dataBean.getCityName() +
                    dataBean.getAreaName() +
                    dataBean.getAddress() +
                    "  " +
                    dataBean.getZip();

            viewHolder.tvAddressZip.setText(address);

            return convertView;
        }
    }

    private class ViewHolder {

        TextView tvReceiver;
        TextView tvPhone;
        TextView tvDefault;
        TextView tvAddressZip;
        CheckBox cbCheck;

        public ViewHolder(View view) {
            tvReceiver = (TextView) view.findViewById(R.id.tv_receiver);
            tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            tvDefault = (TextView) view.findViewById(R.id.tv_default);
            tvAddressZip = (TextView) view.findViewById(R.id.tv_address_zip);
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
        }
    }
}
