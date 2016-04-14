package com.wx.show.wxnews.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.adapter.CityAdapter;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.City;
import com.wx.show.wxnews.entity.Location;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/4/5.
 * 397308937@qq.com
 */
public class CityActivity extends BaseActivity {
    private ArrayList<City.LocsBean> mData;

    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView recyclerView;
    @Bind(R.id.tv_city_loc)
    TextView tvCityLoc;
    @Bind(R.id.iv_loc)
    ImageView ivLoc;
    @Bind(R.id.bmapView)
    MapView mapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mCurrentMarker;

    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    private double latitude;
    private float radius;
    private double longtitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        recyclerView.setGridLayout(3);
        recyclerView.setPullRefreshEnable(false);
        recyclerView.setPushRefreshEnable(false);
        recyclerView.setGravity(Gravity.CENTER_HORIZONTAL);
        initData();
        getLoc();
        gotoLoc();
        ivLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoc();
            }
        });
    }

    //获取当前位置
    private void getLoc() {
        mBaiduMap = mapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(radius)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(latitude)
                .longitude(longtitude).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding10);
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(config);
        // 当不需要定位图层时关闭定位图层
        // mBaiduMap.setMyLocationEnabled(false);
    }

    //定位到当前位置
    private void gotoLoc() {
        {
            //设定中心点坐标
            LatLng cenpt = new LatLng(latitude, longtitude);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(15)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }
    }

    private void initData() {
        mData = new ArrayList();
        ArrayList cityList = (ArrayList) getIntent().getSerializableExtra("cityList");
        Location location = (Location) getIntent().getSerializableExtra("location");
        String locCity = location.city;
        radius = location.radius;
        latitude = location.latitude;
        longtitude = location.longtitude;

        CityAdapter mAdapter = new CityAdapter(this,cityList);
        recyclerView.setAdapter(mAdapter);
        tvCityLoc.setText("当前城市:"+locCity.substring(0,2));
    }
}
