package com.xz.beecircle.activity.login

import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xz.beecircle.activity.MainActivity
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivitySplashBinding

class SplashActivity : ModuleActivity<ActivitySplashBinding>() {

    private var handler: Handler? = null
    private val runnable = Runnable {
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        QMUIStatusBarHelper.setStatusBarLightMode(this)
    }


    override fun initView() {
        fullScreen()
        intent = if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token", ""))) {
            Intent(this@SplashActivity, MainActivity::class.java)
        } else {
            Intent(this@SplashActivity, LoginActivity::class.java)
        }
        val time = 3000 //设置等待时间，单位为毫秒

        handler = Handler()
//        当计时结束时，跳转至主界面
        handler!!.postDelayed(runnable, time.toLong())
    }

    override fun initDate() {

    }
}