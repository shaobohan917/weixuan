package com.wx.show.wxnews.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.BookCatalog;
import com.wx.show.wxnews.entity.Joke;
import com.wx.show.wxnews.entity.News;
import com.wx.show.wxnews.fragment.BookFragment;
import com.wx.show.wxnews.fragment.JokeFragment;
import com.wx.show.wxnews.fragment.NewsFragment;
import com.wx.show.wxnews.util.LogUtil;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Luka on 2016/3/24.
 * E-mail:397308937@qq.com
 */
public class HomeActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener, NavigationView.OnNavigationItemSelectedListener, MaterialTabListener {
    @Bind(R.id.vp_content)
    ViewPager mViewPager;
    @Bind(R.id.materialTabHost)
    MaterialTabHost tabHost;
    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private List<String> mTitleList = new ArrayList();  //标题集合
    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

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
    private String tag = "HomeActivity";

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
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initData() {
        mTitleList.add(getString(R.string.tab_news));
        mTitleList.add(getString(R.string.tab_joke));
        mTitleList.add(getString(R.string.tab_book));

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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        //添加标题
        for (int i = 0; i < mTitleList.size(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(mTitleList.get(i))
                            .setTabListener(this)
            );
            //获取数据
            getNewsByRxJava();
            getBookCatalogByRxJava();
            getJokeByRxJava();
        }
    }

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
     * 书目数据
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
//            Timer timer = new Timer();
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//
//                    ManageDialog dialog = new ManageDialog();
//                    dialog.show(getFragmentManager(), "ManageDialog");
//
//                }
//            };
//            timer.schedule(timerTask, 400);
            LogUtil.d(tag, "setting");
        } else if (id == R.id.nav_about) {
        } else if (id == R.id.nav_recommend) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //开始tab监听
    @Override
    public void onTabSelected(MaterialTab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }
    //结束

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
