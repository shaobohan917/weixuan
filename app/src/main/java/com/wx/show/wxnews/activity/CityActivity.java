package com.wx.show.wxnews.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.adapter.CityAdapter;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.City;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        recyclerView.setGridLayout(3);
        recyclerView.setPullRefreshEnable(false);
        recyclerView.setPushRefreshEnable(false);
        initData();
        tvCityLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void initData() {
        mData = new ArrayList();
        String locCity = getIntent().getStringExtra("cityLoc");
        ArrayList cityList = (ArrayList) getIntent().getSerializableExtra("cityList");
        CityAdapter mAdapter = new CityAdapter(this,cityList);
        recyclerView.setAdapter(mAdapter);

        tvCityLoc.setText(locCity);
    }
}
