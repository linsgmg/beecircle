package com.lin.baselib.utils;

import android.content.Context;

public class DensityUtil {
    /**
     *根据手机的分辨率从dp的单位转换成px（像素）
     */
    public static int dip2px(Context context, float dpValue){
        final float scal=context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scal + 0.5f);
    }
    /**
     * 根据手机的分辨率从px（像素）的单位转换成dp
     */
    public static int px2dip(Context context, float pxValue){
        final float scal=context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scal + 0.5f);
    }

    /**
     * 根据手机的分辨率从px（像素）的单位转换成sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从sp（像素）的单位转换成px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
