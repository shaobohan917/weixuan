package com.wx.show.wxnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.LogTime;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.activity.NewsActivity;
import com.wx.show.wxnews.activity.ViewerActivity;
import com.wx.show.wxnews.entity.News;
import com.wx.show.wxnews.util.LogUtil;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeNewsAdapter extends RecyclerView.Adapter<HomeNewsAdapter.ViewHolder> {

    private HomeActivity context;
    private List<News.ResultBean.ListBean> mList;
    private String tag = "HomeNewsAdapter";

    public HomeNewsAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        Log.d(tag, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(tag, "onBindViewHolder");
        //ripple
        holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
        holder.itemView.setOnClickListener(holder);

        holder.tvTitle.setText("标题:" + mList.get(position).title);
        holder.tvSource.setText("来源:" + mList.get(position).source);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean saveTraffic = sharedPreferences.getBoolean(context.getString(R.string.key_sava_traffic), false);
        if(!saveTraffic){
            Glide.with(context).load(mList.get(position).firstImg).into(holder.ivImg);
            holder.ivImg.setOnClickListener(holder);
        }

        holder.tvTitle.setOnClickListener(holder);
        holder.tvSource.setOnClickListener(holder);
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
        @Bind(R.id.tv_source)
        TextView tvSource;

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
                case R.id.tv_title:
                case R.id.tv_source:
                    Intent intent = NewsActivity.getExtraDataIntent(context, mList.get(position).url);
                    context.startActivity(intent);
                    break;
                case R.id.iv_img:
                    String imgUrl = mList.get(position).firstImg;
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
