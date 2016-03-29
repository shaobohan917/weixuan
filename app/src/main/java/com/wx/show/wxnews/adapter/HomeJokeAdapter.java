package com.wx.show.wxnews.adapter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.wx.show.wxnews.entity.Joke;
import com.wx.show.wxnews.util.NetUtil;
import com.wx.show.wxnews.util.SPUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeJokeAdapter extends RecyclerView.Adapter<HomeJokeAdapter.ViewHolder> {

    private HomeActivity context;
    private List<Joke.ResultBean.DataBean> mList;

    public HomeJokeAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_joke, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        if(!SPUtil.isSaveTraffic(context)|| NetUtil.isWifi(context)){
            Glide.with(context).load(mList.get(position).url).into(holder.ivImg);
        }
        holder.tvTitle.setText(mList.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_img)
        ImageView ivImg;
        @Bind(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
