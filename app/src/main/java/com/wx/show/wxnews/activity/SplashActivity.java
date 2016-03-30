package com.wx.show.wxnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.News;
import com.wx.show.wxnews.entity.SplashImg;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Luka on 2016/3/28.
 * E-mail:397308937@qq.com
 */
public class SplashActivity extends BaseActivity {
    @Bind(R.id.iv_img)
    ImageView ivImg;
    private String imgUrl = "http://news-at.zhihu.com/api/5/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
//        getImgByRxJava();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                mStartActivity(intent, true);
            }
        };
        timer.schedule(timerTask, 500);
    }

    /**
     * 获取数据
     */
    public void getImgByRxJava() {
        Observable<SplashImg> observable = getUrlService(imgUrl).getSplashImg();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SplashImg>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(SplashActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(SplashImg splashImg) {
                        if (splashImg.error_code == 0) {
                            Glide.with(SplashActivity.this).load(splashImg.img).into(ivImg);
                        } else {
                            ToastUtil.showToast(SplashActivity.this, splashImg.error_code + ":" + splashImg.reason);
                        }
                    }
                });
    }
}
