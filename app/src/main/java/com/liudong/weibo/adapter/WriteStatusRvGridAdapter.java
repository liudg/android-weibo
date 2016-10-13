package com.liudong.weibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liudong.weibo.R;
import com.liudong.weibo.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by liudong on 2016/5/22.
 */
public class WriteStatusRvGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<String> datas;
    private ImageLoader imageLoader;
    private int TYPE_ITEM = 0;
    private int TYPE_FOOTER = 1;

    public WriteStatusRvGridAdapter(Context context, ArrayList<String> datas) {
        this.context = context;
        this.datas = datas;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_grid_image, parent, false));
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            FootViewHolder holder = new FootViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_image, parent, false));
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) holder;
            imageLoader.displayImage("file://" + datas.get(position), vh.iv_image);
            vh.iv_delete_image.setVisibility(View.VISIBLE);
            vh.iv_delete_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder fvh = (FootViewHolder) holder;
            fvh.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageUtils.multiImageSelect((Activity) context, datas);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() == 0 ? 0 : datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image;
        public ImageView iv_delete_image;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_images);
            iv_delete_image = (ImageView) itemView.findViewById(R.id.iv_delete_image);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_add;

        public FootViewHolder(View itemView) {
            super(itemView);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
        }
    }
}
