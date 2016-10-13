package com.liudong.weibo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 避免同一个页面需要短时间显示多条toast产生的阻塞现象
 * Created by liudong on 2016/4/27.
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showToast(Context context, CharSequence text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }
}
