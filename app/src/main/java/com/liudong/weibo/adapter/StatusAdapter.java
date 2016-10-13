package com.liudong.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liudong.weibo.R;
import com.liudong.weibo.activity.ImageBrowserActivity;
import com.liudong.weibo.activity.StatusDetailActivity;
import com.liudong.weibo.activity.UserInfoActivity;
import com.liudong.weibo.activity.WriteCommentActivity;
import com.liudong.weibo.activity.WriteStatusActivity;
import com.liudong.weibo.entity.PicUrls;
import com.liudong.weibo.entity.Status;
import com.liudong.weibo.entity.User;
import com.liudong.weibo.utils.DateUtils;
import com.liudong.weibo.utils.ImageOptHelper;
import com.liudong.weibo.utils.StringUtils;
import com.liudong.weibo.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudong on 2016/5/1.
 */
public class StatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Status> datas;
    private ImageLoader imageLoader;
    private int TYPE_ITEM = 0;
    private int TYPE_FOOTER = 1;


    public StatusAdapter(Context context, List<Status> datas) {
        this.context = context;
        this.datas = datas;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_status, parent, false));
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
            //绑定数据
            final Status status = datas.get(position);
            final User user = status.getUser();
            imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatat, ImageOptHelper.getAvatarOptions());
            holder.tv_subhead.setText(user.getName());
            holder.tv_caption.setText(String.format("%s  来自 %s", DateUtils.getShortTime(status.getCreated_at()), Html.fromHtml(status.getSource())));
            holder.tv_content.setText(StringUtils.getWeiboContent(context, holder.tv_content, status.getText()));

            holder.iv_avatat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("userName", user.getName());
                    context.startActivity(intent);
                }
            });

            holder.tv_subhead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("userName", user.getName());
                    context.startActivity(intent);
                }
            });

            setImages(status, holder.include_status_image, holder.gv_image, holder.iv_image);

            //引用微博
            final Status retweeted_status = status.getRetweeted_status();
            if (retweeted_status != null) {
                User retUser = retweeted_status.getUser();
                holder.include_retweeted_status.setVisibility(View.VISIBLE);
                holder.tv_retweeted_content.setText(StringUtils.getWeiboContent(context, holder.tv_retweeted_content,
                        "@" + retUser.getName() + ":" + retweeted_status.getText()));

                setImages(retweeted_status, holder.include_retweeted_status_image, holder.gv_retweeted_image, holder.iv_retweeted_image);
            } else {
                holder.include_retweeted_status.setVisibility(View.GONE);
            }

            //底部菜单
            holder.tv_share_bottom.setText(status.getReposts_count() == 0 ? "转发" : status.getReposts_count() + "");
            holder.tv_comment_bottom.setText(status.getComments_count() == 0 ? "评论" : status.getComments_count() + "");
            holder.tv_favour_bottom.setText(status.getAttitudes_count() == 0 ? "赞" : status.getAttitudes_count() + "");

            holder.ll_card_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StatusDetailActivity.class);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                }
            });

            holder.include_retweeted_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StatusDetailActivity.class);
                    intent.putExtra("status", retweeted_status);
                    context.startActivity(intent);
                }
            });

            holder.ll_share_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WriteStatusActivity.class);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                }
            });

            holder.ll_comment_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status.getComments_count() > 0) {
                        Intent intent = new Intent(context, StatusDetailActivity.class);
                        intent.putExtra("status", status);
                        intent.putExtra("scroll2Comment", true);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, WriteCommentActivity.class);
                        intent.putExtra("status", status);
                        context.startActivity(intent);
                    }
                }
            });

            holder.ll_favour_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showToast(context, "赞一个", Toast.LENGTH_LONG);
                }
            });

        }
    }

    private void setImages(final Status status, FrameLayout imgContainer, RecyclerView gv_image, ImageView iv_image) {
        final ArrayList<PicUrls> pic_urls = status.getPic_urls();
        String thumbnail_pic = status.getBmiddle_pic();
        if (pic_urls != null && pic_urls.size() > 1) {

            imgContainer.setVisibility(View.VISIBLE);
            gv_image.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.GONE);

            GridLayoutManager gridLayoutManager = initGridLayoutManager(context, pic_urls);
            gv_image.setLayoutManager(gridLayoutManager);
            gv_image.setHasFixedSize(true);
            RvGridAdapter adapter = new RvGridAdapter(context, pic_urls);
            gv_image.setAdapter(adapter);

        } else if (thumbnail_pic != null) {

            imgContainer.setVisibility(View.VISIBLE);
            gv_image.setVisibility(View.GONE);
            iv_image.setVisibility(View.VISIBLE);
            imageLoader.displayImage(thumbnail_pic, iv_image);

            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageBrowserActivity.class);
                    intent.putExtra("picUrls", pic_urls);
                    context.startActivity(intent);
                }
            });

        } else {
            imgContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 根据图片数量，初始化GridLayoutManager，并且设置列数
     * 当图片=2时，显示2列
     * 当图片=4时，显示2列
     * 其他情况显示3列
     */
    private GridLayoutManager initGridLayoutManager(Context context, ArrayList<PicUrls> pic_urls) {
        GridLayoutManager gridLayoutManager = null;
        switch (pic_urls.size()) {
            case 2:
                gridLayoutManager = new GridLayoutManager(context, 2);
                break;
            case 4:
                gridLayoutManager = new GridLayoutManager(context, 2);
                break;
            default:
                gridLayoutManager = new GridLayoutManager(context, 3);
                break;
        }
        return gridLayoutManager;
    }


    @Override
    public int getItemCount() {
        return datas.size() == 0 ? 0 : datas.size() + 1;
    }

    //加载多布局
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

        /*发微博人的信息*/
        private ImageView iv_avatat;
        private TextView tv_subhead;
        private TextView tv_caption;

        /*微博内容*/
        private TextView tv_content;

        /*微博配图*/
        private FrameLayout include_status_image;
        private ImageView iv_image;
        private RecyclerView gv_image;

        /*引用微博*/
        private LinearLayout include_retweeted_status;
        private TextView tv_retweeted_content;
        public FrameLayout include_retweeted_status_image;
        public ImageView iv_retweeted_image;
        private RecyclerView gv_retweeted_image;


        /*底部菜单*/
        private LinearLayout ll_share_bottom;
        private LinearLayout ll_comment_bottom;
        private LinearLayout ll_favour_bottom;
        private TextView tv_share_bottom;
        private TextView tv_comment_bottom;
        private TextView tv_favour_bottom;


        public ViewHolder(View root) {
            super(root);
            ll_card_content = (LinearLayout) root.findViewById(R.id.ll_card_content);

            iv_avatat = (ImageView) root.findViewById(R.id.iv_avatat);
            tv_subhead = (TextView) root.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) root.findViewById(R.id.tv_caption);
            tv_content = (TextView) root.findViewById(R.id.tv_content);
            include_status_image = (FrameLayout) root.findViewById(R.id.include_status_image);
            iv_image = (ImageView) root.findViewById(R.id.iv_image);
            gv_image = (RecyclerView) root.findViewById(R.id.gv_image);

            include_retweeted_status = (LinearLayout) root.findViewById(R.id.include_retweeted_status);
            tv_retweeted_content = (TextView) root.findViewById(R.id.tv_retweeted_content);
            include_retweeted_status_image = (FrameLayout) include_retweeted_status.findViewById(R.id.include_status_image);
            iv_retweeted_image = (ImageView) include_retweeted_status.findViewById(R.id.iv_image);
            gv_retweeted_image = (RecyclerView) include_retweeted_status.findViewById(R.id.gv_image);

            ll_share_bottom = (LinearLayout) root.findViewById(R.id.ll_share_bottom);
            ll_comment_bottom = (LinearLayout) root.findViewById(R.id.ll_comment_bottom);
            ll_favour_bottom = (LinearLayout) root.findViewById(R.id.ll_favour_bottom);
            tv_share_bottom = (TextView) root.findViewById(R.id.tv_share_bottom);
            tv_comment_bottom = (TextView) root.findViewById(R.id.tv_comment_bottom);
            tv_favour_bottom = (TextView) root.findViewById(R.id.tv_favour_bottom);

        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
