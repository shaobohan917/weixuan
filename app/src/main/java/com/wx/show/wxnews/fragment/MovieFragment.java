package com.wx.show.wxnews.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.adapter.HomeMovieAdapter;
import com.wx.show.wxnews.entity.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Luka on 2016/3/24.
 * E-mail:397308937@qq.com
 */

@SuppressLint("ValidFragment")
public class MovieFragment extends Fragment  {
    @Bind(R.id.vp_movie)
    public ViewPager mMovieViewPager;
    @Bind(R.id.materialTabHost)
    MaterialTabHost tabHost;
    @Bind(R.id.search_View)
    public android.widget.SearchView searchView;
    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView mRecyclerView;

    private HomeMovieAdapter mAdapter;

    private HomeActivity activity;

    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private MovieInTheaterFragment inTheaterFragment;
    private MovieComingSoonFragment comingSoonFragment;
    private ArrayList<String> mTitleList;
    public boolean searchOpen;

    public MovieFragment(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, null);
        ButterKnife.bind(this, view);
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //设置背景
//                searchView.setBackgroundResource(R.color.white);
//            }
//        });
        searchView.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mMovieViewPager.setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                activity.getMovieSearch(query);
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
        inTheaterFragment = new MovieInTheaterFragment(activity);
        comingSoonFragment = new MovieComingSoonFragment(activity);
        mFragmentList.add(inTheaterFragment);
        mFragmentList.add(comingSoonFragment);

        mRecyclerView.setLinearLayout();
        mRecyclerView.setPullRefreshEnable(false);
        mRecyclerView.setPullRefreshEnable(false);

        mMovieViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mMovieViewPager.setAdapter(new MyPagerAdapter(activity.getSupportFragmentManager(),mFragmentList));

        //添加标题
        mTitleList = new ArrayList();
        mTitleList.add("正在上映");
        mTitleList.add("即将上映");

        for (int i = 0; i < mTitleList.size(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(mTitleList.get(i))
                            .setTabListener(new MaterialTabListener() {
                                @Override
                                public void onTabSelected(MaterialTab tab) {
                                    mMovieViewPager.setCurrentItem(tab.getPosition());
                                }

                                @Override
                                public void onTabReselected(MaterialTab tab) {

                                }

                                @Override
                                public void onTabUnselected(MaterialTab tab) {

                                }
                            })
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //注释super，防止页面被销毁
            //super.destroyItem(container, position, object);
        }
    }

    public void setInThreaterData(List<Movie.SubjectsBean> data) {
        inTheaterFragment.setData(data);
    }

    public void setCoomingSoonData(List<Movie.SubjectsBean> data) {
        comingSoonFragment.setData(data);
    }


    public void setSearchData(ArrayList<Movie.SubjectsBean> data){
        mMovieViewPager.setVisibility(View.GONE);
        if (mAdapter == null) {
            mAdapter = new HomeMovieAdapter(activity, data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setPullLoadMoreCompleted();
        activity.disLoading();

        searchOpen = true;
        activity.disLoading();
    }

}
