package com.wx.show.wxnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.ZhihuNews;
import com.wx.show.wxnews.util.ToastUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Luka on 2016/3/23.
 */
public class ZhihuNewsActivity extends BaseActivity {
    private String zhihuNewsUrl = "http://news-at.zhihu.com/api/4/news/";
    private WebView webView;
    private String newsId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getExtraData();
        initView();
        getZhihuNewsByRxJava();
    }

    public static Intent getExtraDataIntent(Context context, int newsId) {
        Intent intent = new Intent(context, ZhihuNewsActivity.class);
        intent.putExtra("newsId", newsId);
        return intent;
    }

    private void getExtraData() {
        Intent intent = getIntent();
        newsId = intent.getStringExtra("newsId");
    }

    public void getZhihuNewsByRxJava() {
        Observable<ZhihuNews> observable = getUrlService(zhihuNewsUrl).getZhihuNews(newsId);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuNews>() {
                    @Override
                    public void onCompleted() {
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(ZhihuNewsActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuNews zhihuNews) {
                        webView.loadUrl(zhihuNews.share_url);
                    }
                });
    }

    private void initView() {
        showLoading();
        webView = (WebView) findViewById(R.id.wv_web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);

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
    }
}
