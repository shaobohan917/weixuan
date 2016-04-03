package com.wx.show.wxnews.entity;

import java.util.List;

/**
 * Created by SNOVA on 2016/4/3.
 */
public class Event {

    public int count;
    public int start;
    public int total;

    public List<EventsBean> events;

    public static class EventsBean {
        public String subcategory_name;
        public String image;
        public String adapt_url;
        public String loc_name;

        public OwnerBean owner;
        public String alt;
        public String id;
        public String category;
        public String title;
        public int wisher_count;
        public boolean has_ticket;
        public String content;
        public String can_invite;
        public String album;
        public int participant_count;
        public String tags;
        public String image_hlarge;
        public String begin_time;
        public String price_range;
        public String geo;
        public String image_lmobile;
        public String category_name;
        public String loc_id;
        public String end_time;
        public String address;

        public static class OwnerBean {
            public String name;
            public boolean is_banned;
            public boolean is_suicide;
            public String avatar;
            public String uid;
            public String alt;
            public String type;
            public String id;
            public String large_avatar;
        }
    }
}
