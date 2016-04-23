package com.wx.show.wxnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.entity.Item;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Luka on 2016/4/17.
 * 397308937@qq.com
 */
public class BeautyItemDetailFragment extends Fragment {

    private TextView tvTitle;

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

    public void onEventMainThread(Item item){
        if(item!=null){
            tvTitle.setText(item.content);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_beauty,container,false);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }
}
