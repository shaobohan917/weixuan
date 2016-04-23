package com.wx.show.wxnews.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.entity.Eve;
import com.wx.show.wxnews.entity.Event;
import com.wx.show.wxnews.entity.Item;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Luka on 2016/4/17.
 * 397308937@qq.com
 */
public class BeautyItemListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                    EventBus.getDefault().post(new Eve.ItemListEvent(Item.ITEMS));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void onEventMainThread(Eve.ItemListEvent event) {
        setListAdapter(new ArrayAdapter<Item>(getActivity(), R.layout.simple_list_item, R.id.tv_item, event.getItems()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        EventBus.getDefault().post(getListView().getItemAtPosition(position));
    }
}