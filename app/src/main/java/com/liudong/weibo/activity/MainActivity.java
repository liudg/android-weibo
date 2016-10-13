package com.liudong.weibo.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.liudong.weibo.ActivityManager;
import com.liudong.weibo.BaseApplication;
import com.liudong.weibo.R;
import com.liudong.weibo.fragment.FragmentController;
import com.liudong.weibo.utils.ToastUtils;

/**
 * 该类主要演示如何进行授权、SSO登陆。
 * 参考博客：http://blog.csdn.net/nihaoqiulinhe/article/details/16822279
 */

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private long firstTime = 0;

    private RadioGroup rg_tab;
    private FragmentController controller;
    private BaseApplication application;
    private RadioButton rb_home;
    private ImageView iv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        controller = FragmentController.getInstance(this, R.id.fl_content);
        controller.showFragment(0); //默认展示第一个
        initView();

        //添加Activity到容器中
        ActivityManager.getInstance().addActivity(this);
    }

    private void initView() {
        rg_tab = (RadioGroup) this.findViewById(R.id.rg_tab);
        rg_tab.setOnCheckedChangeListener(this); //监听回调
        rb_home = (RadioButton) this.findViewById(R.id.rb_home);
        application = (BaseApplication) getApplication();
        rb_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (application.isIfIndexFragment()) {
                    controller.homeFragment.refresh();
                }
                application.setIfIndexFragment(true);
            }
        });

        iv_add = (ImageView) this.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_message:
                controller.showFragment(1);
                application.setIfIndexFragment(false);
                break;
            case R.id.rb_discover:
                controller.showFragment(2);
                application.setIfIndexFragment(false);
                break;
            case R.id.rb_my:
                controller.showFragment(3);
                application.setIfIndexFragment(false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtils.showToast(this, "再按一次退出应用", Toast.LENGTH_LONG);
            firstTime = secondTime;
        } else {
            finish();
        }
    }
}
