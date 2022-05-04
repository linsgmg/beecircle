package com.xz.beecircle.activity.mine

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lin.baselib.utils.SharedPreferencesUtils
import com.xz.beecircle.*
import com.xz.beecircle.adapter.ReceiveInfoAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.databinding.LayoutListBinding

class ReceiveInfoActivity : ModuleActivity<LayoutListBinding>(), CommonContact.MineView {
    private var receiveInfoAdapter: ReceiveInfoAdapter? = null
    private val minePresenter = Presenter.MinePresenter()
    private var titleRight: ImageView? = null

    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "收款信息"
            titleRight = it.findViewById<ImageView>(R.id.iv_title_right)
            titleRight?.setImageDrawable(getDrawable(R.drawable.tianjia))
            titleRight?.setOnClickListener {
                val intent = Intent(this@ReceiveInfoActivity, ReceiveWayActivity::class.java)
                startActivity(intent)
            }
        }
        initRecyclerView()
    }

    override fun initDate() {
        initPresenter(listOf(minePresenter))
    }

    override fun onResume() {
        super.onResume()
        minePresenter.getMyPayType()
    }

    override fun onGetMyPayTypeSuccess(response: BaseBean<List<MyPayWayData>>) {
        dealData(receiveInfoAdapter, response.data, null)
        if (response.data.size >= 3) titleRight?.visibility = View.GONE else titleRight?.visibility = View.VISIBLE
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        receiveInfoAdapter = ReceiveInfoAdapter()
        initRecyclerView(receiveInfoAdapter, binding.recyclerView, true)
        receiveInfoAdapter!!.setItemClick(object : ModuleAdapter.OnMyItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val bean = receiveInfoAdapter?.data?.get(position)
                when (receiveInfoAdapter?.data?.get(position)?.paytype) {
                    0 -> {
                        startActivity(
                            ReceiveInfoDetailActivity::class.java,
                            hashMapOf("entryType" to "zfb","bean" to bean)
                        )
                    }
                    1 -> {
                        startActivity(
                            ReceiveInfoDetailActivity::class.java,
                            hashMapOf("entryType" to "wx","bean" to bean)
                        )
                    }
                    2 -> {
                        startActivity(
                            ReceiveInfoDetailActivity::class.java,
                            hashMapOf("entryType" to "yhk","bean" to bean)
                        )
                    }
                }
            }
        })
    }
}