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

    /**
     *
     * @param type 返回类型：date,time
     * @return
     */
    public static String getCurrentDate(String type) {
        SimpleDateFormat format;
        if (type.equals("date")) {
            format = new SimpleDateFormat("yyyyMMdd");
        } else if (type.equals("time")) {
            format = new SimpleDateFormat("yyyyMMddHHmmss");
        } else {
            format = new SimpleDateFormat("yyyy");
        }
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }
}
