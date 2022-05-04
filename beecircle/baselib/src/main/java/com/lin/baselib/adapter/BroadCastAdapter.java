package com.lin.baselib.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义布局，下面是常见的图片样式，更多实现可以看demo，可以自己随意发挥
 */
public class BroadCastAdapter extends BannerAdapter<String, BroadCastAdapter.BannerViewHolder> {
    private Context context;
    private int color;

    public BroadCastAdapter(Context context, List<String> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        this.context = context;
    }

    public BroadCastAdapter(Context context, List<String> mDatas, int color) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        this.context = context;
        this.color = color;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextSize(14);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setSingleLine(true);
        if (color != 0) {
            textView.setTextColor(context.getResources().getColor(color));
        }
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new BannerViewHolder(textView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
        holder.imageView.setText(data);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        TextView imageView;

        public BannerViewHolder(@NonNull TextView view) {
            super(view);
            this.imageView = view;
        }
    }
}
