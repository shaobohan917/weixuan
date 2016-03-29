package com.wx.show.wxnews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wx.show.wxnews.R;

/**
 * Created by Luka on 2016/3/29.
 * E-mail:397308937@qq.com
 */
public class SPUtil {
    private SPUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断是否开启节省流量
     * @param context
     * @return
     */
    public static boolean isSaveTraffic(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.key_sava_traffic), false);
    }

}
