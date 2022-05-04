package com.xz.beecircle.base;

import com.lin.baselib.Base.BasePresenter;
import com.lin.baselib.Base.BaseResponse;
import com.lin.baselib.Base.BaseView;
import com.lin.baselib.net.AbstractDialogSubscriber;
import com.lin.baselib.net.RetrofitManager;
import com.lin.baselib.net.RxHelper;
import com.lin.baselib.utils.AppUtils;
import com.lin.baselib.utils.ToastUtils;
import com.xz.beecircle.BaseBean;
import com.xz.beecircle.BaseListBean;
import com.xz.beecircle.CommonService;

import io.reactivex.Observable;

public abstract class ModulePresenter<T extends BaseView> extends BasePresenter<T> {
    public CommonService request;

    public ModulePresenter() {
        request = RetrofitManager.getInstance().mRetrofit.create(CommonService.class);
    }

    public <B> Observable dealRequest(Observable<BaseResponse<B>> observable, BaseView view, OnDealNext onNext) {
        observable.compose(RxHelper.observableIO2Main(AppUtils.getApp()))
                .safeSubscribe(new AbstractDialogSubscriber<BaseResponse<B>>(view) {
                    @Override
                    public void onNext(BaseResponse<B> t) {
                        super.onNext(t);
//                        if (t.isSuccess()) {
                        onNext.onDealNext(t);
//                        }
                    }
                });

        return observable;
    }

    /**
     * 对结果成功与否做拦截处理
     *
     * @param observable
     * @param view
     * @param onNext
     * @param <B>
     * @return
     */
//    public <B> Observable dealRequest(Observable<BaseBean<B>> observable, BaseView view, OnDealNextK onNext) {
//        observable.compose(RxHelper.observableIO2Main(AppUtils.getApp()))
//                .safeSubscribe(new AbstractDialogSubscriber<BaseBean<B>>(view) {
//                    @Override
//                    public void onNext(BaseBean<B> t) {
//                        if (t.getSuccess()) {
//                            onNext.onDealNext(t);
//                        } else {
//                            if (!t.getSuccess()) {
//                                String msg = t.getMessage();
//                                view.showToast(msg);
//                            }
//                        }
//                    }
//                });
//
//        return observable;
//    }
    public <B> Observable dealRequest(Observable<BaseBean<B>> observable, BaseView view, OnDealNextK onNext) {
        observable.compose(RxHelper.observableIO2Main(AppUtils.getApp()))
                .safeSubscribe(new AbstractDialogSubscriber<BaseBean<B>>(view) {
                    @Override
                    public void onNext(BaseBean<B> t) {
                        if (t.getCode() == 200) {
                            onNext.onDealNext(t);
                        } else {
                            ToastUtils.showShort(t.getMessage());
                        }
                    }
                });

        return observable;
    }

    public <B> Observable dealRequestList(Observable<BaseBean<BaseListBean<B>>> observable, BaseView view, OnDealNextK onNext) {
        observable.compose(RxHelper.observableIO2Main(AppUtils.getApp()))
                .safeSubscribe(new AbstractDialogSubscriber<BaseBean<BaseListBean<B>>>(view) {
                    @Override
                    public void onNext(BaseBean<BaseListBean<B>> t) {
                        onNext.onDealNext(t);
                    }
                });

        return observable;
    }

    public void setItemClick(OnDealNext listener) {
        this.listener = listener;
    }

    private OnDealNext listener;

    public interface OnDealNext {
        void onDealNext(BaseResponse baseResponse);
    }

    public interface OnDealNextK {
        void onDealNext(BaseBean baseResponse);
    }
}
