package com.xz.beecircle.activity.mine

import android.view.View
import android.widget.TextView
import com.lin.baselib.utils.SharedPreferencesUtils
import com.xz.beecircle.*
import com.xz.beecircle.adapter.BillDetailAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBillDetailListBinding

class BillDetailListActivity : ModuleActivity<ActivityBillDetailListBinding>(),
    CommonContact.MineView {
    private var entryType = ""
    private var activityType = "0"
    private var billDetailAdapter: BillDetailAdapter? = null
    private val minePresenter = Presenter.MinePresenter()

    override fun initView() {
        entryType = intent.getStringExtra("entryType")!!
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            when (entryType) {
                "bee" -> {
                    title.text = "蜂蜜明细"
                }
                "activity" -> {
                    title.text = "活跃度明细"
                }
                "share" -> {
                    title.text = "分享值明细"
                }
                "honest" -> {
                    title.text = "诚信值明细"
                }
                "btf" -> {
                    title.text = "BTF明细"
                }
            }
        }

        binding?.apply {
            if (entryType == "activity") {
                llActivity.visibility = View.VISIBLE
            }
            tvPersonActivity.isSelected = true
            tvPersonActivity.setOnClickListener {
                tvPersonActivity.isSelected = true
                tvTeamActivity.isSelected = false
                activityType = "0"
                baseCurrent = 1
                billDetailAdapter?.activityType = activityType
                loadDate()
            }
            tvTeamActivity.setOnClickListener {
                tvPersonActivity.isSelected = false
                tvTeamActivity.isSelected = true
                activityType = "1"
                baseCurrent = 1
                billDetailAdapter?.activityType = activityType
                loadDate()
            }
        }
        initRecyclerView()
    }

    override fun initDate() {
        initPresenter(listOf(minePresenter))
        loadDate()
    }

    override fun loadDate() {
        when (entryType) {
            "bee" -> {
                minePresenter.honey(baseCurrent, baseSize)
            }
            "activity" -> {
                if (activityType == "0") {
                    minePresenter.active(baseCurrent, baseSize)
                } else {
                    minePresenter.teamActive()
                }
            }
            "share" -> {
                minePresenter.shared(baseCurrent, baseSize)
            }
            "honest" -> {
                minePresenter.sincerity(baseCurrent, baseSize)
            }
            "btf" -> {
                minePresenter.btf(baseCurrent, baseSize)
            }
        }
    }

    override fun onGetHoneyListSuccess(response: BaseBean<List<HoneyListData>>) {
        binding?.apply {
            refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
            refreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
            dealData(billDetailAdapter, response.data, binding.refreshLayout)
        }
    }

    override fun onGetTeamActiveListSuccess(response: BaseBean<List<HoneyListData>>) {
        dealData(billDetailAdapter, response.data, binding.refreshLayout)
        binding?.apply {
            refreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
            refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        }
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        billDetailAdapter = BillDetailAdapter(entryType,activityType)
        initRecyclerView(billDetailAdapter, binding?.recyclerView, true)
        addRefreshListener(binding?.refreshLayout)
    }

}