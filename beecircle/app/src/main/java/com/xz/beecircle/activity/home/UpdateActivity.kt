package com.xz.beecircle.activity.home

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import cn.ycbjie.ycstatusbarlib.utils.StatusBarUtils
import com.lin.baselib.utils.WebViewInitUtils
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityUpdataBinding
import java.lang.StringBuilder

class UpdateActivity : ModuleActivity<ActivityUpdataBinding>(), CommonContact.MineView {
    private lateinit var viewList: MutableList<TextView>
    private lateinit var viewList2: MutableList<View>
    private val minePresenter = Presenter.MinePresenter()


    override fun initView() {
        fullScreen()
        StatusBarUtils.StatusBarLightMode(this)
        binding?.apply {
            viewList = mutableListOf(tvLevel1, tvLevel2, tvLevel3, tvLevel4, tvLevel5)
            viewList2 = mutableListOf(vLevel1, vLevel2, vLevel3, vLevel4, vLevel5)
            llLevel1.setOnClickListener {
                setSelectView(0)
                minePresenter.teamStrategy("0")
            }
            llLevel2.setOnClickListener {
                setSelectView(1)
                minePresenter.teamStrategy("1")
            }
            llLevel3.setOnClickListener {
                setSelectView(2)
                minePresenter.teamStrategy("2")
            }
            llLevel4.setOnClickListener {
                setSelectView(3)
                minePresenter.teamStrategy("3")
            }
            llLevel5.setOnClickListener {
                setSelectView(4)
                minePresenter.teamStrategy("4")
            }
        }
    }

    private fun setSelectView(value: Int) {
        for (index in viewList.indices) {
            if (value == index) {
                viewList[index].setTextColor(
                    ContextCompat.getColor(
                        this@UpdateActivity,
                        R.color.orange
                    )
                )
            } else {
                viewList[index].setTextColor(
                    ContextCompat.getColor(
                        this@UpdateActivity,
                        R.color.text
                    )
                )
            }
        }

        for (index in viewList2.indices) {
            if (value == index) {
                viewList2[index].visibility = View.VISIBLE
            } else {
                viewList2[index].visibility = View.GONE
            }
        }

        binding?.apply {
            when (value) {
                0 -> {
                    ivNormal1.visibility = View.VISIBLE
                    tvRequire.text = "普通要求"
                    ivStar11.visibility = View.GONE
                    ivStar21.visibility = View.GONE
                    ivStar31.visibility = View.GONE
                    ivStar41.visibility = View.GONE
                    ivNormal2.visibility = View.VISIBLE
                    tvRequire.text = "普通奖励"
                    ivStar12.visibility = View.GONE
                    ivStar22.visibility = View.GONE
                    ivStar32.visibility = View.GONE
                    ivStar42.visibility = View.GONE
                }
                1 -> {
                    ivNormal1.visibility = View.GONE
                    tvRequire.text = "一星要求"
                    ivStar11.visibility = View.VISIBLE
                    ivStar21.visibility = View.GONE
                    ivStar31.visibility = View.GONE
                    ivStar41.visibility = View.GONE
                    ivNormal2.visibility = View.GONE
                    tvRequire.text = "一星奖励"
                    ivStar12.visibility = View.VISIBLE
                    ivStar22.visibility = View.GONE
                    ivStar32.visibility = View.GONE
                    ivStar42.visibility = View.GONE
                }
                2 -> {
                    ivNormal1.visibility = View.GONE
                    tvRequire.text = "二星要求"
                    ivStar11.visibility = View.VISIBLE
                    ivStar21.visibility = View.VISIBLE
                    ivStar31.visibility = View.GONE
                    ivStar41.visibility = View.GONE
                    ivNormal2.visibility = View.GONE
                    tvRequire.text = "二星奖励"
                    ivStar12.visibility = View.VISIBLE
                    ivStar22.visibility = View.VISIBLE
                    ivStar32.visibility = View.GONE
                    ivStar42.visibility = View.GONE
                }
                3 -> {
                    ivNormal1.visibility = View.GONE
                    tvRequire.text = "三星要求"
                    ivStar11.visibility = View.VISIBLE
                    ivStar21.visibility = View.VISIBLE
                    ivStar31.visibility = View.VISIBLE
                    ivStar41.visibility = View.GONE
                    ivNormal2.visibility = View.GONE
                    tvRequire.text = "三星奖励"
                    ivStar12.visibility = View.VISIBLE
                    ivStar22.visibility = View.VISIBLE
                    ivStar32.visibility = View.VISIBLE
                    ivStar42.visibility = View.GONE
                }
                4 -> {
                    ivNormal1.visibility = View.GONE
                    tvRequire.text = "四星要求"
                    ivStar11.visibility = View.VISIBLE
                    ivStar21.visibility = View.VISIBLE
                    ivStar31.visibility = View.VISIBLE
                    ivStar41.visibility = View.VISIBLE
                    ivNormal2.visibility = View.GONE
                    tvRequire.text = "四星奖励"
                    ivStar12.visibility = View.VISIBLE
                    ivStar22.visibility = View.VISIBLE
                    ivStar32.visibility = View.VISIBLE
                    ivStar42.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(minePresenter))
        minePresenter.teamStrategy("0")
    }

    override fun onGetTeamStrategySuccess(response: BaseBean<TeamStrategyData>) {
        binding?.apply {
            response.data.apply {
                val pushSB = StringBuilder()
                val teamSB = StringBuilder()
                for (index in pushRule.indices){
                    pushSB.append("${index.plus(1)}.${pushRule[index]}\n")
                }
                for (index in teamRule.indices){
                    teamSB.append("${index.plus(1)}.${teamRule[index]}\n")
                }
                tvActivity.text = pushSB.toString().trim().substring(0,pushSB.length-1)
                tvTeamRequire.text = teamSB.toString().trim().substring(0,teamSB.length-1)
                tvBeeSource.text = reward.honey
                tvTotalBonus.text = reward.global
                tvTeamBonus.text = reward.team.substring(0,reward.team.length-1)
                WebViewInitUtils.initWebView(webView, more.content)
            }
        }
    }

}