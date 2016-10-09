package com.me.caec.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.me.caec.R;
import com.me.caec.bean.HomeList;
import com.me.caec.globle.BaseClient;
import com.me.caec.globle.RequestUrl;
import com.me.caec.utils.DpTransforUtils;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 * Created by yin on 2016/8/29.
 */

@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.ll_small_circle)
    private LinearLayout llSmallCircle;

    @ViewInject(R.id.iv_circle_select)
    private ImageView ivCircleSelect;

    @ViewInject(R.id.vp_pager)
    private ViewPager vpPager;

    private List<HomeList.DataBean> ads;
    private List<HomeList.DataBean> cars;
    private List<HomeList.DataBean> parts;
    private List<HomeList.DataBean> banners;

    private Adapter adapter;

    @Override
    public void initData() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）

        ViewGroup.LayoutParams l = vpPager.getLayoutParams();
        l.width = width;
        l.height = width / 2;

        getHomeList("app_home_ad");
    }

    private void getHomeList(final String channelId) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "0");
        map.put("channelId", channelId);

        BaseClient.get(getActivity(), RequestUrl.HOME_ACTIVITY, map, HomeList.class, new BaseClient.BaseCallBack() {
            @Override
            public void onSuccess(Object result) {
                HomeList data = (HomeList) result;

                if (data.getResult() == 0) {
                    // TODO: 2016/10/9
                    switch (channelId) {
                        case "app_home_ad":
                            ads = data.getData();
                            initAd();
                            break;
                        case "app_home_car":
                            cars = data.getData();
                            initCar();
                            break;
                        case "app_home_banner":
                            banners = data.getData();
                            initBanner();
                            break;
                        case "app_home_parts":
                            parts = data.getData();
                            initPart();
                            break;
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initPart() {

    }

    private void initBanner() {

    }

    private void initCar() {

    }

    private void initAd() {
        if (adapter == null) {
            adapter = new Adapter();
            vpPager.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        initSmallCircle();
    }

    /**
     * 初始化viewpager的小圆圈
     */
    private void initSmallCircle() {
        llSmallCircle.removeAllViews();
        for (int i = 0, l = ads.size(); i < l; i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setBackgroundResource(R.drawable.small_circle_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = params.leftMargin = DpTransforUtils.dp2px(getActivity(), 3);
            iv.setLayoutParams(params);
            llSmallCircle.addView(iv);
        }
        ivCircleSelect.setVisibility(View.VISIBLE);
    }

    /**
     * viewpager的adapter
     */
    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ads.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            HomeList.DataBean dataBean = ads.get(position);

            ImageView iv = new ImageView(getActivity());

            ImageOptions.Builder builder = new ImageOptions.Builder();
            builder.setLoadingDrawableId(R.drawable.placeholder_400_200);
            x.image().bind(iv, dataBean.getImg(), builder.build());

            container.addView(iv);
            return iv;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
