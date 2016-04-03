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
import com.wx.show.wxnews.entity.Event;
import com.wx.show.wxnews.util.ImageUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeEventAdapter extends RecyclerView.Adapter<HomeEventAdapter.ViewHolder> {

    private HomeActivity context;
    private List<Event.EventsBean> mList;

    public HomeEventAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //ripple
        holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewsActivity.getExtraDataIntent(context, mList.get(position).adapt_url);
                context.startActivity(intent);
            }
        });

        holder.tvTitle.setText(mList.get(position).title);
        holder.subcategoryName.setText("类型:"+mList.get(position).subcategory_name);
        holder.tvAddress.setText("地址:" + mList.get(position).address);

        ImageUtil.showImg(context,mList.get(position).image_lmobile,holder.ivImg);
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
        @Bind(R.id.subcategory_name)
        TextView subcategoryName;
        @Bind(R.id.tv_address)
        TextView tvAddress;

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
                    String imgUrl = mList.get(position).image_hlarge;
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

