package com.wx.show.wxnews.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.adapter.HomeBeautyAdapter;
import com.wx.show.wxnews.entity.Beauty;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/4/15.
 * E-mail:397308937@qq.com
 */

@SuppressLint("ValidFragment")
public class BeautyFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    @Bind(R.id.ll_catogory)
    LinearLayout llCategory;
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.iv_arrow_category)
    ImageView ivArrow;

    private HomeActivity activity;
    private HomeBeautyAdapter mAdapter;
    private int num = 1;
    public boolean isFirst = true;  //是否是首次

    private String[] categories = {"大胸妹", "小清新", "文艺范", "性感妹", "大长腿", "黑丝袜", "小翘臀"};
    private int currentCategory = 34;

    public BeautyFragment() {
    }

    public BeautyFragment(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        ButterKnife.bind(this, view);

        mRecyclerView.setStaggeredGridLayout(3);
        mRecyclerView.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setPushRefreshEnable(true);
        mRecyclerView.setPullRefreshEnable(true);
        mRecyclerView.setOnPullLoadMoreListener(this);

        llCategory.setVisibility(View.VISIBLE);
        tvCategory.setText(categories[0]);
        llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst = true;
                ivArrow.setImageResource(R.drawable.arrow_up);
                //弹出选择框
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setIcon(R.drawable.beauty);
                builder.setTitle("类别");
                builder.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ivArrow.setImageResource(R.drawable.arrow_down);
                        tvCategory.setText(categories[which]);
                        currentCategory = 34 + which;
                        activity.getBeauty(1, 34 + which, true);
                    }
                });
                builder.show();
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.getBeauty(1, 34);
    }

    public void setData(List<Beauty.ShowapiResBodyBean.PagebeanBean> data) {
        if (mAdapter == null) {
            mAdapter = new HomeBeautyAdapter(activity, data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setPullLoadMoreCompleted();
        activity.disLoading();
    }

    @Override
    public void onRefresh() {
        isFirst = true;
        activity.getBeauty(1, currentCategory);
    }

    @Override
    public void onLoadMore() {
        isFirst = false;
        num++;
        activity.getBeauty(num, currentCategory);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
