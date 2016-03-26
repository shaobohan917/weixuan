package com.wx.show.wxnews.entity;

import com.wx.show.wxnews.base.BaseEntity;

import java.util.List;

/**
 * Created by Luka on 2016/3/24.
 * E-mail:397308937@qq.com
 */
//extends BaseEntity
public class Joke extends BaseEntity{
    public ResultBean result;

    public static class ResultBean {

        public List<DataBean> data;

        public static class DataBean {
            public String content;
            public String hashId;
            public int unixtime;
            public String updatetime;
            public String url;
        }
    }
}
