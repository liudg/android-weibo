package com.liudong.weibo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * edittext中的文字处理
 * Created by liudong on 2016/5/4.
 */
public class EditTextStringUtils {

    public static SpannableString getWeiboContent(final Context context, TextView tv, String source) {
        SpannableString spannableString = new SpannableString(source);

        String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";

        Pattern pattern = Pattern.compile(regexEmoji);
        Matcher matcher = pattern.matcher(spannableString);

        while (matcher.find()) {
            //获取匹配的具体字符
            String emojiStr = matcher.group();

            //图片部分
            if (emojiStr != null) {
                int start = matcher.start();
                int imgRes = EmotionUtils.getImgByName(emojiStr);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
                if (bitmap != null) {
                    int size = (int) tv.getTextSize();
                    bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    spannableString.setSpan(imageSpan, start, start + emojiStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spannableString;
    }
}
