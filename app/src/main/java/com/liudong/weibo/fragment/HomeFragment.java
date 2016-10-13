package com.liudong.weibo.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.liudong.weibo.BaseFragment;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.liudong.weibo.adapter.StatusAdapter;
import com.liudong.weibo.api.DongWeiboApi;
import com.liudong.weibo.api.SimpleRequestListener;
import com.liudong.weibo.entity.Status;
import com.liudong.weibo.entity.response.StatusTimeLineResponse;
import com.liudong.weibo.utils.RecyclerViewUtils;
import com.liudong.weibo.utils.TitleBuilder;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.ArrayList;
import java.util.List;

/**
 * 刷新数据要保证是同一个集合对象跟同一个适配器
 * 参考网址:
 * http://www.zhaoyb.cn/2015/04/17/swiperefreshlayout-e8-b0-83-e7-94-a8setrefreshingtrue-e4-b8-8d-e6-98-be-e7-a4-ba-e8-a7-a3-e5-86-b3/
 * http://m.blog.csdn.net/article/details?id=50989549
 * http://blog.csdn.net/TTCCAAA/article/details/50100709
 */
public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private RecyclerView rv_home;
    private SwipeRefreshLayout mSwipeRefresh;
    private StatusAdapter adapter;
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = null;
    private LinearLayoutManager manager;
    private List<Status> statuses = new ArrayList<>();
    private int lastVisibleItem;
    private int curPage = 1;
    //控制一次加载完成后才能继续下拉加载
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView(inflater, container, savedInstanceState);
        loadData(1);
        return view;
    }

    private void initView(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        new TitleBuilder(view)
                .setTitleText("首页")
                .setLeftImage(R.drawable.home_left_sel)
                .setRightImage(R.drawable.home_right_sel);

        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);

        manager = new LinearLayoutManager(mainActivity);
        rv_home.setLayoutManager(manager);
        adapter = new StatusAdapter(mainActivity, statuses);
        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rv_home.setAdapter(headerAndFooterRecyclerViewAdapter);
        //增加一个头部
        RecyclerViewUtils.setHeaderView(rv_home, inflater.inflate(R.layout.header_search, null, false));

        //设置分割线
        rv_home.addItemDecoration(new WeiBoItemSpace(
                (int) mainActivity.getResources().getDimension(R.dimen.home_weiboitem_space)));

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

        //上拉刷新
        rv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem == adapter.getItemCount()) {

                    //当加载更多数据时(下拉刷新时)，屏蔽有可能的重复的上拉操作
                    boolean isRefreshing = mSwipeRefresh.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        //网络请求
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
        DongWeiboApi api = new DongWeiboApi(mainActivity);
        api.statusesHome_timeline(page, new SimpleRequestListener(mainActivity, null) {
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
                adapter.notifyItemRemoved(adapter.getItemCount());
            }
        });
    }

    private void addData(StatusTimeLineResponse resBean) {
        for (Status status : resBean.getStatuses()) {
            if (!statuses.contains(status)) {
                statuses.add(status);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        loadData(1);
    }

    public void refresh() {
        rv_home.scrollToPosition(0);
        mSwipeRefresh.setRefreshing(true);
        loadData(1);
    }
}
