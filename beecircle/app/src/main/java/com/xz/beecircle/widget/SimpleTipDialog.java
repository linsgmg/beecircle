package com.xz.beecircle.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lin.baselib.adapter.GridImageAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xz.beecircle.R;

/**
 * description:自定义dialog
 */

public class SimpleTipDialog extends Dialog {
    private GridImageAdapter adapter;
    private Context context;

    public SimpleTipDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        setCancelable(false);
    }

    /**
     * 内容文本
     */

    private ImageView ivClose;
    private ImageView ivIcon;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_content2;

    /**
     * 内容数据
     */
    private String title;
    private String content;
    private String content2;
    private int res = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_simple_tip);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //去除状态栏变黑问题
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = QMUIDisplayHelper.getScreenHeight(getContext()) - QMUIDisplayHelper.getStatusBarHeight(getContext());

        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        if (onClickBottomListener == null) {
            return;
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBottomListener.onNegativeClick(v);
            }
        });
        tv_content2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBottomListener.onPositiveClick(v);
            }
        });

    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
        if (TextUtils.isEmpty(title)){
            tv_title.setVisibility(View.GONE);
        }else {
            tv_title.setText(title);
        }

        if (res!=0){
            ivIcon.setImageDrawable(ContextCompat.getDrawable(context,res));
            ivIcon.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(content)){
            tv_content.setVisibility(View.GONE);
        }else {
            tv_content.setText(content);
        }

        if (!TextUtils.isEmpty(content2)){
            tv_content2.setVisibility(View.VISIBLE);
            tv_content2.setText(content2);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content2 = (TextView) findViewById(R.id.tv_content2);
    }

    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;

    public SimpleTipDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        /**
         * 点击negative按钮事件
         */
        public void onNegativeClick(View view);

        /**
         * 点击positive按钮事件
         */
        public void onPositiveClick(View view);

    }

    public SimpleTipDialog setTitle(String value) {
        this.title = value;
        return this;
    }

    public SimpleTipDialog setContent(String value) {
        this.content = value;
        return this;
    }

    public SimpleTipDialog setContent2(String value) {
        this.content2 = value;
        return this;
    }

    public SimpleTipDialog setIcon(int value) {
        this.res = value;
        return this;
    }
}

