package com.wx.show.wxnews.entity;

import com.wx.show.wxnews.base.BaseEntity;

import java.util.List;

/**
 * Created by Luka on 2016/3/25.
 * E-mail:397308937@qq.com
 */
public class Book extends BaseEntity{

    public String resultcode;
    public ResultBean result;

    public static class ResultBean {
        public String totalNum;
        public String pn;
        public String rn;
        public List<DataBean> data;

        public static class DataBean {
            public String title;
            public String catalog;
            public String tags;
            public String sub1;
            public String sub2;
            public String img;
            public String reading;
            public String online;
            public String bytime;
        }
    }
}
