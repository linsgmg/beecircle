package com.xz.beecircle.activity.ecosphere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import cn.ycbjie.ycstatusbarlib.utils.StatusBarUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.xz.beecircle.BaseBean
import com.xz.beecircle.CommonContact
import com.xz.beecircle.Presenter
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBeeMine1Binding

class BeeMine1Activity : ModuleActivity<ActivityBeeMine1Binding>(), CommonContact.EcosphereView {
    private var mineState: String? = null
    private var ecospherePresenter: Presenter.EcospherePresenter = Presenter.EcospherePresenter()

    override fun initView() {
        fullScreen()
        StatusBarUtils.StatusBarLightMode(this)

        mineState = intent.getStringExtra("mineState")

        binding?.apply {
            tvConfirm.setOnClickListener {
                ecospherePresenter.mineSignUp(SharedPreferencesUtils.getString(this@BeeMine1Activity, "uid", ""))
            }

            when (mineState) {
                "1" -> {
                    tvTip.text = "蜜蜂矿场，火热报名中... ..."
                    tvConfirm.text = "立即报名"
                }
                "2" -> {
                    tvTip.text = "蜜蜂矿场，火热报名中... ..."
                    tvConfirm.text = "已报名"
                    tvConfirm.isEnabled = false
                }
                "3" -> {
                    tvTip.text = "蜜蜂矿场，火热报名中... ..."
                    tvConfirm.text = "立即报名"
                }
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(ecospherePresenter))
    }

    override fun onMineSignUpSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("报名成功")
        startActivity(BeeMine2Activity::class.java)
        finish()
    }

}