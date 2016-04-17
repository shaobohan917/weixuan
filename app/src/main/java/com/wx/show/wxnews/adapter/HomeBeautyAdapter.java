package com.wx.show.wxnews.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.activity.NewsActivity;
import com.wx.show.wxnews.activity.ViewerActivity;
import com.wx.show.wxnews.entity.Beauty;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeBeautyAdapter extends RecyclerView.Adapter<HomeBeautyAdapter.ViewHolder> {

    private final ArrayList<Integer> mHeights;
    private HomeActivity context;
    private List<Beauty.ShowapiResBodyBean.PagebeanBean> mList;
    private LayoutInflater mInflater;

    public HomeBeautyAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;

        mInflater = LayoutInflater.from(context);
        mHeights = new ArrayList();
        for (int i = 0; i < mList.size(); i++) {
            mHeights.add((int) (200 + Math.random() * 500));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_beauty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewGroup.LayoutParams lp = holder.ivImg.getLayoutParams();
        if(position<15){
            lp.height = mHeights.get(position);
            holder.ivImg.setLayoutParams(lp);
        }

        Glide.with(context).load(mList.get(position).thumb).into(holder.ivImg);
        holder.tvTitle.setText(mList.get(position).title);
        holder.tvTitle.setOnClickListener(holder);
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
                    String imgUrl = mList.get(position).thumb;
                    if (!TextUtils.isEmpty(imgUrl)) {
                        Intent intent1 = new Intent(context, ViewerActivity.class);
                        intent1.putExtra("imgUrl", imgUrl);
                        ActivityTransitionLauncher.with(context).from(v).launch(intent1);
                    }
                    break;
                case R.id.tv_title:
                    context.mStartActivity(NewsActivity.getExtraDataIntent(context, mList.get(position).url), false);
                    break;
            }
        }
    }

    public void addData(int position)
    {
        mList.add(position,mList.get(position));
        mHeights.add( (int) (200 + Math.random() * 500));
        notifyItemInserted(position);
    }
}
