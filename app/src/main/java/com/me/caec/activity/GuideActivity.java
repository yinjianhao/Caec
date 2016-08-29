package com.me.caec.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.me.caec.R;
import com.me.caec.utils.DpTransforUtils;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 导航页
 */
public class GuideActivity extends AppCompatActivity {

    private final String TAG = "GuideActivity";

    @ViewInject(R.id.vp_guide)
    private ViewPager vpGuide;

    @ViewInject(R.id.ll_small_circle)
    private LinearLayout llSmallCircle;

    @ViewInject(R.id.iv_circle_select)
    private ImageView ivCircleSelect;

    @ViewInject(R.id.btn_enter)
    private Button btnEnter;

    private int[] ids = {R.drawable.guide0, R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        x.view().inject(this);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        vpGuide.setAdapter(new GuidePagerAdapter());
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int width = llSmallCircle.getWidth();
                int length = ids.length;
                ivCircleSelect.setTranslationX((int) (width * positionOffset / length + (float) position * width / length));
            }

            @Override
            public void onPageSelected(int position) {
                if (position == ids.length - 1) {
                    btnEnter.setVisibility(View.VISIBLE);
                } else {
                    btnEnter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.setBoolean(getApplicationContext(), "isFirstEnter", false);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
        initSmallCircle();
    }

    /**
     * 初始化viewpager的小圆圈
     */
    private void initSmallCircle() {
        for (int id : ids) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(R.drawable.small_circle_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = params.leftMargin = DpTransforUtils.dp2px(this, 3);
            iv.setLayoutParams(params);
            llSmallCircle.addView(iv);
        }
    }

    /**
     * viewpager的adapter
     */
    private class GuidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(GuideActivity.this);
            iv.setBackgroundResource(ids[position]);
            container.addView(iv);
            return iv;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
