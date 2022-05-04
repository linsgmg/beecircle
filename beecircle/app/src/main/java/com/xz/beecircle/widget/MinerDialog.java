package com.xz.beecircle.widget;

import android.app.Dialog;
import android.content.Context;
import android.opengl.ETC1Util;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lin.baselib.adapter.GridImageAdapter;
import com.lin.baselib.utils.Arith;
import com.lin.baselib.utils.MyTextWatcher;
import com.lin.baselib.utils.RegexUtils;
import com.lin.baselib.utils.ToastUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xz.beecircle.R;

/**
 * description:自定义dialog
 */

public class MinerDialog extends Dialog {
    private GridImageAdapter adapter;
    private Context context;

    public MinerDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        setCancelable(false);
    }

    /**
     * 内容文本
     */

    private ImageView ivClose;
    private TextView tv_confirm;
    private TextView tv_tip;
    private TextView tv_need_bee;
    private TextView tv_balance_bee;
    private EditText et_input;

    /**
     * 内容数据
     */
    private String tip;
    private String needBee;
    private String balanceBee;
    private String rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_miner);
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

        et_input.addTextChangedListener(new MyTextWatcher(-1,2){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String value = et_input.getText().toString().trim();
                if (RegexUtils.checkValue(value,RegexUtils.REGEX_FLOUT2)){
                    tv_need_bee.setText(Arith.round(Arith.mul(Arith.mul(value,"1000"),rate),0));
                }else if (TextUtils.isEmpty(value)){
                    tv_need_bee.setText("0");
                }
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(trim)){
                    ToastUtils.showShort("请输入需要兑换算力数量");
                    return;
                }
                v.setTag(trim);
                onClickBottomListener.onPositiveClick(v);
            }
        });

    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
        if (!TextUtils.isEmpty(tip)) {
            tv_tip.setText("提示：可用蜂蜜余额须保留" + tip + "个");
        }
        if (!TextUtils.isEmpty(needBee)) {
            tv_need_bee.setText(needBee);
        }
        if (!TextUtils.isEmpty(balanceBee)) {
            tv_balance_bee.setText(balanceBee);
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
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_need_bee = (TextView) findViewById(R.id.tv_need_bee);
        tv_balance_bee = (TextView) findViewById(R.id.tv_bee_balance);
        et_input = (EditText) findViewById(R.id.et_input);
    }

    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;

    public MinerDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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

    public MinerDialog setNeedBee(String value) {
        this.needBee = value;
        return this;
    }

    public MinerDialog setBalanceBee(String value) {
        this.balanceBee = value;
        return this;
    }

    public MinerDialog setTip(String value) {
        this.tip = value;
        return this;
    }

    public MinerDialog setRate(String value) {
        this.rate = value;
        return this;
    }
}

