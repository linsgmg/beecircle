package com.xz.beecircle.activity.login

import android.graphics.Color
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.lin.baselib.utils.*
import com.lin.baselib.utils.RegexUtils.REGEX_PWD
import com.luck.picture.lib.tools.DoubleUtils
import com.xz.beecircle.*
import com.xz.beecircle.Constant.*
import com.xz.beecircle.activity.MainActivity
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityLoginBinding
import com.xz.beecircle.widget.SimpleTipDialog

class LoginActivity : ModuleActivity<ActivityLoginBinding>(), CommonContact.LoginView {
    private var entryType = ENTRYTYPE_LOGIN
    private val loginPresenter = Presenter.LoginPresenter()

    override fun initView() {
        setStatusBarColor(R.color.white)

        entryType = intent.getStringExtra("entryType").toString()

        addTitleBar {
            val backView = it.findViewById<ImageView>(R.id.iv_title_left)
            val llTitle = it.findViewById<RelativeLayout>(R.id.ll_title)
            llTitle.setBackgroundColor(Color.WHITE)
            when (entryType) {
                "null", ENTRYTYPE_LOGIN, ENTRYTYPE_REGISTER -> {
                    backView.visibility = View.INVISIBLE
                }
                ENTRYTYPE_FORGET_PWD -> {
                    backView.visibility = View.VISIBLE
                }
            }
        }

        binding?.apply {
            when (entryType) {
                "null", ENTRYTYPE_LOGIN -> {
                    if (!TextUtils.isEmpty(
                            SharedPreferencesUtils.getString(
                                this@LoginActivity,
                                "phone",
                                ""
                            )
                        )
                    ) {
                        binding?.etInputPhone?.setText(
                            SharedPreferencesUtils.getString(
                                this@LoginActivity,
                                "phone",
                                ""
                            )
                        )
                    }
                    if (!TextUtils.isEmpty(
                            SharedPreferencesUtils.getString(
                                this@LoginActivity,
                                "password",
                                ""
                            )
                        )
                    ) {
                        binding?.etInputPwd1?.setText(
                            SharedPreferencesUtils.getString(
                                this@LoginActivity,
                                "password",
                                ""
                            )
                        )
                    }
                    tvTitle.text = "??????"
                    tvPwdText1.visibility = View.VISIBLE
                    llPwd1.visibility = View.VISIBLE
                    vPwdLine1.visibility = View.VISIBLE
                    tvForgetPwd.visibility = View.VISIBLE
                    tvInviteCodeText.visibility = View.GONE
                    etInputInviteCode.visibility = View.GONE
                    vInviteCodeLine.visibility = View.GONE
                    tvVerifyCodeText.visibility = View.GONE
                    llVerifyCode.visibility = View.GONE
                    vVerifyCodeLine.visibility = View.GONE
                    tvPwdText2.visibility = View.GONE
                    llPwd2.visibility = View.GONE
                    vPwdLine2.visibility = View.GONE
                    tvPwdText3.visibility = View.GONE
                    llPwd3.visibility = View.GONE
                    vPwdLine3.visibility = View.GONE
                    llCheck.visibility = View.INVISIBLE
                    tvLoginOrRegister.visibility = View.VISIBLE
                    tvLoginOrRegister.text = "???????????????????????????"
                    tvBtn.text = "??????"
                }
                ENTRYTYPE_REGISTER -> {
                    tvTitle.text = "??????"
                    tvPwdText1.visibility = View.VISIBLE
                    llPwd1.visibility = View.VISIBLE
                    vPwdLine1.visibility = View.VISIBLE
                    tvForgetPwd.visibility = View.GONE
                    tvInviteCodeText.visibility = View.VISIBLE
                    etInputInviteCode.visibility = View.VISIBLE
                    vInviteCodeLine.visibility = View.VISIBLE
                    tvVerifyCodeText.visibility = View.VISIBLE
                    llVerifyCode.visibility = View.VISIBLE
                    vVerifyCodeLine.visibility = View.VISIBLE
                    tvPwdText2.visibility = View.GONE
                    llPwd2.visibility = View.GONE
                    vPwdLine2.visibility = View.GONE
                    tvPwdText3.visibility = View.GONE
                    llPwd3.visibility = View.GONE
                    vPwdLine3.visibility = View.GONE
                    llCheck.visibility = View.VISIBLE
                    tvLoginOrRegister.visibility = View.VISIBLE
                    tvLoginOrRegister.text = "????????????????????????"
                    tvBtn.text = "??????"
                }
                ENTRYTYPE_FORGET_PWD -> {
                    tvTitle.text = "????????????"
                    tvPwdText1.visibility = View.GONE
                    llPwd1.visibility = View.GONE
                    vPwdLine1.visibility = View.GONE
                    tvForgetPwd.visibility = View.GONE
                    tvInviteCodeText.visibility = View.GONE
                    etInputInviteCode.visibility = View.GONE
                    vInviteCodeLine.visibility = View.GONE
                    tvVerifyCodeText.visibility = View.VISIBLE
                    llVerifyCode.visibility = View.VISIBLE
                    vVerifyCodeLine.visibility = View.VISIBLE
                    tvPwdText2.visibility = View.VISIBLE
                    llPwd2.visibility = View.VISIBLE
                    vPwdLine2.visibility = View.VISIBLE
                    tvPwdText3.visibility = View.VISIBLE
                    llPwd3.visibility = View.VISIBLE
                    vPwdLine3.visibility = View.VISIBLE
                    llCheck.visibility = View.INVISIBLE
                    tvLoginOrRegister.visibility = View.GONE
                    tvBtn.text = "??????"
                }
            }

            tvForgetPwd.setOnClickListener {
                startActivity(
                    LoginActivity::class.java,
                    hashMapOf("entryType" to ENTRYTYPE_FORGET_PWD)
                )
            }

            tvLoginOrRegister.setOnClickListener {
                when (entryType) {
                    "null", ENTRYTYPE_LOGIN -> {
                        startActivity(
                            LoginActivity::class.java,
                            hashMapOf("entryType" to ENTRYTYPE_REGISTER)
                        )
                        finish()
                    }
                    ENTRYTYPE_REGISTER -> {
                        startActivity(
                            LoginActivity::class.java,
                            hashMapOf("entryType" to ENTRYTYPE_LOGIN)
                        )
                        finish()
                    }
                }
            }

            etInputPhone.addTextChangedListener(object : MyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    setBtnColor(judgeInput())
                }
            })

            etInputPwd1.addTextChangedListener(object : MyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    setBtnColor(judgeInput())
                }
            })

            etInputVerifyCode.addTextChangedListener(object : MyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    setBtnColor(judgeInput())
                }
            })

            etInputInviteCode.addTextChangedListener(object : MyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    setBtnColor(judgeInput())
                }
            })

            etInputPwd2.addTextChangedListener(object : MyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    setBtnColor(judgeInput())
                }
            })

            etInputPwd3.addTextChangedListener(object : MyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    setBtnColor(judgeInput())
                }
            })

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
                ) //???????????????????????????
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
                ) //???????????????????????????
            })

            ivSee3.setOnClickListener(View.OnClickListener {
                if (ivSee3.isSelected) {
                    ivSee3.isSelected = false
                    etInputPwd3.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                } else {
                    ivSee3.isSelected = true
                    etInputPwd3.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                etInputPwd3.setSelection(
                    etInputPwd3.text.toString().trim { it <= ' ' }.length
                ) //???????????????????????????
            })

            ivCheck.setOnClickListener(View.OnClickListener {
                ivCheck.isSelected = !ivCheck.isSelected
                setBtnColor(judgeInput())
            })

            tvGetVerifyCode.setOnClickListener {
                val phone = etInputPhone.text.toString().trim()
                if (judgePhone(phone)) {
                    when (entryType) {
                        ENTRYTYPE_LOGIN -> {
                            loginPresenter.getSSMCode(phone, "login")
                        }
                        ENTRYTYPE_REGISTER -> {
                            loginPresenter.getSSMCode(phone, "land")
                        }
                        ENTRYTYPE_FORGET_PWD -> {
                            loginPresenter.getSSMCode(phone, "updatepsd")
                        }
                    }
                }
            }

            tvSecretPortrait.setOnClickListener {
                startActivity(
                    PortraitActivity::class.java,
                    hashMapOf("entryType" to WEB_TYPE_PRIMARY)
                )
            }

            tvServicePortrait.setOnClickListener {
                startActivity(
                    PortraitActivity::class.java,
                    hashMapOf("entryType" to WEB_TYPE_TREAT)
                )
            }

            tvBtn.setOnClickListener {
                if (DoubleUtils.isFastDoubleClick()) {
                    return@setOnClickListener
                }
                val phone = etInputPhone.text.toString().trim()
                val pwd1 = etInputPwd1.text.toString().trim()
                val pwd2 = etInputPwd2.text.toString().trim()
                val pwd3 = etInputPwd3.text.toString().trim()
                val inviteCode = etInputInviteCode.text.toString().trim()
                val verifyCode = etInputVerifyCode.text.toString().trim()
                when (entryType) {
                    "null", ENTRYTYPE_LOGIN -> {
                        if (judgePhone(phone) && judgePwd(pwd1)) {
                            loginPresenter.login(phone, pwd1)
                        }
                    }
                    ENTRYTYPE_REGISTER -> {
                        if (judgePhone(phone) && judgePwd(pwd1) && judgeInviteCode(inviteCode)
                            && judgeVerifyCode(verifyCode) && judgeCheck()
                        ) {
                            loginPresenter.register(phone, pwd1, inviteCode, verifyCode)
                        }
                    }
                    ENTRYTYPE_FORGET_PWD -> {
                        if (judgePhone(phone) && judgeVerifyCode(verifyCode) && judgePwd(pwd2) && judgePwd2(pwd2, pwd3)) {
                            loginPresenter.changeLoginPwd(phone, verifyCode, pwd2, pwd3)
                        }
                    }
                }
            }
        }
    }

    private var simpleTipDialog: SimpleTipDialog? = null

    private fun setTipDialog(vararg value: String) {
        if (simpleTipDialog != null && simpleTipDialog!!.isShowing) {
            return
        }
        simpleTipDialog = SimpleTipDialog(this)
        simpleTipDialog?.setTitle(value[0])
            ?.setContent(value[1])

        simpleTipDialog?.setOnClickBottomListener(object : SimpleTipDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                simpleTipDialog!!.dismiss()
            }

            override fun onPositiveClick(view: View?) {
                simpleTipDialog!!.dismiss()

            }
        })?.show()
    }

    private var timer: CountDownTimer? = null

    private fun setTime() {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)??????
         * ?????????????????????????????????????????????????????????????????????
         * ?????????????????????????????????????????????onTick?????????1??????????????????onFinish?????????
         */
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val dhms = (millisUntilFinished / 1000).toString() + "???"
                binding.tvGetVerifyCode.text = dhms
                binding.tvGetVerifyCode.isEnabled = false
            }

            override fun onFinish() {
                binding.tvGetVerifyCode.text = "???????????????"
                binding.tvGetVerifyCode.isEnabled = true
            }
        }
        //?????? CountDownTimer ????????? start() ???????????????????????????????????????????????????
        timer!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) timer?.cancel()
    }

    /**
     * ????????????????????????
     */
    private fun setBtnColor(value: Boolean) {
        if (value) {
            binding.tvBtn.background = ContextCompat.getDrawable(this, R.drawable.bg_yellow_40)
        } else {
            binding.tvBtn.background =
                ContextCompat.getDrawable(this, R.drawable.bg_light_yellow_40)
        }
    }

    /**
     * ????????????????????????
     */
    private fun judgeInput(): Boolean {
        binding?.apply {
            val phone = etInputPhone.text.toString().trim()
            val pwd1 = etInputPwd1.text.toString().trim()
            val pwd2 = etInputPwd2.text.toString().trim()
            val pwd3 = etInputPwd3.text.toString().trim()
            val inviteCode = etInputInviteCode.text.toString().trim()
            val verifyCode = etInputVerifyCode.text.toString().trim()
            when (entryType) {
                "null", ENTRYTYPE_LOGIN -> {
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd1)) {
                        return false
                    }
                }
                ENTRYTYPE_REGISTER -> {
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(
                            inviteCode
                        ) || TextUtils.isEmpty(verifyCode)
                        || !ivCheck.isSelected
                    ) {
                        return false
                    }
                }
                ENTRYTYPE_FORGET_PWD -> {
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(verifyCode) || TextUtils.isEmpty(
                            pwd2
                        ) || TextUtils.isEmpty(pwd3)
                    ) {
                        return false
                    }
                }
            }
        }
        return true
    }

    /**
     * ???????????????
     */
    private fun judgePhone(value: String): Boolean {
        if (TextUtils.isEmpty(value) || value.length != 11) {
            ToastUtils.showShort("??????????????????")
            return false
        }
        return true
    }

    /**
     * ????????????
     */
    private fun judgePwd(value: String): Boolean {
        if (!RegexUtils.checkValue(value, REGEX_PWD)) {
            ToastUtils.showShort("?????????6-16?????????????????????+???????????????")
            return false
        }
        return true
    }

    /**
     * ????????????2
     */
    private fun judgePwd2(value: String, value2: String): Boolean {
        if (value != value2) {
            ToastUtils.showShort("?????????????????????")
            return false
        }
        return true
    }

    /**
     * ???????????????
     */
    private fun judgeInviteCode(value: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            ToastUtils.showShort("??????????????????")
            return false
        }
        return true
    }

    /**
     * ???????????????
     */
    private fun judgeVerifyCode(value: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            ToastUtils.showShort("??????????????????")
            return false
        }
        return true
    }

    /**
     * ????????????????????????
     */
    private fun judgeCheck(): Boolean {
        if (!binding.ivCheck.isSelected) {
            ToastUtils.showShort("???????????????????????????????????????????????????????????????")
            return false
        }
        return true
    }


    override fun initDate() {
        initPresenter(listOf(loginPresenter))
    }

    override fun onRegisterSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("????????????")
        startActivity(LoginActivity::class.java, hashMapOf("entryType" to ENTRYTYPE_LOGIN))
        finish()
    }

    override fun onGetSSMCode(response: BaseBean<Any>) {
        ToastUtils.showShort("??????????????????")
        setTime()
    }

    override fun onLoginSuccess(response: BaseBean<LoginData>) {
        SharedPreferencesUtils.saveString(this, "token", response.token)
        SharedPreferencesUtils.saveString(this, "uid", response.data.uid)
        SharedPreferencesUtils.saveString(
            this,
            "phone",
            binding?.etInputPhone?.text?.toString()?.trim()
        );
        SharedPreferencesUtils.saveString(
            this,
            "password",
            binding?.etInputPwd1?.text?.toString()?.trim()
        );
        ToastUtils.showShort("????????????")
        startActivity(MainActivity::class.java)
        finish()
    }

    override fun onChangeLoginPwdSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("????????????")
        SharedPreferencesUtils.saveString(this, "password", "")
        startActivity(LoginActivity::class.java, hashMapOf("entryType" to ENTRYTYPE_LOGIN))
        finish()
    }

    override fun tokenInvalid(type: Int, message: String?) {
        super.tokenInvalid(type, message)
        if (type == 210) {
            val split = message?.split(";")
            if (split?.size == 2) {
                setTipDialog(split[0], split[1])
            }
        }
    }

}