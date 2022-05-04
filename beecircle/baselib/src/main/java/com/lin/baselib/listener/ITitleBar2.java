package com.lin.baselib.listener;

import android.view.View;

import com.lin.baselib.bean.TitleBean;

/**
 * @author Dankal Android Developer
 * @since 2018/7/3
 */

public interface ITitleBar2 {

    /**
     * 设置标题栏的布局id
     *
     * @return 标题栏的布局id
     */
//    int getViewResId();

    /**
     * 设置完成后的TitleBar
     */
    void onBindTitleBar(TitleBean titleView);

}
