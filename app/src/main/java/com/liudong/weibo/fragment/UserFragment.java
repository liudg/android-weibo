package com.liudong.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liudong.weibo.BaseApplication;
import com.liudong.weibo.BaseFragment;
import com.liudong.weibo.R;
import com.liudong.weibo.activity.FansActivity;
import com.liudong.weibo.activity.FriendsActivity;
import com.liudong.weibo.activity.SettingActivity;
import com.liudong.weibo.activity.UserAllStatusActivity;
import com.liudong.weibo.activity.UserInfoActivity;
import com.liudong.weibo.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.liudong.weibo.adapter.UserItemAdapter;
import com.liudong.weibo.api.DongWeiboApi;
import com.liudong.weibo.api.SimpleRequestListener;
import com.liudong.weibo.constants.AccessTokenKeeper;
import com.liudong.weibo.entity.User;
import com.liudong.weibo.entity.UserItem;
import com.liudong.weibo.utils.ImageOptHelper;
import com.liudong.weibo.utils.RecyclerViewUtils;
import com.liudong.weibo.utils.TitleBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.ArrayList;

public class UserFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;

    //用户信息
    private View UserInfo;
    private ImageView iv_avatar;
    private TextView tv_subhead;
    private TextView tv_caption;

    private LinearLayout include_userinfo_interaction;
    private LinearLayout ll_status_count;
    private LinearLayout ll_follow_count;
    private LinearLayout ll_fans_count;
    private TextView tv_status_count;
    private TextView tv_follow_count;
    private TextView tv_fans_count;

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView recyclerView;

    private User user;

    private UserItemAdapter adapter;
    private LinearLayoutManager manager;
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = null;
    private ArrayList<UserItem> userItems;

    private DongWeiboApi weiboApi;
    private Oauth2AccessToken accessToken;
    private ImageLoader imageLoader;

    //第一次显示fragment时加载数据
    private boolean isFirstShow = true;
    //禁止未刷新完成再次下拉
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);

        weiboApi = new DongWeiboApi(mainActivity);
        accessToken = AccessTokenKeeper.readAccessToken(mainActivity);
        imageLoader = ImageLoader.getInstance();
        initView();
        setItem();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        //fragment显示时加载数据
        if (!hidden) {
            if (isFirstShow) {
                mSwipeRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefresh.setRefreshing(true);
                    }
                });
                loadData();
                isFirstShow = false;
            }
        }
    }

    private void initView() {
        new TitleBuilder(view)
                .setTitleText("我")
                .setLeftText("添加好友")
                .setRightText("设置")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent2Activity(SettingActivity.class);
                    }
                });
        //用户信息
        UserInfo = View.inflate(mainActivity, R.layout.fragment_user_header, null);
        iv_avatar = (ImageView) UserInfo.findViewById(R.id.iv_avatar);
        tv_subhead = (TextView) UserInfo.findViewById(R.id.tv_subhead);
        tv_caption = (TextView) UserInfo.findViewById(R.id.tv_caption);

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, UserInfoActivity.class);
                startActivity(intent);
            }
        });

        tv_subhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, UserInfoActivity.class);
                startActivity(intent);
            }
        });

        //互动信息栏
        include_userinfo_interaction = (LinearLayout) UserInfo.findViewById(R.id.include_userinfo_interaction);

        ll_status_count = (LinearLayout) include_userinfo_interaction.findViewById(R.id.ll_status_count);
        ll_follow_count = (LinearLayout) include_userinfo_interaction.findViewById(R.id.ll_follow_count);
        ll_fans_count = (LinearLayout) include_userinfo_interaction.findViewById(R.id.ll_fans_count);

        tv_status_count = (TextView) include_userinfo_interaction.findViewById(R.id.tv_status_count);
        tv_follow_count = (TextView) include_userinfo_interaction.findViewById(R.id.tv_follow_count);
        tv_fans_count = (TextView) include_userinfo_interaction.findViewById(R.id.tv_fans_count);

        ll_status_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, UserAllStatusActivity.class);
                startActivity(intent);
            }
        });

        ll_follow_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, FriendsActivity.class);
                startActivity(intent);
            }
        });

        ll_fans_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, FansActivity.class);
                startActivity(intent);
            }
        });

        //设置栏列表
        userItems = new ArrayList<>();
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_myfrag);
        manager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(manager);
        adapter = new UserItemAdapter(mainActivity, userItems);
        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);
        //增加头部
        RecyclerViewUtils.setHeaderView(recyclerView, UserInfo);

        //设置加载图标颜色
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this);
    }

    //设置用户信息
    private void setUserInfo() {
        imageLoader.displayImage(user.getAvatar_large(), iv_avatar, ImageOptHelper.getAvatarOptions());
        tv_subhead.setText(user.getName());
        if (TextUtils.isEmpty(user.getDescription())) {
            tv_caption.setText("简介：暂无介绍");
        } else {
            tv_caption.setText("简介：" + user.getDescription());
        }
        tv_status_count.setText("" + user.getStatuses_count());
        tv_follow_count.setText("" + user.getFriends_count());
        tv_fans_count.setText("" + user.getFollowers_count());
    }

    //列表栏模拟数据
    private void setItem() {
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_8, "新的好友", ""));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_1, "新手任务", "完成任务，抽取大奖"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_2, "我的相册", ""));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_3, "我的赞", ""));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_4, "微博支付", "积分好礼换不停"));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_6, "微博运动", "奔跑2016搬到这里了"));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_5, "草稿箱", ""));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_7, "更多", "数据中心、点评、收藏"));
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        weiboApi.usersShow(accessToken.getUid(), "", new SimpleRequestListener(mainActivity, null) {
            @Override
            public void onComplete(String s) {
                super.onComplete(s);
                BaseApplication application = (BaseApplication) mainActivity.getApplication();
                application.currentUser = user = JSON.parseObject(s, User.class);
                setUserInfo();
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

    @Override
    public void onRefresh() {
        if (!isLoading) {
            loadData();
            isLoading = true;
        }
    }
}
