package com.lin.baselib.utils;//package com.example.zhilian.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lin.baselib.Base.BaseApplication;
import com.lin.baselib.bean.UserInfoBean;

public class XZUserManager {

    private static Context mContext = BaseApplication.getContext();
    private static SharedPreferences mSpUserInfo;
    private static SharedPreferences mSpToken;
    private static final String PreferenceUserInfo = "userinfo";
    private static final String PreferenceToken = "token";
    private static UserInfoBean.DataBean userInfo;

    static {
        mSpUserInfo = mContext.getSharedPreferences(PreferenceUserInfo, Context.MODE_PRIVATE);
        mSpToken = mContext.getSharedPreferences(PreferenceToken, Context.MODE_PRIVATE);
    }


    /**
     * 最初mUserInfo各属性内容为空，
     * 如登录后可更新当前的UserInfo和本地的缓存
     */
    public static void saveUserInfo(UserInfoBean.DataBean userInfo) {
        if (userInfo!=null) {
            PreferenceUtil.updateBean(mSpUserInfo, getUserInfo(), userInfo);
        }

    }

    public static void updateUserInfo(UserInfoBean.DataBean userInfo) {
        if (userInfo!=null) {
            PreferenceUtil.updateBean(mSpUserInfo, getUserInfo(), userInfo);
        }
    }

    /**
     * 如果从本地缓存中获取对象为空则实例化一个空对象
     * 判断是否登录全程通过user_id是不是为0来判断而不是通过
     * mUserInfo是否等于null，防止UserManager.getUserInfo出现空指针
     */
    private static void readUserInfo() {
        userInfo = (UserInfoBean.DataBean) PreferenceUtil.getBeanValue(mSpUserInfo, UserInfoBean.DataBean.class);
        if (userInfo == null) {
            userInfo = new UserInfoBean.DataBean();
        }
    }


    /**
     * 清空缓存时调用
     */
    public static void resetUserInfo() {
        SharedPreferences.Editor editor = mSpUserInfo.edit();
        editor.clear();
        editor.apply();
        userInfo = new UserInfoBean.DataBean();
    }

    /**
     * 判断已经登录
     */
    public static boolean isLogined() {
        return !TextUtils.isEmpty(getUserInfo().getToken());
    }


    /**
     * 获取用户登录信息
     * @return
     */
    public static UserInfoBean.DataBean getUserInfo() {
        if (userInfo == null) {
            readUserInfo();
        }
        return userInfo;
    }
}
