package com.xz.beecircle.activity.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lin.baselib.utils.SaveQRCodeUtils
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityVerifyCodeBuyBinding

class VerifyCodeBuyActivity : ModuleActivity<ActivityVerifyCodeBuyBinding>() ,
    CommonContact.MineView{
    private val minePresenter = Presenter.MinePresenter()

    override fun initView() {
        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "认证码购买"
        }

        binding?.apply {
            tvWx.setOnClickListener {
                SaveQRCodeUtils.copy(this@VerifyCodeBuyActivity, tvWx.text.toString().trim().split("：")[1])
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(minePresenter))
        minePresenter.getAttBuy()
    }

    override fun onGetAttBuySuccess(response: BaseBean<AttBuyData>) {
        binding?.apply {
            response.data.apply {
                tvLink.text = "购买链接：$buyUrl"
                tvWx.text = "代购微信号：$buyWx"
            }
        }
    }

}