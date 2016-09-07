package com.me.caec.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.me.caec.R;
import com.me.caec.bean.CommentList;
import com.me.caec.globle.Client;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 评论页面
 */
public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.btn_back)
    private LinearLayout tvBack;

    @ViewInject(R.id.lv_comment)
    private ListView lvComment;

    private String orderId;
    private List<CommentList.DataBean> commentList;
    private Adapter adapter;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_confirm:
                confirmComment();
                break;
            default:
                break;
        }
    }

    /**
     * 获取商品评论信息
     */
    private void getCommentDetail() {
        RequestParams params = new RequestParams(Client.COMMENT_LIST_URL);
        params.addBodyParameter("token", PreferencesUtils.getString(this, "token", ""));
        params.addBodyParameter("orderId", orderId);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommentList list = JSON.parseObject(result, CommentList.class);
                if (list.getResult() == 0) {
                    commentList = list.getData();
                    adapter = new Adapter();
                    lvComment.setAdapter(adapter);
                    lvComment.addFooterView(View.inflate(CommentActivity.this, R.layout.listview_footer_comment_list, null));
                } else {
                    Toast.makeText(CommentActivity.this, "获取评论数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommentActivity.this, "数据获取失败,请稍候再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 提交评论
     */
    private void confirmComment() {

    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public CommentList.DataBean getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(CommentActivity.this, R.layout.listview_item_comment_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            CommentList.DataBean dataBean = getItem(position);

            ImageOptions.Builder builder = new ImageOptions.Builder();
            builder.setLoadingDrawableId(R.drawable.placeholder_200_200);
            ImageOptions options = builder.build();
            x.image().bind(viewHolder.ivGood, dataBean.getImg(), options);

            return convertView;
        }
    }

    private class ViewHolder {

        private ImageView ivGood;
        private EditText etDesc;
        private LinearLayout llPhotoContent;
        private LinearLayout llTakePhoto;
        private RatingBar ratingBar;

        public ViewHolder(View view) {
            ivGood = (ImageView) view.findViewById(R.id.iv_good);
            etDesc = (EditText) view.findViewById(R.id.et_desc);
            llPhotoContent = (LinearLayout) view.findViewById(R.id.ll_photo_content);
            llTakePhoto = (LinearLayout) view.findViewById(R.id.ll_take_photo);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }
}
