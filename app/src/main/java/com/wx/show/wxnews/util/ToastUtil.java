package com.wx.show.wxnews.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * Created by Luka on 2016/1/4.
 */
public class ToastUtil {
    private static Toast mToast = null;

    /**
     * 显示提示信息
     *
     * @param context
     * @param text_resouce
     */
    public static void showToast(Context context, int text_resouce) {
        String text = context.getResources().getString(text_resouce);
        showToast(context, text);
    }
    /**
     * 显示提示信息
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void cancel(){
        if (mToast!=null) {
            mToast.cancel();
        }
    }
}
