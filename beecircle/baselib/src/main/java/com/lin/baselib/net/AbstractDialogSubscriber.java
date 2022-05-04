package com.lin.baselib.net;

import android.util.Log;

import com.lin.baselib.Base.BaseView;
import com.lin.baselib.Base.BaseResponse;

import io.reactivex.disposables.Disposable;

public abstract class AbstractDialogSubscriber<T> extends AbstractSubscriber<T> {

    public AbstractDialogSubscriber(BaseView view) {
        super(view);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (view != null) view.dismissLoadingDialog();
    }


    @Override
    public void onComplete() {
        if (view != null) view.dismissLoadingDialog();
        super.onComplete();
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if (view != null) view.showLoadingDialog();
    }

    @Override
    public void onNext(T t) {
//        BaseResponse baseResponse = (BaseResponse) t;
//        if (!baseResponse.isSuccess()){
//            String msg = baseResponse.getMsg();
//            view.showToast(msg);
//        }
    }

}
