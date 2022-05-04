package com.xz.beecircle.activity.ecosphere

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.xz.beecircle.*
import com.xz.beecircle.adapter.MyExamAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityMyExamBinding
import com.xz.beecircle.widget.MinerDialog
import com.xz.beecircle.widget.RuleDialog

class MyExamActivity : ModuleActivity<ActivityMyExamBinding>(), CommonContact.EcosphereView , CommonContact.MineView{
    private var myExamAdapter: MyExamAdapter? = null
    private var ecospherePresenter: Presenter.EcospherePresenter = Presenter.EcospherePresenter()
    private var minePresenter  =  Presenter.MinePresenter()
    private var myExam: String? = null

    override fun initView() {
        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "我的算力"
            val tvTitleRight = it.findViewById<TextView>(R.id.tv_title_right)
            tvTitleRight.setTextColor(ContextCompat.getColor(this, R.color.text))
            tvTitleRight.text = "规则"
            tvTitleRight.setOnClickListener {
                minePresenter.getNotice("mfq_wdsl")
            }
        }
        myExam = intent.getStringExtra("myExam")
        binding.tvMyExam.text = myExam
        initRecyclerView()

    }

    override fun initDate() {
        initPresenter(listOf(ecospherePresenter,minePresenter))
        loadDate()
    }

    override fun loadDate() {
        ecospherePresenter.getMyPowerDetails(SharedPreferencesUtils.getString(this, "uid", ""), baseCurrent, baseSize)
    }

    override fun onGetNoticeSuccess(response: BaseBean<ArticleData>) {
        setTipDialog(response.data.content)
    }

    override fun onGetMyPowerDetailsSuccess(response: BaseBean<List<MyPowerDetailsDate>>) {
        dealData(myExamAdapter, response.data, binding?.refreshLayout)
    }

    private var ruleDialog: RuleDialog? = null

    private fun setTipDialog(vararg value: String) {
        if (ruleDialog != null && ruleDialog!!.isShowing) {
            return
        }
        ruleDialog = RuleDialog(this)
        ruleDialog?.setHtmlBody(value[0])
                ?.setOnClickBottomListener(object : RuleDialog.OnClickBottomListener {
                    override fun onNegativeClick(view: View?) {
                        ruleDialog!!.dismiss()
                    }

                    override fun onPositiveClick(view: View?) {
                        ruleDialog!!.dismiss()
                        ToastUtils.showShort(view?.tag as String)
                    }
                })?.show()
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        myExamAdapter = MyExamAdapter()
        initRecyclerView(myExamAdapter, binding.recyclerView, true)
        addRefreshListener(binding?.refreshLayout)
    }
}