package com.liudong.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liudong.weibo.R;
import com.liudong.weibo.activity.ImageBrowserActivity;
import com.liudong.weibo.entity.PicUrls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by liudong on 2016/5/22.
 */
public class RvGridAdapter extends RecyclerView.Adapter<RvGridAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PicUrls> datas;
    private ImageLoader imageLoader;

    private int IMAGE_TYPE_LONG_PIC = 1; //长微博
    private int IMAGE_TYPE_GIF = 2; //GIF图


    public RvGridAdapter(Context context, ArrayList<PicUrls> datas) {
        this.context = context;
        this.datas = datas;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grid_image, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        imageLoader.displayImage(datas.get(position).getBmiddle_pic(), holder.iv_images, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                //根据加载完成的BitMap大小，判断是否是长微博图片，设置右下角的图片类型
                if (returnImageType(bitmap) == IMAGE_TYPE_LONG_PIC) {
                    holder.iv_type.setVisibility(View.VISIBLE);
                    holder.iv_type.setImageResource(R.mipmap.timeline_image_longimage);
                }

                //根据URL判断是否是GIF
                if (returnImageType(datas.get(position).getBmiddle_pic()) == IMAGE_TYPE_GIF) {
                    holder.iv_type.setVisibility(View.VISIBLE);
                    holder.iv_type.setImageResource(R.mipmap.timeline_image_gif);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });

        holder.iv_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageBrowserActivity.class);
                intent.putExtra("picUrls", datas);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_images;
        public ImageView iv_type;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_images = (ImageView) itemView.findViewById(R.id.iv_images);
            iv_type = (ImageView) itemView.findViewById(R.id.iv_type);
        }
    }

    /**
     * 根据下载的图片长宽，返回图片类型
     */
    private int returnImageType(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (height >= width * 3) {
            return IMAGE_TYPE_LONG_PIC;
        }
        return 0;
    }

    /**
     * 根据url链接判断是否为Gif，返回图片类型
     */
    private int returnImageType(String url) {
        if (url.endsWith(".gif")) {
            return IMAGE_TYPE_GIF;
        }
        return 0;
    }
}
