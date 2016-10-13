package com.liudong.weibo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.liudong.weibo.R;
import com.liudong.weibo.utils.DisplayUtils;

/**
 * Created by liudong on 2016/6/22.
 */
public class UnderlineIndicatorView extends LinearLayout {

    private int mCurrentPosition;

    public UnderlineIndicatorView(Context context) {
        this(context, null);
    }

    public UnderlineIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);
        int count = 3;
        for (int i = 0; i < count; i++) {
            View view = new View(context);
            LayoutParams params = new LayoutParams(DisplayUtils.dp2px(context, 56), LayoutParams.MATCH_PARENT);
            params.leftMargin = DisplayUtils.dp2px(context, 12);
            params.rightMargin = DisplayUtils.dp2px(context, 12);
            view.setLayoutParams(params);
            view.setBackgroundResource(R.color.transparent);
            addView(view);
        }
    }

    public void setCurrentItem(int position) {
        final View oldChild = getChildAt(mCurrentPosition);
        final View newChild = getChildAt(position);

        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, position - mCurrentPosition,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0
        );

        translateAnimation.setDuration(200);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                oldChild.setBackgroundResource(R.color.transparent);
                newChild.setBackgroundResource(R.color.colorOrange);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        oldChild.setAnimation(translateAnimation);
        mCurrentPosition = position;

        //刷新视图
        invalidate();
    }
}
