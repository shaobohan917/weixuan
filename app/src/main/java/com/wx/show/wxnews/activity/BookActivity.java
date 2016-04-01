package com.wx.show.wxnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.adapter.BookAdapter;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.entity.APIService;
import com.wx.show.wxnews.entity.Book;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Luka on 2016/3/23.
 */
public class BookActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private String bookUrl = "http://apis.juhe.cn/";
    private String bookKey = "91b9052ac36278374cfaf1b1fcf05b5a";

    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView mRecyclerView;
    private int catalogId;     //目录编号
    private int pn = 1;            //数据返回起始
    private int rn = 5;            //数据返回条数，最大30
    private List<Book.ResultBean.DataBean> mData;

    private BookAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        getExtraData();
        initView();
        initData();
        initListener();
    }


    public static Intent getExtraDataIntent(Context context, int catalog_id) {
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra("catalog_id", catalog_id);
        return intent;
    }

    private void getExtraData() {
        Intent intent = getIntent();
        catalogId = intent.getIntExtra("catalog_id", 0);
    }

    private void initView() {
        showLoading();
        mRecyclerView.setLinearLayout();
    }

    private void initData() {
        mData = new ArrayList<>();
        getBookByRxJava();
    }

    private void initListener() {
        mRecyclerView.setOnPullLoadMoreListener(this);
    }

    /**
     * 获取数据
     */
    public void getBookByRxJava() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(bookUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        Observable<Book> observable = service.getBookData(catalogId, pn, rn, bookKey);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Book>() {
                    @Override
                    public void onCompleted() {
                        if (mAdapter == null) {
                            mAdapter = new BookAdapter(BookActivity.this, mData);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                        mRecyclerView.setPullLoadMoreCompleted();
                        disLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(BookActivity.this, "Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Book book) {
                        if (book.error_code == 0 && book.result != null) {
                            if (pn == 1) {
                                mData.clear();
                            }
                            mData.addAll(book.result.data);
                        } else {
                            ToastUtil.showToast(BookActivity.this, book.error_code + ":" + book.reason);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        pn = 1;
        getBookByRxJava();
    }

    @Override
    public void onLoadMore() {
        pn = pn + rn;
        getBookByRxJava();
    }
}
