package com.wx.show.wxnews.entity;

import com.wx.show.wxnews.base.BaseEntity;

import java.util.List;

/**
 * Created by Luka on 2016/3/25.
 * E-mail:397308937@qq.com
 */
public class BookCatalog extends BaseEntity {

    public String resultcode;
    public List<ResultBean> result;

    public static class ResultBean {
        public String id;
        public String catalog;
    }
}
