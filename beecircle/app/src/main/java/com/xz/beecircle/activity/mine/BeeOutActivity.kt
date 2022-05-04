package com.xz.beecircle.activity.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBeeOutBinding

class BeeOutActivity : ModuleActivity<ActivityBeeOutBinding>() {


    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "资产转出"
        }

        binding?.apply {
            llBeeOut.setOnClickListener {
                startActivity(BeeOutDetailActivity::class.java, hashMapOf("entryType" to "bee"))
            }
            llBtfOut.setOnClickListener {
                startActivity(BeeOutDetailActivity::class.java, hashMapOf("entryType" to "btf"))
            }
        }

    }

    override fun initDate() {

    }

}