package com.liudong.weibo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {

    private ImageView[] points;
    private int[] point = {R.id.point1, R.id.point2, R.id.point3, R.id.point4};
    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initViews();
        initPoint();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(GuideActivity.this);
        View view = inflater.inflate(R.layout.guide_four, null);
        views = new ArrayList<>();

        views.add(inflater.inflate(R.layout.guide_one, null));
        views.add(inflater.inflate(R.layout.guide_two, null));
        views.add(inflater.inflate(R.layout.guide_three, null));
        views.add(view);

        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        ViewPager scrollView = (ViewPager) findViewById(R.id.scrollView);
        scrollView.setAdapter(adapter);
        scrollView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < views.size(); i++) {
                    if (i == position) {
                        points[i].setImageResource(R.drawable.card_type22_point_highlighted);
                    } else {
                        points[i].setImageResource(R.drawable.card_type22_point);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ImageView iv_four = (ImageView) view.findViewById(R.id.iv_four);
        iv_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Activity(AuthActivity.class);
                finish();
            }
        });
    }

    private void initPoint() {
        points = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            points[i] = (ImageView) findViewById(point[i]);
        }
    }
}
