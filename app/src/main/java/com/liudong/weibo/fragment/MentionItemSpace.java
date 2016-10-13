package com.liudong.weibo.fragment;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 为RecyclerView每个item增加分割线
 * Created by liudong on 2016/5/22.
 */
public class MentionItemSpace extends RecyclerView.ItemDecoration {
    private int space;

    public MentionItemSpace(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, space, 0, 0);
        if (parent.getChildLayoutPosition(view) + 1 == state.getItemCount()) {
            outRect.set(0, space, 0, space);
        }
    }
}
