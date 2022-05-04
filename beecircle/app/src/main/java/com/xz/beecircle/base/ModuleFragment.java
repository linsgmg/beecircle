package com.xz.beecircle.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.lin.baselib.Base.BaseFragment;
import com.lin.baselib.Base.BasePresenter;
import com.lin.baselib.Base.CheckVersionBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ModuleFragment<T extends ViewBinding> extends BaseFragment {

    protected T binding;

    protected boolean isInitData = false;

    public abstract void initView();

    public abstract void initDate();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            binding = (T) inflate.invoke(null, inflater, container, false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        super.onCreateView(inflater, container, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return binding.getRoot();
    }

    /**
     * 无用方法，不加会报错
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetMessage(Object message) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            EventBus.getDefault().postSticky(new CheckVersionBean());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().postSticky(new CheckVersionBean());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initDate();
    }

//    /**
//     * 设置textview内容
//     * @param value
//     * @param view
//     */
//    public void setTextView( TextView view,String value){
//        if (TextUtils.isEmpty(value)) {
//            view.setText("");
//        }else {
//            view.setText(value);
//        }
//    }

    /**
     * 界面跳转
     * @param cls
     * @param items
     */
    public void startActivity(Class<?> cls, Object... items) {
        Intent intent = new Intent(getContext(), cls);
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
                    }else if (value instanceof Integer) {
                        intent.putExtra(key, (Integer) value);
                    }else if (value instanceof Boolean) {
                        intent.putExtra(key, (Boolean) value);
                    }else if (value instanceof Parcelable) {
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
     * @param presenters
     */
    protected void initPresenter(List<BasePresenter> presenters) {
        presenters.forEach((item) -> {
            item.attachView(this);
        });
        isInitData = true;
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
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
    public int baseSize = 10;

    public void initRecyclerView(ModuleAdapter adapter, RecyclerView recyclerView, Boolean isNest) {
        recyclerView.setNestedScrollingEnabled(isNest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        } else {
            if (list != null) {
                if (list.size() == 0) {
                    baseCurrent--;
                }
            }
        }
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
        if (list == null) {
            list = new ArrayList();
            for (int i = 0; i < 10; i++) {
                list.add("new Object()");
            }
        }
        adapter.addData(list);
        adapter.notifyDataSetChanged();
    }
}
