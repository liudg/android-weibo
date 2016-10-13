package com.liudong.weibo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 发微博，评论微博页面图片处理
 * https://github.com/lovetuzitong/MultiImageSelector
 * Created by liudong on 2016/5/16.
 */
public class ImageUtils {

    public static final int REQUEST_IMAGE = 5001;

    /**
     * 显示获取照片不同方式对话框
     */
    public static void multiImageSelect(final Activity activity, ArrayList<String> defaultDataArray) {
        Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大图片选择数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择图片,回填选项(支持String ArrayList)
        if (defaultDataArray != null && defaultDataArray.size() > 0) {
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
        }
        activity.startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * 保存微博图片
     */
    public static void saveFile(Context context, Bitmap bm, String fileName) throws IOException {
        //未安装SD卡时不保存
        String storageState = Environment.getExternalStorageState();
        if (!storageState.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.showToast(context, "未检测到SD卡", Toast.LENGTH_SHORT);
            return;
        }

        //图片文件保存路径
        File directory = Environment.getExternalStorageDirectory();
        File path = new File(directory, "/dongweibo/images");
        //图片路径不存在就创建
        if (!path.exists()) {
            path.mkdirs();
        }
        //图片文件如果不存在则创建
        File file = new File(path, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        //将图片压缩到对应的流里，即保存图片到该文件中
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }
}
