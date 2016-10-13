package com.liudong.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;

public class PopupActivity extends BaseActivity implements View.OnTouchListener {

    private ImageView compose_close;
    private LinearLayout compose_idea;
    private LinearLayout compose_photo;
    private LinearLayout compose_headlines;
    private LinearLayout compose_lbs;
    private LinearLayout compose_review;
    private LinearLayout compose_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        initView();
    }

    private void initView() {

        compose_close = (ImageView) findViewById(R.id.compose_close);
        compose_idea = (LinearLayout) findViewById(R.id.compose_idea);
        compose_photo = (LinearLayout) findViewById(R.id.compose_photo);
        compose_headlines = (LinearLayout) findViewById(R.id.compose_headlines);
        compose_lbs = (LinearLayout) findViewById(R.id.compose_lbs);
        compose_review = (LinearLayout) findViewById(R.id.compose_review);
        compose_more = (LinearLayout) findViewById(R.id.compose_more);

        compose_idea.setOnTouchListener(this);
        compose_photo.setOnTouchListener(this);
        compose_headlines.setOnTouchListener(this);
        compose_lbs.setOnTouchListener(this);
        compose_review.setOnTouchListener(this);
        compose_more.setOnTouchListener(this);

        compose_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scaleViewAnimation(v, 1.2f);
                break;
            case MotionEvent.ACTION_UP:
                scaleViewAnimation(v, 1.0f);
                switch (v.getId()) {
                    case R.id.compose_idea:
                        intent2Activity(WriteStatusActivity.class);
                        finish();
                        break;
                    case R.id.compose_photo:
                        Intent intent = new Intent(PopupActivity.this, WriteStatusActivity.class);
                        intent.putExtra("startMultiImage", true);
                        startActivity(intent);
                        finish();
                        break;
                }
                break;
        }
        return true;
    }

    /**
     * 缩放动画
     */
    private void scaleViewAnimation(View view, float value) {
        view.animate().scaleX(value).scaleY(value).setDuration(80).start();
    }
}
