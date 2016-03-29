package com.wx.show.wxnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Luka on 2016/3/28.
 * E-mail:397308937@qq.com
 */
public class SplashActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                mStartActivity(intent, true);
            }
        };
        timer.schedule(timerTask, 3000);
    }
}
