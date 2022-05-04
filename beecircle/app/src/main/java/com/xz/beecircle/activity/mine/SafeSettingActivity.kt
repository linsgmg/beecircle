package com.xz.beecircle.activity.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBeeOutBinding
import com.xz.beecircle.databinding.ActivitySafeSettingBinding

class SafeSettingActivity : ModuleActivity<ActivitySafeSettingBinding>() {


    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "安全设置"
        }

        binding?.apply {
            llLoginPwd.setOnClickListener {
                startActivity(SafeSettingDetailActivity::class.java, hashMapOf("entryType" to "login"))
            }
            llTradePwd.setOnClickListener {
                startActivity(SafeSettingDetailActivity::class.java, hashMapOf("entryType" to "trade"))
            }
        }

    }

    override fun initDate() {

    }

}