package com.lin.baselib.Base;

import io.reactivex.disposables.Disposable;

/**
 * @author lin
 * 创建日期：2019/5/28 17
 * 描述：BaseView
 */

//回调接口的高度抽象
public interface BaseView {
    /**
     * 显示等待进度条弹窗
     */
    void showLoadingDialog();

    /**
     * 销毁等待进度条弹窗
     */
    void dismissLoadingDialog();

    /**
     * 显示上传进度条弹窗
     */
    void showUpLoadDialog();

    /**
     * 销毁上传精度条弹窗
     */
//    void dismissUpLoadDialog();

    /**
     * 显示Toast
     *
     * @param message Toast 消息
     */
    void showToast(String message);

    /**
     * 登录信息失效
     */
    void tokenInvalid(int type,String message);

    /**
     * 添加网络请求
     *
     * @param d onSubscribe(Disposable d)
     */
    void addNetworkRequest(Disposable d);

    /**
     * 界面跳转（实名认证，设置交易密码等）
     */
    void jumpActivity(int type);
}
