package com.wx.show.wxnews.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.activity.NewsActivity;
import com.wx.show.wxnews.activity.ViewerActivity;
import com.wx.show.wxnews.util.ImageUtil;
import com.wx.show.wxnews.util.LogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/4/15.
 * E-mail:397308937@qq.com
 */

@SuppressLint("ValidFragment")
public class AmberFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    @Bind(R.id.sv_search)
    SearchView svSearch;

    private HomeActivity activity;
    private HomeAmberAdapter mAdapter;
    private int mPage = 1;
    public String mQuery = "guocaijie";
    public AmberFragment() {
    }

    public AmberFragment(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        ButterKnife.bind(this, view);

        mRecyclerView.setGridLayout(2);
        mRecyclerView.setPushRefreshEnable(true);
        mRecyclerView.setPullRefreshEnable(true);
        mRecyclerView.setOnPullLoadMoreListener(this);

        svSearch.setVisibility(View.VISIBLE);
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                char[] chars = query.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < chars.length; i++) {
                    sb.append(Pinyin.toPinyin(chars[i]).toLowerCase());
                }
                query = sb.toString();
                mQuery = query;
                LogUtil.d("haha", query);
                mPage = 1;
                activity.getAmber(query, mPage);

                svSearch.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.getAmber(mQuery,mPage);
    }

    public void setData(List<String> data) {
        if (mAdapter == null) {
            mAdapter = new HomeAmberAdapter(activity, data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setPullLoadMoreCompleted();
        activity.disLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        activity.getAmber(mQuery,mPage);

    }

    @Override
    public void onLoadMore() {
        mPage++;
        activity.getAmber(mQuery,mPage);
    }


    public class HomeAmberAdapter extends RecyclerView.Adapter<HomeAmberAdapter.ViewHolder> {

        private HomeActivity context;
        private List<String> mList;

        public HomeAmberAdapter(HomeActivity context, List mList) {
            this.context = context;
            this.mList = mList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_beauty, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            ImageUtil.showImg(context, mList.get(position), holder.ivImg);
            holder.ivImg.setOnClickListener(holder);
//        holder.tvTitle.setText(mList.get(position).title);
//        holder.tvTitle.setOnClickListener(holder);
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @Bind(R.id.iv_img)
            ImageView ivImg;

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
                        String imgUrl = mList.get(position);
                        if (!TextUtils.isEmpty(imgUrl)) {
                            Intent intent1 = new Intent(context, ViewerActivity.class);
                            intent1.putExtra("imgUrl", imgUrl);
                            ActivityTransitionLauncher.with(context).from(v).launch(intent1);
                        }
                        break;
                    case R.id.tv_title:
                        context.mStartActivity(NewsActivity.getExtraDataIntent(context, mList.get(position)), false);
                        break;
                }
            }
        }
    }
}
