package com.wx.show.wxnews.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.CityActivity;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.adapter.HomeEventAdapter;
import com.wx.show.wxnews.entity.Event;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/24.
 * E-mail:397308937@qq.com
 */

@SuppressLint("ValidFragment")
public class EventFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener{

    private static final int CITY_NAME = 1;
    private String location;
    private HomeActivity activity;
    private HomeEventAdapter mAdapter;
    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    @Bind(R.id.rl_city)
    RelativeLayout rlCity;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.tv_city_more)
    TextView tvCityMore;
    private String cityId;


    public EventFragment() {
    }

    public EventFragment(HomeActivity activity,String location) {
        this.activity = activity;
        this.location = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        ButterKnife.bind(this,view);
        rlCity.setVisibility(View.VISIBLE);
        if(tvCity!=null){
            tvCity.setText("当前定位城市:"+location.substring(0,2));
        }
        tvCityMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CityActivity.class);
                startActivityForResult(intent,CITY_NAME);
            }
        });
        mRecyclerView.setLinearLayout();
        mRecyclerView.setPushRefreshEnable(false);
        mRecyclerView.setOnPullLoadMoreListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.getEvent("108296");
    }

    public void setData(List<Event.EventsBean> data) {
        if (mAdapter == null) {
            mAdapter = new HomeEventAdapter(activity, data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setPullLoadMoreCompleted();
    }

    @Override
    public void onRefresh() {
        activity.getEvent(cityId);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cityId = data.getStringExtra("cityId");
        activity.getEvent(cityId);
        tvCity.setText("当前城市:"+data.getStringExtra("cityName"));
    }

}
