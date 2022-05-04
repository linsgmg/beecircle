package com.xz.beecircle.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.lin.baselib.Base.BaseActivity;
import com.lin.baselib.Base.CheckVersionBean;
import com.lin.baselib.bean.TitleBean;
import com.lin.baselib.listener.ITitleBar;
import com.lin.baselib.listener.ITitleBar2;
import com.lin.baselib.utils.SharedPreferencesUtils;
import com.lin.baselib.utils.TitleBarUtils;
import com.lin.baselib.widgt.ConfirmDialog;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xz.beecircle.BaseBean;
import com.xz.beecircle.CommonContact;
import com.xz.beecircle.Presenter;
import com.xz.beecircle.R;
import com.xz.beecircle.VersionData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lin.baselib.Base.BasePresenter;

public abstract class ModuleActivity<T extends ViewBinding> extends BaseActivity implements CommonContact.HomeView {
    private Presenter.HomePresenter homePresenter = new Presenter.HomePresenter();
    protected T binding;
    protected Boolean isCheckVersion = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (T) inflate.invoke(null, getLayoutInflater());
            setContentView(binding.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        setStatusBarColor(R.color.windowBg);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * 全屏设置
     */
    public void fullScreen() {
        //设置沉浸式栏透明
        QMUIStatusBarHelper.translucent(this);
    }

    /**
     * 界面跳转
     *
     * @param cls
     * @param items
     */
    public void startActivity(Class<?> cls, Object... items) {
        Intent intent = new Intent(this, cls);
        int requestCode = -1;
        for (Object item : items) {
            if (item instanceof Integer) {
                requestCode = (int) item;
            } else if (item instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) item;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        intent.putExtra(key, (String) value);
                    } else if (value instanceof Integer) {
                        intent.putExtra(key, (Integer) value);
                    } else if (value instanceof Boolean) {
                        intent.putExtra(key, (Boolean) value);
                    } else if (value instanceof Parcelable) {
                        intent.putExtra(key, (Parcelable) value);
                    }
                }
            }
        }
        if (requestCode == -1) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 初始化 presenters
     *
     * @param presenters
     */
    protected void initPresenter(List<BasePresenter> presenters) {
        presenters.forEach((item) -> {
            item.attachView(this);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList list = new ArrayList();
        list.add(homePresenter);
        initPresenter(list);
        //TODO 版本更新暂时关闭
        if (isCheckVersion) {
            homePresenter.getVersion();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetMessage(CheckVersionBean message) {
        ArrayList list = new ArrayList();
        list.add(homePresenter);
        initPresenter(list);
        //TODO 版本更新暂时关闭
        homePresenter.getVersion();
    }

    /**
     * 添加没有返回的标题
     *
     * @param iTitleBar
     */
    public void addTitleBar(ITitleBar iTitleBar) {
        if (iTitleBar == null)
            return;
//        titleBarResId = iTitleBar.getViewResId();
        View toolbarContainer = TitleBarUtils.init(this, R.layout.layout_title);
        iTitleBar.onBindTitleBar(toolbarContainer);
    }

    /**
     * 添加没有返回的标题
     *
     * @param iTitleBar
     */
    public void addTitleBar2(ITitleBar2 iTitleBar) {
        if (iTitleBar == null)
            return;
//        titleBarResId = iTitleBar.getViewResId();
        View toolbarContainer = TitleBarUtils.init(this, R.layout.layout_title);
        TextView tvTitle = (TextView) toolbarContainer.findViewById(R.id.tv_title);
        ImageView iv_title_left = (ImageView) toolbarContainer.findViewById(R.id.iv_title_left);
        TextView tv_title_right = (TextView) toolbarContainer.findViewById(R.id.tv_title_right);
        ImageView iv_title_right = (ImageView) toolbarContainer.findViewById(R.id.iv_title_right);
        RelativeLayout rl_title = (RelativeLayout) toolbarContainer.findViewById(R.id.ll_title);
        TitleBean titleBean = new TitleBean(tvTitle, iv_title_left, tv_title_right,
                iv_title_right, rl_title);
        iTitleBar.onBindTitleBar(titleBean);
    }

//    @Override
//    public abstract void setContent(@Nullable Bundle savedInstanceState);

    @Override
    public abstract void initView();

    @Override
    public abstract void initDate();

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
//        ExitDialog.showLogoutDialog(this,"123");

    }

    private ConfirmDialog confirmDialog;

    @Override
    public void onGetVersionSuccess(BaseBean<VersionData> response) {
        if (!response.getData().getVersion().equalsIgnoreCase(getVersionCode(this))) {
//        if (true) {
            if (confirmDialog != null && confirmDialog.isShowing()) {
                return;
            }
            confirmDialog = new ConfirmDialog(this);
            confirmDialog.setPositiveText("立即更新")
                    .setNegativeText("取消")
                    .setContent(response.getData().getContent())
                    .setIconResource(R.drawable.banbengengxin)
                    .setSubContent(response.getData().getContent())
                    .setOnClickBottomListener(new ConfirmDialog.OnClickBottomListener() {
                        @Override
                        public void onNegativeClick(View view) {
                            finishAll();
//                            SharedPreferencesUtils.saveString(ModuleActivity.this, "token", "");
                            confirmDialog.dismiss();
                        }

                        @Override
                        public void onPositiveClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://"+response.getData().getHttpaddress()));
                            startActivity(intent);
                            confirmDialog.dismiss();
                        }

                        @Override
                        public void onAllClick(View view) {
                        }
                    }).show();
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionCode(Activity activity) {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            String version = info.versionName;
            Log.d("versionCode", "getVersionCode: " + info.versionName);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }


    public int baseCurrent = 1;
    public int baseSize = 20;

    public void initRecyclerView(ModuleAdapter adapter, RecyclerView recyclerView, Boolean isNest) {
        recyclerView.setNestedScrollingEnabled(isNest);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void initRecyclerView2(ModuleAdapter adapter, RecyclerView recyclerView, Boolean isNest, int count) {
        recyclerView.setNestedScrollingEnabled(isNest);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public void addRefreshListener(SmartRefreshLayout refreshLayout) {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                baseCurrent = 1;
                loadDate();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                baseCurrent += 1;
                loadDate();
            }
        });
    }

    public void loadDate() {
    }

    public void dealData(ModuleAdapter adapter, List list, SmartRefreshLayout refreshLayout) {
        if (baseCurrent == 1) {
            if (adapter.getData().size() != 0) {
                adapter.getData().clear();
            }
            if (list == null) {
                list = new ArrayList();
                for (int i = 0; i < 10; i++) {
                    list.add("new Object()");
                }
            }
            adapter.addData(list);
            adapter.notifyDataSetChanged();
        } else {
            if (list != null) {
                if (list.size() == 0) {
                    baseCurrent--;
                }
            }
            if (list == null) {
                list = new ArrayList();
                for (int i = 0; i < 10; i++) {
                    list.add("new Object()");
                }
            }
            adapter.addData(list);
            adapter.notifyItemRangeChanged(adapter.getData().size() - list.size(), list.size());
        }
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }

//        if (list == null) {
//            list = new ArrayList();
//            for (int i = 0; i < 10; i++) {
//                list.add("new Object()");
//            }
//        }
//        adapter.addData(list);
//        adapter.notifyDataSetChanged();
    }

    private String TAG = "MyBaseFragmentActivity";

    /**
     * 沉浸式页面如果有输入框 并且 android:windowSoftInputMode="adjustPan" 会影响这个方法回调，导致activity接受不要结果。
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int indext = 0; indext < fragmentManager.getFragments().size(); indext++) {
            Fragment fragment = fragmentManager.getFragments().get(indext); //找到第一层Fragment
            if (fragment == null)
                Log.w(TAG, "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            else
                handleResult(fragment, requestCode, resultCode, data);
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     *
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        Log.e(TAG, "MyBaseFragmentActivity");
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if (childFragment != null)
            for (Fragment f : childFragment)
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
        if (childFragment == null)
            Log.e(TAG, "MyBaseFragmentActivity1111");
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable @org.jetbrains.annotations.Nullable Bundle options) {
//        if (intent.getComponent().getClassName().equals("11")){
//
//        }
        Log.d("startActivityForResult", "startActivityForResult: "+intent.getComponent().getClassName());

        super.startActivityForResult(intent, requestCode, options);
    }
}
