package com.xz.beecircle.activity.home

import android.view.View
import androidx.core.content.ContextCompat
import cn.ycbjie.ycstatusbarlib.utils.StatusBarUtils
import com.xz.beecircle.*
import com.xz.beecircle.Constant.WEB_TYPE_NOTICE
import com.xz.beecircle.activity.login.PortraitActivity
import com.xz.beecircle.adapter.NoticeAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.databinding.ActivityNoticeListBinding

class NoticeListActivity : ModuleActivity<ActivityNoticeListBinding>(), CommonContact.HomeView {
    private val noticeAdapter: NoticeAdapter = NoticeAdapter()
    private var homePresenter: Presenter.HomePresenter = Presenter.HomePresenter()

    override fun initView() {
        setStatusBarColor(R.color.yellow2)
        StatusBarUtils.StatusBarLightMode(this)

        addTitleBar2 {
            it?.apply {
                tvTitle.text = "平台公告"
                rl_title.setBackgroundColor(ContextCompat.getColor(this@NoticeListActivity, R.color.yellow2))
            }
        }
        initRecyclerView()
    }

    override fun initDate() {
        initPresenter(listOf(homePresenter))
        loadDate()
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        initRecyclerView(noticeAdapter, binding?.recyclerView, true)
        noticeAdapter.setItemClick(object : ModuleAdapter.OnMyItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                startActivity(PortraitActivity::class.java,
                        hashMapOf("entryType" to WEB_TYPE_NOTICE,
                                "bean" to noticeAdapter.getItem(position)))
            }
        })
        addRefreshListener(binding.refreshLayout)
    }

    override fun loadDate() {
        homePresenter.getNoticeList(baseCurrent, baseSize, 0, 0)
    }

    override fun onGetNoticeListSuccess(response: BaseBean<List<NoticeListData>>) {
        dealData(noticeAdapter, response.data, binding?.refreshLayout)
        binding?.apply {
            if (noticeAdapter.data.size == 0) tvEmpty.visibility = View.VISIBLE else tvEmpty.visibility = View.GONE
        }
    }
}