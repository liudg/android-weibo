package com.liudong.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liudong.weibo.R;
import com.liudong.weibo.activity.UserInfoActivity;
import com.liudong.weibo.entity.Comment;
import com.liudong.weibo.entity.User;
import com.liudong.weibo.utils.DateUtils;
import com.liudong.weibo.utils.ImageOptHelper;
import com.liudong.weibo.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by liudong on 2016/6/7.
 */
public class StatusDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Comment> comments;
    private ImageLoader imageLoader;

    public StatusDetailAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) vh;
            //绑定数据
            Comment comment = comments.get(position);
            final User user = comment.getUser();
            imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatat, ImageOptHelper.getAvatarOptions());
            holder.tv_subhead.setTextColor(context.getResources().getColor(R.color.colorTxtBlack));
            holder.tv_subhead.setText(user.getName());
            holder.tv_caption.setText(DateUtils.getShortTime(comment.getCreated_at()));
            holder.tv_comment.setText(StringUtils.getWeiboContent(context, holder.tv_comment, comment.getText()));

            holder.tv_subhead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("userName", user.getName());
                    context.startActivity(intent);
                }
            });

            holder.iv_avatat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("userName", user.getName());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_avatat;
        private RelativeLayout rl_content;
        private TextView tv_subhead;
        private TextView tv_caption;
        private TextView tv_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_avatat = (ImageView) itemView.findViewById(R.id.iv_avatat);
            rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            tv_subhead = (TextView) itemView.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) itemView.findViewById(R.id.tv_caption);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
        }
    }
}
