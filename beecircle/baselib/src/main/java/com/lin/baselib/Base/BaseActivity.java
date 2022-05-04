package com.lin.baselib.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.lin.baselib.R;
import com.lin.baselib.receiver.NetReceivers;
import com.lin.baselib.utils.ToastUtils;
import com.lin.baselib.utils.XZUserManager;
import com.lin.baselib.widgt.TipDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.youth.banner.util.LogUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.security.auth.login.LoginException;

//import butterknife.ButterKnife;
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import cn.ycbjie.ycstatusbarlib.utils.StatusBarUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity
        implements BaseView {

    protected TipDialog loadingDialog;
    private CompositeDisposable mDisposables = new CompositeDisposable();


    /**
     * 记录处于前台的Activity
     */
    private static BaseActivity mForegroundActivity = null;

    private static Stack<Activity> activityStack;

    /**
     * 记录所有活动的Activity
     */
    private static final List<BaseActivity> mActivities = new LinkedList<>();
    private NetReceivers mReceiver;
    private IntentFilter mFilter = new IntentFilter();

    private Handler handler = new Handler(Looper.getMainLooper());
    public boolean isConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContent();
//        ButterKnife.bind(this);

        mActivities.add(this);
        addActivity(this);

        mReceiver = new NetReceivers();

        Log.d("currentActivity", "onCreate: " + this.getLocalClassName());

        initView();
//        initTitle();
        initDate();

    }

    public void back(View view) {
        hintKeyboard();
        finish();
    }

//    public abstract void setContent();

    public abstract void initView();

    public abstract void initDate();

//    public abstract void initTitle();


//    @Subscribe
//    public abstract void setContent(@Nullable Bundle savedInstanceState);

//    /**
//     *     退出确认操作
//     */
//    private long exitTime = 0;
//    @Override
//    public void onBackPressed() {
//
//        if (activityStack.size() == 1){
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                ToastUtils.showShort("再按一次退出程序");
//                exitTime = System.currentTimeMillis();
//                return;
//            } else {
//                super.onBackPressed();
//            }
//        }else {
//            finish();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivities.remove(this);
        if (mDisposables != null) mDisposables.clear();
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor(int id) {
        //设置状态栏背景颜色
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, id));
        //状态栏亮色模式，设置状态栏黑色文字、图标(白底时候才需要设置字体颜色）
        if (id == R.color.white) {
            StatusBarUtils.StatusBarLightMode(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mForegroundActivity = this;
        LogUtils.i("---------当前运行的类：" + getClass().getName());//The Current Runing Class
    }

    @Override
    protected void onPause() {
        super.onPause();
        mForegroundActivity = null;
    }

    /**
     * 关闭所有Activity
     */
    public static void finishAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
    }

    /**
     * 关闭所有Activity，除了参数传递的Activity
     */
    public static void finishAll(BaseActivity except) {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            if (activity != except)
                activity.finish();
        }
    }

    /**
     * 清空页面栈
     */
    public static void clearActivities() {
//        synchronized (activityStack){
//
//        }
        activityStack.clear();
        finishAll();
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        finishAll();

    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void showTip(String msg, View view) {
        final QMUITipDialog tipDialog = new QMUITipDialog.Builder(view.getContext())
                .setTipWord(msg)
                .create();
        tipDialog.show();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 2000);
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
//                loadingDialog = new TipDialog.Builder(this)
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
        loadingDialog = new TipDialog.Builder(this)
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

    @Override
    public void showToast(String message) {
        try {
//            showTip(message,new TextView());
            ToastUtils.showShort(message);
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void tokenInvalid(int type,String message) {
//        EMClient.getInstance().logout(true);
        //TODO  这个后面再调
        if (getComponentName().getClassName().equals("com.xz.beecircle.activity.login.LoginActivity") || message.contains("交易")){
            return;
        }
        XZUserManager.resetUserInfo();
        finishAll();
        Intent intent = new Intent();
        intent.setClassName("com.xz.beecircle","com.xz.beecircle.activity.login.LoginActivity");
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
