package com.liudong.weibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.liudong.weibo.adapter.RvGridAdapter;
import com.liudong.weibo.adapter.StatusDetailAdapter;
import com.liudong.weibo.api.SimpleRequestListener;
import com.liudong.weibo.entity.Comment;
import com.liudong.weibo.entity.PicUrls;
import com.liudong.weibo.entity.Status;
import com.liudong.weibo.entity.User;
import com.liudong.weibo.entity.response.CommentsResponse;
import com.liudong.weibo.utils.DateUtils;
import com.liudong.weibo.utils.ImageOptHelper;
import com.liudong.weibo.utils.RecyclerViewUtils;
import com.liudong.weibo.utils.StringUtils;
import com.liudong.weibo.utils.TitleBuilder;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.ArrayList;
import java.util.List;

public class StatusDetailActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final int REQUEST_CODE_WRITE_COMMENT = 2333;
    //微博信息
    private View status_detail_info;
    private LinearLayout il_detail_content;
    private RelativeLayout il_detail_bar;

    private ImageView ivAvatat;
    private TextView tvSubhead;
    private TextView tvCaption;

    private TextView tv_content;

    private FrameLayout include_status_image;
    private RecyclerView gv_image;
    private ImageView iv_image;

    private LinearLayout include_retweeted_status;
    private TextView tv_retweeted_content;
    private FrameLayout fl_retweeted_imageview;
    private RecyclerView gv_retweeted_image;
    private ImageView iv_retweeted_image;

    //悬浮菜单栏
    private TextView tv_retweet;
    private TextView tv_comment;
    private TextView tv_like;


    //底部互动菜单
    private LinearLayout status_detail_controlbar;
    private LinearLayout ll_share_bottom;
    private LinearLayout ll_comment_bottom;
    private LinearLayout ll_like_bottom;

    //详情页微博信息
    private Status status;
    //是否需要滚动到评论部分
    private boolean scroll2Comment;
    //评论当前已加载页数
    private long curPage = 1;

    private List<Comment> comments = new ArrayList<>();

    private SwipeRefreshLayout srl_status_detail;
    private RecyclerView rv_status_detail;
    private LinearLayoutManager manager;
    private StatusDetailAdapter adapter;
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter;

    private int lastVisibleItem;
    //控制一次加载完成后才能继续下拉加载
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        //获取intent传入的信息
        status = (Status) getIntent().getSerializableExtra("status");
        scroll2Comment = getIntent().getBooleanExtra("scroll2Comment", false);

        //初始化view
        initView();
        //设置数据信息
        setData();
        //开始加载第一页评论数据
        loadComments(1);
    }

    private void initView() {
        //标题栏
        initTitle();
        //微博信息
        initDetailHead();
        //菜单栏
        initTab();
        //下拉刷新控件
        initRecyclerView();
        //底部互动栏
        initControlBar();
    }

    private void initTitle() {
        new TitleBuilder(this)
                .setTitleText("微博正文")
                .setLeftImage(R.drawable.detail_left_sel)
                .setRightImage(R.drawable.detail_right_sel)
                .setLeftOnClickListener(this);
    }

    private void initDetailHead() {

        status_detail_info = View.inflate(this, R.layout.status_detail_head, null);
        il_detail_content = (LinearLayout) status_detail_info.findViewById(R.id.il_detail_content);
        il_detail_content.findViewById(R.id.il_bottom_control).setVisibility(View.GONE);

        ivAvatat = (ImageView) il_detail_content.findViewById(R.id.iv_avatat);
        tvSubhead = (TextView) il_detail_content.findViewById(R.id.tv_subhead);
        tvCaption = (TextView) il_detail_content.findViewById(R.id.tv_caption);
        include_status_image = (FrameLayout) il_detail_content.findViewById(R.id.include_status_image);
        gv_image = (RecyclerView) il_detail_content.findViewById(R.id.gv_image);
        iv_image = (ImageView) il_detail_content.findViewById(R.id.iv_image);
        tv_content = (TextView) il_detail_content.findViewById(R.id.tv_content);
        include_retweeted_status = (LinearLayout) il_detail_content.findViewById(R.id.include_retweeted_status);
        tv_retweeted_content = (TextView) il_detail_content.findViewById(R.id.tv_retweeted_content);
        fl_retweeted_imageview = (FrameLayout) include_retweeted_status.findViewById(R.id.include_status_image);
        gv_retweeted_image = (RecyclerView) fl_retweeted_imageview.findViewById(R.id.gv_image);
        iv_retweeted_image = (ImageView) fl_retweeted_imageview.findViewById(R.id.iv_image);
        iv_image.setOnClickListener(this);

    }

    private void initTab() {
        il_detail_bar = (RelativeLayout) status_detail_info.findViewById(R.id.il_detail_bar);
        tv_retweet = (TextView) il_detail_bar.findViewById(R.id.tv_retweet);
        tv_comment = (TextView) il_detail_bar.findViewById(R.id.tv_comment);
        tv_like = (TextView) il_detail_bar.findViewById(R.id.tv_like);
        tv_retweet.setOnClickListener(this);
        tv_like.setOnClickListener(this);
    }

    private void initRecyclerView() {
        srl_status_detail = (SwipeRefreshLayout) findViewById(R.id.srl_status_detail);
        rv_status_detail = (RecyclerView) findViewById(R.id.rv_status_detail);

        manager = new LinearLayoutManager(this);
        rv_status_detail.setLayoutManager(manager);
        adapter = new StatusDetailAdapter(this, comments);
        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rv_status_detail.setAdapter(headerAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setHeaderView(rv_status_detail, status_detail_info);

        //设置加载图标颜色
        srl_status_detail.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        srl_status_detail.setOnRefreshListener(this);

        //第一次进入页面显示加载图标
        srl_status_detail.post(new Runnable() {
            @Override
            public void run() {
                srl_status_detail.setRefreshing(true);
            }
        });

        //上拉刷新
        rv_status_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem - 1 == adapter.getItemCount()) {

                    //当加载更多数据时(下拉刷新时)，屏蔽有可能的重复的上拉操作
                    boolean isRefreshing = srl_status_detail.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        //网络请求
                        loadComments(curPage + 1);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
    }

    private void initControlBar() {
        status_detail_controlbar = (LinearLayout) findViewById(R.id.status_detail_controlbar);
        ll_share_bottom = (LinearLayout) status_detail_controlbar.findViewById(R.id.ll_share_bottom);
        ll_comment_bottom = (LinearLayout) status_detail_controlbar.findViewById(R.id.ll_comment_bottom);
        ll_like_bottom = (LinearLayout) status_detail_controlbar.findViewById(R.id.ll_favour_bottom);
        ll_share_bottom.setOnClickListener(this);
        ll_comment_bottom.setOnClickListener(this);
        ll_like_bottom.setOnClickListener(this);
    }

    private void setData() {
        //微博信息
        User user = status.getUser();
        imageLoader.displayImage(user.getProfile_image_url(), ivAvatat, ImageOptHelper.getAvatarOptions());
        tvSubhead.setText(user.getName());
        tvCaption.setText(DateUtils.getShortTime(status.getCreated_at()) + "  来自 " + Html.fromHtml(status.getSource()));

        setImages(status, include_status_image, gv_image, iv_image);

        if (TextUtils.isEmpty(status.getText())) {
            tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setVisibility(View.VISIBLE);
            SpannableString weiboContent = StringUtils.getWeiboContent(this, tv_content, status.getText());
            tv_content.setText(weiboContent);
        }

        Status retweetedStatus = status.getRetweeted_status();
        if (retweetedStatus != null) {
            include_retweeted_status.setVisibility(View.VISIBLE);
            String retweetContent = "@" + retweetedStatus.getUser().getName() + ":" + retweetedStatus.getText();
            SpannableString weiboContent = StringUtils.getWeiboContent(this, tv_retweeted_content, retweetContent);
            tv_retweeted_content.setText(weiboContent);
            setImages(retweetedStatus, fl_retweeted_imageview, gv_retweeted_image, iv_retweeted_image);
        } else {
            include_retweeted_status.setVisibility(View.GONE);
        }

        tv_retweet.setText("转发 " + status.getReposts_count());
        tv_comment.setText("评论 " + status.getComments_count());
        tv_like.setText("赞 " + status.getAttitudes_count());

    }

    private void setImages(Status status, FrameLayout imgContainer, RecyclerView gv_image, ImageView iv_image) {
        if (status == null) {
            return;
        }

        ArrayList<PicUrls> pic_urls = status.getPic_urls();
        String thumbnail_pic = status.getBmiddle_pic();
        if (pic_urls != null && pic_urls.size() > 1) {

            imgContainer.setVisibility(View.VISIBLE);
            gv_image.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.GONE);

            GridLayoutManager gridLayoutManager = initGridLayoutManager(this, pic_urls);
            gv_image.setLayoutManager(gridLayoutManager);
            gv_image.setHasFixedSize(true);
            RvGridAdapter adapter = new RvGridAdapter(this, pic_urls);
            gv_image.setAdapter(adapter);

        } else if (thumbnail_pic != null) {

            imgContainer.setVisibility(View.VISIBLE);
            gv_image.setVisibility(View.GONE);
            iv_image.setVisibility(View.VISIBLE);
            imageLoader.displayImage(thumbnail_pic, iv_image);

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

    /**
     * 根据微博ID返回某条微博的评论列表
     */
    private void loadComments(final long requestPage) {
        weiboApi.commentsShow(status.getId(), requestPage, new SimpleRequestListener(this, null) {
            @Override
            public void onComplete(String s) {
                super.onComplete(s);

                showLog("status comments = " + s);

                //如果是加载第一页，先清空已有数据
                if (requestPage == 1) {
                    comments.clear();
                }
                curPage = requestPage;

                //解析返回数据
                CommentsResponse commentsResponse = JSON.parseObject(s, CommentsResponse.class);
                //更新评论数信息
                tv_comment.setText("评论 " + commentsResponse.getTotal_number());

                //将获取的评论信息添加到列表上
                addData(commentsResponse);

                isLoading = false;

                //判断是否需要滚动至评论部分
                if (scroll2Comment) {
                    scroll2Comment = false;
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                super.onWeiboException(e);
                isLoading = false;
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                //通知下拉刷新控件完成刷新
                srl_status_detail.setRefreshing(false);
                adapter.notifyItemRemoved(adapter.getItemCount());
            }
        });
    }

    private void addData(CommentsResponse response) {
        //将获取的数据添加至列表中，重复数据不添加
        for (Comment comment : response.getComments()) {
            if (!comments.contains(comment)) {
                comments.add(comment);
            }
        }

        adapter.notifyDataSetChanged();

        if (comments.size() == 0) {
            RecyclerViewUtils.setFooterView(rv_status_detail, View.inflate(this, R.layout.empty_default, null));
        } else if (comments.size() < response.getTotal_number() && response.getComments().size() > 0) {
            RecyclerViewUtils.setFooterView(rv_status_detail, View.inflate(this, R.layout.status_detail_loading, null));
        } else {
            RecyclerViewUtils.removeFooterView(rv_status_detail);
            showToast("已加载全部评论");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_iv_left:
                finish();
                break;
            case R.id.ll_comment_bottom:
                //跳转到写评论页面
                Intent intent = new Intent(this, WriteCommentActivity.class);
                intent.putExtra("status", status);
                startActivityForResult(intent, REQUEST_CODE_WRITE_COMMENT);
                break;
            case R.id.ll_share_bottom:
                //跳转到发微博页面
                Intent intentWriteStatus = new Intent(this, WriteStatusActivity.class);
                intentWriteStatus.putExtra("status", status);
                startActivity(intentWriteStatus);
                break;
            case R.id.tv_like:
                showToast("API暂未支持");
                break;
            case R.id.tv_retweet:
                showToast("API暂未支持");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //如果按BACK键，取消发送等情况，则不做后续处理
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_WRITE_COMMENT:
                // 如果是评论发送成功的返回结果,则重新加载最新评论,同时要求滚动至评论部分
                boolean sendCommentSuccess = data.getBooleanExtra("sendCommentSuccess", false);
                if (sendCommentSuccess) {
                    scroll2Comment = true;
                    loadComments(1);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadComments(1);
    }
}
