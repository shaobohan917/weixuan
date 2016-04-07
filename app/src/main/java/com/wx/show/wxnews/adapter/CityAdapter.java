package com.wx.show.wxnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.CityActivity;
import com.wx.show.wxnews.entity.City;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/4/6.
 * 397308937@qq.com
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<City.LocsBean> data;
    private CityActivity context;

    public CityAdapter(CityActivity context, List<City.LocsBean> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvCity.setText(data.get(position).name);
        holder.tvCity.setOnClickListener(holder);

        holder.setPosition(position);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_city)
        TextView tvCity;

        private int position;

        private void setPosition(int position) {
            this.position = position;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("cityId", data.get(position).id);
            intent.putExtra("cityName", data.get(position).name);
            context.setResult(context.RESULT_OK, intent);
            context.finish();
        }
    }

}
