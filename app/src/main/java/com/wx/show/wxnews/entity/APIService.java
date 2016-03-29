package com.wx.show.wxnews.entity;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Luka on 2016/3/22.
 */
public interface APIService {
//    @GET("")
//    Call<Movie> loadMovie(@Query("pno") int pno,@Query("ps") int ps,@Query("dtype") String dtype, @Query("key") String key);

    /**
     * 获取文章
     *
     * @param pno 当前页数，默认1
     * @param ps  每页返回条数，最大100，默认20
     * @param key
     * @return
     */
    @GET("weixin/query")
    Observable<News> getNewsData(@Query("pno") int pno, @Query("ps") int ps, @Query("key") String key);

    /**
     * 获取笑话
     *
     * @param page     当前页数，默认1
     * @param pagesize 每页返回条数，最大20，默认1
     * @param key
     * @return
     */
    @GET("joke/img/text.from")
    Observable<Joke> getJokeData(@Query("page") int page, @Query("pagesize") int pagesize, @Query("key") String key);

    /**
     *
     * @param key
     * @return
     */
    @GET("goodbook/catalog")
    Observable<BookCatalog> getBookCatalogData(@Query("key") String key);

    /**
     *
     * @param catalog_id    目录编号
     * @param pn            数据返回起始
     * @param rn            数据返回条数，最大30
     * @return
     */
    @GET("goodbook/query")
    Observable<Book> getBookData(@Query("catalog_id") int catalog_id, @Query("pn") int pn, @Query("rn") int rn,@Query("key") String key);

    @GET("wooyun/index")
    Observable<Wooyun> getWooyunData(@Query("key") String key);

}
