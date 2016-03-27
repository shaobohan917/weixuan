package com.wx.show.wxnews.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.BookActivity;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.entity.BookCatalog;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.ViewHolder> {

    private HomeActivity context;
    private List<BookCatalog.ResultBean> mList;
    private String tag = "hehe";

    public HomeBookAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookcatalog, parent, false);
        Log.d(tag, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(tag, "onBindViewHolder");
        holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
        holder.itemView.setOnClickListener(holder);

        holder.tvCatalog.setText(mList.get(position).catalog);
        holder.tvCatalog.setOnClickListener(holder);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_catalog)
        TextView tvCatalog;

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
                case R.id.tv_catalog:
                    Intent intent = BookActivity.getExtraDataIntent(context, Integer.valueOf(mList.get(position).id));
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
