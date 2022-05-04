package com.lin.baselib.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lin.baselib.R;
import com.lin.baselib.listener.OnItemLongClickListener;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.luck.picture.lib.tools.DateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author：luck
 * @date：2016-7-27 23:02
 * @describe：GridImageAdapter
 */
public class GridImageAdapter3 extends
        RecyclerView.Adapter<GridImageAdapter3.ViewHolder> {
    public static final String TAG = "PictureSelector";
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private int selectMax = 9;
    private boolean canDelete = true;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;
    protected OnItemDelClickListener mItemDelClickListener;
    private String text;
    private int showDelete = -1;
    private boolean addEnable = true;
    private int index2 = 0;

    public interface onAddPicClickListener {
        void onAddPicClick(int position);
    }

    /**
     * 删除
     */
    public void delete(int position) {
        try {

            if (position != RecyclerView.NO_POSITION && list.size() > position) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GridImageAdapter3(Context context, onAddPicClickListener mOnAddPicClickListener, String text) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
        this.text = text;
    }

    public GridImageAdapter3(Context context, onAddPicClickListener mOnAddPicClickListener, String text, int showDelete) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
        this.text = text;
        this.showDelete = showDelete;
    }

    public GridImageAdapter3(Context context, onAddPicClickListener mOnAddPicClickListener, String text, boolean addEnable) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
        this.text = text;
        this.addEnable = addEnable;
    }

    public GridImageAdapter3(Context context,  boolean addEnable) {
        this.mInflater = LayoutInflater.from(context);
        this.addEnable = addEnable;
    }

    public GridImageAdapter3(Context context, GridImageAdapter3.onAddPicClickListener mOnAddPicClickListener,
                             GridImageAdapter3.OnItemDelClickListener mItemDelClickListener, String text,
                             boolean addEnable, int index) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
        this.mItemDelClickListener = mItemDelClickListener;
        this.text = text;
        this.addEnable = addEnable;
        this.index2 = index;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public List<LocalMedia> getData() {
        return list == null ? new ArrayList<>() : list;
    }

    public void remove(int position) {
        if (list != null && position < list.size()) {
            list.remove(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        ImageView mIvDel;
        TextView tvDuration;
        TextView tv_remark;

        public ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.fiv);
            mIvDel = view.findViewById(R.id.iv_del);
            tv_remark = view.findViewById(R.id.tv_remark);
            tvDuration = view.findViewById(R.id.tv_duration);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gv_filter_image2,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            if (!addEnable) {
                viewHolder.mImg.setEnabled(false);
            }
            viewHolder.mImg.setImageResource(R.mipmap.xsxq_tjtp);
            viewHolder.mImg.setOnClickListener(v -> mOnAddPicClickListener.onAddPicClick(index2));
            viewHolder.mIvDel.setVisibility(View.INVISIBLE);
            viewHolder.tv_remark.setVisibility(View.GONE);
        } else {
            viewHolder.mIvDel.setVisibility(View.VISIBLE);
            viewHolder.mIvDel.setOnClickListener(view -> {
                int index = viewHolder.getAdapterPosition();
                // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                if (index != RecyclerView.NO_POSITION && list.size() > index) {
                    list.remove(index);
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index, list.size());
                }

                if (null != mItemDelClickListener) {
                    mItemDelClickListener.onItemDelClick(index,index2);
                }
            });
            LocalMedia media = list.get(position);
            if (media == null
                    || TextUtils.isEmpty(media.getPath())) {
                return;
            }
            int chooseModel = media.getChooseModel();
            String path;
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }

            Log.i(TAG, "原图地址::" + media.getPath());

            if (media.isCut()) {
                Log.i(TAG, "裁剪地址::" + media.getCutPath());
            }
            if (media.isCompressed()) {
                Log.i(TAG, "压缩地址::" + media.getCompressPath());
                Log.i(TAG, "压缩后文件大小::" + new File(media.getCompressPath()).length() / 1024 + "k");
            }
            if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
                Log.i(TAG, "Android Q特有地址::" + media.getAndroidQToPath());
            }
            if (media.isOriginal()) {
                Log.i(TAG, "是否开启原图功能::" + true);
                Log.i(TAG, "开启原图功能后地址::" + media.getOriginalPath());
            }

            long duration = media.getDuration();
            viewHolder.tvDuration.setVisibility(PictureMimeType.isHasVideo(media.getMimeType())
                    ? View.VISIBLE : View.GONE);
            if (chooseModel == PictureMimeType.ofAudio()) {
                viewHolder.tvDuration.setVisibility(View.VISIBLE);
                viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
                        (R.drawable.picture_icon_audio, 0, 0, 0);

            } else {
                viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
                        (R.drawable.picture_icon_video, 0, 0, 0);
            }
            viewHolder.tvDuration.setText(DateUtils.formatDurationTime(duration));
            if (chooseModel == PictureMimeType.ofAudio()) {
                viewHolder.mImg.setImageResource(R.drawable.picture_audio_placeholder);
            } else {
                Glide.with(viewHolder.itemView.getContext())
                        .load(PictureMimeType.isContent(path) && !media.isCut() && !media.isCompressed() ? Uri.parse(path)
                                : path)
                        .centerCrop()
                        .placeholder(R.color.app_color_f6)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(viewHolder.mImg);
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(v -> {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    mItemClickListener.onItemClick(v, adapterPosition);
                });
            }

            if (mItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(v -> {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    mItemLongClickListener.onItemLongClick(viewHolder, adapterPosition, v);
                    return true;
                });
            }
            if (canDelete) {
                viewHolder.mIvDel.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mIvDel.setVisibility(View.GONE);
            }

            if (position == 0) {
                viewHolder.tv_remark.setVisibility(View.VISIBLE);
                viewHolder.tv_remark.setText(text);
                viewHolder.mIvDel.setVisibility(View.GONE);
            } else {
                viewHolder.tv_remark.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(text)){
                viewHolder.tv_remark.setVisibility(View.GONE);
            }

            if (showDelete == 1) {
                viewHolder.mIvDel.setVisibility(View.VISIBLE);
            }

            if (!addEnable) {
                viewHolder.mIvDel.setVisibility(View.GONE);
            }
        }
    }

    public void judgeDelete(boolean canDelete) {
        this.canDelete = canDelete;
        notifyDataSetChanged();
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }

    private OnItemLongClickListener mItemLongClickListener;

    public void setItemLongClickListener(OnItemLongClickListener l) {
        this.mItemLongClickListener = l;
    }

    public interface OnItemDelClickListener {
        void onItemDelClick(int position,int i);
    }

    public void setOnItemDelClickListener(OnItemDelClickListener listener) {
        this.mItemDelClickListener = listener;
    }
}
