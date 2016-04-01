package com.wx.show.wxnews.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Luka on 2016/3/30.
 * E-mail:397308937@qq.com
 */
public class ImageUtil {
    private ImageUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showImg(Context context, String uri, ImageView ivImg) {
        if (!SPUtil.isSaveTraffic(context) || NetUtil.isWifi(context)) {
            Glide.with(context).load(uri).into(ivImg);
        }
    }
}
