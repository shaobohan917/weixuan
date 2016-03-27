package com.wx.show.wxnews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wx.show.wxnews.R;
import com.wx.show.wxnews.base.BaseActivity;
import com.wx.show.wxnews.util.LogUtil;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Luka on 2016/3/26.
 * E-mail:397308937@qq.com
 */
public class TestActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> mData = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        for (int i = 100; i < 110; i++) {
            mData.add(i + "");
        }
        recyclerView.setAdapter(new MyAdapter(this, mData));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private Context context;
        private ArrayList mData;

        public MyAdapter(Context context, ArrayList mData) {
            this.context = context;
            this.mData = mData;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_bookcatalog, parent, false);
            LogUtil.d("heh","return");
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ToastUtil.showToast(context,mData.get(position)+"");
                }
            });
            holder.tvCatalog.setText(mData.get(position) + "");
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvCatalog;
            public ViewHolder(View itemView) {
                super(itemView);
                tvCatalog = (TextView) itemView.findViewById(R.id.tv_catalog);
            }
        }


    }
}
