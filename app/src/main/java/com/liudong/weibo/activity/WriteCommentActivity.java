package com.liudong.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.EmotionGvAdapter;
import com.liudong.weibo.adapter.EmotionPagerAdapter;
import com.liudong.weibo.api.SimpleRequestListener;
import com.liudong.weibo.entity.Status;
import com.liudong.weibo.utils.DisplayUtils;
import com.liudong.weibo.utils.EditTextStringUtils;
import com.liudong.weibo.utils.EmotionKeyBoardUtils;
import com.liudong.weibo.utils.EmotionUtils;
import com.liudong.weibo.utils.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class WriteCommentActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ScrollView sc_write;
    //评论输入框
    private EditText et_write_status;

    // 底部按钮
    private ImageView iv_image;
    private ImageView iv_at;
    private ImageView iv_topic;
    private ImageView iv_emoji;
    private ImageView iv_add;
    //表情选择面板
    private LinearLayout ll_emotion_dashboard;
    private ViewPager vp_emotion_dashboard;

    private EmotionPagerAdapter emotionPagerAdapter;

    //待评论的微博
    private Status status;

    private EmotionKeyBoardUtils emotionKeyBoardUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        //获取intent传入的微博
        status = (Status) getIntent().getSerializableExtra("status");

        initView();
    }

    private void initView() {
        new TitleBuilder(this)
                .setTitleText("发评论")
                .setLeftText("取消")
                .setRightText("发送")
                .setLeftOnClickListener(this)
                .setRightOnClickListener(this);

        sc_write = (ScrollView) findViewById(R.id.sc_write);
        et_write_status = (EditText) findViewById(R.id.et_write_status);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_at = (ImageView) findViewById(R.id.iv_at);
        iv_topic = (ImageView) findViewById(R.id.iv_topic);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        // 表情选择面板
        ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
        vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);

        iv_image.setVisibility(View.GONE);
        iv_at.setOnClickListener(this);
        iv_topic.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        emotionKeyBoardUtils = new EmotionKeyBoardUtils(this, sc_write, ll_emotion_dashboard, et_write_status);

        initEmotion();
    }

    private void sendComment() {
        String comment = et_write_status.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            showToast("评论内容不能为空");
            return;
        }

        weiboApi.commentsCreate(status.getId(), comment, new SimpleRequestListener(this, null) {
            @Override
            public void onComplete(String s) {
                super.onComplete(s);
                showToast("评论发送成功");
                //微博发送成功后，设置result结果数据，然后关闭页面
                Intent data = new Intent();
                data.putExtra("sendCommentSuccess", true);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    /**
     * 初始化表情面板
     */
    private void initEmotion() {
        int screenWidth = DisplayUtils.getScreenWidthPixels(this);
        int spacing = DisplayUtils.dp2px(this, 8);

        int itemWidth = (screenWidth - spacing * 8) / 7;
        int gvHeight = itemWidth * 4 + 5 * spacing;

        List<GridView> gvs = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        for (String emojiName : EmotionUtils.emojiMap.keySet()) {
            emotionNames.add(emojiName);
            if (emotionNames.size() == 27) {
                GridView emotionGridView = createEmotionGridView(emotionNames, screenWidth, gvHeight, spacing, itemWidth);
                gvs.add(emotionGridView);
                emotionNames = new ArrayList<>();
            }
        }
        //不足20个时也需要创建一个面板显示
        if (emotionNames.size() > 0) {
            GridView emotionGridView = createEmotionGridView(emotionNames, screenWidth, gvHeight, spacing, itemWidth);
            gvs.add(emotionGridView);
        }

        emotionPagerAdapter = new EmotionPagerAdapter(gvs);
        vp_emotion_dashboard.getLayoutParams().height = 0;
        ((LinearLayout.LayoutParams) vp_emotion_dashboard.getLayoutParams()).weight = 1.0F;
        vp_emotion_dashboard.setAdapter(emotionPagerAdapter);
    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int gvHeight, int padding, int itemWidth) {
        GridView gv = new GridView(this);
        gv.setBackgroundResource(R.color.bg_gray);
        gv.setNumColumns(7);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);

        EmotionGvAdapter adapter = new EmotionGvAdapter(this, emotionNames, itemWidth);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
        return gv;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_tv_left:
                finish();
                break;
            case R.id.titlebar_tv_right:
                sendComment();
                break;
            case R.id.iv_emoji:
                emotionKeyBoardUtils.emotionKeyBoard();
                if (ll_emotion_dashboard.isShown()) {
                    iv_emoji.setImageResource(R.drawable.comment_keyboard_sel);
                } else {
                    iv_emoji.setImageResource(R.drawable.comment_emoji_sel);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Adapter itemAdapter = parent.getAdapter();
        if (itemAdapter instanceof EmotionGvAdapter) {
            EmotionGvAdapter emotionAdapter = (EmotionGvAdapter) itemAdapter;
            if (position == emotionAdapter.getCount() - 1) {
                //调用系统的回退事件
                et_write_status.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else {
                String emotionName = emotionAdapter.getItem(position);
                //获取当前光标的位置
                int curPosition = et_write_status.getSelectionStart();
                //字符串拼接
                StringBuilder sb = new StringBuilder(et_write_status.getText().toString());
                sb.insert(curPosition, emotionName);
                SpannableString weiboContent = EditTextStringUtils.getWeiboContent(this, et_write_status, sb.toString());
                et_write_status.setText(weiboContent);
                et_write_status.setSelection(curPosition + emotionName.length());
            }
        }
    }

    @Override
    public void onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         */
        if (!emotionKeyBoardUtils.interceptBackPress()) {
            super.onBackPressed();
        }
    }
}
