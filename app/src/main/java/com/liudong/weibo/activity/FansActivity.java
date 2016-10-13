package com.liudong.weibo.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.FansAdapter;
import com.liudong.weibo.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.liudong.weibo.api.SimpleRequestListener;
import com.liudong.weibo.constants.AccessTokenKeeper;
import com.liudong.weibo.entity.User;
import com.liudong.weibo.entity.response.FansResponse;
import com.liudong.weibo.utils.RecyclerViewUtils;
import com.liudong.weibo.utils.TitleBuilder;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.ArrayList;
import java.util.List;

public class FansActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView rv_fans;
    private LinearLayoutManager manager;
    private FansAdapter adapter;
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter;

    private List<User> fans;
    private int lastVisibleItem;
    private int curCursor;
    private boolean isLoading = false;

    private Oauth2AccessToken accessToken;
    private FansResponse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);

        accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        initView();
        loadData(0);
    }

    private void initView() {
        new TitleBuilder(this)
                .setTitleText("粉丝")
                .setLeftImage(R.drawable.detail_left_sel)
                .setRightImage(R.drawable.detail_right_sel)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        fans = new ArrayList<>();
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        rv_fans = (RecyclerView) findViewById(R.id.rv_fans);

        //设置加载图标颜色
        swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        swipe_refresh.setOnRefreshListener(this);

        //第一次进入页面显示加载图标
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(true);
            }
        });

        manager = new LinearLayoutManager(this);
        rv_fans.setLayoutManager(manager);
        adapter = new FansAdapter(this, fans);
        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rv_fans.setAdapter(headerAndFooterRecyclerViewAdapter);

        rv_fans.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    boolean refreshing = swipe_refresh.isRefreshing();
                    if (refreshing) {
                        RecyclerViewUtils.removeFooterView(rv_fans);
                        return;
                    }

                    if (!isLoading) {
                        isLoading = true;
                        loadData(user.getNext_cursor());
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

    private void loadData(final int cursor) {
        weiboApi.fans(accessToken.getUid(), "", cursor, new SimpleRequestListener(this, null) {
            @Override
            public void onComplete(String s) {
                super.onComplete(s);
                if (cursor == 0) {
                    fans.clear();
                }
                curCursor = cursor;
                user = JSON.parseObject(s, FansResponse.class);
                addData(user);
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
                swipe_refresh.setRefreshing(false);
            }
        });
    }

    private void addData(FansResponse userBean) {
        for (User user : userBean.getUsers()) {
            if (!fans.contains(user)) {
                fans.add(user);
            }
        }

        if (userBean.getUsers().size() > 0) {
            adapter.notifyDataSetChanged();
        }

        if (fans.size() < userBean.getTotal_number() && userBean.getUsers().size() > 0) {
            RecyclerViewUtils.setFooterView(rv_fans, View.inflate(this, R.layout.status_detail_loading, null));
        } else {
            RecyclerViewUtils.removeFooterView(rv_fans);
        }

    }

    @Override
    public void onRefresh() {
        loadData(0);
    }
}
