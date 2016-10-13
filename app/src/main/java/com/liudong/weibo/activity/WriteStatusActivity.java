package com.liudong.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.EmotionGvAdapter;
import com.liudong.weibo.adapter.EmotionPagerAdapter;
import com.liudong.weibo.adapter.WriteStatusRvGridAdapter;
import com.liudong.weibo.api.DongWeiboApi;
import com.liudong.weibo.api.SimpleRequestListener;
import com.liudong.weibo.entity.Status;
import com.liudong.weibo.utils.DisplayUtils;
import com.liudong.weibo.utils.EditTextStringUtils;
import com.liudong.weibo.utils.EmotionKeyBoardUtils;
import com.liudong.weibo.utils.EmotionUtils;
import com.liudong.weibo.utils.ImageUtils;
import com.liudong.weibo.utils.StringUtils;
import com.liudong.weibo.utils.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class WriteStatusActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ScrollView sc_write;
    //输入框
    private EditText et_write_status;
    //添加的九宫格图片
    private RecyclerView gv_write_status;
    //转发微博内容
    private View include_retweeted_status_card;
    private ImageView iv_rstatus_img;
    private TextView tv_rstatus_username;
    private TextView tv_rstatus_content;
    //底部添加栏
    private ImageView iv_image;
    private ImageView iv_at;
    private ImageView iv_topic;
    private ImageView iv_emoji;
    private ImageView iv_add;
    //表情选择面板
    private LinearLayout ll_emotion_dashboard;
    private ViewPager vp_emotion_dashboard;

    private WriteStatusRvGridAdapter statusImgsAdapter;
    private ArrayList<String> imgUris = new ArrayList<>();
    private EmotionPagerAdapter emotionPagerAdapter;
    private Status retweeted_status;
    //用于区分是转发原创微博还是转发 转发的微博
    private Status cardStatus;

    //用于判断是否需要跳转到多图选择页面
    private boolean startMultiImage;

    private EmotionKeyBoardUtils emotionKeyBoardUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        retweeted_status = (Status) getIntent().getSerializableExtra("status");
        startMultiImage = getIntent().getBooleanExtra("startMultiImage", false);

        if (startMultiImage == true) {
            ImageUtils.multiImageSelect(this, imgUris);
        }

        initView();
    }

    private void initView() {
        new TitleBuilder(this)
                .setTitleText("发微博")
                .setLeftText("取消")
                .setLeftOnClickListener(this)
                .setRightText("发送")
                .setRightOnClickListener(this);
        sc_write = (ScrollView) findViewById(R.id.sc_write);
        //输入框
        et_write_status = (EditText) findViewById(R.id.et_write_status);
        // 添加的九宫格图片
        gv_write_status = (RecyclerView) findViewById(R.id.gv_write_status);
        // 转发微博内容
        include_retweeted_status_card = findViewById(R.id.include_retweeted_status_card);
        iv_rstatus_img = (ImageView) findViewById(R.id.iv_rstatus_img);
        tv_rstatus_username = (TextView) findViewById(R.id.tv_rstatus_username);
        tv_rstatus_content = (TextView) findViewById(R.id.tv_rstatus_content);
        // 底部添加栏
        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_at = (ImageView) findViewById(R.id.iv_at);
        iv_topic = (ImageView) findViewById(R.id.iv_topic);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        // 表情选择面板
        ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
        vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gv_write_status.setLayoutManager(gridLayoutManager);
        gv_write_status.setHasFixedSize(true);
        statusImgsAdapter = new WriteStatusRvGridAdapter(this, imgUris);
        gv_write_status.setAdapter(statusImgsAdapter);

        iv_image.setOnClickListener(this);
        iv_at.setOnClickListener(this);
        iv_topic.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        emotionKeyBoardUtils = new EmotionKeyBoardUtils(this, sc_write, ll_emotion_dashboard, et_write_status);

        initRetweetedStstus();
        initEmotion();
    }

    /**
     * 发送微博
     */
    private void sendStatus() {
        String statusContent = et_write_status.getText().toString();
        if (TextUtils.isEmpty(statusContent)) {
            showToast("微博内容不能为空");
            return;
        }

        String uri = null;
        if (imgUris.size() > 0) {
            //API限制，只能发送一个图片，所以只发送第一个
            uri = imgUris.get(0);
        }

        long retweetedStatusId = cardStatus == null ? -1 : cardStatus.getId();

        new DongWeiboApi(this).statusesSend(statusContent, uri, retweetedStatusId, new SimpleRequestListener(this, null) {
            @Override
            public void onComplete(String s) {
                super.onComplete(s);
                showToast("微博发送成功");
                finish();
            }
        });
    }

    /**
     * 初始化引用微博
     */
    private void initRetweetedStstus() {
        if (retweeted_status != null) {
            //判断转发的是原创微博还是转发微博
            Status rrStatus = this.retweeted_status.getRetweeted_status();
            if (rrStatus != null) {
                String content = "//@" + retweeted_status.getUser().getName() + ":" + retweeted_status.getText();
                et_write_status.setText(StringUtils.getWeiboContent(this, et_write_status, content));
                cardStatus = rrStatus;
            } else {
                et_write_status.setHint("说说分享心得...");
                cardStatus = retweeted_status;
            }

            //判断被转发微博是否有图片
            String imgUrl = cardStatus.getBmiddle_pic();
            if (TextUtils.isEmpty(imgUrl)) {
                iv_rstatus_img.setVisibility(View.GONE);
            } else {
                iv_rstatus_img.setVisibility(View.VISIBLE);
                imageLoader.displayImage(imgUrl, iv_rstatus_img);
            }

            //设置内容区域
            tv_rstatus_username.setText("@" + cardStatus.getUser().getName());
            tv_rstatus_content.setText(cardStatus.getText());

            //转发微博不能添加图片 隐藏底部选择图片按钮
            iv_image.setVisibility(View.GONE);

            //显示转发内容区域
            include_retweeted_status_card.setVisibility(View.VISIBLE);
        } else {
            include_retweeted_status_card.setVisibility(View.GONE);
        }
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

    /**
     * 更新显示图片
     */
    private void updateImgs() {
        if (imgUris.size() > 0) {
            gv_write_status.setVisibility(View.VISIBLE);
            statusImgsAdapter.notifyDataSetChanged();
        } else {
            gv_write_status.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_tv_left:
                finish();
                break;
            case R.id.titlebar_tv_right:
                sendStatus();
                break;
            case R.id.iv_image:
                ImageUtils.multiImageSelect(this, imgUris);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ImageUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                for (String uri : data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)) {
                    if (!imgUris.contains(uri)) {
                        imgUris.add(uri);
                    }
                }
                updateImgs();
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
