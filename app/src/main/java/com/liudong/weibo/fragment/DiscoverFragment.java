package com.liudong.weibo.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liudong.weibo.BaseFragment;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.liudong.weibo.adapter.UserItemAdapter;
import com.liudong.weibo.adapter.ViewPagerAdapter;
import com.liudong.weibo.entity.UserItem;
import com.liudong.weibo.utils.RecyclerViewUtils;
import com.liudong.weibo.widget.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends BaseFragment {

    private List<View> views;
    private int[] pics = {R.mipmap.img, R.mipmap.img};
    private ImageView[] points;
    private int[] point = {R.id.point1, R.id.point2};

    private View view;
    private RecyclerView rv_discover;
    private LinearLayoutManager manager;
    private ArrayList<UserItem> userItems;

    private UserItemAdapter adapter;
    private HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = null;
    private View vp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);

        initView();
        initPoint();
        setItem();
        return view;
    }

    private void initView() {

        vp = LayoutInflater.from(mainActivity).inflate(R.layout.include_frag_discover, null, false);

        views = new ArrayList<>();
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(mainActivity);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(views);
        MyViewPager vp_scroll = (MyViewPager) vp.findViewById(R.id.vp_scroll);
        vp_scroll.setAdapter(viewPagerAdapter);
        vp_scroll.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < views.size(); i++) {
                    if (i == position) {
                        points[i].setImageResource(R.mipmap.card_type22_point_highlighted);
                    } else {
                        points[i].setImageResource(R.mipmap.card_type22_point);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        userItems = new ArrayList<>();
        manager = new LinearLayoutManager(mainActivity);
        rv_discover = (RecyclerView) view.findViewById(R.id.rv_discover);
        rv_discover.setLayoutManager(manager);
        adapter = new UserItemAdapter(mainActivity, userItems);
        headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rv_discover.setAdapter(headerAndFooterRecyclerViewAdapter);

        RecyclerViewUtils.setHeaderView(rv_discover, vp);

    }

    private void initPoint() {
        points = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            points[i] = (ImageView) vp.findViewById(point[i]);
        }
    }

    //列表栏模拟数据
    private void setItem() {
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_8, "热门微博", "全站最热微博尽搜罗"));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_6, "找人", ""));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_5, "530网络红人节", "530狂欢不打烊"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_2, "玩游戏", "微博上最火的游戏"));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_6, "周边", "发现值得去的地方"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_8, "随手拍", "发照片，赢赏金"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_5, "股票", "直播解盘让你在股市赚翻"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_7, "电影", "优惠电影票就在这里"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_6, "红人淘", "女神压箱好货限时抢"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_8, "直播", "女神明星视频直播中"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_5, "微博头条", "随时随地一起看新闻"));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_7, "更多频道", ""));
        adapter.notifyDataSetChanged();
    }

}
