package com.xz.beecircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xz.beecircle.NoticeDetailData;
import com.xz.beecircle.NoticeListData;
import com.xz.beecircle.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义布局，下面是常见的图片样式，更多实现可以看demo，可以自己随意发挥
 */
public class BroadCastAdapter2 extends BannerAdapter<NoticeListData, BroadCastAdapter2.BannerViewHolder> {
    private Context context;

    public BroadCastAdapter2(Context context, List<NoticeListData> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        this.context = context;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        LinearLayout inflate = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notice, parent, false);
        return new BannerViewHolder(inflate);
    }

    @Override
    public void onBindView(BannerViewHolder holder, NoticeListData data, int position, int size) {
        holder.content.setText(data.getTitle());
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView money;

        public BannerViewHolder(@NonNull LinearLayout view) {
            super(view);
            this.content = view.findViewById(R.id.tv_content);
        }
    }
}
