package com.wx.show.wxnews.entity;

import java.util.List;

/**
 * Created by Luka on 2016/4/8.
 * 397308937@qq.com
 */
public class Music {
    public int showapi_res_code;
    public String showapi_res_error;

    public ShowapiResBodyBean showapi_res_body;

    public static class ShowapiResBodyBean {

        public PagebeanBean pagebean;
        public int ret_code;

        public static class PagebeanBean {
            public int cur_song_num;
            public int currentPage;
            public int ret_code;

            public List<SonglistBean> songlist;

            public static class SonglistBean {
                public int albumid;
                public String albummid;
                public String albumpic_big;
                public String albumpic_small;
                public String downUrl;
                public int seconds;
                public int singerid;
                public String singername;
                public int songid;
                public String songname;
                public String url;
            }
        }
    }
}
