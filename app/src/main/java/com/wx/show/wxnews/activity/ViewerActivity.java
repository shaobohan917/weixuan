package com.wx.show.wxnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/23.
 */
public class ViewerActivity extends BaseActivity{
    @Bind(R.id.iv_pic)
    ImageView ivPic;
    @Bind(R.id.ll_content)
    LinearLayout llContent;

    ExitActivityTransition exitTransition;
    private String imgUrl;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        ButterKnife.bind(this);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exitTransition != null) {
                    exitTransition.exit(ViewerActivity.this);
                }
            }
        });
        getExtraData();
        Glide.with(this).load(imgUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                exitTransition = ActivityTransition.with(getIntent()).duration(400).to(ivPic).start(savedInstanceState);
                return false;
            }
        }).into(ivPic);
    }

    @Override
    public void onBackPressed() {
        if (exitTransition != null) {
            exitTransition.exit(this);
        }
    }

    public static Intent getExtraDataIntent(HomeActivity context, String imgUrl) {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra("imgUrl", imgUrl);
        return intent;
    }

    private void getExtraData() {
        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");
    }
}
