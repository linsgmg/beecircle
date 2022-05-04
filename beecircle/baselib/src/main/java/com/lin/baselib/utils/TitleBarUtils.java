package com.lin.baselib.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;


/**
 * description: 标题栏配置工具类
 *
 * @author Dankal Android Developer
 * @since 2018/1/30
 */

public class TitleBarUtils {

    public static View init(Activity activity, int layoutResId) {
        //获取Activity的ContentView
        FrameLayout mFrameLayout = activity.findViewById(android.R.id.content);

        //获取Activity的Layout的根布局
        View rootView = mFrameLayout.getChildAt(0);

        //获取TitleBar的View实例
        ViewGroup titlebar = (ViewGroup) View.inflate(activity, layoutResId, null);

        //此LinearLayout将替换根布局添加进ContentView中
        LinearLayout wraplayout = new LinearLayout(activity.getApplicationContext());
        wraplayout.setOrientation(LinearLayout.VERTICAL);

        //ContentView移除根布局，再将添加了标题栏和根布局的LinearLayout添加进ContentView
        mFrameLayout.removeView(rootView);

        //依次添加标题栏和根布局
        wraplayout.addView(titlebar);
        wraplayout.addView(rootView);

        mFrameLayout.addView(wraplayout);

        return titlebar;
    }


    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    QMUIStatusBarHelper.getStatusbarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }

    }

}
