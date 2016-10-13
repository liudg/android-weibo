package com.liudong.weibo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.constants.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class WelcomeActivity extends BaseActivity {

    private static final int WHAT_INTENT2MAIN = 1;
    private static final int WHAT_INTENT2LOGIN = 2;
    private static final int WHAT_INTENT2GUIDE = 3;
    private static final long SPLASH_DUR_TIME = 1000; //延迟1000ms

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_INTENT2MAIN:
                    //跳转到主页面
                    intent2Activity(MainActivity.class);
                    finish();
                    break;
                case WHAT_INTENT2LOGIN:
                    //跳转到授权页
                    intent2Activity(AuthActivity.class);
                    finish();
                    break;
                case WHAT_INTENT2GUIDE:
                    //跳转到引导页
                    intent2Activity(GuideActivity.class);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        boolean isFirstIn = sp.getBoolean("isFirstIn", true);
        if (!isFirstIn) {
            //判断是否已授权，如果是直接跳转到主页面，否则进入授权页面
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(this);
            if (accessToken.isSessionValid()) {
                //授权可用 发消息给handler让他在欢迎页面停留1s分发任务
                handler.sendEmptyMessageDelayed(WHAT_INTENT2MAIN, SPLASH_DUR_TIME);
            } else {
                //授权不可用
                handler.sendEmptyMessageDelayed(WHAT_INTENT2LOGIN, SPLASH_DUR_TIME);
            }
        }else {
            //第一次进入，打开引导页
            handler.sendEmptyMessageDelayed(WHAT_INTENT2GUIDE, SPLASH_DUR_TIME);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirstIn",false);
            editor.apply();
        }
    }
}
