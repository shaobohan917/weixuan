package com.wx.show.wxnews.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.activity.NewsActivity;
import com.wx.show.wxnews.activity.ViewerActivity;
import com.wx.show.wxnews.entity.Movie;
import com.wx.show.wxnews.util.ImageUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeMovieAdapter extends RecyclerView.Adapter<HomeMovieAdapter.ViewHolder> {

    private HomeActivity context;
    private List<Movie.SubjectsBean> mList;

    public HomeMovieAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_intheater, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.mStartActivity(NewsActivity.getExtraDataIntent(context, mList.get(position).alt), false);
            }
        });
        ImageUtil.showImg(context, mList.get(position).images.medium, holder.ivImg);
        holder.tvTitle.setText(mList.get(position).title);
        String genres = mList.get(position).genres.toString();
        genres = genres.substring(1, genres.length() - 1);
        holder.tvGenres.setText("类型:" + genres);
        holder.tvAverage.setText("评分:" + mList.get(position).rating.average + "");
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
        @Bind(R.id.tv_genres)
        TextView tvGenres;
        @Bind(R.id.tv_average)
        TextView tvAverage;
        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_img:
                    String imgUrl = mList.get(position).images.large;
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
