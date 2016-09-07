package com.me.caec.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.caec.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 评论页面
 */
public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        x.view().inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("发表评论");
        tvBack.setOnClickListener(this);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");

        getCommentDetail();
    }

    /**
     * 获取商品评论信息
     */
    private void getCommentDetail() {
        RequestParams params = new RequestParams();
        x.http().get(params, new Callback.CommonCallback<Object>() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Intent i = new Intent();
                i.putExtra("update", true);
                setResult(1, i);
                finish();
                break;
            default:
                break;
        }
    }
}
