package com.wx.show.wxnews.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;

import com.umeng.analytics.MobclickAgent;
import com.wx.show.wxnews.entity.APIService;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Luka on 2016/3/23.
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mLoadingDialog;
    public boolean dismissLoadingDialog = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public APIService getUrlService(String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        return service;
    }


    public void showLoading() {
        showLoading("请稍后");
    }

    /**
     * 显示Dialog
     *
     * @canReturn
     */
    public void showLoading(String content) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(this);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setMessage(content);
            Window window = mLoadingDialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
        if (!isFinishing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 隐藏Dialog
     */
    public void disLoading() {
        dismissLoadingDialog();
    }

    public void dismissLoadingDialog() {
        if (!isFinishing() && mLoadingDialog != null && dismissLoadingDialog) {
            mLoadingDialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
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

    public void mStartActivity(Intent intent, boolean needFinish){
        startActivity(intent);
        if(needFinish){finish();}
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_slide_out_bottom);
    }
}
