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
public class ConfirmDialog extends Dialog implements View.OnClickListener {

    private String title;
    private String body;
    private OnConfirmListener listener;

    public ConfirmDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        if (title != null) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }

        TextView tvBody = (TextView) findViewById(R.id.tv_body);
        if (body != null) {
            tvBody.setText(body);
        }

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 设置内容
     *
     * @param body 内容
     */
    public void setBody(String body) {
        this.body = body;
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
