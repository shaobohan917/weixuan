package com.wx.show.wxnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.City;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Luka on 2016/4/5.
 * 397308937@qq.com
 */
public class CityActivity extends BaseActivity {
    private ArrayList<City.LocsBean> mData;

    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView recyclerView;
    private MyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        getCityList();

        mData = new ArrayList();
        recyclerView.setGridLayout(3);
        recyclerView.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
    }

    public void getCityList() {
        Observable<City> observable = getUrlService(doubanBaseUrl).getCityList();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<City>() {
            @Override
            public void onCompleted() {
                if(mAdapter==null){
                    mAdapter =  new MyAdapter(mData);
                    recyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
                disLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(City city) {
                mData.clear();
                mData.addAll(city.locs);
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<City.LocsBean> data;

        public MyAdapter(ArrayList<City.LocsBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CityActivity.this).inflate(R.layout.item_city, parent, false);
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

            private void setPosition(int position){
                this.position = position;
            }

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            public void onClick(View v) {
                ToastUtil.showToast(CityActivity.this,data.get(position).name);
                Intent intent = new Intent();
                intent.putExtra("cityId",data.get(position).id);
                intent.putExtra("cityName",data.get(position).name);
                setResult(RESULT_OK,intent);
                finish();
            }
        }

    }
}
