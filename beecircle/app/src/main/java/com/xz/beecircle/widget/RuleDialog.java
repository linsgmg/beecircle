package com.xz.beecircle.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lin.baselib.utils.PicUtils;
import com.lin.baselib.utils.WebViewInitUtils;
import com.xz.beecircle.R;

/**
 * description:自定义dialog
 */

public class RuleDialog extends Dialog {

    public RuleDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCancelable(false);
    }

    /**
     * 内容文本
     */
//    private WebView webView;
//    private TextView tv_title;
//    private TextView tv_confirm;

    private ImageView ivAd;
    private ImageView ivClose;
    private WebView webView;
    private TextView tv_title;

    /**
     * 内容数据
     */
    private String htmlBody;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rule);
        //按空白处不能取消动画
//        setCanceledOnTouchOutside(true);
        //去除状态栏变黑问题
        Window window = getWindow();
        window.setDimAmount(0.3f);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = QMUIDisplayHelper.getScreenHeight(getContext())- QMUIDisplayHelper.getStatusBarHeight(getContext());

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

    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {

        if (!TextUtils.isEmpty(htmlBody)) {
            WebViewInitUtils.initWebView(webView,htmlBody);
        }

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
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
        ivAd = (ImageView) findViewById(R.id.iv_ad);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        tv_title = (TextView) findViewById(R.id.tv_title);
        webView = (WebView) findViewById(R.id.webView);
    }


    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;

    public RuleDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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

    public RuleDialog setHtmlBody(String value) {
        this.htmlBody = value;
        return this;
    }

    public RuleDialog setTitle(String value) {
        this.title = value;
        return this;
    }

}

