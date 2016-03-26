package com.wx.show.wxnews.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.BookCatalog;
import com.wx.show.wxnews.entity.Joke;
import com.wx.show.wxnews.entity.News;
import com.wx.show.wxnews.fragment.BookFragment;
import com.wx.show.wxnews.fragment.JokeFragment;
import com.wx.show.wxnews.fragment.NewsFragment;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Luka on 2016/3/24.
 * E-mail:397308937@qq.com
 */
public class HomeActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener,NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.vp_content)
    ViewPager mViewPager;
    ActionBar mActionBar;

    private List<String> mTitleList = new ArrayList();  //标题集合
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    private List<ActionBar.Tab> tablist = new ArrayList<>();

    private ArrayList<News.ResultBean.ListBean> mNewsData;
    private ArrayList<Joke.ResultBean.DataBean> mJokeData;
    private ArrayList<BookCatalog.ResultBean> mBookData;

    private String newsUrl = "http://v.juhe.cn/";
    private String jokeUrl = "http://japi.juhe.cn/";
    private String bookUrl = "http://apis.juhe.cn/";
    private String newsKey = "220c2251d82b641a400a4694d18ee8dd";    //申请的key，过期要更换
    private String jokeKey = "d8c9a7c2ac395bbf6efbc4d5eaf02981";
    private String bookKey = "91b9052ac36278374cfaf1b1fcf05b5a";
    private int mNewsPage = 1;
    private int mJokePage = 1;
    private NewsFragment newsFragment;
    private BookFragment bookFragment;
    private JokeFragment jokeFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        showLoading();
        //侧滑页
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);
    }

    private void initData() {
        mTitleList.add("文章");
        mTitleList.add("图书");
        mTitleList.add("趣图");

        mNewsData = new ArrayList<>();
        mBookData = new ArrayList<>();
        mJokeData = new ArrayList<>();

        newsFragment = new NewsFragment(this);
        bookFragment = new BookFragment(this);
        jokeFragment = new JokeFragment(this);

        mFragmentList.add(newsFragment);
        mFragmentList.add(bookFragment);
        mFragmentList.add(jokeFragment);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList));

        //添加标题
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);
        for (int i = 0; i != mTitleList.size(); i++) {
            tablist.add(mActionBar.newTab().setText(mTitleList.get(i))
                    .setTabListener(mTabListener));
            mActionBar.addTab(tablist.get(i));
        }


        getNewsByRxJava();
        getBookCatalogByRxJava();
        getJokeByRxJava();
    }

    private ActionBar.TabListener mTabListener = new ActionBar.TabListener() {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (tab == tablist.get(0)) {
                mViewPager.setCurrentItem(0);
            } else if (tab == tablist.get(1)) {
                mViewPager.setCurrentItem(1);
            } else if (tab == tablist.get(2)) {
                mViewPager.setCurrentItem(2);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    };

    /**
     * 文章数据
     */
    public void getNewsByRxJava() {
        Observable<News> observable = getUrlService(newsUrl).getNewsData(mNewsPage, 20, newsKey);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<News>() {
                    @Override
                    public void onCompleted() {
                        newsFragment.setData(mNewsData);
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(News news) {
                        if (news.error_code == 0 && news.result != null) {
                            if (mNewsPage == 1) {
                                mNewsData.clear();
                            }
                            mNewsData.addAll(news.result.list);
                        } else {
                            ToastUtil.showToast(HomeActivity.this, news.error_code + ":" + news.reason);
                        }
                    }
                });
    }

    /**
     * 笑话数据
     */
    public void getJokeByRxJava() {
        Observable<Joke> observable = getUrlService(jokeUrl).getJokeData(mJokePage, 5, jokeKey);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Joke>() {
                    @Override
                    public void onCompleted() {
                        jokeFragment.setData(mJokeData);
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Joke joke) {
                        if (joke.error_code == 0 && joke.result != null) {
                            if (mJokePage == 1) {
                                mJokeData.clear();
                            }
                            mJokeData.addAll(joke.result.data);
                        } else {
                            ToastUtil.showToast(HomeActivity.this, joke.error_code + ":" + joke.reason);
                        }
                    }
                });
    }

    /**
     * 笑话数据
     */
    public void getBookCatalogByRxJava() {
        Observable<BookCatalog> observable = getUrlService(bookUrl).getBookCatalogData(bookKey);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookCatalog>() {
                    @Override
                    public void onCompleted() {
                        bookFragment.setData(mBookData);
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BookCatalog bookCatalog) {
                        if (bookCatalog.error_code == 0 && bookCatalog.result != null) {
                            if (mNewsPage == 1) {
                                mBookData.clear();
                            }
                            mBookData.addAll(bookCatalog.result);
                        } else {
                            ToastUtil.showToast(HomeActivity.this, bookCatalog.error_code + ":" + bookCatalog.reason);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        mNewsPage = 1;
        mJokePage = 1;
        getNewsByRxJava();
        getJokeByRxJava();
    }

    @Override
    public void onLoadMore() {
        mNewsPage++;
        mJokePage++;
        getNewsByRxJava();
        getJokeByRxJava();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return true;
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
    }


}
