package com.me.caec.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.me.caec.R;

import org.xutils.view.annotation.ViewInject;

public class InvoiceActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.cb_check)
    private CheckBox cbCheck;

    @ViewInject(R.id.tv_type)
    private TextView tvType;

    @ViewInject(R.id.ll_type)
    private LinearLayout llType;

    @ViewInject(R.id.ll_name)
    private LinearLayout llName;

    @ViewInject(R.id.et_name)
    private EditText etName;

    @ViewInject(R.id.btn_confirm)
    private Button btnConfirm;

    private int type;

    private String[] typeArray = new String[]{"个人", "公司"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_invoice);
    }

    @Override
    public void render() {
        tvTitle.setText("发票信息");
        tvBack.setOnClickListener(this);
        llType.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);

        if (type == 1) {
            cbCheck.setChecked(true);
        } else if (type == 2) {
            llName.setVisibility(View.VISIBLE);
            String companyName = intent.getStringExtra("companyName");
            cbCheck.setChecked(true);
            tvType.setText("公司");
            etName.setText(companyName);
            etName.setSelection(companyName.length());
        }
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
            case R.id.ll_type:
                changeType();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            default:
                break;
        }
    }

    private void confirm() {
        String companyName = "";
        if (!cbCheck.isChecked()) {
            type = -1;
        } else {
            if (type == 2) {
                companyName = etName.getText().toString();
                if (companyName.isEmpty()) {
                    Toast.makeText(this, "请填写公司名称", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        Intent i = new Intent();
        i.putExtra("type", type);
        i.putExtra("companyName", companyName);
        setResult(RESULT_OK, i);
        finish();
    }

    private void changeType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(typeArray, type - 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    type = 1;
                    tvType.setText("个人");
                    etName.setText("");
                    llName.setVisibility(View.GONE);
                } else {
                    type = 2;
                    tvType.setText("公司");
                    llName.setVisibility(View.VISIBLE);
                }
                type = which + 1;
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Invoice Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
