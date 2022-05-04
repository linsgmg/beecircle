package com.xz.beecircle.activity.ecosphere

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lin.baselib.utils.*
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xz.beecircle.*
import com.xz.beecircle.adapter.TimeAdapter
import com.xz.beecircle.adapter.TimeAdapter2
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.databinding.ActivityChargeAndMenmberBinding

class ChargeAndMemberActivity : ModuleActivity<ActivityChargeAndMenmberBinding>(), CommonContact.EcosphereView {

    private var entryType: String? = null
    private var chargeType = "0"
    private val ecospherePresenter: Presenter.EcospherePresenter = Presenter.EcospherePresenter()
    private var phoneChargeInfoData: PhoneChargeInfoData? = null
    private var vipChargeInfoData: VipChargeInfoData? = null
    private var monthList: MutableList<String>? = null


    override fun initView() {
        entryType = intent.getStringExtra("entryType")
        addTitleBar {
            val rightView = it.findViewById<ImageView>(R.id.iv_title_right)
            val title = it.findViewById<TextView>(R.id.tv_title)
            when (entryType) {
                "charge" -> {
                    title.text = "充话费"
                    rightView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.huafeijilu))
                }
                "member" -> {
                    title.text = "视频会员"
                    rightView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shipinhuiyuanjilu))
                }
            }
            rightView.setOnClickListener {
                startActivity(ChargeRecordActivity::class.java, hashMapOf("entryType" to entryType))
            }
        }

        binding?.apply {
            tvTime.tag = 0
            when (entryType) {
                "charge" -> {
                    llTime.visibility = View.GONE
                    llPayType.visibility = View.GONE
                    etInputMoney.hint = "请输入充值金额"
                }
                "member" -> {
                    tvChargeText.text = "按月充值："
                    etInputMoney.isEnabled = false
                }
            }

            etInputMoney.addTextChangedListener(object : MyTextWatcher(-1, 2) {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    val money = etInputMoney.text.toString().trim()
                    if (RegexUtils.checkValue(money, RegexUtils.REGEX_FLOUT2)) {
                        val phoneConfig = phoneChargeInfoData?.phoneConfig
                        val rate = Arith.div(phoneConfig?.payhoney, phoneConfig?.rechargenum)
                        tvPayBee.text = Arith.round(Arith.mul(money, rate), 2) + " 蜜蜂"
                    } else if (TextUtils.isEmpty(money)) {
                        tvPayBee.text = "0.00 蜜蜂"
                    }
                }
            })

            group.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb_tx -> {
                        chargeType = "0"
                        ecospherePresenter.getRechargeVip(SharedPreferencesUtils.getString(this@ChargeAndMemberActivity,
                                "uid", ""), "0")
                    }
                    R.id.rb_yk -> {
                        chargeType = "1"
                        ecospherePresenter.getRechargeVip(SharedPreferencesUtils.getString(this@ChargeAndMemberActivity,
                                "uid", ""), "1")
                    }
                    R.id.rb_aqy -> {
                        chargeType = "2"
                        ecospherePresenter.getRechargeVip(SharedPreferencesUtils.getString(this@ChargeAndMemberActivity,
                                "uid", ""), "2")
                    }
                }
            }

            llTime.setOnClickListener {
                showPopwindow(llTime, monthList)
            }

            tvConfirm.setOnClickListener {
                val account = etInputPhone.text.toString().trim()
                val money = etInputMoney.text.toString().trim()
                val pwd = etInputPwd.text.toString().trim()
                val vipId = vipChargeInfoData?.vipConfig?.get(tvTime.tag as Int)?.id
                when (entryType) {
                    "charge" -> {
                        if (judgePhone(account) && judgeMoney(money) && judgePwd(pwd)) {
                            ecospherePresenter.rechargePhone(SharedPreferencesUtils.getString(this@ChargeAndMemberActivity, "uid", ""),
                                    account, pwd, money)
                        }
                    }
                    "member" -> {
                        if (judgePhone(account) && judgePwd(pwd)) {
                            ecospherePresenter.rechargeVip(SharedPreferencesUtils.getString(this@ChargeAndMemberActivity, "uid", ""),
                                    account, pwd, vipId)
                        }
                    }
                }
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(ecospherePresenter))
        when (entryType) {
            "charge" -> {
                ecospherePresenter.getRechargePhone(SharedPreferencesUtils.getString(this, "uid", ""))
            }
            "member" -> {//vip类型（0腾讯（1优酷（2爱奇艺
                ecospherePresenter.getRechargeVip(SharedPreferencesUtils.getString(this, "uid", ""), "0")
            }
        }
    }

    /**
     * 判断手机号
     */
    private fun judgePhone(value: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            ToastUtils.showShort("请输入充值账号")
            return false
        }
        return true
    }

    /**
     * 判断金额
     */
    private fun judgeMoney(value: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            ToastUtils.showShort("请输入充值金额")
            return false
        }
        return true
    }

    /**
     * 判断密码
     */
    private fun judgePwd(value: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            ToastUtils.showShort("请输入交易密码")
            return false
        }
        return true
    }


    /**
     * 弹窗
     */
    private var popupWindow: PopupWindow? = null
    private fun showPopwindow(view: View?, list: MutableList<String>?) {
        if (list == null) return
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = layoutInflater.inflate(R.layout.pop_list, null, false)
        //列表
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        var timeAdapter = TimeAdapter2(binding?.tvTime?.tag as Int)
        timeAdapter.data = list
        initRecyclerView(timeAdapter, recyclerView)
        popupWindow = PopupWindow(contentView, DensityUtil.dip2px(this, 75f),
                LinearLayout.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(ColorDrawable(0x11333333))
        popupWindow!!.setOnDismissListener {
            popupWindow = null
        }
        popupWindow!!.showAsDropDown(view, 0, 5)
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView(timeAdapter: TimeAdapter2, recyclerView: RecyclerView) {
        initRecyclerView(timeAdapter, recyclerView, true)
        timeAdapter.setItemClick(object : ModuleAdapter.OnMyItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                popupWindow?.dismiss()
                binding?.tvTime?.tag = position
                binding?.tvTime?.text = timeAdapter.data.get(position)
                binding?.tvPayBee?.text = vipChargeInfoData?.vipConfig?.get(position)?.payhoney + " 蜂蜜"
            }
        })
    }

    override fun onGetPhoneChargeInfoSuccess(response: BaseBean<PhoneChargeInfoData>) {
        phoneChargeInfoData = response.data
        binding?.tvUsefulBee?.text = response.data.user.honey + " 蜜蜂"
    }

    override fun onGetVipChargeInfoSuccess(response: BaseBean<VipChargeInfoData>) {
        vipChargeInfoData = response.data
        binding?.tvPayBee?.text = response.data.vipConfig[0].payhoney + " 蜜蜂"
        binding?.tvTime?.text = response.data.vipConfig[0].rechargenum + "个月"
        binding?.tvUsefulBee?.text = response.data.user.honey + " 蜜蜂"
        monthList = mutableListOf()
        for (item in response.data.vipConfig) {
            monthList?.add(item.rechargenum + "个月")
        }
    }

    override fun onRechargePhoneSuccess(response: BaseBean<AdData>) {
        ToastUtils.showShort("购买成功")
        ecospherePresenter.getRechargePhone(SharedPreferencesUtils.getString(this, "uid", ""))
    }

    override fun onRechargeVipSuccess(response: BaseBean<AdData>) {
        ToastUtils.showShort("购买成功")
        ecospherePresenter.getRechargeVip(SharedPreferencesUtils.getString(this, "uid", ""), chargeType)
    }
}