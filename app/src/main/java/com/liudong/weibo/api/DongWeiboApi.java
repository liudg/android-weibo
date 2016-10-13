package com.liudong.weibo.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.liudong.weibo.constants.AccessTokenKeeper;
import com.liudong.weibo.constants.CommonConstants;
import com.liudong.weibo.constants.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 微博接口调用以及接口工具类封装
 * Created by liudong on 2016/4/29.
 */
public class DongWeiboApi extends AbsOpenAPI {

    /**
     * 参数为主线程轮询器，因为需要在其他主线程使用到 所以需要此参数
     */
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());

    /**
     * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
     */
    public DongWeiboApi(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    /**
     * 简化构造函数，以免每次进行创建子类都需要传递appkey跟accessToken
     */
    public DongWeiboApi(Context context) {
        this(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
    }

    /**
     * 对request请求进行封装
     * 在request回调方法中进行主线程的封装，再通过requestInMainLooper方法的回调监听将方法暴露出去
     */
    public void requestInMainLooper(String url, WeiboParameters params, String httpMethod, final RequestListener listener) {
        requestAsync(url, params, httpMethod, new RequestListener() {
            @Override
            public void onComplete(final String s) {
                mainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //回调方法
                        listener.onComplete(s);
                    }
                });
            }

            @Override
            public void onWeiboException(final WeiboException e) {
                mainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onWeiboException(e);
                    }
                });
            }
        });
    }

    /**
     * 这是联网操作 应该在子线程里操作 通过handler通知主线程
     */
    @Override
    protected void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        super.requestAsync(url, params, httpMethod, listener);
    }

    /**
     * 查询首页微博
     */
    public void statusesHome_timeline(long page, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("page", page);
        requestInMainLooper(CommonConstants.statuses_home_timeline, parameters, HTTPMETHOD_GET, listener);
    }

    /**
     * 根据微博ID返回某条微博的评论列表
     */
    public void commentsShow(long id, long page, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("id", id);
        parameters.put("page", page);
        requestInMainLooper(CommonConstants.commentShow, parameters, HTTPMETHOD_GET, listener);
    }

    /**
     * 对一条微博进行评论
     */
    public void commentsCreate(long id, String comment, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("id", id);
        parameters.put("comment", comment);
        requestInMainLooper(CommonConstants.commentCreate, parameters, HTTPMETHOD_POST, listener);
    }

    /**
     * 发布或转发一条微博
     *
     * @param status            要发布的微博文本内容
     * @param imgFilePath       要上传的图片文件路径(为空则代表发布无图微博)
     * @param retweetedStatusId 要转发的微博ID（<=0时为原创微博）
     * @param listener
     */
    public void statusesSend(String status, String imgFilePath, long retweetedStatusId, RequestListener listener) {
        String url;
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("status", status);
        if (retweetedStatusId > 0) {
            url = CommonConstants.statusesRepost;
            parameters.put("id", retweetedStatusId);
        } else if (!TextUtils.isEmpty(imgFilePath)) {
            url = CommonConstants.statusesUpload;
            parameters.put("pic", imgFilePath);
        } else {
            url = CommonConstants.statusesUpdate;
        }
        requestInMainLooper(url, parameters, HTTPMETHOD_POST, listener);
    }

    /**
     * 获取用户信息(uid和screen_name二选一)
     *
     * @param uid         根据用户ID获取用户信息
     * @param screen_name 需要查询的用户昵称。
     * @param listener
     */
    public void usersShow(String uid, String screen_name, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        if (!TextUtils.isEmpty(uid)) {
            parameters.put("uid", uid);
        } else if (!TextUtils.isEmpty(screen_name)) {
            parameters.put("screen_name", screen_name);
        }
        requestInMainLooper(CommonConstants.usersShow, parameters, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取@我的微博
     */
    public void statusesMention(long page, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("page", page);
        requestInMainLooper(CommonConstants.statusesMention, parameters, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取@我的评论
     */
    public void commentsMentions(long page, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("page", page);
        requestInMainLooper(CommonConstants.commentsMentions, parameters, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取某个用户最新发表的微博(uid和screen_name二选一)
     */
    public void statusesUser(String uid, String screen_name, long page, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("page", page);
        if (!TextUtils.isEmpty(uid)) {
            parameters.put("uid", uid);
        } else if (!TextUtils.isEmpty(screen_name)) {
            parameters.put("screen_name", screen_name);
        }
        requestInMainLooper(CommonConstants.statusesUser, parameters, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的关注列表(uid和screen_name二选一)
     *
     * @param cursor 返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     */
    public void friends(String uid, String screen_name, int cursor, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("cursor", cursor);
        if (!TextUtils.isEmpty(uid)) {
            parameters.put("uid", uid);
        } else if (!TextUtils.isEmpty(screen_name)) {
            parameters.put("screen_name", screen_name);
        }
        requestInMainLooper(CommonConstants.friends, parameters, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的粉丝列表(uid和screen_name二选一)
     *
     * @param cursor 返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     */
    public void fans(String uid, String screen_name, int cursor, RequestListener listener) {
        WeiboParameters parameters = new WeiboParameters(Constants.APP_KEY);
        parameters.put("cursor", cursor);
        if (!TextUtils.isEmpty(uid)) {
            parameters.put("uid", uid);
        } else if (!TextUtils.isEmpty(screen_name)) {
            parameters.put("screen_name", screen_name);
        }
        requestInMainLooper(CommonConstants.fans, parameters, HTTPMETHOD_GET, listener);
    }
}
