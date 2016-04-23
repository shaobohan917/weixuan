package com.wx.show.wxnews.entity;

import java.util.List;

/**
 * Created by Luka on 2016/4/17.
 * 397308937@qq.com
 */
public class Eve {
    /**
     * 列表加载事件
     */
    public static class ItemListEvent {
        private List<Item> items;

        public ItemListEvent(List<Item> items) {
            this.items = items;
        }

        public List<Item> getItems() {
            return items;
        }
    }

}

