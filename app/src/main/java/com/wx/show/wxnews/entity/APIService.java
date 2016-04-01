package com.wx.show.wxnews.entity;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
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
     * 获取正在上映的电影
     * @return
     */
    @GET("movie/in_theaters")
    Observable<Movie> getMovieInTheater();
    /**
     * 获取即将上映的电影
     * @return
     */
    @GET("movie/coming_soon")
    Observable<Movie> getMovieComingSoon();
    /**
     * 搜索电影
     * @return
     */
    @GET("movie/search")
    Observable<Movie> getMovieSearch(@Query("q") String movieSearch);

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

    @GET("start-image/1080*1776")
    Observable<SplashImg> getSplashImg();

    @GET("news/before/{date}")
    Observable<ZhihuDaily> getZhihuDaily(@Path("date") String date);

    @GET("news/{id}")
    Observable<ZhihuNews> getZhihuNews(@Path("id") int newsId);

}
