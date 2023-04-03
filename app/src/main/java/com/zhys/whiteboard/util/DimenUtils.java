package com.zhys.whiteboard.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 跟尺寸相关的工具类
 */

public class DimenUtils {


    /**
     * dp转成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转成px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * pt转成sp
     *
     * @param context
     * @param ptValue
     * @return
     */
    public static float pt2sp(Context context, float ptValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return ptValue/fontScale+4;
    }



    /**
     * 获取手机屏幕的宽度
     *
     * @param ctx
     * @return
     */
    public static int getScreenWidth(Context ctx) {
        if (ctx == null) {
            return 0;
        }
        if (ctx instanceof Activity) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay()
                    .getMetrics(dm);
            return dm.widthPixels;
        } else {
            return 0;
        }
    }

    /**
     * 获取手机屏幕的宽度
     *
     * @param ctx
     * @return
     */
    public static int getScreenHeight(Context ctx) {
        if (ctx == null) {
            return 0;
        }
        if (ctx instanceof Activity) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay()
                    .getMetrics(dm);
            return dm.heightPixels;
        } else {
            return 0;
        }
    }

    public static int getMinLength(int... params) {
        int min = params[0];
        for (int para : params) {
            if (para < min) {
                min = para;
            }
        }
        return min;
    }

}
