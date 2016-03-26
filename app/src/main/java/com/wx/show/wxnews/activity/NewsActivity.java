package com.wx.show.wxnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;

/**
 * Created by Luka on 2016/3/23.
 */
public class NewsActivity extends BaseActivity {

    private WebView webView;
    private String newsUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getExtraData();
        initView();
    }

    public static Intent getExtraDataIntent(Context context, String newsUrl) {
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("newsUrl", newsUrl);
        return intent;
    }

    private void getExtraData() {
        Intent intent = getIntent();
        newsUrl = intent.getStringExtra("newsUrl");
    }

    private void initView() {
        showLoading();
        webView = (WebView) findViewById(R.id.wv_web);
        webView.setWebViewClient(new WebViewClient() { // 设置web视图
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                disLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.getSettings().setBlockNetworkImage(false);
                disLoading();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl(newsUrl);
    }
}
