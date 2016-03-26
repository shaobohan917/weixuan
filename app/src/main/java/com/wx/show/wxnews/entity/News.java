package com.wx.show.wxnews.entity;

import com.wx.show.wxnews.base.BaseEntity;

import java.util.List;

/**
 * Created by Luka on 2016/3/23.
 */
public class News extends BaseEntity {

    public ResultBean result;

    public static class ResultBean {
        public int totalPage;
        public int ps;
        public int pno;
        public List<ListBean> list;

        public static class ListBean {
            public String id;
            public String title;
            public String source;
            public String firstImg;
            public String mark;
            public String url;
        }
    }
}
