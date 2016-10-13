package com.liudong.weibo.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.MentionAdapter;
import com.liudong.weibo.api.SimpleRequestListener;
import com.liudong.weibo.entity.Status;
import com.liudong.weibo.entity.response.StatusTimeLineResponse;
import com.liudong.weibo.fragment.MentionItemSpace;
import com.liudong.weibo.utils.TitleBuilder;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.ArrayList;

/**
 * /@我的页面
 * 只返回授权用户的评论，非授权用户的评论将不返回 导致显示的数量跟返回的实际数量有差别
 */
public class MentionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView rv_mention;
    private LinearLayoutManager manager;
    private MentionAdapter adapter;

    private ArrayList<Status> statuses;
    private int lastVisibleItem;

    private int curPage = 1;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mention);

        initView();
        loadData(1);
    }

    private void initView() {
        new TitleBuilder(this)
                .setLeftImage(R.drawable.detail_left_sel)
                .setTitleText("所有微博")
                .setRightText("设置")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        statuses = new ArrayList<>();

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefresh);
        rv_mention = (RecyclerView) findViewById(R.id.rv_mention);

        //设置加载图标颜色
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this);

        //第一次进入页面显示加载图标
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });

        manager = new LinearLayoutManager(this);
        rv_mention.setLayoutManager(manager);
        adapter = new MentionAdapter(this, statuses);
        rv_mention.setAdapter(adapter);
        rv_mention.addItemDecoration(new MentionItemSpace((int) this.getResources().getDimension(R.dimen.home_weiboitem_space)));

        //上拉加载
        rv_mention.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {

                    boolean isRefreshing = mSwipeRefresh.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }

                    if (!isLoading) {
                        isLoading = true;
                        loadData(curPage + 1);
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

    private void loadData(final int page) {
        weiboApi.statusesMention(page, new SimpleRequestListener(this, null) {
            @Override
            public void onComplete(String s) {
                super.onComplete(s);
                if (page == 1) {
                    statuses.clear();
                }
                curPage = page;

                //处理返回的json字符串
                addData(JSON.parseObject(s, StatusTimeLineResponse.class));
                isLoading = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                super.onWeiboException(e);
                isLoading = false;
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void addData(StatusTimeLineResponse resBean) {
        for (Status status : resBean.getStatuses()) {
            if (!statuses.contains(status)) {
                statuses.add(status);
            }
        }

        if (resBean.getStatuses().size() > 0) {
            adapter.notifyDataSetChanged();
        }

        //判断当前条数是否达到总条数，未达到则显示加载更多
        if (statuses.size() == resBean.getTotal_number()) {
            adapter.notifyItemRemoved(adapter.getItemCount());
            showToast("已加载@我的信息");
        }
    }


    @Override
    public void onRefresh() {
        loadData(1);
    }
}
