package com.liudong.weibo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.liudong.weibo.BaseFragment;
import com.liudong.weibo.R;
import com.liudong.weibo.activity.CommentActivity;
import com.liudong.weibo.activity.MentionActivity;
import com.liudong.weibo.utils.TitleBuilder;
import com.liudong.weibo.utils.ToastUtils;

public class MessageFragment extends BaseFragment {


    private View view;
    private View il_search;
    private EditText hot_search;

    private LinearLayout ll_at;
    private LinearLayout ll_comment;
    private LinearLayout ll_like;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);

        initView();

        return view;
    }

    private void initView() {
        new TitleBuilder(view)
                .setTitleText("消息")
                .setLeftText("发现群")
                .setRightImage(R.drawable.message_right_sel);

        il_search = view.findViewById(R.id.il_search);
        hot_search = (EditText) il_search.findViewById(R.id.hot_search);
        hot_search.setHint("搜索联系人和群");

        ll_at = (LinearLayout) view.findViewById(R.id.ll_at);
        ll_comment = (LinearLayout) view.findViewById(R.id.ll_comment);
        ll_like = (LinearLayout) view.findViewById(R.id.ll_like);

        ll_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, MentionActivity.class);
                mainActivity.startActivity(intent);
            }
        });

        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, CommentActivity.class);
                mainActivity.startActivity(intent);
            }
        });

        ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(mainActivity, "API暂未支持", Toast.LENGTH_LONG);
            }
        });
    }

}
