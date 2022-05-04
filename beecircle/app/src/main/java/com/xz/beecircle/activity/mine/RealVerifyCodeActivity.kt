package com.xz.beecircle.activity.mine

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.lin.baselib.utils.SaveQRCodeUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.lin.baselib.utils.WebViewInitUtils
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityRealVerifyCodeBinding
import com.xz.beecircle.widget.SimpleTipDialog
class RealVerifyCodeActivity : ModuleActivity<ActivityRealVerifyCodeBinding>(),
    CommonContact.MineView,
    CommonContact.LoginView{

    private val loginPresenter = Presenter.LoginPresenter()
    private val upLoadPresenter = Presenter.UpLoadPresenter()
    private val minePresenter = Presenter.MinePresenter()
    private var link: String = ""
    private var code: String = ""

    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "认证码实名"
        }

        binding?.apply {
            tvVerify.setOnClickListener {
                val code = etInputVerifyCode.text.toString().trim()
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showShort("请输入认证码")
                    return@setOnClickListener
                }
                minePresenter.checkCode(code)
            }

            tvGetVerifyCode.setOnClickListener {
                setTipDialog(0, "获取认证码", "购买链接：$link", "代购微信号：$code")
            }

            tvConfirm.setOnClickListener {
                judge()
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf( minePresenter, loginPresenter))
        loginPresenter.getUserInfo()
        minePresenter.realNameConfig()
    }

    override fun onGetRealNameConfigSuccess(response: BaseBean<RealNameConfigData>) {
        WebViewInitUtils.initWebView(binding?.webView, response.data.codeMore)
        link = response.data.buyUrl
        code = response.data.buyWx
    }

    override fun onCheckCodeSuccess(response: BaseBean<Any>) {
        binding?.apply {
            val data = response.data as Boolean
            if (data) {
                tvVerify.visibility = View.GONE
                ivSuccess.visibility = View.VISIBLE
                ToastUtils.showShort("验证成功")
            } else {
                setTipDialog(R.drawable.shenheshibai, "认证码不符，请重新输入")
            }
        }
    }

    private var simpleTipDialog: SimpleTipDialog? = null

    private fun setTipDialog(res: Int, vararg title: String) {
        if (simpleTipDialog != null && simpleTipDialog!!.isShowing) {
            return
        }
        simpleTipDialog = SimpleTipDialog(this)
        if (title.size == 1) {
            simpleTipDialog?.setIcon(res)
                ?.setTitle(title[0])
        } else {
            simpleTipDialog?.setTitle(title[0])?.setContent(title[1])?.setContent2(title[2])
        }
        simpleTipDialog?.setOnClickBottomListener(object : SimpleTipDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                simpleTipDialog!!.dismiss()
                if (title[0] == "验证成功") finish()
            }

            override fun onPositiveClick(view: View?) {
                simpleTipDialog!!.dismiss()
                val textView = view as TextView
                SaveQRCodeUtils.copy(this@RealVerifyCodeActivity, textView.text.split("：")[1])
            }
        })?.show()
    }

    private fun judge() {
        binding?.apply {
            val verifyCode = etInputVerifyCode.text.toString().trim()
            val name = etInputName.text.toString().trim()
            val id = etInputId.text.toString().trim()
            val phone = etInputPhone.text.toString().trim()
            if (TextUtils.isEmpty(verifyCode)) {
                ToastUtils.showShort("请输入认证码")
                return
            }
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShort("请输入您的姓名")
                return
            }
            if (TextUtils.isEmpty(id)) {
                ToastUtils.showShort("请输入您的身份证号码")
                return
            }
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.showShort("请输入手机号")
                return
            }
            minePresenter.realNameCode(verifyCode, name, id, phone)
        }
    }

    override fun onRealNameCodeSuccess(response: BaseBean<Any>) {
        setTipDialog(R.drawable.tijiaochenggong, "提交成功!")
    }

    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
        binding?.etInputPhone?.text = response.data.phone
    }


}