package com.me.caec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

/**
 * 基础activity类
 *
 * @auther yjh
 * @date 2016/9/13
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        x.view().inject(this);

        render();
        onShow();
    }

    /**
     * 初始化页面(只调用一次)
     */
    public abstract void render();

    /**
     * 初始化数据(可以多次调用)
     */
    public abstract void onShow();

    /**
     * 重新加载数据
     */
    public void reloadData() {
        onShow();
    }

    /**
     * 上个页面返回  reload == true时,刷新数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK && data.getBooleanExtra("reload", false)) {
            reloadData();
        }
    }
}
