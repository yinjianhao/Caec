package com.me.caec.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.me.caec.R;

import org.xutils.x;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        x.view().inject(this);
        initView();
    }

    private void initView() {

    }
}
