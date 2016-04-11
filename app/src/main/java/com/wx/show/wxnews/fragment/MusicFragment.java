package com.wx.show.wxnews.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.adapter.HomeMusicAdapter;
import com.wx.show.wxnews.entity.Music;
import com.wx.show.wxnews.util.LogUtil;

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
public class MusicFragment extends Fragment  {

    @Bind(R.id.vp_music)
    public ViewPager mMusicViewPager;
    @Bind(R.id.materialTabHost)
    MaterialTabHost tabHost;
    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView mRecyclerView;

    private HomeActivity activity;

    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合
    private MusicTaiwanFragment musicTaiwanFragment;
    private MusicJapanFragment musicJapanFragment;
    private ArrayList<String> mTitleList;

    public MusicFragment(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("qq","onCreateView");
        View view = inflater.inflate(R.layout.fragment_music, null);
        ButterKnife.bind(this, view);
        mRecyclerView.setPullRefreshEnable(false);
        mRecyclerView.setPushRefreshEnable(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("qq","onActivityCreated");
        musicTaiwanFragment = new MusicTaiwanFragment(activity);
        musicJapanFragment = new MusicJapanFragment(activity);

        mFragmentList.add(musicTaiwanFragment);
        mFragmentList.add(musicJapanFragment);

        mRecyclerView.setLinearLayout();
        mRecyclerView.setPullRefreshEnable(false);
        mRecyclerView.setPullRefreshEnable(false);

        mMusicViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        mMusicViewPager.setAdapter(new MyPagerAdapter(activity.getSupportFragmentManager(),mFragmentList));

        //添加标题
        mTitleList = new ArrayList();
        mTitleList.add("港台榜");
        mTitleList.add("日韩榜");

        for (int i = 0; i < mTitleList.size(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(mTitleList.get(i))
                            .setTabListener(new MaterialTabListener() {
                                @Override
                                public void onTabSelected(MaterialTab tab) {
                                    mMusicViewPager.setCurrentItem(tab.getPosition());
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

    public void setMusicTaiwanData(List<Music.ShowapiResBodyBean.PagebeanBean.SonglistBean> data) {
        musicTaiwanFragment.setData(data);
    }

    public void setMusicJapanData(List<Music.ShowapiResBodyBean.PagebeanBean.SonglistBean> data) {
        musicJapanFragment.setData(data);
    }
}
