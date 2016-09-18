package com.me.caec.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.me.caec.R;

/**
 * 确认取消对话框
 *
 * @auther yjh
 * @date 2016/8/31
 */
public class CartDialog extends Dialog implements View.OnClickListener {

    private OnConfirmListener listener;

    public CartDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cart);

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmListener {
        void confirm();

        void cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (listener != null) {
                    listener.cancel();
                }
                break;
            case R.id.btn_confirm:
                if (listener != null) {
                    listener.confirm();
                }
                break;
            default:
                break;
        }
        dismiss();
    }
}
