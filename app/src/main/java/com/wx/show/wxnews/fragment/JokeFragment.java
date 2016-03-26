package com.wx.show.wxnews.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.adapter.HomeJokeAdapter;
import com.wx.show.wxnews.entity.Joke;

import java.util.List;

/**
 * Created by Luka on 2016/3/24.
 * E-mail:397308937@qq.com
 */

@SuppressLint("ValidFragment")
public class JokeFragment extends Fragment {
    private PullLoadMoreRecyclerView mRecyclerView;
    private HomeJokeAdapter mAdapter;
    private HomeActivity activity;

    public JokeFragment() {
    }

    public JokeFragment(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        mRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLinearLayout();
        mRecyclerView.setOnPullLoadMoreListener(activity);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setData(List<Joke.ResultBean.DataBean> data) {
        if (mAdapter == null) {
            mAdapter = new HomeJokeAdapter(activity, data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setPullLoadMoreCompleted();
    }

}
