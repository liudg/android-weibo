package com.liudong.weibo.utils;

import android.graphics.Bitmap;

import com.liudong.weibo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * UniversalImageLoader显示图片的配置
 */
public class ImageOptHelper {

    /**
     * 公共设置
     * @return
     */
    public static DisplayImageOptions getImgOptions() {
        DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.mipmap.message_image_default)
                .showImageForEmptyUri(R.mipmap.message_image_default)
                .showImageOnFail(R.mipmap.empty_picture)
                .build();
        return imgOptions;
    }

    /**
     * 头像加载初始化
     * @return
     */
    public static DisplayImageOptions getAvatarOptions() {
        DisplayImageOptions avatarOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.mipmap.avatar_default)
                .showImageForEmptyUri(R.mipmap.avatar_default)
                .showImageOnFail(R.mipmap.avatar_default)
                .displayer(new RoundedBitmapDisplayer(999))
                .build();
        return avatarOptions;
    }

    /**
     * 自定义圆角度数
     * @param cornerRadiusPixels
     * @return
     */
    public static DisplayImageOptions getCornerOptions(int cornerRadiusPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.mipmap.message_image_default)
                .showImageForEmptyUri(R.mipmap.message_image_default)
                .showImageOnFail(R.mipmap.empty_picture)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).build();
        return options;
    }
}
