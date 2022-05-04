package com.xz.beecircle.activity.ecosphere

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.xz.beecircle.*
import com.xz.beecircle.adapter.PagerAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityMyAssetBinding
import com.xz.beecircle.fragment.AssetRecordFragment
import com.xz.beecircle.widget.SimpleTipDialog

class MyAssetActivity : ModuleActivity<ActivityMyAssetBinding>(),
    CommonContact.MineView {
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private var myAsset: String? = null
    private val minePresenter = Presenter.MinePresenter()

    override fun initView() {
        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "我的资产"
        }
        myAsset = intent.getStringExtra("myAsset")
        binding?.tvMyAsset?.text = myAsset
        addFragment()
        initTabAndPager()
        binding?.apply {
            tvCharge.setOnClickListener {
                setTipDialog("", "暂未开放")
            }

            tvDrawback.setOnClickListener {
                setTipDialog("", "暂未开放")
            }

            tvTransfer.setOnClickListener {
                setTipDialog("", "暂未开放")
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(minePresenter))
    }

    private var simpleTipDialog: SimpleTipDialog? = null

    private fun setTipDialog(vararg value: String) {
        if (simpleTipDialog != null && simpleTipDialog!!.isShowing) {
            return
        }
        simpleTipDialog = SimpleTipDialog(this)
        simpleTipDialog?.setTitle(value[0])
            ?.setContent(value[1])

        simpleTipDialog?.setOnClickBottomListener(object : SimpleTipDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                simpleTipDialog!!.dismiss()
            }

            override fun onPositiveClick(view: View?) {
                simpleTipDialog!!.dismiss()

            }
        })?.show()
    }

    private fun addFragment() {
        for (i in 0..1) {
            val assetRecordFragment = AssetRecordFragment()
            val bundle = Bundle()
            bundle.putInt("entryType", i)
            assetRecordFragment.arguments = bundle
            fragmentList.add(assetRecordFragment)
        }
    }

    private fun initTabAndPager() {
        val adapter = PagerAdapter(
            fragmentList,
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        binding.apply {
            viewPager.adapter = adapter
            tabSegment.reset()
            tabSegment.addTab(QMUITabSegment.Tab("资产记录"))
            tabSegment.addTab(QMUITabSegment.Tab("挖矿记录"))
            tabSegment.setIndicatorWidthAdjustContent(true)
            tabSegment.setDefaultNormalColor(
                ContextCompat.getColor(
                    this@MyAssetActivity,
                    R.color.black
                )
            )
            tabSegment.setDefaultSelectedColor(
                ContextCompat.getColor(
                    this@MyAssetActivity,
                    R.color.text
                )
            )
            tabSegment.setupWithViewPager(viewPager, false)
            tabSegment.mode = QMUITabSegment.MODE_FIXED
        }
    }

}