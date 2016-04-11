package com.wx.show.wxnews.adapter;

import android.app.DownloadManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.activity.ViewerActivity;
import com.wx.show.wxnews.entity.Music;
import com.wx.show.wxnews.util.ImageUtil;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Luka on 2016/3/21.
 */
public class HomeMusicAdapter extends RecyclerView.Adapter<HomeMusicAdapter.ViewHolder> {

    private HomeActivity context;
    private List<Music.ShowapiResBodyBean.PagebeanBean.SonglistBean> mList;
    private String tag = "HomeMusicAdapter";
    private MediaPlayer mediaPlayer;

    public HomeMusicAdapter(HomeActivity context, List mList) {
        this.context = context;
        this.mList = mList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
        Log.d(tag, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(tag, "onBindViewHolder");
        holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
        holder.itemView.setOnClickListener(holder);

        holder.tvSong.setText(mList.get(position).songname);
        holder.tvSinger.setText(mList.get(position).singername);
        ImageUtil.showImg(context,mList.get(position).albumpic_small,holder.ivImg);

        holder.ivImg.setOnClickListener(holder);
//        holder.btDownLoad.setOnClickListener(holder);
        holder.llContent.setOnClickListener(holder);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.iv_img)
        ImageView ivImg;
        @Bind(R.id.tv_song)
        TextView tvSong;
        @Bind(R.id.tv_singer)
        TextView tvSinger;
//        @Bind(R.id.bt_download)
//        TextView btDownLoad;
        @Bind(R.id.ll_content)
        LinearLayout llContent;
        @Bind(R.id.progress)
        ContentLoadingProgressBar progressBar;

        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_img:
                    String imgUrl = mList.get(position).albumpic_big;
                    if (!TextUtils.isEmpty(imgUrl)) {
                        Intent intent1 = new Intent(context, ViewerActivity.class);
                        intent1.putExtra("imgUrl", imgUrl);
                        ActivityTransitionLauncher.with(context).from(v).launch(intent1);
                    }
                    break;
                case R.id.ll_content:
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer = MediaPlayer.create(context, Uri.parse(mList.get(position).url));
//              播放歌曲
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            } else {
                                mediaPlayer.start();
                            }
                        }
                    }).start();
                    progressBar.hide();
                    break;
//                case R.id.bt_download:
//                    ToastUtil.showToast(context,"下载:"+mList.get(position).songname);
//                    break;
            }
        }
    }
}
