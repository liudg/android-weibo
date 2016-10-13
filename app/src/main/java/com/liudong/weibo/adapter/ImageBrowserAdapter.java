package com.liudong.weibo.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liudong.weibo.R;
import com.liudong.weibo.entity.PicUrls;
import com.liudong.weibo.utils.DisplayUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by liudong on 2016/6/13.
 */
public class ImageBrowserAdapter extends PagerAdapter {

    private Activity context;
    private ArrayList<PicUrls> picUrls;
    private ArrayList<View> picViews;

    private ImageLoader imageLoader;

    public ImageBrowserAdapter(Activity context, ArrayList<PicUrls> picUrls) {
        this.context = context;
        this.picUrls = picUrls;
        this.imageLoader = ImageLoader.getInstance();
        initImgs();
    }

    private void initImgs() {
        picViews = new ArrayList<>();

        for (int i = 0; i < picUrls.size(); i++) {
            //填充显示图片的页面布局
            View view = View.inflate(context, R.layout.item_image_browser, null);
            picViews.add(view);
        }
    }

    @Override
    public int getCount() {
        //无限轮播
        if (picUrls.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return picUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //无限轮播position取得是int型的最大值，所以需要去余数控制在0-图片数之间循环
        int index = position % picUrls.size();
        View view = picViews.get(index);
        final ImageView iv_image_browser = (ImageView) view.findViewById(R.id.iv_image_browser);
        PicUrls picUrl = picUrls.get(index);

        String url = picUrl.isShowOriImag() ? picUrl.getOriginal_pic() : picUrl.getBmiddle_pic();
        //加载图片而不会显示，我们需要动态计算图片宽高，让他居中显示或则可滚动
        imageLoader.loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                float scale = (float) bitmap.getHeight() / bitmap.getWidth();

                //动态设置图片控件的宽高，在图片控件设置图片不足全屏时居中
                int screenWidthPixels = DisplayUtils.getScreenWidthPixels(context);
                int screenHeightPixels = DisplayUtils.getScreenHeightPixels(context);
                int height = (int) (screenWidthPixels * scale);

                if (height < screenHeightPixels) {
                    height = screenHeightPixels;
                }

                ViewGroup.LayoutParams params = iv_image_browser.getLayoutParams();
                params.height = height;
                params.width = screenWidthPixels;

                iv_image_browser.setImageBitmap(bitmap);

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        //重写这个方法才能进行点击显示原图更新视图
        return POSITION_NONE;
    }

    public PicUrls getPic(int position) {
        return picUrls.get(position % picUrls.size());
    }

    /**
     * 返回当前显示的ImageView中的Bitmap图片
     */
    public Bitmap getBitmap(int position) {
        Bitmap bitmap = null;
        View view = picViews.get(position % picViews.size());
        ImageView iv_image_browser = (ImageView) view.findViewById(R.id.iv_image_browser);
        Drawable drawable = iv_image_browser.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bitmap = bd.getBitmap();
        }
        return bitmap;
    }
}
