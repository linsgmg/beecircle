package com.xz.beecircle.activity.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.widget.TextView
import com.lin.baselib.constant.RegexConstants.REGEX_MOBILE_SIMPLE
import com.lin.baselib.utils.*
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBeeOutBinding
import com.xz.beecircle.databinding.ActivityBeeOutDetailBinding

class BeeOutDetailActivity : ModuleActivity<ActivityBeeOutDetailBinding>(), CommonContact.LoginView,
    CommonContact.MineView {
    private var entryType = ""
    private var phone = ""
    private val loginPresenter = Presenter.LoginPresenter()
    private val minePresenter = Presenter.MinePresenter()


    override fun initView() {
        entryType = intent.getStringExtra("entryType")!!
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            when (entryType) {
                "bee" -> {
                    title.text = "蜂蜜转出"
                }
                "btf" -> {
                    title.text = "BTF转出"
                }
            }
        }

        binding?.apply {

            when (entryType) {
                "bee" -> {
                    tvUnit.text = "蜂蜜"
                }
                "btf" -> {
                    tvUnit.text = "BTF"
                }
            }

            etInputMoney.addTextChangedListener(object : MyTextWatcher(-1, 2) {})

            tvGetVerifyCode.setOnClickListener {
                loginPresenter.getSSMCode(phone, "pay")
            }
            tvConfirm.setOnClickListener {
                summit()
            }
        }

    }

    override fun initDate() {
        initPresenter(listOf(loginPresenter, minePresenter))
        loginPresenter.getUserInfo()
        when (entryType) {
            "bee" -> {
                minePresenter.outHoneyConfig()
            }
            "btf" -> {
                minePresenter.outBtfConfig()
            }
        }
    }

    override fun onGetOutBtfConfigSuccess(response: BaseBean<OutBtfConfigData>) {
        binding.tvTip.text = "注：${response.data.min}个起转，手续费${response.data.charge}"
    }

    override fun onGetOutHoneyConfigSuccess(response: BaseBean<OutBtfConfigData>) {
        binding.tvTip.text = "注：只可转给直推下级，${response.data.min}个起转，手续费${response.data.charge}"
    }

    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
        binding?.apply {
            this@BeeOutDetailActivity.phone = response.data.phone
            when (entryType) {
                "bee" -> {
                    tvBalance.text = "蜂蜜余额：${response.data.honey}"
                }
                "btf" -> {
                    tvBalance.text = "BTF余额：${response.data.btf}"
                }
            }
        }
    }

    private fun summit() {
        binding?.apply {
            val account = etInputPhone.text.toString().trim()
            if (TextUtils.isEmpty(account)) {
                ToastUtils.showShort("请输入转出账号")
                return
            }
            val money = etInputMoney.text.toString().trim()
            if (!RegexUtils.checkValue(money, RegexUtils.REGEX_FLOUT2)) {
                ToastUtils.showShort("请输入转出金额")
                return
            }
            val pwd = etInputPwd.text.toString().trim()
            if (TextUtils.isEmpty(pwd) || pwd.length != 6) {
                ToastUtils.showShort("请输入交易密码")
                return
            }
            val verifyCode = etInputVerifyCode.text.toString().trim()
            if (TextUtils.isEmpty(verifyCode) || verifyCode.length != 6) {
                ToastUtils.showShort("请输入验证码")
                return
            }
            when (entryType) {
                "bee" -> {
                    minePresenter.outHoney( account, pwd, verifyCode, money)
                }
                "btf" -> {
                    minePresenter.outBtf(account, pwd, verifyCode, money)
                }
            }
        }
    }

    override fun onOutBtfSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("转出成功")
        loginPresenter.getUserInfo()
    }

    override fun onOutHoneySuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("转出成功")
        loginPresenter.getUserInfo()
    }

    override fun onGetSSMCode(response: BaseBean<Any>) {
        ToastUtils.showShort(response.message)
        setTime()
    }


    private var timer: CountDownTimer? = null

    private fun setTime() {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val dhms = (millisUntilFinished / 1000).toString() + "秒"
                binding.tvGetVerifyCode.text = dhms
                binding.tvGetVerifyCode.isEnabled = false
            }

            override fun onFinish() {
                binding.tvGetVerifyCode.text = "获取验证码"
                binding.tvGetVerifyCode.isEnabled = true
            }
        }
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) timer?.cancel()
    }

}