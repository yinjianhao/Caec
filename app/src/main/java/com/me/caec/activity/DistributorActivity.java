package com.me.caec.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.me.caec.bean.Location;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.LocationUtils;

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

    @ViewInject(R.id.tv_select)
    private TextView tvSelect;

    @ViewInject(R.id.lv_list)
    private ListView lvList;

    @ViewInject(R.id.btn_confirm)
    private Button btnConfirm;

    private List<Distributor.DataBean> dataBeanList;

    private Adapter adapter;

    private int dealerId;
    private int provinceId;
    private int cityId;
    private int carId;

    private int checkedPosition = -1;

    private List<Location.DataBean> provinceList;
    private String[] provinceNameArray;
    private int tempProvinceId;  //缓存id,当选择市之后才替换provinceId
    private String provinceName;

    private List<Location.DataBean.CityBean> cityList;
    private String[] cityNameArray;
    private String cityName;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_distributor);
    }

    @Override
    public void render() {
        tvTitle.setText("选择经销商");
        tvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        tvSelect.setOnClickListener(this);

        Intent intent = getIntent();
        dealerId = intent.getIntExtra("dealerId", -1);
        provinceId = intent.getIntExtra("provinceId", 23);   //默认重庆
        cityId = intent.getIntExtra("cityId", 268);
        carId = intent.getIntExtra("carId", -1);

        provinceName = LocationUtils.findProvinceNameWithId(this, provinceId);
        cityName = LocationUtils.findCityNameWithId(this, cityId);
        if (provinceName != null && cityName != null) {
            tvSelect.setText(provinceName + " " + cityName);
        }

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
            case R.id.tv_select:
                showProvinceDialog();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            default:
                break;
        }
    }

    /**
     * 选择省份弹出框
     */
    private void showProvinceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择省份");
        if (provinceList == null) {
            provinceList = LocationUtils.getLocation(this);
            if (provinceList != null) {
                int length = provinceList.size();
                provinceNameArray = new String[length];
                for (int i = 0; i < length; i++) {
                    provinceNameArray[i] = provinceList.get(i).getAreaname();
                }
            }
        }

        builder.setSingleChoiceItems(provinceNameArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempProvinceId = provinceList.get(which).getAreaid();
                provinceName = provinceList.get(which).getAreaname();
                showCityDialog();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 选择市弹出框
     */
    private void showCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择市");

        cityList = LocationUtils.findCityBeanWithProvinceId(provinceList, tempProvinceId);
        if (cityList != null) {
            int length = cityList.size();
            cityNameArray = new String[length];
            for (int i = 0; i < length; i++) {
                cityNameArray[i] = cityList.get(i).getAreaname();
            }
        }

        builder.setSingleChoiceItems(cityNameArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                provinceId = tempProvinceId;
                cityId = cityList.get(which).getAreaid();
                //填入省市信息
                cityName = cityList.get(which).getAreaname();
                tvSelect.setText(provinceName + " " + cityName);
                checkedPosition = -1;
                getDistributorList();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void confirm() {
        if (checkedPosition == -1) {
            Toast.makeText(getApplicationContext(), "请选择经销商", Toast.LENGTH_SHORT).show();
            return;
        }

        Distributor.DataBean dataBean = dataBeanList.get(checkedPosition);

        Intent intent = new Intent();
        intent.putExtra("id", dataBean.getId());
        intent.putExtra("dealerId", dataBean.getDealerId());
        intent.putExtra("dealerName", dataBean.getName());
        intent.putExtra("provinceId", provinceId);
        intent.putExtra("provinceName", provinceName);
        intent.putExtra("cityId", cityId);
        intent.putExtra("cityName", cityName);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void processData() {
        if (dealerId != -1) {
            for (Distributor.DataBean dataBean : dataBeanList) {
                if (dataBean.getDealerId() == dealerId) {
                    dataBeanList.remove(dataBean);
                    dataBeanList.add(0, dataBean);
                    checkedPosition = 0;
                    break;
                }
            }
            dealerId = -1;
        }
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
                    processData();
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

            if (checkedPosition == position) {
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
