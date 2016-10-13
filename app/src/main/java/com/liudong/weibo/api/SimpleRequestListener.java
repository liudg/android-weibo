package com.liudong.weibo.api;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.liudong.weibo.utils.LoggerUtils;
import com.liudong.weibo.utils.ToastUtils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

/**
 * 封装RequestListener，简化使用
 * Created by liudong on 2016/4/29.
 */
public class SimpleRequestListener implements RequestListener {

    private Context context;
    private Dialog progressDialog;

    public SimpleRequestListener(Context context, Dialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @Override
    public void onComplete(String s) {
        onAllDone();
        LoggerUtils.show("REQUEST onComplete", s);
    }

    @Override
    public void onWeiboException(WeiboException e) {
        onAllDone();
        ToastUtils.showToast(context,e.getMessage(), Toast.LENGTH_LONG);
        LoggerUtils.show("WeiboException", e.toString());
    }

    /**
     * 用于对话框关闭
     * 通知下拉刷新控件完成刷新
     * ...
     */
    public void onAllDone(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
