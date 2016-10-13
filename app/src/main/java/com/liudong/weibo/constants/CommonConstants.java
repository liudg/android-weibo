package com.liudong.weibo.constants;

/**
 * 常用常量类
 * 接口：
 * 接口中的变量会被隐式地指定为public static final变量（并且只能是public static final变量，用private修饰会报编译错误），
 * 而方法会被隐式地指定为public abstract方法且只能是public abstract方法（用其他关键字，比如private、protected、static、 final等修饰会报编译错误），
 * 并且接口中所有的方法不能有具体的实现，也就是说，接口中的方法必须都是抽象方法。
 * 从这里可以隐约看出接口和抽象类的区别，接口是一种极度抽象的类型，它比抽象类更加“抽象”，并且一般情况下不在接口中定义变量
 * <p/>
 * Created by liudong on 2016/4/27.
 */
public interface CommonConstants {

    /**
     * SharedPreferences保存文件名
     */
    String SP_name = "info";
    /**
     * 是否显示LOG
     */
    boolean isShowLog = true;
    /**
     * 微博接口URL
     * 参数拆分 便于后续增加API链接跟修改API版本号
     */
    String BASE_URL = "https://api.weibo.com/2/";
    String statuses_home_timeline = BASE_URL + "statuses/home_timeline.json";

    //微博评论列表
    String commentShow = BASE_URL + "comments/show.json";

    //评论某条微博
    String commentCreate = BASE_URL + "comments/create.json";

    //转发一条微博
    String statusesRepost = BASE_URL + "statuses/repost.json";

    //发布一条微博（带图片）
    String statusesUpload = BASE_URL + "statuses/upload.json";

    //发布一条微博（不带图片）
    String statusesUpdate = BASE_URL + "statuses/update.json";

    // 获取用户信息
    String usersShow = BASE_URL + "users/show.json";

    //获取@我的微博
    String statusesMention = BASE_URL + "statuses/mentions.json";

    //获取@我的评论
    String commentsMentions = BASE_URL + "comments/to_me.json";

    //获取某个用户用户最新发表的微博列表
    String statusesUser = BASE_URL + "statuses/user_timeline.json";

    //获取用户的关注列表
    String friends = BASE_URL + "friendships/friends.json";

    //获取用户粉丝列表
    String fans = BASE_URL + "friendships/followers.json";

}
