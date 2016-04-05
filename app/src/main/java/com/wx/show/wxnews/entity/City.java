package com.wx.show.wxnews.entity;

import java.util.List;

/**
 * Created by Luka on 2016/4/5.
 * 397308937@qq.com
 */
public class City {


    public int count;
    public int start;
    public int total;

    public List<LocsBean> locs;

    public static class LocsBean {
        public String parent;
        public String habitable;
        public String id;
        public String name;
        public String uid;
    }
}
