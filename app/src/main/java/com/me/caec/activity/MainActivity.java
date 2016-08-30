package com.me.caec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

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

    //返回键点击次数
    private int flagBackCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        initFragment(R.id.rb_home);    //初始化默认选中的首页

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
    private void initFragment(int id) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transition = fm.beginTransaction();

        if (currentFragment != null) {
            transition.hide(currentFragment);
        }

        switch (id) {
            case R.id.rb_home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transition.add(R.id.fl_content, homeFragment);
                } else {
                    transition.show(homeFragment);
                }
                currentFragment = homeFragment;
                break;
            case R.id.rb_class:
                if (classFragment == null) {
                    classFragment = new ClassFragment();
                    transition.add(R.id.fl_content, classFragment);
                } else {
                    transition.show(classFragment);
                }
                currentFragment = classFragment;
                break;
            case R.id.rb_activity:
                if (activityFragment == null) {
                    activityFragment = new ActivityFragment();
                    transition.add(R.id.fl_content, activityFragment);
                } else {
                    transition.show(activityFragment);
                }
                currentFragment = activityFragment;
                break;
            case R.id.rb_cart:
                if (cartFragment == null) {
                    cartFragment = new CartFragment();
                    transition.add(R.id.fl_content, cartFragment);
                } else {
                    transition.show(cartFragment);
                }
                currentFragment = cartFragment;
                break;
            case R.id.rb_my:
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

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        if (flagBackCount < 1) {
            flagBackCount++;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    flagBackCount = 0;
                }
            }, 2500);
            Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
