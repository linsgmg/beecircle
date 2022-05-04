package com.xz.beecircle.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lin.baselib.adapter.GridImageAdapter;
import com.lin.baselib.utils.DensityUtil;
import com.lin.baselib.utils.ToastUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xz.beecircle.R;
import com.xz.beecircle.base.ModuleDialog;
import com.xz.beecircle.databinding.DialogExitBinding;

/**
 * description:自定义dialog
 */

public class ExitDialog extends ModuleDialog<DialogExitBinding> {
    private GridImageAdapter adapter;
    private Context context;

    public ExitDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        setCancelable(false);
    }

    /**
     * 内容文本
     */

    private ImageView ivClose;
    private TextView tv_positive;
    private TextView tv_negative;
    private TextView tv_title;

    /**
     * 内容数据
     */
    private String title;
    private String negative;
    private String positive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getBinding().getRoot());
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //去除状态栏变黑问题
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = QMUIDisplayHelper.getScreenWidth(context)- QMUIDisplayHelper.dp2px(context,40);
//        lp.height = QMUIDisplayHelper.getScreenHeight(getContext()) - QMUIDisplayHelper.getStatusBarHeight(getContext());

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
        tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBottomListener.onNegativeClick(v);
            }
        });
        tv_positive.setOnClickListener(new View.OnClickListener() {
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
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(negative)) {
            tv_negative.setText(negative);
        }
        if (!TextUtils.isEmpty(positive)) {
            tv_positive.setText(positive);
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
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_positive = (TextView) findViewById(R.id.tv_positive);
        tv_negative = (TextView) findViewById(R.id.tv_negative);
    }

    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;

    public ExitDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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

    public ExitDialog setPositive(String value) {
        this.positive = value;
        return this;
    }

    public ExitDialog setNegative(String value) {
        this.negative = value;
        return this;
    }

    public ExitDialog setTitle(String value) {
        this.title = value;
        return this;
    }
}

