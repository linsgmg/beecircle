package com.xz.beecircle.fragment

import android.util.Log
import android.view.View
import com.lin.baselib.utils.SharedPreferencesUtils
import com.xz.beecircle.BaseBean
import com.xz.beecircle.CommonContact
import com.xz.beecircle.Constant.*
import com.xz.beecircle.MyBuyListData
import com.xz.beecircle.Presenter
import com.xz.beecircle.activity.change.MyOrderDetailActivity
import com.xz.beecircle.activity.login.PortraitActivity
import com.xz.beecircle.adapter.MyOrderAdapter
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.ModuleFragment
import com.xz.beecircle.databinding.LayoutListBinding

class MyOrderFragment : ModuleFragment<LayoutListBinding>() , CommonContact.ChangeView{
    private var entryType: Int? = null
    private var myOrderType: Int? = null
    private var myOrderAdapter: MyOrderAdapter? = null
    private var changerPresenter = Presenter.ChangerPresenter()

    override fun initView() {
        myOrderType = SharedPreferencesUtils.getInt(context,"myOrderType",0)
        entryType = arguments!!.getInt("entryType", -1)
        initRecyclerView()
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        myOrderAdapter = MyOrderAdapter(entryType,myOrderType)
        initRecyclerView(myOrderAdapter, binding.recyclerView, true)
        addRefreshListener(binding.refreshLayout)
        myOrderAdapter!!.setItemClick(object : ModuleAdapter.OnMyItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                var type = MY_ORDER_SELL_DSK
                when (myOrderType) {
                    0 -> {
                        when (entryType) {
                            0 -> {
                                type = if (myOrderAdapter?.data?.get(position)?.state == 0) MY_ORDER_SELL_DSK//我的卖出未完成-待收款
                                else MY_ORDER_SELL_DQR//我的卖出未完成-待确认
                            }
                            1 -> {//我的卖出申诉中
                                type = MY_ORDER_SELL_SSZ
                            }
                            2 -> { //我的卖出已完成
                                type = MY_ORDER_SELL_YWC
                            }
                        }
                    }
                    1 -> {
                        when (entryType) {//我的发布待付款
                            0 -> {
                                type = MY_ORDER_PUBLISH_DFK
                            }
                            1 -> {//我的发布待收货
                                type = MY_ORDER_PUBLISH_DSH
                            }
                            2 -> {//我的发布申诉中
                                type = MY_ORDER_PUBLISH_SSZ
                            }
                            3 -> {//我的发布已完成
                                type = MY_ORDER_PUBLISH_YWC
                            }
                        }
                    }
                }
                startActivity(MyOrderDetailActivity::class.java, hashMapOf("entryType" to type,"orderId" to myOrderAdapter?.getItem(position)?.orderid))
            }
        })
    }

    override fun loadDate() {
        when (myOrderType) {
            0 -> {
                when (entryType) {
                    0 -> {//我的卖出待收款
                       changerPresenter.getMySell(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "1")
                    }
                    1 -> {//我的卖出申诉中
                        changerPresenter.getMySell(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "3")
                    }
                    2 -> { //我的卖出已完成
                        changerPresenter.getMySell(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "2")
                    }
                    3 -> { //我的卖出已取消
                        changerPresenter.getMySell(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "4")
                    }
                }
            }
            1 -> {
                when (entryType) {//我的发布待付款
                    0 -> {
                        changerPresenter.getMyBuy(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "0")
                    }
                    1 -> {//我的发布待收货
                        changerPresenter.getMyBuy(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "1")
                    }
                    2 -> {//我的发布申诉中
                        changerPresenter.getMyBuy(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "3")
                    }
                    3 -> {//我的发布已完成
                        changerPresenter.getMyBuy(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "2")
                    }
                    4 -> {//我的发布已取消
                        changerPresenter.getMyBuy(baseCurrent,baseSize,SharedPreferencesUtils.getString(context,"uid",""), "4")
                    }
                }
            }
        }
    }


    override fun initDate() {
        initPresenter(listOf(changerPresenter))
    }

    override fun onResume() {
        super.onResume()
        loadDate()
    }

    override fun onGetMyBuyListSuccess(response: BaseBean<List<MyBuyListData>>) {
        dealData(myOrderAdapter, response.data, binding.refreshLayout)
    }

    override fun onGetMySellListSuccess(response: BaseBean<List<MyBuyListData>>) {
        dealData(myOrderAdapter, response.data, binding.refreshLayout)
    }
}