package com.liudong.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liudong.weibo.R;
import com.liudong.weibo.activity.UserInfoActivity;
import com.liudong.weibo.entity.User;
import com.liudong.weibo.utils.ImageOptHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by liudong on 2016/6/11.
 */
public class FansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<User> datas;
    private ImageLoader imageLoader;

    public FansAdapter(Context context, List<User> datas) {
        this.context = context;
        this.datas = datas;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_fans, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        ViewHolder holder = (ViewHolder) vh;
        final User user = datas.get(position);
        imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatar, ImageOptHelper.getAvatarOptions());
        holder.tv_subhead.setText(user.getName());
        holder.tv_caption.setText(user.getDescription());

        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userName", user.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_friend;
        private ImageView iv_avatar;
        private TextView tv_subhead;
        private TextView tv_caption;
        private LinearLayout ll_state;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_friend = (LinearLayout) itemView.findViewById(R.id.ll_friend);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_subhead = (TextView) itemView.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) itemView.findViewById(R.id.tv_caption);
            ll_state = (LinearLayout) itemView.findViewById(R.id.ll_state);
        }
    }
}
