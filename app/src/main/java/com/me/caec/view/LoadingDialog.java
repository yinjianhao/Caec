package com.me.caec.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.me.caec.R;

/**
 * loading弹出框
 * <p/>
 * Created by yin on 2016/9/7.
 */
public class LoadingDialog extends ProgressDialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
    }
}
