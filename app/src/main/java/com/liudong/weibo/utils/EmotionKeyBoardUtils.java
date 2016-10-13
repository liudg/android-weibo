package com.liudong.weibo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by liudong on 2016/10/11.
 */

public class EmotionKeyBoardUtils {
    private static final String SHARE_PREFERENCE_NAME = "EmotionKeyboard";
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";

    private SharedPreferences sharedPreferences;

    private Activity activity;
    private ScrollView sc_write;
    private LinearLayout ll_emotion_dashboard;
    private EditText et_write_status;

    private InputMethodManager mInputManager;//软键盘管理类

    public EmotionKeyBoardUtils(Activity activity, ScrollView sc_write, final LinearLayout ll_emotion_dashboard, final EditText et_write_status) {

        this.activity = activity;
        this.sc_write = sc_write;
        this.ll_emotion_dashboard = ll_emotion_dashboard;
        this.et_write_status = et_write_status;

        this.sharedPreferences = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.mInputManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);

        et_write_status.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && ll_emotion_dashboard.isShown()) {
                    lockContentHeight();
                    hideEmotionLayout(true);

                    et_write_status.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    }, 200L);
                }
                return false;
            }
        });
    }

    /**
     * 控制表情面板跟输入法切换
     */
    public void emotionKeyBoard() {
        if (ll_emotion_dashboard.isShown()) {
            lockContentHeight(); //锁定内容高度，防止切换跳闪
            hideEmotionLayout(true); //隐藏表情布局，显示软键盘
            unlockContentHeightDelayed();//软件盘显示后，释放内容高度
        } else {
            if (isSoftInputShown()) {
                lockContentHeight();
                showEmotionLayout();
                unlockContentHeightDelayed();
            } else {
                showEmotionLayout(); //两者都没显示，直接显示表情布局
            }
        }
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sc_write.getLayoutParams();
        params.height = sc_write.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        sc_write.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) sc_write.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    /**
     * 显示表情布局
     */
    private void showEmotionLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = sharedPreferences.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 400);
        }
        hideSoftInput();
        ll_emotion_dashboard.getLayoutParams().height = softInputHeight;
        ll_emotion_dashboard.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    private void hideEmotionLayout(boolean showSoftInput) {
        if (ll_emotion_dashboard.isShown()) {
            ll_emotion_dashboard.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }


    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        et_write_status.requestFocus();
        et_write_status.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(et_write_status, 0);
            }
        });
    }

    /**
     * 是否显示软件盘
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(et_write_status.getWindowToken(), 0);
    }

    /**
     * 获取软件盘的高度
     */
    public int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }

        if (softInputHeight > 0) {
            sharedPreferences.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
        }

        return softInputHeight;
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 点击返回键时先隐藏表情
     */
    public boolean interceptBackPress(){
        if (ll_emotion_dashboard.isShown()) {
            hideEmotionLayout(false);
            return true;
        }
        return false;
    }
}
