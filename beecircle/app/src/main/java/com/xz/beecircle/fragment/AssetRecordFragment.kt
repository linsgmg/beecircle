package com.xz.beecircle.fragment

import android.view.View
import com.lin.baselib.utils.SharedPreferencesUtils
import com.xz.beecircle.*
import com.xz.beecircle.adapter.AssetRecordAdapter
import com.xz.beecircle.adapter.DigRecordAdapter
import com.xz.beecircle.adapter.MyMinerAdapter
import com.xz.beecircle.base.ModuleFragment
import com.xz.beecircle.databinding.FragmentAssetRecordBinding

class AssetRecordFragment : ModuleFragment<FragmentAssetRecordBinding>(),
    CommonContact.EcosphereView, CommonContact.MineView {
    private var entryType: Int? = null
    private var assetRecordAdapter: AssetRecordAdapter? = null
    private var digRecordAdapter: DigRecordAdapter? = null
    private var myMinerAdapter: MyMinerAdapter? = null
    private var ecospherePresenter = Presenter.EcospherePresenter()
    private var minePresenter = Presenter.MinePresenter()

    override fun initView() {
        entryType = arguments!!.getInt("entryType", -1)
        when (entryType) {
            0 -> initRecyclerView()
            1 -> initRecyclerView2()
            2, 3 -> {
                binding?.apply {
                    tv1.text = "账号"
                    tv2.text = "矿工算力"
                    tv3.text = "贡献算力"
                    tv4.text = "挖矿状态"
                    tv5.text = "注册时间"
                }
                initRecyclerView3()
            }
        }
        binding?.refreshLayout?.setEnableRefresh(false)
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        assetRecordAdapter = AssetRecordAdapter()
        initRecyclerView(assetRecordAdapter, binding?.recyclerView, true)
        addRefreshListener(binding?.refreshLayout)
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView2() {
        binding?.llRecordTitle?.visibility = View.VISIBLE
        digRecordAdapter = DigRecordAdapter()
        initRecyclerView(digRecordAdapter, binding.recyclerView, true)
        addRefreshListener(binding?.refreshLayout)
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView3() {
        binding?.llRecordTitle?.visibility = View.VISIBLE
        myMinerAdapter = MyMinerAdapter(entryType!!)
        initRecyclerView(myMinerAdapter, binding.recyclerView, true)
        addRefreshListener(binding?.refreshLayout)
    }

    override fun loadDate() {
        when (entryType) {
            0 -> minePresenter.btf(baseCurrent, baseSize)
            1 -> ecospherePresenter.mineRecordList(
                SharedPreferencesUtils.getString(context, "uid", ""), baseCurrent, baseSize
            )
            2 -> ecospherePresenter.getMyMinerDetails(
                SharedPreferencesUtils.getString(context, "uid", ""),
                baseCurrent, baseSize, "1"
            )
            3 -> ecospherePresenter.getMyMinerDetails(
                SharedPreferencesUtils.getString(context, "uid", ""),
                baseCurrent, baseSize, "2"
            )
        }
    }


    override fun initDate() {
        initPresenter(listOf(ecospherePresenter, minePresenter))
        loadDate()
    }

    override fun onGetHoneyListSuccess(response: BaseBean<List<HoneyListData>>) {
        dealData(assetRecordAdapter, response.data, binding.refreshLayout)
    }

    override fun onGetMineRecordListSuccess(response: BaseBean<List<MineRecordListDate>>) {
        dealData(digRecordAdapter, response.data, binding.refreshLayout)
    }

    override fun onGetMyMinerDetailsSuccess(response: BaseBean<MyMinerDetailsDate>) {
        binding?.apply {
            response.data.apply {
                dealData(myMinerAdapter, response.data.minerList, refreshLayout)
            }
        }

    }
}