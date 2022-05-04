package com.xz.beecircle.activity.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBeeOutBinding
import com.xz.beecircle.databinding.ActivityBillDetailBinding

class BillDetailActivity : ModuleActivity<ActivityBillDetailBinding>() {


    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "账单明细"
        }

        binding?.apply {
            llBee.setOnClickListener {
                startActivity(BillDetailListActivity::class.java, hashMapOf("entryType" to "bee"))
            }
            llActivity.setOnClickListener {
                startActivity(BillDetailListActivity::class.java, hashMapOf("entryType" to "activity"))
            }
            llShare.setOnClickListener {
                startActivity(BillDetailListActivity::class.java, hashMapOf("entryType" to "share"))
            }
            llHonest.setOnClickListener {
                startActivity(BillDetailListActivity::class.java, hashMapOf("entryType" to "honest"))
            }
            llBtf.setOnClickListener {
                startActivity(BillDetailListActivity::class.java, hashMapOf("entryType" to "btf"))
            }
        }

    }

    override fun initDate() {

    }

}