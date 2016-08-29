package com.me.caec.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.me.caec.R;
import com.me.caec.utils.PreferencesUtils;

/**
 * 闪屏页面
 */
public class SplashActivity extends AppCompatActivity {

    //进入主页面
    private final int FLAG_GO_HOME = 1;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_GO_HOME:
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.sendEmptyMessageDelayed(FLAG_GO_HOME, 2000);
    }
}
