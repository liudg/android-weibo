package com.liudong.weibo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liudong.weibo.ActivityManager;
import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.api.SimpleRequestListener;
import com.liudong.weibo.entity.User;
import com.liudong.weibo.utils.ImageOptHelper;
import com.liudong.weibo.utils.TitleBuilder;
import com.liudong.weibo.widget.UnderlineIndicatorView;

/**
 * http://www.bubuko.com/infodetail-977938.html
 */
public class UserInfoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    //用户相关信息
    private boolean isCurrentUser;
    private User user;
    private String userName;
    private RelativeLayout rl_titlebar;
    private ImageView iv_divider;

    private ImageView iv_cover_image;
    private ImageView iv_avatar;
    private TextView tv_name;
    private ImageView tv_user_sex;
    private TextView tv_follows;
    private TextView tv_fans;
    private TextView tv_intro;

    private UnderlineIndicatorView uliv_user_info;
    private RadioGroup rg_user_info;

    private TextView tv_address;
    private TextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userName = getIntent().getStringExtra("userName");
        if (TextUtils.isEmpty(userName)) {
            isCurrentUser = true;
            user = application.currentUser;
        }

        initView();
        loadData();

        ActivityManager.getInstance().addActivity(this);
    }

    private void initView() {
        new TitleBuilder(this)
                .setLeftImage(R.drawable.userinfo_left_sel)
                .setRightImage(R.drawable.userinfo_right_sel)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent2Activity(SettingActivity.class);
                    }
                });

        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        rl_titlebar.setBackgroundColor(getResources().getColor(R.color.transparent));
        iv_divider = (ImageView) findViewById(R.id.iv_divider);
        iv_divider.setVisibility(View.GONE);

        iv_cover_image = (ImageView) findViewById(R.id.iv_cover_image);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_user_sex = (ImageView) findViewById(R.id.tv_user_sex);
        tv_follows = (TextView) findViewById(R.id.tv_follows);
        tv_fans = (TextView) findViewById(R.id.tv_fans);
        tv_intro = (TextView) findViewById(R.id.tv_intro);

        rg_user_info = (RadioGroup) findViewById(R.id.rg_user_info);
        rg_user_info.setOnCheckedChangeListener(this);

        uliv_user_info = (UnderlineIndicatorView) findViewById(R.id.uliv_user_info);
        uliv_user_info.setCurrentItem(0);

        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_info = (TextView) findViewById(R.id.tv_info);

    }

    private void loadData() {
        if (isCurrentUser) {
            //如果是当前授权用户，直接设置信息
            setUserInfo();
        } else {
            //如果是其他人，则调用获取用户信息的接口
            loadUserInfo();
        }
    }

    private void setUserInfo() {
        imageLoader.displayImage(user.getCover_image_phone(), iv_cover_image);
        imageLoader.displayImage(user.getAvatar_large(), iv_avatar, ImageOptHelper.getAvatarOptions());
        tv_name.setText(user.getName());
        tv_follows.setText("关注  " + user.getFriends_count());
        tv_fans.setText("粉丝  " + user.getFollowers_count());
        tv_intro.setText(user.getDescription());
        if (TextUtils.isEmpty(user.getDescription())) {
            tv_intro.setText("简介：暂无简介");
            tv_info.setText("他很懒，什么都没有留下");
        } else {
            tv_intro.setText("简介：" + user.getDescription());
            tv_info.setText(user.getDescription());
        }
        tv_address.setText(user.getLocation());
    }

    private void loadUserInfo() {
        weiboApi.usersShow("", userName, new SimpleRequestListener(this, null) {
            @Override
            public void onComplete(String s) {
                super.onComplete(s);
                //获取用户信息并设置
                user = JSON.parseObject(s, User.class);
                setUserInfo();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = group.indexOfChild(group.findViewById(checkedId));
        uliv_user_info.setCurrentItem(index);
    }
}
