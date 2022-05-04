package com.lin.baselib.Base;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lin.baselib.R;
import com.lin.baselib.net.RetrofitManager;
import com.lin.baselib.utils.AppUtils;
import com.lin.baselib.utils.DensityAdaptationUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.simple.spiderman.SpiderMan;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class BaseApplication extends Application {
    public static RequestOptions options;
    public static RequestOptions goodsptions;
    private static BaseApplication context;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.black);//全局设置主题颜色
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Scale);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //初始化报错信息
        SpiderMan.init(this).setTheme(R.style.SpiderManTheme_Dark);
        // 初始化配置Glide配置
        initOptions();
        //初始化retrofit
        RetrofitManager.init(this);
        //适配方案
        DensityAdaptationUtils.setDensity(this, 375);
        AppUtils.init(this);
        //根据链接生成二维码
        ZXingLibrary.initDisplayOpinion(this);
    }

    public static Context getContext() {
        return context;
    }

    private void initOptions() {

        options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.wd_tx)//图片加载出来前，显示的图片
                .fallback( R.mipmap.wd_tx) //url为空的时候,显示的图片
                .error(R.mipmap.wd_tx)//图片加载失败后，显示的图片
                .override(350, 350);
        goodsptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.image_750_305)
//                .error(R.mipmap.image_750_305)
                .override(750, 305);
    }

}
