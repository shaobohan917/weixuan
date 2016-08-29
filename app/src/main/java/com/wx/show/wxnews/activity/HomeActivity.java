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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.Beauty;
import com.wx.show.wxnews.entity.City;
import com.wx.show.wxnews.entity.Event;
import com.wx.show.wxnews.entity.ImageListDomain;
import com.wx.show.wxnews.entity.Location;
import com.wx.show.wxnews.entity.Movie;
import com.wx.show.wxnews.entity.ZhihuDaily;
import com.wx.show.wxnews.fragment.BeautyFragment;
import com.wx.show.wxnews.fragment.EventFragment;
import com.wx.show.wxnews.fragment.MovieFragment;
import com.wx.show.wxnews.fragment.ZhihuDailyFragment;
import com.wx.show.wxnews.util.DateUtil;
import com.wx.show.wxnews.util.LogUtil;
import com.wx.show.wxnews.util.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
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
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialTabListener, ViewPager.OnPageChangeListener, PullLoadMoreRecyclerView.PullLoadMoreListener {
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


    private ArrayList<Fragment> mFragmentList = new ArrayList<>();//页卡视图集合

    private ArrayList<Movie.SubjectsBean> mMvoieInTheaterData;
    private ArrayList<Movie.SubjectsBean> mMvoieCoomingSoonData;
    private ArrayList<Movie.SubjectsBean> mMovieSearchData;
    private ArrayList<Event.EventsBean> mEventData;
    private ArrayList<ZhihuDaily.StoriesBean> mZhihuData;
    private ArrayList<City.LocsBean> mCityData;
    private ArrayList<Beauty.ShowapiResBodyBean.PagebeanBean> mBeautyData;
    private ArrayList<String> mAmberData;

    private int mNewsPage = 1;
    private MovieFragment movieFragment;
    private ZhihuDailyFragment zhihuDailyFragment;
    private EventFragment eventFragment;
    private BeautyFragment beautyFragment;
//    private AmberFragment amberFragment;

    private ArrayList<Drawable> mIconList;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrent;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private long mExitTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();

        initView();
        initTab();
//        initData(loc);
        initListener();
    }

    private void initTab() {
        //添加标题
        mIconList = new ArrayList();
        mIconList.add(getResources().getDrawable(R.mipmap.ic_videocam_black_48dp));
        mIconList.add(getResources().getDrawable(R.mipmap.ic_fiber_new_black_48dp));
        mIconList.add(getResources().getDrawable(R.mipmap.ic_library_music_black_48dp));
        for (int i = 0; i < mIconList.size(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(mIconList.get(i))
                            .setTabListener(this)
            );
        }
    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //封装对象
            Location loc = new Location();
            loc.city = location.getCity();
            loc.radius = location.getRadius();
            loc.latitude = location.getLatitude();
            loc.longtitude = location.getLongitude();

            initData(loc);
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }
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

    private void initData(Location loc) {
        mEventData = new ArrayList<>();
        mZhihuData = new ArrayList<>();
        mMvoieInTheaterData = new ArrayList<>();
        mMvoieCoomingSoonData = new ArrayList<>();
        mMovieSearchData = new ArrayList();
        mCityData = new ArrayList<>();
        mBeautyData = new ArrayList<>();
        mAmberData = new ArrayList<>();

        //添加fragment
        eventFragment = new EventFragment(this, loc);
        movieFragment = new MovieFragment(this);
        zhihuDailyFragment = new ZhihuDailyFragment(this);
//        beautyFragment = new BeautyFragment(this);

        mFragmentList.add(movieFragment);
        mFragmentList.add(eventFragment);
        mFragmentList.add(zhihuDailyFragment);
//        mFragmentList.add(beautyFragment);

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
    public void getMovieInTheater() {
        Observable<Movie> observable = getUrlService(doubanBaseUrl, true).getMovieInTheater();
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
        Observable<Movie> observable = getUrlService(doubanBaseUrl, true).getMovieComingSoon();
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
        Observable<Movie> observable = getUrlService(doubanBaseUrl, true).getMovieSearch(movieSearch);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Movie in) {
                        mMovieSearchData.clear();
                        mMovieSearchData.addAll(in.subjects);
                        movieFragment.setSearchData(mMovieSearchData);
                    }
                });
    }

    public void getZhihuDaily(String date) {
        Observable<ZhihuDaily> observable = getUrlService(zhihuDailyUrl, true).getZhihuDaily(date);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {
                        zhihuDailyFragment.setData(mZhihuData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihu) {
                        mZhihuData.clear();
                        mZhihuData.addAll(zhihu.stories);
                    }
                });
    }

    public void getCityList() {
        Observable<City> observable = getUrlService(doubanBaseUrl, false).getCityList();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<City>() {
            @Override
            public void onCompleted() {
                eventFragment.getCityList(mCityData);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(City city) {
                mCityData.clear();
                mCityData.addAll(city.locs);
            }
        });
    }

    public void getCityEvent(String cityId) {
        Observable<Event> observable = getUrlService(doubanBaseUrl, true).getEvent(cityId, "future", "all");
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Event>() {
                    @Override
                    public void onCompleted() {
                        eventFragment.setEventData(mEventData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Event en) {
                        mEventData.clear();
                        mEventData.addAll(en.events);
                    }
                });
    }

    public void getBeauty(int page, int type) {
        getBeauty(page, type, false);
    }

    public void getBeauty(int page, int type, boolean showLoading) {
        Observable<Beauty> observable = getUrlService(showUrl, showLoading).getBeauty(15, page, music_appid, DateUtil.getCurrentDate("time"), type, music_sign);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Beauty>() {
                    @Override
                    public void onCompleted() {
                        beautyFragment.setData(mBeautyData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(HomeActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Beauty beauty) {
                        if (beautyFragment.isFirst) {
                            mBeautyData.clear();
                        }
                        mBeautyData.add(beauty.showapi_res_body.a0);
                        mBeautyData.add(beauty.showapi_res_body.a1);
                        mBeautyData.add(beauty.showapi_res_body.a2);
                        mBeautyData.add(beauty.showapi_res_body.a3);
                        mBeautyData.add(beauty.showapi_res_body.a4);
                        mBeautyData.add(beauty.showapi_res_body.a5);
                        mBeautyData.add(beauty.showapi_res_body.a6);
                        mBeautyData.add(beauty.showapi_res_body.a7);
                        mBeautyData.add(beauty.showapi_res_body.a8);
                        mBeautyData.add(beauty.showapi_res_body.a9);
                        mBeautyData.add(beauty.showapi_res_body.a10);
                        mBeautyData.add(beauty.showapi_res_body.a11);
                        mBeautyData.add(beauty.showapi_res_body.a12);
                        mBeautyData.add(beauty.showapi_res_body.a13);
                        mBeautyData.add(beauty.showapi_res_body.a14);
                    }

                });
    }


    /**
     * 监听
     */
    @Override
    public void onRefresh() {
        switch (mCurrent) {
            case 0:
                break;
        }
    }

    @Override
    public void onLoadMore() {
        switch (mCurrent) {
            case 0:
                mNewsPage++;
//                getNewsByRxJava();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (movieFragment.searchOpen) {
            //判断搜索的fragment
            //显示viewpager
            movieFragment.mMovieViewPager.setVisibility(View.VISIBLE);
//            movieFragment.searchView.setVisibility(View.GONE);
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


    /**
     * jsoup
     */
    public void getImage() {
        Observable<List<ImageListDomain>> observable = Observable.create(new Observable.OnSubscribe<List<ImageListDomain>>() {
            @Override
            public void call(Subscriber<? super List<ImageListDomain>> subscriber) {
                List<ImageListDomain> imageListDomainList = new ArrayList();
                try {
                    Document document = Jsoup.connect("http://m.xxxiao.com/cat/MRXT").get();
                    Element imageListelement = document.getElementById("blog-grid");
                    Elements imageListElements = imageListelement.getElementsByAttributeValueContaining("class", "col-lg-4 col-md-4 three-columns post-box");

                    for (Element imageListElement : imageListElements) {
                        Element link = imageListelement.select("a[href]").first();
                        Element image = imageListElement.select("img").first();
                        String linkUrl = link.attr("abs:href");
                        String imageUrl = image.attr("abs:src");
                        String imageTitle = image.attr("alt").trim();
                        imageListDomainList.add(new ImageListDomain(linkUrl, imageUrl, imageTitle));

                    }
                    subscriber.onNext(imageListDomainList);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
        Subscriber<List<ImageListDomain>> subscriber = new Subscriber<List<ImageListDomain>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d("haha", e.getMessage());
            }

            @Override
            public void onNext(List<ImageListDomain> imageListDomains) {
                String url = imageListDomains.get(0).getImageUrl();
                LogUtil.d("haha", url);
            }
        };

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 郭采洁
     */
    public void getAmber(final String query, final int page) {
        Observable<List<ImageListDomain>> observable = Observable.create(new Observable.OnSubscribe<List<ImageListDomain>>() {
            @Override
            public void call(Subscriber<? super List<ImageListDomain>> subscriber) {
                List<ImageListDomain> imageListDomainList = new ArrayList();
                try {
                    Document document = Jsoup.connect("http://www.n63.com/photodir/?album=/china/" + query + "&page=" + page + "&").get();
                    Element imageListelement = document.getElementById("container");        //container
                    Elements imageListElements = imageListelement.getElementsByAttributeValueContaining("class", "grid");

                    LogUtil.d("haha", "imageListElements.size():" + imageListElements.size());
                    for (Element imageListElement : imageListElements) {
                        Element image = imageListElement.select("img").first();
                        String imageUrl = image.attr("abs:src");
                        String imageTitle = image.attr("src");
                        imageListDomainList.add(new ImageListDomain(imageUrl, imageTitle));
                    }
                    subscriber.onNext(imageListDomainList);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
        Subscriber<List<ImageListDomain>> subscriber = new Subscriber<List<ImageListDomain>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d("haha", "onError:" + e.getMessage());
                ToastUtil.showToast(HomeActivity.this,e.getMessage());
            }

            @Override
            public void onNext(List<ImageListDomain> imageListDomains) {
                if (page == 1) {
                    mAmberData.clear();
                }
                for (int i = 0; i < imageListDomains.size(); i++) {
                    mAmberData.add(imageListDomains.get(i).getImageUrl());
                    LogUtil.d("haha", imageListDomains.get(i).getImageUrl());
                }
//                amberFragment.setData(mAmberData);
            }
        };

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //两次返回键退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                showToast("再按一次退出");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
