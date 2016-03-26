package com.wx.show.wxnews.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.activity.ViewerActivity;
import com.wx.show.wxnews.entity.Joke;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeJokeAdapter extends RecyclerView.Adapter<HomeJokeAdapter.ViewHolder> {

    private HomeActivity context;
    private List<Joke.ResultBean.DataBean> mList;
    private String tag = "HomeJokeAdapter";

    public HomeJokeAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_joke, parent, false);
        Log.d(tag, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(tag, "onBindViewHolder");
        Glide.with(context).load(mList.get(position).url).into(holder.ivImg);
        holder.tvTitle.setText(mList.get(position).content);
        holder.ivImg.setOnClickListener(holder);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.iv_img)
        ImageView ivImg;
        @Bind(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_img:
                    String imgUrl = mList.get(position).url;
                    if (!TextUtils.isEmpty(imgUrl)) {
                        Intent intent1 = new Intent(context, ViewerActivity.class);
                        intent1.putExtra("imgUrl", imgUrl);
                        ActivityTransitionLauncher.with(context).from(v).launch(intent1);
                    }
                    break;
            }
        }
    }
}
