package com.liudong.weibo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liudong.weibo.R;
import com.liudong.weibo.activity.StatusDetailActivity;
import com.liudong.weibo.entity.Comment;
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
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Comment> datas;
    private ImageLoader imageLoader;

    private int TYPE_ITEM = 0;
    private int TYPE_FOOTER = 1;
    private AlertDialog.Builder builder;

    private String[] items = new String[]{"回复评论", "查看微博", "举报", "删除"};

    public CommentAdapter(Context context, ArrayList<Comment> datas) {
        this.context = context;
        this.datas = datas;
        this.imageLoader = ImageLoader.getInstance();
        builder = new AlertDialog.Builder(context);
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
            Comment comment = datas.get(position);
            User user = comment.getUser();
            final Status status = comment.getStatus();
            User retUser = status.getUser();
            imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatat, ImageOptHelper.getAvatarOptions());
            holder.tv_subhead.setText(user.getName());
            holder.tv_caption.setText(String.format("%s  来自 %s", DateUtils.getShortTime(comment.getCreated_at()), Html.fromHtml(comment.getSource())));

            holder.tv_content.setText(StringUtils.getWeiboContent(context, holder.tv_content, comment.getText()));

            imageLoader.displayImage(status.getBmiddle_pic(), holder.iv_rstatus_img);
            holder.tv_rstatus_username.setText("@" + retUser.getName());
            holder.tv_rstatus_content.setText(status.getText());

            holder.il_bottom_control.setVisibility(View.GONE);

            holder.ll_card_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.ll_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StatusDetailActivity.class);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                }
            });

            holder.ll_card_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
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

        private LinearLayout ll_card_content;
        private LinearLayout ll_status;

        private ImageView iv_avatat;
        private TextView tv_subhead;
        private TextView tv_caption;

        private TextView tv_content;

        private ImageView iv_rstatus_img;
        private TextView tv_rstatus_username;
        private TextView tv_rstatus_content;

        private LinearLayout il_bottom_control;


        public ViewHolder(View itemView) {
            super(itemView);

            ll_card_content = (LinearLayout) itemView.findViewById(R.id.ll_card_content);
            ll_status = (LinearLayout) itemView.findViewById(R.id.ll_status);

            iv_avatat = (ImageView) itemView.findViewById(R.id.iv_avatat);
            tv_subhead = (TextView) itemView.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) itemView.findViewById(R.id.tv_caption);

            tv_content = (TextView) itemView.findViewById(R.id.tv_content);

            iv_rstatus_img = (ImageView) itemView.findViewById(R.id.iv_rstatus_img);
            tv_rstatus_username = (TextView) itemView.findViewById(R.id.tv_rstatus_username);
            tv_rstatus_content = (TextView) itemView.findViewById(R.id.tv_rstatus_content);

            il_bottom_control = (LinearLayout) itemView.findViewById(R.id.il_bottom_control);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
