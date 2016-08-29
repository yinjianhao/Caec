package com.me.caec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.me.caec.R;
import com.me.caec.fragment.ActivityFragment;
import com.me.caec.fragment.BaseFragment;
import com.me.caec.fragment.CartFragment;
import com.me.caec.fragment.ClassFragment;
import com.me.caec.fragment.HomeFragment;
import com.me.caec.fragment.MyFragment;
import com.me.caec.utils.PreferencesUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.security.PolicySpi;
import java.util.ArrayList;

/**
 * 主页面
 */
public class MainActivity extends FragmentActivity {

    @ViewInject(R.id.fl_content)
    private FrameLayout flContent;

    @ViewInject(R.id.rg_footer)
    private RadioGroup rgFooter;

    //对应footer的fragment
    private HomeFragment homeFragment;
    private ClassFragment classFragment;
    private ActivityFragment activityFragment;
    private CartFragment cartFragment;
    private MyFragment myFragment;

    //当前选中的fragment
    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //第一次进来时跳向GuideActivity
        Boolean isFirstEnter = PreferencesUtils.getBoolean(getApplicationContext(), "isFirstEnter", true);
        if (isFirstEnter) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        initFragment(0);    //初始化默认选中的首页

        //给footer设置改变的监听事件
        rgFooter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initFragment(checkedId);
            }
        });
    }

    /**
     * 初始化footer对应的fragment
     */
    private void initFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transition = fm.beginTransaction();

        if (currentFragment != null) {
            transition.hide(currentFragment);
        }

        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transition.add(R.id.fl_content, homeFragment);
                } else {
                    transition.show(homeFragment);
                }
                currentFragment = homeFragment;
                break;
            case 1:
                if (classFragment == null) {
                    classFragment = new ClassFragment();
                    transition.add(R.id.fl_content, classFragment);
                } else {
                    transition.show(classFragment);
                }
                currentFragment = classFragment;
                break;
            case 2:
                if (activityFragment == null) {
                    activityFragment = new ActivityFragment();
                    transition.add(R.id.fl_content, activityFragment);
                } else {
                    transition.show(activityFragment);
                }
                currentFragment = activityFragment;
                break;
            case 3:
                if (cartFragment == null) {
                    cartFragment = new CartFragment();
                    transition.add(R.id.fl_content, cartFragment);
                } else {
                    transition.show(cartFragment);
                }
                currentFragment = cartFragment;
                break;
            case 4:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transition.add(R.id.fl_content, myFragment);
                } else {
                    transition.show(myFragment);
                }
                currentFragment = myFragment;
                break;
            default:
                break;
        }

        transition.commit();
    }
}
