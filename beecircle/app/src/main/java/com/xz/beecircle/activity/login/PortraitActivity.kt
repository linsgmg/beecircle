package com.xz.beecircle.activity.login

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lin.baselib.utils.WebViewInitUtils
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityPortraitBinding

class PortraitActivity : ModuleActivity<ActivityPortraitBinding>(), CommonContact.MineView {

    private var entryType: String? = null
    private var bean: NoticeListData? = null
    private var minePresenter  =  Presenter.MinePresenter()
    private lateinit var title: TextView

    override fun initView() {

        addTitleBar {
            title = it.findViewById<TextView>(R.id.tv_title)
            title.setTextColor(ContextCompat.getColor(this, R.color.titel_black))
        }
    }

    override fun initDate() {
        bean = intent.getParcelableExtra("bean")
        entryType = intent.getStringExtra("entryType")
        if (bean != null) {
            binding.apply {
                tvSubtitle.text = bean?.title
                tvTime.text = bean?.createtime
                initWebView(bean?.content!!)
            }
        } else {
            initPresenter(listOf(minePresenter))
            minePresenter.getNotice(entryType)
        }
        if (entryType == Constant.WEB_TYPE_TREAT) {
            binding.llSubtitle.visibility = View.GONE
            title.text = "服务条款"
            minePresenter.getNotice("land_fwtk")
        }
        if (entryType == Constant.WEB_TYPE_PRIMARY) {
            binding.llSubtitle.visibility = View.GONE
            title.text = "隐私政策"
            minePresenter.getNotice("land_yszc")
        }
        if (entryType == Constant.WEB_TYPE_NOTICE) {
            binding.llSubtitle.visibility = View.VISIBLE
            title.text = "公告详情"
        }
        if (entryType == Constant.WEB_TYPE_ABOUT_US) {
            binding.llSubtitle.visibility = View.GONE
            title.text = "关于我们"
            minePresenter.getNotice("user_about")
        }
        if (entryType == Constant.WEB_TYPE_BEE_BOOK) {
            binding.llSubtitle.visibility = View.VISIBLE
            binding.tvTime.visibility = View.GONE
            binding.line.visibility = View.GONE
            title.text = "白皮书"
            minePresenter.getNotice("mine_bps")
        }
        if (entryType == Constant.WEB_TYPE_TRADE_RULE) {
            binding.llSubtitle.visibility = View.VISIBLE
            binding.tvTime.visibility = View.GONE
            binding.line.visibility = View.GONE
            title.text = "交易规则"
            minePresenter.getNotice("trading_rule")
        }
        if (entryType == Constant.WEB_TYPE_HELP_CENTER) {
            binding.llSubtitle.visibility = View.VISIBLE
            binding.tvTime.visibility = View.VISIBLE
            binding.line.visibility = View.GONE
            title.text = "帮助中心详情"
        }
    }

    /**
     * 初始化webView
     *
     * @param htmlbody
     */
    private fun initWebView(htmlbody: String) {
        val htmlData = WebViewInitUtils.getHtmlData(htmlbody)
        binding.webView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
    }

    override fun onGetNoticeSuccess(response: BaseBean<ArticleData>) {
        initWebView(response.data.content)
        when(entryType ){
            Constant.WEB_TYPE_BEE_BOOK,Constant.WEB_TYPE_TRADE_RULE->binding?.tvSubtitle?.text = response.data.title
        }
    }
}