package com.lin.baselib.Base;

import android.content.Context;

/**
 * @author lin
 * 创建日期：2019/5/28 16
 * 描述：BasePresenter03
 */
public abstract class BasePresenter<T extends BaseView> {

    private T view;

    public T getView() {
        return view;
    }

    public void attachView(T view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
        //终止请求
    }
}
