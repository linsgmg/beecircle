package com.lin.baselib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import java.lang.reflect.Field;

import io.reactivex.annotations.NonNull;

//Toast统一管理类

public final class ToastUtils {
    public static void showShort(String s) {
        if (!isShowToast()) {
            Toast.makeText(AppUtils.getApp(), s, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Prevent continuous click, jump two pages
     */
    private static long lastToastTime;
    private final static long TIME = 1500;

    public static boolean isShowToast() {
        long time = System.currentTimeMillis();
        if (time - lastToastTime < TIME) {
            return true;
        }
        lastToastTime = time;
        return false;
    }
}