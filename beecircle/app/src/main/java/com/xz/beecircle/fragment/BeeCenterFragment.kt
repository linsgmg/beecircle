package com.xz.beecircle.fragment

import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.xz.beecircle.*
import com.xz.beecircle.adapter.BeeCenterAdapter
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.ModuleFragment
import com.xz.beecircle.databinding.LayoutListBinding
import java.util.*

class BeeCenterFragment : ModuleFragment<LayoutListBinding>(), CommonContact.HomeView, CommonContact.LoginView {
    private var entryType: Int? = null
    private var beeCenterAdapter: BeeCenterAdapter? = null
    private var homePresenter: Presenter.HomePresenter = Presenter.HomePresenter()
    private var loginPresenter: Presenter.LoginPresenter = Presenter.LoginPresenter()
    private var beeValue: String? = null
    private var shareValue: String? = null
    private var phone: String? = null
    private var tvVerifyCode: TextView? = null

    override fun initView() {
        entryType = arguments!!.getInt("entryType", -1)
        initRecyclerView()
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        beeCenterAdapter = BeeCenterAdapter(entryType!!)
        initRecyclerView(beeCenterAdapter, binding.recyclerView, true)
        addRefreshListener(binding.refreshLayout)
        beeCenterAdapter!!.setItemClick(object : ModuleAdapter.OnMyItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                when (view.id) {
                    R.id.tv_buy -> {
                        showAddressDialog(beeCenterAdapter?.data?.get(position)?.id)
                    }
                }
            }
        })
    }

    /**
     * 购买确认弹框
     */
    private var bottomSheetDialog: BottomSheetDialog? = null

    private fun showAddressDialog(honeyid: String?) {
        bottomSheetDialog = BottomSheetDialog(context!!)
        bottomSheetDialog?.setCancelable(false)
        bottomSheetDialog?.setCanceledOnTouchOutside(true)
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_buy, null)
        val tvBuyTex = view.findViewById<TextView>(R.id.tv_buy_tex)
        val tvGetVerifyCode = view.findViewById<TextView>(R.id.tv_get_verify_code)
        val tvConfirm = view.findViewById<TextView>(R.id.tv_confirm)
        val etInputPwd = view.findViewById<EditText>(R.id.et_input_pwd)
        val etInputVerifyCode = view.findViewById<EditText>(R.id.et_input_verify_code)
        val llBee = view.findViewById<LinearLayout>(R.id.ll_bee)
        val llShare = view.findViewById<LinearLayout>(R.id.ll_share)
        val ivBee = view.findViewById<TextView>(R.id.iv_bee)
        val ivShare = view.findViewById<TextView>(R.id.iv_share)
        val tvValue1 = view.findViewById<TextView>(R.id.tv_value1)
        val tvValue2 = view.findViewById<TextView>(R.id.tv_value2)
        tvBuyTex.text = "选择购买方式"
        tvValue1.text = "可以：$beeValue"
        tvValue2.text = "可以：$shareValue"
        var selectPayWay = -1
        llBee.setOnClickListener {
            ivBee.isSelected = true
            ivShare.isSelected = false
            selectPayWay = 0
        }
        llShare.setOnClickListener {
            ivBee.isSelected = false
            ivShare.isSelected = true
            selectPayWay = 1
        }
        this.tvVerifyCode = tvGetVerifyCode
        tvGetVerifyCode.setOnClickListener {
            loginPresenter.getSSMCode(phone, "pay")
        }
        tvConfirm.setOnClickListener {
            val pwd = etInputPwd.text.toString().trim()
            val verifyCode = etInputVerifyCode.text.toString().trim()
            if (selectPayWay == -1) {
                ToastUtils.showShort("请选择购买方式")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.showShort("请输入交易密码")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(verifyCode)) {
                ToastUtils.showShort("请输入验证码")
                return@setOnClickListener
            }
            homePresenter.buyHoney(SharedPreferencesUtils.getString(context, "uid", ""),
                    honeyid, pwd, selectPayWay.toString(), verifyCode)
            bottomSheetDialog?.dismiss()
        }
        bottomSheetDialog?.setOnDismissListener {
            if (timer != null) timer?.cancel()
            bottomSheetDialog = null
        }
        val layoutParams: ViewGroup.LayoutParams = LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(context),
                ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomSheetDialog?.setContentView(view, layoutParams)
        bottomSheetDialog?.delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
        bottomSheetDialog?.show()
    }

    private var timer: CountDownTimer? = null

    private fun setTime(view: TextView) {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val dhms = (millisUntilFinished / 1000).toString() + "秒"
                view.text = dhms
                view.isEnabled = false
            }

            override fun onFinish() {
                view.text = "获取验证码"
                view.isEnabled = true
            }
        }
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer!!.start()
    }

    override fun loadDate() {
        when (entryType) {
            0 -> homePresenter.getBeeCenterJSList(baseCurrent, baseSize, SharedPreferencesUtils.getString(context, "uid", ""))
            1 -> homePresenter.myHoney(baseCurrent, baseSize, SharedPreferencesUtils.getString(context, "uid", ""))
            2 -> homePresenter.overHoney(baseCurrent, baseSize, SharedPreferencesUtils.getString(context, "uid", ""))
        }
    }

    override fun onResume() {
        super.onResume()
        baseCurrent = 1
        loginPresenter.getUserInfo()
        loadDate()
    }

    override fun initDate() {
        initPresenter(listOf(homePresenter, loginPresenter))
//        loginPresenter.getUserInfo()
//        loadDate()
    }

    override fun onGetBeeCenterJSListSuccess(response: BaseBean<List<BeeCenterData>>) {
        dealData(beeCenterAdapter, response.data, binding.refreshLayout)
    }

    override fun onGetBeeCenterMyListSuccess(response: BaseBean<List<BeeCenterData>>) {
        dealData(beeCenterAdapter, response.data, binding.refreshLayout)
    }

    override fun onGetBeeCenterGQListSuccess(response: BaseBean<List<BeeCenterData>>) {
        dealData(beeCenterAdapter, response.data, binding.refreshLayout)
    }

    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
        response.data.apply {
            beeValue = honey
            shareValue = shard
            this@BeeCenterFragment.phone = phone
        }
    }

    override fun onGetSSMCode(response: BaseBean<Any>) {
        tvVerifyCode?.let { setTime(it) }
    }

    override fun onBuyHoneySuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("购买成功")
        baseCurrent = 1
        loadDate()
    }

}