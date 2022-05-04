package com.xz.beecircle.activity.ecosphere

import android.widget.TextView
import com.lin.baselib.utils.SharedPreferencesUtils
import com.xz.beecircle.*
import com.xz.beecircle.adapter.BeeCenterAdapter
import com.xz.beecircle.adapter.ChargeAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.LayoutListBinding

class ChargeRecordActivity : ModuleActivity<LayoutListBinding>(),CommonContact.EcosphereView {
    private var entryType: String? = null
    private var chargeAdapter: ChargeAdapter? = null
    private val ecospherePresenter:Presenter.EcospherePresenter = Presenter.EcospherePresenter()

    override fun initView() {
        entryType = intent.getStringExtra("entryType")
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            when (entryType) {
                "charge" -> {
                    title.text = "话费充值记录"
                }
                "member" -> {
                    title.text = "视频会员充值记录"
                }
            }
        }
        initRecyclerView()
    }

    override fun initDate() {
        initPresenter(listOf(ecospherePresenter))
        loadDate()
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        chargeAdapter = ChargeAdapter(entryType!!)
        initRecyclerView(chargeAdapter, binding.recyclerView, true)
        addRefreshListener(binding.refreshLayout)
    }

    override fun loadDate() {
        when (entryType) {
            "charge" -> {
                ecospherePresenter.getPhoneRechargeList(baseCurrent,baseSize,SharedPreferencesUtils.getString(this, "uid",""))
            }
            "member" -> {
                ecospherePresenter.getVipRechargeList(baseCurrent,baseSize,SharedPreferencesUtils.getString(this, "uid",""))
            }
        }
    }

    override fun onGetChargePhoneListSuccess(response: BaseBean<List<RechargeListData>>) {
        dealData(chargeAdapter, response.data, binding.refreshLayout)
    }

    override fun onGetChargeVideoListSuccess(response: BaseBean<List<RechargeListData>>) {
        dealData(chargeAdapter, response.data, binding.refreshLayout)
    }

}