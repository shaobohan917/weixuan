package com.wx.show.wxnews.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Luka on 2016/3/29.
 * E-mail:397308937@qq.com
 */
public class DateUtil {
    private DateUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }
}
