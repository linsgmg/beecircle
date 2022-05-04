package com.lin.baselib.widgt;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.lin.baselib.R;


/**
 * description:自定义dialog
 */

public class ConfirmDialog extends Dialog {

    public ConfirmDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        setCancelable(false);
    }

    /**
     * 内容文本
     */
    private TextView tv_content;

    /**
     * 内容文本
     */
    private TextView tv_subContent;
    private NestedScrollView scrollView;

    /**
     * negative按钮
     */
    private TextView tv_negative;

    /**
     * positive按钮
     */
    private TextView tv_positive;

    /**
     * 输入view
     */
    private EditText et_input;

    /**
     * 支付父view
     */
    private LinearLayout ll_pay;

    /**
     * 支付输入view
     */
    private EditText et_pay_input;

    /**
     * 支付全部view
     */
    private TextView tv_all;

    /**
     * 输入父iew
     */
    private LinearLayout ll_input;

    /**
     * 图标
     */
    private ImageView iv_icon;

    /**
     * 内容数据
     */
    private String content_text;
    private String subContent_text;
    private String negative_text;
    private String positive_text;
    private String input_text;
    private String input_hint_text;
    private String input_text_pay;
    private String input_hint_text_pay;
    private int iconResource = 0;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_version);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //此处设置位置窗体大小
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBottomListener.onAllClick(v);
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {

        if (!TextUtils.isEmpty(content_text)) {
            tv_content.setText(content_text);
        }

        if (iconResource != 0) {
            iv_icon.setImageResource(iconResource);
        }else {
            iv_icon.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(subContent_text)) {
            tv_subContent.setText(subContent_text);
        } else {
            tv_subContent.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(negative_text)) {
            tv_negative.setText(negative_text);
        } else {
            tv_negative.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(positive_text)) {
            tv_positive.setText(positive_text);
        } else {
            tv_positive.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(input_hint_text) || !TextUtils.isEmpty(input_text)) {
            if (!TextUtils.isEmpty(input_hint_text)) {
                et_input.setHint(input_hint_text);
            } else {
                et_input.setText(input_text);
            }
            et_input.setVisibility(View.VISIBLE);
        } else {
            et_input.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(input_hint_text_pay) || !TextUtils.isEmpty(input_text_pay)) {
            if (!TextUtils.isEmpty(input_hint_text_pay)) {
                et_pay_input.setHint(input_hint_text_pay);
            } else {
                et_pay_input.setText(input_text_pay);
            }
            ll_pay.setVisibility(View.VISIBLE);
        } else {
            ll_pay.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(input_hint_text_pay) && TextUtils.isEmpty(input_text) && TextUtils.isEmpty(input_hint_text_pay) && TextUtils.isEmpty(input_text_pay)) {
            ll_input.setVisibility(View.GONE);
        } else {
            ll_input.setVisibility(View.VISIBLE);
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
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_subContent = (TextView)this. findViewById(R.id.tv_subContent);
        scrollView = (NestedScrollView) this.findViewById(R.id.scrollView);
        tv_negative = (TextView) this.findViewById(R.id.tv_negative);
        tv_positive = (TextView) this.findViewById(R.id.tv_positive);
        et_input = (EditText) this.findViewById(R.id.et_input);
        ll_input = (LinearLayout) this.findViewById(R.id.ll_input);
        et_pay_input = (EditText) this.findViewById(R.id.et_pay_input);
        ll_pay = (LinearLayout) this.findViewById(R.id.ll_pay);
        tv_all = (TextView) this.findViewById(R.id.tv_all);
        iv_icon = (ImageView) this.findViewById(R.id.iv_icon);
    }

    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;

    public ConfirmDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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

        /**
         * 点击全部按钮事件
         */
        public void onAllClick(View view);

    }

    public ConfirmDialog setContent(String content) {
        this.content_text = content;
        return this;
    }

    public ConfirmDialog setSubContent(String subContent_text) {
        this.subContent_text = subContent_text;
        return this;
    }

    public ConfirmDialog setNegativeText(String negative_text) {
        this.negative_text = negative_text;
        return this;
    }

    public ConfirmDialog setPositiveText(String positive_text) {
        this.positive_text = positive_text;
        return this;
    }

    public ConfirmDialog setInputText(String input_text) {
        this.input_text = input_text;
        return this;
    }

    public ConfirmDialog setInputHintText(String input_hint_text) {
        this.input_hint_text = input_hint_text;
        return this;
    }

    public ConfirmDialog setInputTextPay(String input_text_pay) {
        this.input_text_pay = input_text_pay;
        return this;
    }

    public ConfirmDialog setInputHintTextPay(String input_hint_text_pay) {
        this.input_hint_text_pay = input_hint_text_pay;
        return this;
    }

    public ConfirmDialog setIconResource(int iconResource) {
        this.iconResource = iconResource;
        return this;
    }

}

