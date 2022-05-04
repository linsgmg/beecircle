package com.xz.beecircle.activity.ecosphere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.lin.baselib.utils.SharedPreferencesUtils
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.xz.beecircle.*
import com.xz.beecircle.adapter.PagerAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityMyMinerBinding
import com.xz.beecircle.fragment.AssetRecordFragment

class MyMinerActivity : ModuleActivity<ActivityMyMinerBinding>() {
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private var wokers:String? = null
    private var exam:String? = null

    override fun initView() {
        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "我的矿工"
        }
        wokers = intent.getStringExtra("wokers")
        exam = intent.getStringExtra("exam")
        binding?.apply {
            tvMyMiner.text = wokers
            tvExam.text = exam
        }
        addFragment()
        initTabAndPager()
    }

    override fun initDate() {

    }

    private fun addFragment() {
        for (i in 0..1) {
            val assetRecordFragment = AssetRecordFragment()
            val bundle = Bundle()
            bundle.putInt("entryType", i+2)
            assetRecordFragment.arguments = bundle
            fragmentList.add(assetRecordFragment)
        }
    }

    private fun initTabAndPager() {
        val adapter = PagerAdapter(fragmentList, supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        binding.apply {
            viewPager.adapter = adapter
            tabSegment.reset()
            tabSegment.addTab(QMUITabSegment.Tab("一级矿工"))
            tabSegment.addTab(QMUITabSegment.Tab("二级矿工"))
            tabSegment.setIndicatorWidthAdjustContent(true)
            tabSegment.setDefaultNormalColor(ContextCompat.getColor(this@MyMinerActivity, R.color.black))
            tabSegment.setDefaultSelectedColor(ContextCompat.getColor(this@MyMinerActivity, R.color.text))
            tabSegment.setupWithViewPager(viewPager, false)
            tabSegment.mode = QMUITabSegment.MODE_FIXED
        }
    }

}