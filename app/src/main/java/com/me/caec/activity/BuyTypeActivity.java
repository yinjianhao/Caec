package com.me.caec.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.caec.R;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.view.annotation.ViewInject;

public class BuyTypeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.et_name)
    private EditText etName;

    @ViewInject(R.id.et_phone)
    private EditText etPhone;

    @ViewInject(R.id.et_card)
    private EditText etCard;

    @ViewInject(R.id.et_company_name)
    private EditText etCompanyName;

    @ViewInject(R.id.et_license)
    private EditText etLicense;

    @ViewInject(R.id.et_charge_name)
    private EditText etChargeName;

    @ViewInject(R.id.et_charge_phone)
    private EditText etChargePhone;

    @ViewInject(R.id.ll_person)
    private LinearLayout llPerson;

    @ViewInject(R.id.ll_company)
    private LinearLayout llCompany;

    @ViewInject(R.id.btn_confirm)
    private Button btnConfirm;

    private String[] typeArray = new String[]{"个人购买", "企业购买"};

    private int type;   //1个人 2企业
    private String receiver;
    private String mobile;
    private String no;
    private String name;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_buy_type);
    }

    @Override
    public void render() {
        tvTitle.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        receiver = intent.getStringExtra("receiver");
        mobile = intent.getStringExtra("mobile") == null ?
                PreferencesUtils.getString(this, "phone", "") : intent.getStringExtra("mobile");
        no = intent.getStringExtra("no");
        name = intent.getStringExtra("name");

        if (type == 1) {
            etName.setText(receiver);
            etCard.setText(no);
        } else {
            etCompanyName.setText(name);
            etChargeName.setText(receiver);
            etLicense.setText(no);
            llPerson.setVisibility(View.GONE);
            llCompany.setVisibility(View.VISIBLE);
        }
        etPhone.setText(mobile);
        etChargePhone.setText(mobile);
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
            case R.id.tv_title:
                chooseType();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            default:
                break;
        }
    }

    private void confirm() {
        if (type == 1) {
            receiver = etName.getText().toString();
            mobile = etPhone.getText().toString();
            no = etCard.getText().toString();
            name = "";
        } else {
            receiver = etChargeName.getText().toString();
            mobile = etChargePhone.getText().toString();
            no = etLicense.getText().toString();
            name = etCompanyName.getText().toString();
        }

        if (receiver.isEmpty() || mobile.isEmpty() || no.isEmpty() || (type == 2 && name.isEmpty())) {
            Toast.makeText(this, "请填写所有信息", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("receiver", receiver);
        intent.putExtra("mobile", mobile);
        intent.putExtra("no", no);
        intent.putExtra("name", name);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void chooseType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(typeArray, type - 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    llPerson.setVisibility(View.VISIBLE);
                    llCompany.setVisibility(View.GONE);
                } else {
                    llPerson.setVisibility(View.GONE);
                    llCompany.setVisibility(View.VISIBLE);
                }
                type = which + 1;
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
