package com.liudong.weibo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by liudong on 2016/5/20.
 */
public class EmotionPagerAdapter extends PagerAdapter {

    private List<GridView> gvs;

    public EmotionPagerAdapter(List<GridView> gvs) {
        this.gvs = gvs;
    }

    @Override
    public int getCount() {
        return gvs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(gvs.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(gvs.get(position));
        return gvs.get(position);
    }
}
