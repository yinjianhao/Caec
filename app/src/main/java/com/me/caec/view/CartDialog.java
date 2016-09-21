package com.me.caec.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.me.caec.R;

/**
 * 确认取消对话框
 *
 * @auther yjh
 * @date 2016/8/31
 */
public class CartDialog extends Dialog implements View.OnClickListener {

    private OnOperationListener listener;

    private EditText etNum;
    private int num;
    private int stock;
    private TextView tvAdd;
    private TextView tvPlus;

    public CartDialog(Context context, int num, int stock) {
        super(context);
        this.num = num;
        this.stock = stock;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cart);

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        tvAdd = (TextView) findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(this);

        tvPlus = (TextView) findViewById(R.id.tv_plus);
        tvPlus.setOnClickListener(this);

        etNum = (EditText) findViewById(R.id.et_num);
        String s = String.valueOf(num);
        etNum.setText(s);
        etNum.setSelection(s.length());

        if (num == 1) {
            tvPlus.setEnabled(false);
        }

        if (num >= stock || num >= 99) {
            tvAdd.setEnabled(false);
        }

        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")) {
//                    etNum.setText("1");
                    tvPlus.setEnabled(false);
                    num = 1;
                } else {
                    int countNum = Integer.parseInt(s.toString());
                    if (countNum == 1) {
                        tvPlus.setEnabled(false);
                    } else {
                        tvPlus.setEnabled(true);
                    }

                    if (countNum >= stock) {
                        countNum = stock;
                        tvAdd.setEnabled(false);
                    } else {
                        tvAdd.setEnabled(true);
                    }

//                    etNum.setText(String.valueOf(countNum));
                    num = countNum;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnOperationListener(OnOperationListener listener) {
        this.listener = listener;
    }

    public interface OnOperationListener {
        void confirm(int num);

        void cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (listener != null) {
                    listener.cancel();
                }
                dismiss();
                break;
            case R.id.btn_confirm:
                if (listener != null) {
                    listener.confirm(num);
                }
                dismiss();
                break;
            case R.id.tv_add:
                add();
                break;
            case R.id.tv_plus:
                plus();
                break;
            default:
                break;
        }
    }

    private void plus() {
        num--;

        if (num == 1) {
            tvPlus.setEnabled(false);
        }

        if (num >= stock) {
            num = stock;
            tvAdd.setEnabled(false);
        } else {
            tvAdd.setEnabled(true);
        }

        String s = String.valueOf(num);
        etNum.setText(s);
        etNum.setSelection(s.length());
    }

    private void add() {
        if (num == 1) {
            tvPlus.setEnabled(true);
        }

        num++;

        if (num >= stock || num >= 99) {
            tvAdd.setEnabled(false);
        }

        String s = String.valueOf(num);
        etNum.setText(s);
        etNum.setSelection(s.length());
    }
}
