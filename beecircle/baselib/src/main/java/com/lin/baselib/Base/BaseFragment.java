package com.lin.baselib.Base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lin.baselib.R;
import com.lin.baselib.utils.ToastUtils;
import com.lin.baselib.utils.XZUserManager;
import com.lin.baselib.widgt.TipDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.Subscribe;

//import butterknife.ButterKnife;
//import butterknife.Unbinder;
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.lin.baselib.Base.BaseActivity.finishAll;


public abstract class BaseFragment<V extends BaseView, P extends BasePresenter<V>> extends Fragment implements BaseView {
    protected TipDialog loadingDialog;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    //    private Unbinder unbinder;
    public View rootView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        rootView = setContent();
//        unbinder = ButterKnife.bind(this,rootView);
        return null;
    }

//    @Subscribe
//    public abstract View setContent();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();

    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor(int id) {
        //设置状态栏背景颜色
        StateAppBar.setStatusBarColor(getActivity(),
                ContextCompat.getColor(getActivity(),
                        id));
        //状态栏亮色模式，设置状态栏黑色文字、图标(白底时候才需要设置字体颜色）
        if (id == R.color.white) {
//            StatusBarUtils.StatusBarLightMode(MainActivity.this);//这种会抛异常
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    public void showLoadingDialog() {
        return;
//        if (loadingDialog != null ) {
//            if (loadingDialog.isShowing()){
//                return;
//            }
//            loadingDialog.dismiss();
//            loadingDialog = null;
//            return;
//        }
//        if (loadingDialog == null) {
//            synchronized (this) {
//                loadingDialog = new TipDialog.Builder(getActivity())
//                        .setIconType(TipDialog.Builder.ICON_TYPE_LOADING)
////                        .setTipWord("正在加载")
//                        .create();
//                loadingDialog.show();
//            }
//        }
    }

    @Override
    public void showUpLoadDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        loadingDialog = new TipDialog.Builder(getActivity())
                .setIconType(TipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传图片")
                .create();
        loadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void showTip(String msg, View view) {
        final QMUITipDialog tipDialog = new QMUITipDialog.Builder(getContext())
                .setTipWord(msg)
                .create();
        tipDialog.show();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);
    }

    @Override
    public void showToast(String message) {
        try {
            ToastUtils.showShort(message);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void tokenInvalid(int type,String message) {
//        EMClient.getInstance().logout(true);
        //TODO  这个后面再调
        if (getActivity().getComponentName().getClassName().equals("com.xz.beecircle.activity.login.LoginActivity")|| message.contains("交易")) {
            return;
        }
        XZUserManager.resetUserInfo();
        finishAll();
        Intent intent = new Intent();
        intent.setClassName("com.xz.beecircle", "com.xz.beecircle.activity.login.LoginActivity");
        intent.setAction("cn.xz.beecircle.login.activity.login");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//            startActivity(new Intent(this,
//                    Class.forName(getString(R.string.login_activity_path))));
    }

    @Override
    public void jumpActivity(int type) {
        Intent intent = new Intent();
        if (type == 212) {
            intent.setClassName("com.xz.beecircle", "com.xz.beecircle.activity.mine.RealVerifyActivity");
        }else if (type == 215 ){
            intent.setClassName("com.xz.beecircle", "com.xz.beecircle.activity.mine.SafeSettingDetailActivity");
            intent.putExtra("entryType","trade");
        }
        startActivity(intent);
    }

    @Override
    public void addNetworkRequest(Disposable d) {
        mDisposables.add(d);
    }
}
