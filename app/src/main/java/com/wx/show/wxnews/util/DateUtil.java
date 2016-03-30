package com.wx.show.wxnews.util;

import android.support.v4.app.Fragment;

/**
 * Created by Luka on 2016/3/29.
 * E-mail:397308937@qq.com
 */
public class DateUtil {
    private DateUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getCurrentDate(Fragment fragment) {
        return fragment.getArguments().getString("date");
    }
}
