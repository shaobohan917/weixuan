package com.wx.show.wxnews.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.activity.ViewerActivity;
import com.wx.show.wxnews.activity.ZhihuNewsActivity;
import com.wx.show.wxnews.entity.ZhihuDaily;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeZhihuDailyAdapter extends RecyclerView.Adapter<HomeZhihuDailyAdapter.ViewHolder> {

    private HomeActivity context;
    private List<ZhihuDaily.StoriesBean> mList;
    private String tag = "hehe";

    public HomeZhihuDailyAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_zhihu, parent, false);
        Log.d(tag, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(tag, "onBindViewHolder");
        holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
        holder.itemView.setOnClickListener(holder);

        Glide.with(context).load(mList.get(position).images.get(0)).into(holder.ivImg);
        holder.tvTitle.setText("标题:" + mList.get(position).title);
        holder.tvTitle.setOnClickListener(holder);
        holder.ivImg.setOnClickListener(holder);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_img)
        ImageView ivImg;

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
                    Intent intent = ViewerActivity.getExtraDataIntent(context, mList.get(position).images.get(0));
                    context.startActivity(intent);
                    break;
                case R.id.tv_title:
                    Intent intent1 = ZhihuNewsActivity.getExtraDataIntent(context, mList.get(position).id);
                    context.startActivity(intent1);
                    break;
            }
        }
    }
}
