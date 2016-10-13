package com.liudong.weibo.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.utils.TitleBuilder;

public class WebViewActivity extends BaseActivity {

    private WebView wb_html;
    private String url;
    private TitleBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        url = getIntent().getStringExtra("url");
        initView();
    }

    private void initView() {

        builder = new TitleBuilder(this)
                .setTitleText("加载中...")
                .setLeftImage(R.drawable.detail_left_sel)
                .setRightImage(R.drawable.detail_right_sel)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        wb_html = (WebView) findViewById(R.id.wb_html);
        WebSettings settings = wb_html.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wb_html.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        wb_html.loadUrl(url);

        wb_html.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                builder.setTitleText(title);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wb_html.canGoBack()) {
            wb_html.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
