package com.liudong.weibo.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liudong.weibo.R;

/**
 * 标题栏封装工具类，仿照AlertDialog.builder设计模式
 * Created by liudong on 2016/4/28.
 */
public class TitleBuilder {

    private View viewTitle;
    private TextView tvTitle;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvleft;
    private TextView tvRight;

    public TitleBuilder(Activity context) {
        viewTitle = context.findViewById(R.id.rl_titlebar);
        tvTitle = (TextView) viewTitle.findViewById(R.id.titlebar_tv_tit);
        ivLeft = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_left);
        ivRight = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_right);
        tvleft = (TextView) viewTitle.findViewById(R.id.titlebar_tv_left);
        tvRight = (TextView) viewTitle.findViewById(R.id.titlebar_tv_right);
    }

    /**
     * 供Fragment等使用
     *
     * @param context
     */
    public TitleBuilder(View context) {
        viewTitle = context.findViewById(R.id.rl_titlebar);
        tvTitle = (TextView) viewTitle.findViewById(R.id.titlebar_tv_tit);
        ivLeft = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_left);
        ivRight = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_right);
        tvleft = (TextView) viewTitle.findViewById(R.id.titlebar_tv_left);
        tvRight = (TextView) viewTitle.findViewById(R.id.titlebar_tv_right);
    }

    /**
     * 标题栏设置  背景  标题文字
     */
    public TitleBuilder setTitleBgRes(int resid) {
        viewTitle.setBackgroundResource(resid);
        return this; //返回本类对象，便于方法点方法调用
    }

    public TitleBuilder setTitleText(String text) {
        tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvTitle.setText(text);
        return this;
    }

    /**
     * 标题栏左边内容 事件监听
     */
    public TitleBuilder setLeftImage(int resid) {
        ivLeft.setVisibility(resid > 0 ? View.VISIBLE : View.GONE);
        ivLeft.setImageResource(resid);
        return this;
    }

    public TitleBuilder setLeftText(String text) {
        tvleft.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvleft.setText(text);
        return this;
    }

    public TitleBuilder setLeftOnClickListener(View.OnClickListener listener) {
        if (ivLeft.getVisibility() == View.VISIBLE) {
            ivLeft.setOnClickListener(listener);
        } else if (tvleft.getVisibility() == View.VISIBLE) {
            tvleft.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 标题栏又边内容 事件监听
     */
    public TitleBuilder setRightImage(int resid) {
        ivRight.setVisibility(resid > 0 ? View.VISIBLE : View.GONE);
        ivRight.setImageResource(resid);
        return this;
    }

    public TitleBuilder setRightText(String text) {
        tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvRight.setText(text);
        return this;
    }

    public TitleBuilder setRightOnClickListener(View.OnClickListener listener) {
        if (ivRight.getVisibility() == View.VISIBLE) {
            ivRight.setOnClickListener(listener);
        } else if (tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setOnClickListener(listener);
        }
        return this;
    }

}
