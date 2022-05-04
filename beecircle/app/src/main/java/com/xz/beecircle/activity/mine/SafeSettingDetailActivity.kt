package com.xz.beecircle.activity.mine

import android.graphics.Typeface
import android.os.CountDownTimer
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.TextView
import com.lin.baselib.utils.RegexUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.xz.beecircle.*
import com.xz.beecircle.activity.login.LoginActivity
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivitySafeSettingDetailBinding


class SafeSettingDetailActivity : ModuleActivity<ActivitySafeSettingDetailBinding>(),
    CommonContact.LoginView,
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
                "login" -> {
                    title.text = "修改登录密码"
                }
                "trade" -> {
                    title.text = "修改交易密码"
                }
            }
        }

        binding?.apply {
            phone = SharedPreferencesUtils.getString(this@SafeSettingDetailActivity, "phone", "")

            when (entryType) {
                "login" -> {

                }
                "trade" -> {
                    llPwd1.visibility = View.GONE
                    llPwd2.visibility = View.GONE
                    llPwd3.visibility = View.VISIBLE
                    llPwd4.visibility = View.VISIBLE
                }
            }

            ivSee1.setOnClickListener(View.OnClickListener {
                if (ivSee1.isSelected) {
                    ivSee1.isSelected = false
                    etInputPwd1.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                } else {
                    ivSee1.isSelected = true
                    etInputPwd1.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                etInputPwd1.setSelection(
                    etInputPwd1.text.toString().trim { it <= ' ' }.length
                ) //将光标移至文字末尾
            })

            ivSee2.setOnClickListener(View.OnClickListener {
                if (ivSee2.isSelected) {
                    ivSee2.isSelected = false
                    etInputPwd2.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                } else {
                    ivSee2.isSelected = true
                    etInputPwd2.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                etInputPwd2.setSelection(
                    etInputPwd2.text.toString().trim { it <= ' ' }.length
                ) //将光标移至文字末尾
            })

            ivSee3.setOnClickListener(View.OnClickListener {
                if (ivSee3.isSelected) {
                    ivSee3.isSelected = false
                    etInputPwd3.transformationMethod = PasswordTransformationMethod.getInstance();
                } else {
                    ivSee3.isSelected = true
                    etInputPwd3.transformationMethod =
                        HideReturnsTransformationMethod.getInstance();
                }
                etInputPwd3.setSelection(
                    etInputPwd3.text.toString().trim { it <= ' ' }.length
                ) //将光标移至文字末尾
            })

            ivSee4.setOnClickListener(View.OnClickListener {
                if (ivSee4.isSelected) {
                    ivSee4.isSelected = false
                    etInputPwd4.transformationMethod = PasswordTransformationMethod.getInstance();
                } else {
                    ivSee4.isSelected = true
                    etInputPwd4.transformationMethod =
                        HideReturnsTransformationMethod.getInstance();
                }
                etInputPwd4.setSelection(
                    etInputPwd4.text.toString().trim { it <= ' ' }.length
                ) //将光标移至文字末尾
            })


            tvGetVerifyCode.setOnClickListener {
                loginPresenter.getSSMCode(phone, "updatepsd")
            }
            tvConfirm.setOnClickListener {
                summit()
            }
        }

    }

    override fun initDate() {
        initPresenter(listOf(loginPresenter, minePresenter))
    }

    private fun summit() {
        binding?.apply {
            val pwd1 = etInputPwd1.text.toString().trim()
            val pwd2 = etInputPwd2.text.toString().trim()
            val pwd3 = etInputPwd3.text.toString().trim()
            val pwd4 = etInputPwd4.text.toString().trim()
            if (entryType == "login") {
                if (!RegexUtils.checkValue(pwd1, RegexUtils.REGEX_PWD)) {
                    ToastUtils.showShort("请输入6-16位包含大小字母+数字的密码")
                    return
                }
                if (pwd1 != pwd2) {
                    ToastUtils.showShort("两次密码不一样")
                    return
                }
            } else {
                if (TextUtils.isEmpty(pwd3)) {
                    ToastUtils.showShort("请输入新密码")
                    return
                }
                if (TextUtils.isEmpty(pwd4)) {
                    ToastUtils.showShort("请确认您的新密码")
                    return
                }
            }
            val verifyCode = etInputVerifyCode.text.toString().trim()
            if (TextUtils.isEmpty(verifyCode) || verifyCode.length != 6) {
                ToastUtils.showShort("请输入验证码")
                return
            }
            when (entryType) {
                "login" -> {
                    minePresenter.updatePassword(verifyCode, pwd1, pwd2)
                }
                "trade" -> {
                    minePresenter.updatePayPsd(verifyCode, pwd3, pwd4)
                }
            }
        }
    }

    override fun onUpdatePayPsdSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("提交成功")
    }

    override fun onUpdatePasswordSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("提交成功")
        SharedPreferencesUtils.saveString(this, "password", "")
        SharedPreferencesUtils.saveString(this, "token", "")
        SharedPreferencesUtils.saveString(this, "uid", "")
        finishAll()
        startActivity(LoginActivity::class.java)
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

    override fun onChangeLoginPwdSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("修改成功")
        SharedPreferencesUtils.saveString(this, "password", "")
        SharedPreferencesUtils.saveString(this, "token", "")
        SharedPreferencesUtils.saveString(this, "uid", "")
        startActivity(LoginActivity::class.java, hashMapOf("entryType" to Constant.ENTRYTYPE_LOGIN))
        finishAll()
    }

    override fun onGetSSMCode(response: BaseBean<Any>) {
        ToastUtils.showShort("验证码已发送")
        setTime()
    }

}