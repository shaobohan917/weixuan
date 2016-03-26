package com.wx.show.wxnews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/23.
 */
public class ViewerActivity extends Activity {
    @Bind(R.id.iv_pic)
    ImageView ivPic;
    @Bind(R.id.ll_content)
    LinearLayout llContent;

    ExitActivityTransition exitTransition;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        String imgFile = getIntent().getStringExtra("imgUrl");
        Glide.with(this).load(imgFile).listener(new RequestListener<String, GlideDrawable>() {
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
}
