package com.lin.baselib.bean;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 通用标题栏view
 */
public class TitleBean {
    private TextView tvTitle;
    private ImageView iv_title_left;
    private TextView tv_title_right;
    private ImageView iv_title_right;
    private RelativeLayout rl_title;

    public TitleBean(TextView tvTitle, ImageView iv_title_left, TextView tv_title_right, ImageView iv_title_right, RelativeLayout rl_title) {
        this.tvTitle = tvTitle;
        this.iv_title_left = iv_title_left;
        this.tv_title_right = tv_title_right;
        this.iv_title_right = iv_title_right;
        this.rl_title = rl_title;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public ImageView getIv_title_left() {
        return iv_title_left;
    }

    public void setIv_title_left(ImageView iv_title_left) {
        this.iv_title_left = iv_title_left;
    }

    public TextView getTv_title_right() {
        return tv_title_right;
    }

    public void setTv_title_right(TextView tv_title_right) {
        this.tv_title_right = tv_title_right;
    }

    public ImageView getIv_title_right() {
        return iv_title_right;
    }

    public void setIv_title_right(ImageView iv_title_right) {
        this.iv_title_right = iv_title_right;
    }

    public RelativeLayout getRl_title() {
        return rl_title;
    }

    public void setRl_title(RelativeLayout rl_title) {
        this.rl_title = rl_title;
    }
}
