package com.wx.show.wxnews.entity;

import com.wx.show.wxnews.base.BaseEntity;

import java.util.List;

/**
 * Created by Luka on 2016/3/29.
 * E-mail:397308937@qq.com
 */
public class Wooyun extends BaseEntity{

    public List<ResultBean> result;

    public static class ResultBean {
        public String title;
        public String status;
        public String user_harmlevel;
        public String corp_harmlevel;
        public String corp_rank;
        public String comment;
        public String date;
        public String timestamp;
        public String author;
        public String link;
        public String id;
    }
}
