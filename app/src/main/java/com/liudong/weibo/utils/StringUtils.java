package com.liudong.weibo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liudong.weibo.R;
import com.liudong.weibo.activity.UserInfoActivity;
import com.liudong.weibo.activity.WebViewActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微博正文中表情，@，话题显示不同颜色以及添加点击事件处理
 * SpannableString为扩展性文字，可以将一些图片，特殊文字样式等拓展内容附着到我们的文本上
 * Created by liudong on 2016/5/4.
 */
public class StringUtils {
    public static SpannableString getWeiboContent(final Context context, TextView tv, String source) {
        //正则表达式匹配
        String regexAt = "@[\u4e00-\u9fa5\\w]+";
        String regexTopic = "#[\u4e00-\u9fa5\\w]+#";
        String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";
        String regexUrl = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        //正则规则 涉及到group()处理
        String regex = "(" + regexAt + ")|(" + regexTopic + ")|(" + regexEmoji + ")|(" + regexUrl + ")";

        SpannableString spannableString = new SpannableString(source);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannableString);

        //给字符串添加点击事件需要这样处理
        if (matcher.find()) {
            //设置该句使文本的超连接起作用
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            matcher.reset();
        }

        while (matcher.find()) {
            //获取匹配的具体字符
            final String atStr = matcher.group(1);
            final String topicStr = matcher.group(2);
            String emojiStr = matcher.group(3);
            final String urlStr = matcher.group(4);

            //非空判断 @部分
            if (atStr != null) {
                int start = matcher.start(1);
                MyClickableSpan clickableSpan = new MyClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent(context, UserInfoActivity.class);
                        intent.putExtra("userName", atStr.substring(1));
                        context.startActivity(intent);
                    }
                };
                spannableString.setSpan(clickableSpan, start, start + atStr.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //话题部分
            if (topicStr != null) {
                int start = matcher.start(2);
                MyClickableSpan clickableSpan = new MyClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        // TODO: 2016/5/4  话题点击事件处理
                        ToastUtils.showToast(context, "话题：" + topicStr, Toast.LENGTH_LONG);
                    }
                };
                spannableString.setSpan(clickableSpan, start, start + topicStr.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //图片部分
            if (emojiStr != null) {
                int start = matcher.start(3);

                int imgRes = EmotionUtils.getImgByName(emojiStr);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
                if (bitmap != null) {
                    int size = (int) tv.getTextSize();
                    bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    spannableString.setSpan(imageSpan, start, start + emojiStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            //网址
            if (urlStr != null) {
                int start = matcher.start(4);
                MyClickableSpan clickableSpan = new MyClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("url",urlStr);
                        context.startActivity(intent);
                    }
                };
                spannableString.setSpan(clickableSpan, start, start + urlStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }

        return spannableString;
    }

    //ClickableSpan默认有自定的颜色跟下划线，我们只需要一个蓝色字体就行，所以需要重写它
    static class MyClickableSpan extends ClickableSpan {

        private Context context;

        public MyClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.txt_at_blue));
            ds.setUnderlineText(false);
        }
    }
}
