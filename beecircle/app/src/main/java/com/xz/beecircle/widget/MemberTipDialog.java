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

import com.lin.baselib.adapter.GridImageAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xz.beecircle.R;

/**
 * description:自定义dialog
 */

public class MemberTipDialog extends Dialog {
    private GridImageAdapter adapter;
    private Context context;

    public MemberTipDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        setCancelable(false);
    }

    /**
     * 内容文本
     */

    private ImageView ivClose;
    private ImageView iv_star1;
    private ImageView iv_star2;
    private ImageView iv_star3;
    private ImageView iv_star4;
    private TextView tv_tip;
    private TextView tv_tip2;
    private TextView tv_star_key;
    private TextView tv_star_value;
    private TextView tv_person_key;
    private TextView tv_person_value;
    private TextView tv_activity_value;
    private TextView tv_activity2_value;
    private LinearLayout ll_star;
    private LinearLayout ll_person;
    private LinearLayout ll_activity;
    private LinearLayout ll_activity2;

    /**
     * 内容数据
     */
    private String currentStar;
    private String lackStar;
    private String lackStarNum;
    private String person;
    private String personNum;
    private String activity;
    private String activity2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_member_tip);
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

    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {

        if (TextUtils.isEmpty(lackStar)) {
            tv_tip.setVisibility(View.GONE);
            ll_star.setVisibility(View.GONE);
            ll_person.setVisibility(View.GONE);
            ll_activity.setVisibility(View.GONE);
            String value = "";
            if (currentStar == "普通") {
                value = "小蜜蜂";
            }
            if (currentStar == "一星") {
                value = "工蜂";
            }
            if (currentStar == "二星") {
                value = "守卫蜂";
            }
            if (currentStar == "三星") {
                value = "雄蜂";
            }
            if (currentStar == "四星") {
                value = "蜂王";
            }
            tv_tip2.setText("恭喜您成功升级" + value + "！");
            if (currentStar == "普通") {
                iv_star1.setVisibility(View.GONE);
                iv_star2.setVisibility(View.GONE);
                iv_star3.setVisibility(View.GONE);
                iv_star4.setVisibility(View.GONE);
            }
            if (currentStar == "一星") {
                iv_star1.setVisibility(View.VISIBLE);
                iv_star2.setVisibility(View.GONE);
                iv_star3.setVisibility(View.GONE);
                iv_star4.setVisibility(View.GONE);
            }
            if (currentStar == "二星") {
                iv_star1.setVisibility(View.VISIBLE);
                iv_star2.setVisibility(View.VISIBLE);
                iv_star3.setVisibility(View.GONE);
                iv_star4.setVisibility(View.GONE);
            }
            if (currentStar == "三星") {
                iv_star1.setVisibility(View.VISIBLE);
                iv_star2.setVisibility(View.VISIBLE);
                iv_star3.setVisibility(View.VISIBLE);
                iv_star4.setVisibility(View.GONE);
            }
            if (currentStar == "四星") {
                iv_star1.setVisibility(View.VISIBLE);
                iv_star2.setVisibility(View.VISIBLE);
                iv_star3.setVisibility(View.VISIBLE);
                iv_star4.setVisibility(View.VISIBLE);
            }
        } else {
            iv_star1.setVisibility(View.GONE);
            iv_star2.setVisibility(View.GONE);
            iv_star3.setVisibility(View.GONE);
            iv_star4.setVisibility(View.GONE);
            tv_tip2.setVisibility(View.GONE);
            ll_activity2.setVisibility(View.VISIBLE);
            String value = "";
            if (currentStar == "普通") {
                value = "小蜜蜂";
                ll_star.setVisibility(View.GONE);
                ll_person.setVisibility(View.GONE);
                ll_activity.setVisibility(View.GONE);
            }
            if (currentStar == "一星") {
                value = "工蜂";
                ll_star.setVisibility(View.GONE);
                ll_person.setVisibility(View.GONE);
                ll_activity.setVisibility(View.GONE);
            }
            if (currentStar == "二星") {
                value = "守卫蜂";
            }
            if (currentStar == "三星") {
                value = "雄蜂";
            }
            if (currentStar == "四星") {
                value = "蜂王";
            }
            tv_tip.setText("您现在离升级" + value + "团队还差");
            tv_star_key.setText("直推" + lackStar + "星：");
            tv_star_value.setText(lackStarNum + "个");
            tv_person_key.setText(person + "代内人数：");
            tv_person_value.setText(personNum + "人");
            tv_activity_value.setText(activity);
            tv_activity2_value.setText(activity2+"人");
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
        iv_star1 = (ImageView) findViewById(R.id.iv_star1);
        iv_star2 = (ImageView) findViewById(R.id.iv_star2);
        iv_star3 = (ImageView) findViewById(R.id.iv_star3);
        iv_star4 = (ImageView) findViewById(R.id.iv_star4);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_tip2 = (TextView) findViewById(R.id.tv_tip2);
        tv_star_key = (TextView) findViewById(R.id.tv_star_key);
        tv_star_value = (TextView) findViewById(R.id.tv_star_value);
        tv_person_key = (TextView) findViewById(R.id.tv_person_key);
        tv_person_value = (TextView) findViewById(R.id.tv_person_value);
        tv_activity_value = (TextView) findViewById(R.id.tv_activity_value);
        tv_activity2_value = (TextView) findViewById(R.id.tv_activity2_value);
        ll_star = (LinearLayout) findViewById(R.id.ll_star);
        ll_person = (LinearLayout) findViewById(R.id.ll_person);
        ll_activity = (LinearLayout) findViewById(R.id.ll_activity);
        ll_activity2 = (LinearLayout) findViewById(R.id.ll_activity2);
    }

    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;

    public MemberTipDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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

    public MemberTipDialog setCurrentStar(String value) {
        this.currentStar = value;
        return this;
    }

    public MemberTipDialog setLack(String value, String value2) {
        this.lackStar = value;
        this.lackStarNum = value2;
        return this;
    }

    public MemberTipDialog setPerson(String value, String value2) {
        this.person = value;
        this.personNum = value2;
        return this;
    }

    public MemberTipDialog setActivity(String value) {
        this.activity = value;
        return this;
    }

    public MemberTipDialog setActivity2(String value) {
        this.activity2 = value;
        return this;
    }
}

