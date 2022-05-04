package com.lin.baselib.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lin.baselib.Base.BaseApplication;
import com.lin.baselib.R;
import com.lin.baselib.glide.RoundedCornersTransform;
import com.lin.baselib.utils.DensityUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.youth.banner.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义布局，下面是常见的图片样式，更多实现可以看demo，可以自己随意发挥
 */
public class ImageLocalAdapter extends BannerAdapter<String, ImageLocalAdapter.BannerViewHolder> {
    private Context context;
    private boolean isRound = true;

    public ImageLocalAdapter(Context context, ArrayList<String> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        this.context = context;
    }

    public ImageLocalAdapter(Context context, ArrayList<String> mDatas, boolean isRound) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        this.context = context;
        this.isRound = isRound;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
//        //在activity或Fragment或适配器中调用：
//        CornerTransformUtil transformation = new CornerTransformUtil(context, dip2px(context, 5));
//        //只是绘制左上角和右上角圆角
//        transformation.setExceptCorner(false, false, false, false);
        //Glide 加载图片简单用法
//        Glide.with(context)
////                .applyDefaultRequestOptions(BaseApplication.options)
//                .asBitmap()
//                .load(context.getDrawable(data))
////                .skipMemoryCache(true)
//                .thumbnail(0.5f)
////                .transform(transformation)
//                .into(holder.imageView);
        RoundedCornersTransform transform = new RoundedCornersTransform(context, QMUIDisplayHelper.dp2px(context, 5));
        RequestOptions options = new RequestOptions().transform(transform);
        transform.setNeedCorner(true, true, true, true);
        if (isRound) {
            Glide.with(context)
                    .load(data)
                    .apply(options)
                    .into(holder.imageView);
        } else {
            Glide.with(context)
                    .load(data)
                    .into(holder.imageView);
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }
}