package com.xz.beecircle.activity.home

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import cn.ycbjie.ycstatusbarlib.utils.StatusBarUtils
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.xz.beecircle.R
import com.xz.beecircle.adapter.PagerAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBeeCenterBinding
import com.xz.beecircle.fragment.BeeCenterFragment

class BeeCenterActivity : ModuleActivity<ActivityBeeCenterBinding>() {
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun initView() {
        setStatusBarColor(R.color.yellow2)
        StatusBarUtils.StatusBarLightMode(this)

        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "蜜源中心"
            val llTitle = it.findViewById<RelativeLayout>(R.id.ll_title)
            llTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow2))
        }

        addFragment()
        initTabAndPager()
    }

    private fun addFragment() {
        for (i in 0..2) {
            val beeCenterFragment = BeeCenterFragment()
            val bundle = Bundle()
            bundle.putInt("entryType", i)
            beeCenterFragment.arguments = bundle
            fragmentList.add(beeCenterFragment)
        }
    }

    private fun initTabAndPager() {
        val adapter = PagerAdapter(fragmentList, supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        binding.apply {
            viewPager.adapter = adapter
            tabSegment.reset()
            tabSegment.addTab(QMUITabSegment.Tab("蜜源集市"))
            tabSegment.addTab(QMUITabSegment.Tab("我的蜜源"))
            tabSegment.addTab(QMUITabSegment.Tab("过期"))
            tabSegment.setIndicatorWidthAdjustContent(true)
            tabSegment.setDefaultNormalColor(ContextCompat.getColor(this@BeeCenterActivity, R.color.black))
            tabSegment.setDefaultSelectedColor(ContextCompat.getColor(this@BeeCenterActivity, R.color.text))
            tabSegment.setupWithViewPager(viewPager, false)
            tabSegment.mode = QMUITabSegment.MODE_FIXED
        }
    }

    override fun initDate() {

    }

}