package com.wx.show.wxnews.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;

import com.roger.catloadinglibrary.CatLoadingView;
import com.umeng.analytics.MobclickAgent;
import com.wx.show.wxnews.entity.APIService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Luka on 2016/3/23.
 */
public class BaseActivity extends AppCompatActivity {
    public String doubanBaseUrl = "https://api.douban.com/v2/";
    public String bookUrl = "http://apis.juhe.cn/";

    public String zhihuDailyUrl = "http://news.at.zhihu.com/api/4/";
    public String bookKey = "91b9052ac36278374cfaf1b1fcf05b5a";

    private CatLoadingView loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public APIService getUrlService(String url) {
        showLoading();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        return service;
    }

    public void showLoading() {
        if(loadingView==null){
            loadingView = new CatLoadingView();
            loadingView.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * 隐藏Dialog
     */
    public void disLoading() {
        if (!isFinishing() && loadingView != null){
            loadingView.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingView != null && loadingView.isVisible()) {
            loadingView.dismiss();
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void mStartActivity(Intent intent, boolean needFinish) {
        startActivity(intent);
        if (needFinish) {
            finish();
        }
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_slide_out_bottom);
    }
}
