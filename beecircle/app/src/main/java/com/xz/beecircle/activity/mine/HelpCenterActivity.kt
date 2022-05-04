package com.xz.beecircle.activity.mine

import android.content.Intent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import cn.ycbjie.ycstatusbarlib.utils.StatusBarUtils
import com.xz.beecircle.*
import com.xz.beecircle.Constant.WEB_TYPE_HELP_CENTER
import com.xz.beecircle.Constant.WEB_TYPE_NOTICE
import com.xz.beecircle.activity.login.PortraitActivity
import com.xz.beecircle.adapter.HelpCenterAdapter
import com.xz.beecircle.adapter.NoticeAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.databinding.ActivityNoticeListBinding

class HelpCenterActivity : ModuleActivity<ActivityNoticeListBinding>(), CommonContact.HomeView {
    private var helpCenterAdapter: HelpCenterAdapter? = null
    private val honePresenter = Presenter.HomePresenter()

    override fun initView() {
        setStatusBarColor(R.color.light_red)
        StatusBarUtils.StatusBarLightMode(this)

        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "帮助中心"
            val llTitle = it.findViewById<RelativeLayout>(R.id.ll_title)
            llTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.light_red))
        }
        initRecyclerView()
        binding?.apply {
            llPage.setBackgroundColor(
                ContextCompat.getColor(
                    this@HelpCenterActivity,
                    R.color.light_red
                )
            )
            ivIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    this@HelpCenterActivity,
                    R.drawable.bangzhuzhongxin
                )
            )
        }
    }

    override fun initDate() {
        initPresenter(listOf(honePresenter))
        loadDate()
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        helpCenterAdapter = HelpCenterAdapter()
        initRecyclerView(helpCenterAdapter, binding?.recyclerView, true)
        helpCenterAdapter!!.setItemClick(object : ModuleAdapter.OnMyItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(this@HelpCenterActivity, PortraitActivity::class.java)
                intent.putExtra("entryType", WEB_TYPE_HELP_CENTER)
                intent.putExtra("bean", helpCenterAdapter?.getItem(position))
                startActivity(intent)
            }
        })
        addRefreshListener(binding.refreshLayout)
    }

    override fun loadDate() {
        honePresenter.getNoticeList(baseCurrent, baseSize, 2, 0)
    }

    override fun onGetNoticeListSuccess(response: BaseBean<List<NoticeListData>>) {
        dealData(helpCenterAdapter, response.data, binding?.refreshLayout)
        binding?.apply {
            if (helpCenterAdapter?.data?.size == 0) tvEmpty.visibility =
                View.VISIBLE else tvEmpty.visibility = View.GONE
        }
    }

}