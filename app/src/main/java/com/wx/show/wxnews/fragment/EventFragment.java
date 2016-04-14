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
import com.wx.show.wxnews.adapter.CityAdapter;
import com.wx.show.wxnews.adapter.HomeEventAdapter;
import com.wx.show.wxnews.entity.City;
import com.wx.show.wxnews.entity.Event;
import com.wx.show.wxnews.entity.Location;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.ArrayList;
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
    private Location location;
    private String locCity;
    private HomeActivity activity;
    private HomeEventAdapter mEventAdapter;
    private ArrayList<City.LocsBean> mCityList;
    private CityAdapter mCityAdapter;

    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    @Bind(R.id.rl_city)
    RelativeLayout rlCity;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.tv_city_more)

    TextView tvCityMore;
    private String cityId;
    private boolean isContain;


    public EventFragment() {
    }

    public EventFragment(HomeActivity activity, Location location) {
        this.activity = activity;
        this.location = location;
        locCity = location.city;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container,false);
        ButterKnife.bind(this,view);

        rlCity.setVisibility(View.VISIBLE);
        final String cityLoc = "当前定位城市:"+ locCity.substring(0,2);
        if(tvCity!=null){
            tvCity.setText(cityLoc);
        }
        tvCityMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CityActivity.class);
                intent.putExtra("cityList",mCityList);
                intent.putExtra("location",location);
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
        activity.getCityList();
    }

    private void getLocEvent() {
        for(City.LocsBean city:mCityList){
            if(this.locCity.contains(city.name)){
                activity.getCityEvent(city.id);
                isContain = true;
            }
        }
        if (!isContain) {
            //没有该城市，显示上海
            activity.getCityEvent("108296");
            ToastUtil.showToast(activity, "无法获取您所在城市的活动/n为您显示上海的活动");
        }
    }

    public void setEventData(List<Event.EventsBean> data) {
        if (mEventAdapter == null) {
            mEventAdapter = new HomeEventAdapter(activity, data);
            mRecyclerView.setAdapter(mEventAdapter);
        } else {
            mEventAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setPullLoadMoreCompleted();
        activity.disLoading();
    }

    public void getCityList(ArrayList list){
        mCityList = list;
        getLocEvent();
    }

    @Override
    public void onRefresh() {
        activity.getCityEvent(cityId);
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
        activity.getCityEvent(cityId);
        tvCity.setText("当前城市:"+data.getStringExtra("cityName"));
    }

}
