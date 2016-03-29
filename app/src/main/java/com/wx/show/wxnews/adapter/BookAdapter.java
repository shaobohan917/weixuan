package com.wx.show.wxnews.adapter;

import android.content.Intent;
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
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.BookActivity;
import com.wx.show.wxnews.activity.NewsActivity;
import com.wx.show.wxnews.activity.ViewerActivity;
import com.wx.show.wxnews.entity.Book;
import com.wx.show.wxnews.util.NetUtil;
import com.wx.show.wxnews.util.SPUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private BookActivity context;
    private List<Book.ResultBean.DataBean> mList;
    private String tag = "hehe";

    public BookAdapter(BookActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        Log.d(tag, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(tag, "onBindViewHolder");
        holder.tvTitle.setText("书名:" + mList.get(position).title);
        holder.tvCatalog.setText("类目:" + mList.get(position).catalog);
        holder.tvTags.setText("标签:" + mList.get(position).tags);
        holder.tvSub1.setText("简介:" + mList.get(position).sub1);
        holder.tvReading.setText(mList.get(position).reading);

        if(!SPUtil.isSaveTraffic(context)||NetUtil.isWifi(context)){
            Glide.with(context).load(mList.get(position).img).into(holder.ivImg);
            holder.ivImg.setOnClickListener(holder);
        }
        holder.llContent.setOnClickListener(holder);
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
        @Bind(R.id.tv_catalog)
        TextView tvCatalog;
        @Bind(R.id.tv_tags)
        TextView tvTags;
        @Bind(R.id.tv_sub1)
        TextView tvSub1;
        @Bind(R.id.tv_reading)
        TextView tvReading;
        @Bind(R.id.ll_content)
        LinearLayout llContent;

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
                case R.id.ll_content:
                    String onlineUrl = mList.get(position).online;
                    if (!TextUtils.isEmpty(onlineUrl)) {
                        int start = onlineUrl.indexOf("h");
                        String[] urls = onlineUrl.split(" ");
                        String jdUrl = urls[0].substring(start);
                        Intent intent = NewsActivity.getExtraDataIntent(context, jdUrl);
                        context.startActivity(intent);
                    }
                    break;
                case R.id.iv_img:
                    String imgUrl = mList.get(position).img;
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
