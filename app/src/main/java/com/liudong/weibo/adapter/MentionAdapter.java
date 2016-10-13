package com.liudong.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liudong.weibo.R;
import com.liudong.weibo.activity.UserInfoActivity;
import com.liudong.weibo.entity.Status;
import com.liudong.weibo.entity.User;
import com.liudong.weibo.utils.DateUtils;
import com.liudong.weibo.utils.ImageOptHelper;
import com.liudong.weibo.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by liudong on 2016/6/1.
 */
public class MentionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Status> datas;
    private ImageLoader imageLoader;

    private int TYPE_ITEM = 0;
    private int TYPE_FOOTER = 1;

    public MentionAdapter(Context context, ArrayList<Status> datas) {
        this.context = context;
        this.datas = datas;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mention, parent, false));
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            FootViewHolder holder = new FootViewHolder(LayoutInflater.from(context).inflate(R.layout.item_foot, parent, false));
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) vh;
            Status status = datas.get(position);
            final User user = status.getUser();
            Status retweeted_status = status.getRetweeted_status();
            User retUser = retweeted_status.getUser();
            imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatat, ImageOptHelper.getAvatarOptions());
            holder.tv_subhead.setText(user.getName());
            holder.tv_caption.setText(String.format("%s  来自 %s", DateUtils.getShortTime(status.getCreated_at()), Html.fromHtml(status.getSource())));

            holder.tv_content.setText(StringUtils.getWeiboContent(context, holder.tv_content, status.getText()));

            imageLoader.displayImage(retweeted_status.getBmiddle_pic(), holder.iv_rstatus_img);
            holder.tv_rstatus_username.setText("@" + retUser.getName());
            holder.tv_rstatus_content.setText(retweeted_status.getText());

            holder.tv_share_bottom.setText(status.getReposts_count() == 0 ? "转发" : status.getReposts_count() + "");
            holder.tv_comment_bottom.setText(status.getComments_count() == 0 ? "评论" : status.getComments_count() + "");
            holder.tv_favour_bottom.setText(status.getAttitudes_count() == 0 ? "赞" : status.getAttitudes_count() + "");

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

        private ImageView iv_avatat;
        private TextView tv_subhead;
        private TextView tv_caption;

        private TextView tv_content;

        private ImageView iv_rstatus_img;
        private TextView tv_rstatus_username;
        private TextView tv_rstatus_content;

        private TextView tv_share_bottom;
        private TextView tv_comment_bottom;
        private TextView tv_favour_bottom;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_avatat = (ImageView) itemView.findViewById(R.id.iv_avatat);
            tv_subhead = (TextView) itemView.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) itemView.findViewById(R.id.tv_caption);

            tv_content = (TextView) itemView.findViewById(R.id.tv_content);

            iv_rstatus_img = (ImageView) itemView.findViewById(R.id.iv_rstatus_img);
            tv_rstatus_username = (TextView) itemView.findViewById(R.id.tv_rstatus_username);
            tv_rstatus_content = (TextView) itemView.findViewById(R.id.tv_rstatus_content);

            tv_share_bottom = (TextView) itemView.findViewById(R.id.tv_share_bottom);
            tv_comment_bottom = (TextView) itemView.findViewById(R.id.tv_comment_bottom);
            tv_favour_bottom = (TextView) itemView.findViewById(R.id.tv_favour_bottom);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
