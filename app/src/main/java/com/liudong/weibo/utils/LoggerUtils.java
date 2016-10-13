package com.liudong.weibo.utils;

import android.util.Log;

import com.liudong.weibo.constants.CommonConstants;

/**
 * Created by liudong on 2016/4/27.
 */
public class LoggerUtils {

    /**
     * 显示LOG（默认级别）
     *
     * @param TAG
     * @param msg
     */
    public static void show(String TAG, String msg) {
        if (!CommonConstants.isShowLog) {
            return;
        }
        show(TAG, msg, Log.INFO);
    }


    /**
     * 显示LOG
     *
     * @param TAG
     * @param msg
     * @param level
     */
    public static void show(String TAG, String msg, int level) {
        if (!CommonConstants.isShowLog) {
            return;
        }
        switch (level) {
            case Log.VERBOSE:
                Log.v(TAG, msg);
                break;
            case Log.DEBUG:
                Log.d(TAG, msg);
                break;
            case Log.WARN:
                Log.w(TAG, msg);
                break;
            case Log.ERROR:
                Log.e(TAG, msg);
                break;
        }
    }
}
