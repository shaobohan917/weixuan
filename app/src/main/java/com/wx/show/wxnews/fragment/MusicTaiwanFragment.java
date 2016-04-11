package com.wx.show.wxnews.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.adapter.HomeMusicAdapter;
import com.wx.show.wxnews.entity.Music;
import com.wx.show.wxnews.util.LogUtil;

import java.util.List;

/**
 * Created by Luka on 2016/3/24.
 * E-mail:397308937@qq.com
 */

@SuppressLint("ValidFragment")
public class MusicTaiwanFragment extends Fragment {

    private PullLoadMoreRecyclerView mRecyclerView;
    private HomeActivity activity;
    private HomeMusicAdapter mAdapter;

    public MusicTaiwanFragment() {
    }

    public MusicTaiwanFragment(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("qq","onCreateView1");
        View view = inflater.inflate(R.layout.activity_main, container,false);
        mRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLinearLayout();
        mRecyclerView.setPullRefreshEnable(false);
        mRecyclerView.setPushRefreshEnable(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtil.d("qq","onActivityCreated1");
        super.onActivityCreated(savedInstanceState);
        activity.getMusic("5");
    }

    public void setData(List<Music.ShowapiResBodyBean.PagebeanBean.SonglistBean> data) {
        if (mAdapter == null) {
            mAdapter = new HomeMusicAdapter(activity, data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setPullLoadMoreCompleted();
        activity.disLoading();
    }
}
