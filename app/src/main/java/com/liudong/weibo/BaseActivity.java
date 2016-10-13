package com.liudong.weibo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.liudong.weibo.api.DongWeiboApi;
import com.liudong.weibo.constants.CommonConstants;
import com.liudong.weibo.utils.LoggerUtils;
import com.liudong.weibo.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by liudong on 2016/4/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG;
    protected BaseApplication application;
    protected SharedPreferences sp;

    protected DongWeiboApi weiboApi;
    protected ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getSimpleName();
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        application = (BaseApplication) getApplication();
        sp = getSharedPreferences(CommonConstants.SP_name, MODE_PRIVATE);

        weiboApi = new DongWeiboApi(this);
        imageLoader = ImageLoader.getInstance();
    }

    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    protected void showToast(String msg) {
        ToastUtils.showToast(this, msg, Toast.LENGTH_LONG);
    }

    protected void showLog(String msg) {
        LoggerUtils.show(TAG, msg);
    }
}
