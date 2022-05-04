package com.xz.beecircle.activity.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityReceiveWayBinding
import com.xz.beecircle.databinding.ActivitySafeSettingBinding

class ReceiveWayActivity : ModuleActivity<ActivityReceiveWayBinding>() {


    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "收款方式"
        }

        binding?.apply {
            llZfb.setOnClickListener {
                startActivity(ReceiveInfoDetailActivity::class.java, hashMapOf("entryType" to "zfb"))
            }
            llWx.setOnClickListener {
                startActivity(ReceiveInfoDetailActivity::class.java, hashMapOf("entryType" to "wx"))
            }
            llYhk.setOnClickListener {
                startActivity(ReceiveInfoDetailActivity::class.java, hashMapOf("entryType" to "yhk"))
            }
        }

    }

    override fun initDate() {

    }

}