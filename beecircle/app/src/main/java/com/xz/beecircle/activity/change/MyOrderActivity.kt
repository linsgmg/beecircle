package com.xz.beecircle.activity.change

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import cn.ycbjie.ycstatusbarlib.utils.StatusBarUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.luck.picture.lib.tools.SPUtils
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.xz.beecircle.R
import com.xz.beecircle.adapter.PagerAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityMyOrderBinding
import com.xz.beecircle.fragment.MyOrderFragment

class MyOrderActivity : ModuleActivity<ActivityMyOrderBinding>() {
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private var selectType = 0

    override fun initView() {
        setStatusBarColor(R.color.yellow2)
        StatusBarUtils.StatusBarLightMode(this)

        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "我的订单"
            val llTitle = it.findViewById<RelativeLayout>(R.id.ll_title)
            llTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow2))
        }

        SharedPreferencesUtils.removeString(this,"myOrderType")

        addFragment(2)
        initTabAndPager("待收款", "申诉中", "已完成")

        binding?.apply {
            llMySell.setOnClickListener {
                line1.visibility = View.VISIBLE
                line2.visibility = View.GONE
                tvMySell.typeface = Typeface.DEFAULT_BOLD
                tvMyPublish.typeface = Typeface.DEFAULT
                selectType = 0
                SharedPreferencesUtils.saveInt(this@MyOrderActivity,"myOrderType",selectType)
                addFragment(2)
                initTabAndPager("待完成", "申诉中", "已完成")
            }
            llMyPublish.setOnClickListener {
                line1.visibility = View.GONE
                line2.visibility = View.VISIBLE
                tvMySell.typeface = Typeface.DEFAULT
                tvMyPublish.typeface = Typeface.DEFAULT_BOLD
                selectType = 1
                SharedPreferencesUtils.saveInt(this@MyOrderActivity,"myOrderType",selectType)
                addFragment(3)
                initTabAndPager("待付款", "待收货", "申诉中", "已完成")
            }
        }
    }

    private fun addFragment(count: Int) {
        if (fragmentList.size != 0) fragmentList.clear()
        for (i in 0..count) {
            val myOrderFragment = MyOrderFragment()
            val bundle = Bundle()
            bundle.putInt("entryType", i)
            myOrderFragment.arguments = bundle
            fragmentList.add(myOrderFragment)
        }
    }

    private fun initTabAndPager(vararg tabs: String) {
        val adapter = PagerAdapter(fragmentList, supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        binding.apply {
            viewPager.adapter = adapter
            tabSegment.reset()
            for (item in tabs) {
                tabSegment.addTab(QMUITabSegment.Tab(item))
            }
            tabSegment.setIndicatorWidthAdjustContent(true)
            tabSegment.setDefaultNormalColor(ContextCompat.getColor(this@MyOrderActivity, R.color.text))
            tabSegment.setDefaultSelectedColor(ContextCompat.getColor(this@MyOrderActivity, R.color.yellow2))
            tabSegment.setupWithViewPager(viewPager, false)
            tabSegment.mode = QMUITabSegment.MODE_FIXED
            tabSegment.notifyDataChanged()
        }
    }

    override fun initDate() {

    }

}