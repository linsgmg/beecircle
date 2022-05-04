package com.lin.baselib.net;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.lin.baselib.Base.BaseView;
import com.lin.baselib.exception.NetErrorException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class AbstractSubscriber<T> implements Observer<T> {

    protected BaseView view;

    public AbstractSubscriber(BaseView view) {
        this.view = view;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (view != null) view.addNetworkRequest(d);
    }

    @Override
    public void onError(Throwable e) {
        try {
            Log.d("onError", "onError: " + e.getLocalizedMessage());
            Log.d("onError", "onError: " + e.getMessage());
            if (e == null || view == null) return;
            NetErrorException error = null;
            if (e != null) {
                // 对不是自定义抛出的错误进行解析
                if (!(e instanceof NetErrorException)) {
                    if (e instanceof UnknownHostException) {
                        error = new NetErrorException(e, NetErrorException.NoConnectError);
                    } else if (e instanceof JSONException || e instanceof JsonParseException) {
                        error = new NetErrorException(e, NetErrorException.PARSE_ERROR);
                    } else if (e instanceof SocketTimeoutException) {
                        error = new NetErrorException(e, NetErrorException.SocketTimeoutError);
                    } else if (e instanceof ConnectException) {
                        error = new NetErrorException(e, NetErrorException.ConnectExceptionError);
                    } else if (e instanceof HttpException) {
                        error = new NetErrorException(e, NetErrorException.HttpException);
                    } else {
                        error = new NetErrorException(e, NetErrorException.OTHER);
                    }
                } else {
                    error = new NetErrorException(e.getMessage(), ((NetErrorException) e).getErrorType());
                }
            }
            if (error.getErrorType() == 210 || error.getErrorType() == 214) {//210账号冻结  214token失效
                view.tokenInvalid(error.getErrorType(),error.getMessage());
            }
            if (error.getErrorType() == 212 || error.getErrorType() == 215) {//212未实名认证 215未设置支付密码
                view.jumpActivity(error.getErrorType());
            }
            if (error.getErrorType() != 210) {
                view.showToast(error.getMessage());
            }
        } catch (Exception exception) {
            Log.e("onError", "onError: " + e.getStackTrace());
        }
    }

    @Override
    public void onComplete() {
        this.view = null;
    }
}
