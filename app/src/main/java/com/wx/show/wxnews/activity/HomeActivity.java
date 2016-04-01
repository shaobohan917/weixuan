package com.wx.show.wxnews.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.BookCatalog;
import com.wx.show.wxnews.entity.Movie;
import com.wx.show.wxnews.entity.News;
import com.wx.show.wxnews.entity.ZhihuDaily;
import com.wx.show.wxnews.fragment.BookFragment;
import com.wx.show.wxnews.fragment.MovieFragment;
import com.wx.show.wxnews.fragment.NewsFragment;
import com.wx.show.wxnews.fragment.ZhihuDailyFragment;
import com.wx.show.wxnews.util.DateUtil;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialTabListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.vp_content)
    ViewPager mViewPager;
    @Bind(R.id.materialTabHost)
    MaterialTabHost tabHost;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private List<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    private ArrayList<Movie.SubjectsBean> mMvoieInTheaterData;
    private ArrayList<Movie.SubjectsBean> mMvoieCoomingSoonData;
    private ArrayList<BookCatalog.ResultBean> mBookData;
    private ArrayList<ZhihuDaily.StoriesBean> mZhihuData;
//    private ArrayList<News.ResultBean.ListBean> mNewsData;

    private String newsUrl = "http://v.juhe.cn/";
    private String doubanBaseUrl = "https://api.douban.com/v2/";
    private String bookUrl = "http://apis.juhe.cn/";

    private String zhihuDailyUrl = "http://news.at.zhihu.com/api/4/";

    private String newsKey = "220c2251d82b641a400a4694d18ee8dd";    //申请的key，过期要更换
    private String bookKey = "91b9052ac36278374cfaf1b1fcf05b5a";
    private int mNewsPage = 1;
    private BookFragment bookFragment;
    private MovieFragment movieFragment;
    private ZhihuDailyFragment zhihuDailyFragment;
    private NewsFragment newsFragment;
    private String tag = "HomeActivity";
    private ArrayList<Drawable> mIconList;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        initToggle();
        showLoading();
    }

    /**
     * 初始化按钮
     */
    private void initToggle() {

        toolbar.setTitle("微选");
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.nav_about,
                R.string.nav_recommend) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void initData() {
//        mNewsData = new ArrayList<>();
        mZhihuData = new ArrayList<>();
        mBookData = new ArrayList<>();
        mMvoieInTheaterData = new ArrayList<>();
        mMvoieCoomingSoonData = new ArrayList<>();
        //添加标题
        mIconList = new ArrayList();
        mIconList.add(getResources().getDrawable(R.mipmap.ic_fiber_new_black_48dp));
        mIconList.add(getResources().getDrawable(R.mipmap.ic_movie_creation_black_48dp));
        mIconList.add(getResources().getDrawable(R.mipmap.ic_import_contacts_black_48dp));
//        mIconList.add(getResources().getDrawable(R.mipmap.ic_wb_incandescent_black_48dp));
        for (int i = 0; i < mIconList.size(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(mIconList.get(i))
                            .setTabListener(this)
            );
        }
        //添加fragment
        bookFragment = new BookFragment(this);
        movieFragment = new MovieFragment(this);
//        newsFragment = new NewsFragment(this);
        zhihuDailyFragment = new ZhihuDailyFragment(this);
        mFragmentList.add(zhihuDailyFragment);
        mFragmentList.add(movieFragment);
        mFragmentList.add(bookFragment);
//        mFragmentList.add(newsFragment);

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList));
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

    private void initListener() {
        navigationView.setNavigationItemSelectedListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 获取数据
     */
//    public void getNewsByRxJava() {
//        Observable<News> observable = getUrlService(newsUrl).getNewsData(mNewsPage, 20, newsKey);
//        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<News>() {
//                    @Override
//                    public void onCompleted() {
//                        newsFragment.setData(mNewsData);
//                        disLoading();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(News news) {
//                        if (news.error_code == 0 && news.result != null) {
//                            if (mNewsPage == 1) {
//                                mNewsData.clear();
//                            }
//                            mNewsData.addAll(news.result.list);
//                        } else {
//                            ToastUtil.showToast(HomeActivity.this, news.error_code + ":" + news.reason);
//                        }
//                    }
//                });
//    }

    public void getBookCatalogByRxJava() {
        Observable<BookCatalog> observable = getUrlService(bookUrl).getBookCatalogData(bookKey);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookCatalog>() {
                    @Override
                    public void onCompleted() {
                        bookFragment.setData(mBookData);
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

    public void getMovieInTheater() {
        Observable<Movie> observable = getUrlService(doubanBaseUrl).getMovieInTheater();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {
                        movieFragment.setInThreaterData(mMvoieInTheaterData);
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Movie in) {
                        mMvoieInTheaterData.clear();
                        mMvoieInTheaterData.addAll(in.subjects);
                    }
                });
    }

    public void getMovieComingSoon() {
        Observable<Movie> observable = getUrlService(doubanBaseUrl).getMovieComingSoon();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {
                        movieFragment.setCoomingSoonData(mMvoieCoomingSoonData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Movie in) {
                        mMvoieCoomingSoonData.clear();
                        mMvoieCoomingSoonData.addAll(in.subjects);
                    }
                });
    }

    public void getMovieSearch(String movieSearch) {
        Observable<Movie> observable = getUrlService(doubanBaseUrl).getMovieSearch(movieSearch);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {
                        movieFragment.setCoomingSoonData(mMvoieCoomingSoonData);
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Movie in) {
                        mMvoieCoomingSoonData.clear();
                        mMvoieCoomingSoonData.addAll(in.subjects);
                    }
                });
    }

    public void getZhihuDailyByRxJava() {
        Observable<ZhihuDaily> observable = getUrlService(zhihuDailyUrl).getZhihuDaily(DateUtil.getCurrentDate());
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {
                        zhihuDailyFragment.setData(mZhihuData);
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihu) {
                        mZhihuData.addAll(zhihu.stories);
                    }
                });
    }

    /**
     * 监听
     */
//    @Override
//    public void onRefresh() {
//        switch (mCurrent) {
//            case 0:
//                mNewsPage = 1;
////                getNewsByRxJava();
//                break;
//        }
//    }
//
//    @Override
//    public void onLoadMore() {
//        switch (mCurrent) {
//            case 0:
//                mNewsPage++;
////                getNewsByRxJava();
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //监听tab开始
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
    //监听tab结束

    //监听页面开始
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setSelectedNavigationItem(position);
        //记录当前页面
        mCurrent = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
    //监听页面结束

    //监听侧栏
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            };
            timer.schedule(timerTask, 400);
        } else if (id == R.id.nav_about) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                    startActivity(intent);
                }
            };
            timer.schedule(timerTask, 400);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
