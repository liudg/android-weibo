package com.liudong.weibo;

import android.app.Application;
import android.content.Context;

import com.liudong.weibo.entity.User;
import com.liudong.weibo.utils.ImageOptHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 进行初始化
 * Created by liudong on 2016/4/26.
 */
public class BaseApplication extends Application {

    /**
     * 判断是否为首页微博列表，如果是点击首页图标进行下拉刷新，否则切换到首页
     */
    boolean ifIndexFragment = true;

    /**
     * 保存当前的用户信息
     */
    public User currentUser;

    public boolean isIfIndexFragment() {
        return ifIndexFragment;
    }

    public void setIfIndexFragment(boolean ifIndexFragment) {
        this.ifIndexFragment = ifIndexFragment;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

    // 初始化图片处理
    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(ImageOptHelper.getImgOptions())
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
