package com.wx.show.wxnews.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Luka on 2016/4/15.
 * 397308937@qq.com
 */
public class Beauty {

    public int showapi_res_code;
    public String showapi_res_error;

    public ShowapiResBodyBean showapi_res_body;

    public static class ShowapiResBodyBean {
        public int ret_code;

        @SerializedName("0")
        public PagebeanBean a0;
        @SerializedName("1")
        public PagebeanBean a1;
        @SerializedName("2")
        public PagebeanBean a2;
        @SerializedName("3")
        public PagebeanBean a3;
        @SerializedName("4")
        public PagebeanBean a4;
        @SerializedName("5")
        public PagebeanBean a5;
        @SerializedName("6")
        public PagebeanBean a6;
        @SerializedName("7")
        public PagebeanBean a7;
        @SerializedName("8")
        public PagebeanBean a8;
        @SerializedName("9")
        public PagebeanBean a9;
        @SerializedName("10")
        public PagebeanBean a10;
        @SerializedName("11")
        public PagebeanBean a11;
        @SerializedName("12")
        public PagebeanBean a12;
        @SerializedName("13")
        public PagebeanBean a13;
        @SerializedName("14")
        public PagebeanBean a14;


        public static class PagebeanBean {
            public String thumb;
            public String title;
            public String url;
        }


    }

    public String getJson(){
        JSONObject object = new JSONObject(true);
//        object.keySet();
        String comment = object.getString("0");
        ShowapiResBodyBean.PagebeanBean bean0 = JSON.parseObject(comment, ShowapiResBodyBean.PagebeanBean.class);

        return bean0.title;
    }
}
