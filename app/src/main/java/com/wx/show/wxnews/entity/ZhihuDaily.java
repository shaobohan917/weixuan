package com.wx.show.wxnews.entity;

import java.util.List;

/**
 * Created by Luka on 2016/3/29.
 * E-mail:397308937@qq.com
 */
public class ZhihuDaily {

    public String date;
    public List<StoriesBean> stories;

    public static class StoriesBean {
        public int type;
        public int id;
        public String ga_prefix;
        public String title;
        public List<String> images;
    }
}
