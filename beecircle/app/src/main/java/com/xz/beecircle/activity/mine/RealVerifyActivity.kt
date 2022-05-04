package com.xz.beecircle.activity.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityRealVerifyBinding
import com.xz.beecircle.databinding.ActivitySafeSettingBinding

class RealVerifyActivity : ModuleActivity<ActivityRealVerifyBinding>(),
    CommonContact.MineView {
    private val minePresenter = Presenter.MinePresenter()
    private var state = -1
    private var msg = ""

    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "实名认证"
        }

        binding?.apply {
            llCodeVerify.setOnClickListener {
                when (state) {
                    -1, 4 -> startActivity(RealVerifyCodeActivity::class.java)
                    0 -> ToastUtils.showShort("手持认证中")
                    1 -> ToastUtils.showShort("已认证码认证")
                    5 -> ToastUtils.showShort("已手持认证")
                    else -> startActivity(
                        VerifyStatusActivity::class.java,
                        hashMapOf("status" to state)
                    )
                }
//                startActivity(RealVerifyCodeActivity::class.java)
//                startActivity(VerifyStatusActivity::class.java, hashMapOf("status" to 0))
//                startActivity(VerifyStatusActivity::class.java, hashMapOf("status" to 1))
//                startActivity(VerifyStatusActivity::class.java, hashMapOf("status" to 2))
//                startActivity(VerifyStatusActivity::class.java, hashMapOf("status" to 3))
//                startActivity(VerifyStatusActivity::class.java, hashMapOf("status" to 4))
            }
            llHandVerify.setOnClickListener {
                when (state) {
                    -1, 2, 3 -> startActivity(RealVerifyHandActivity::class.java)
                    1 -> ToastUtils.showShort("已认证码认证")
                    5 -> ToastUtils.showShort("已手持认证")
                    else -> startActivity(
                        VerifyStatusActivity::class.java,
                        hashMapOf("status" to state, "msg" to msg)
                    )
                }
            }
        }

    }

    override fun initDate() {
        initPresenter(listOf(minePresenter))
    }

    override fun onResume() {
        super.onResume()
        minePresenter.getRealState()
    }

    override fun onGetRealStateSuccess(response: BaseBean<RealStateData>) {
        binding?.apply {
            state = response.data.state
            msg = response.data.msg
            when (state) {
                -1 -> {//未实名认证
                    tvCodeVerify.text = "未认证"
                    tvHandVerify.text = "未认证"
                }
                0 -> {//待审核
                    tvCodeVerify.text = "未认证"
                    tvHandVerify.text = "认证中"
                }
                1 -> {//认证码审核成功
                    tvCodeVerify.text = "已认证"
                    tvHandVerify.text = "未认证"
                }
                2 -> {//认证码审核失败
                    tvCodeVerify.text = "未认证"
                    tvHandVerify.text = "未认证"
                }
                3 -> {//认证码二次失败
                    tvCodeVerify.text = "未认证"
                    tvHandVerify.text = "未认证"
                }
                4 -> {//手持认证失败
                    tvCodeVerify.text = "未认证"
                    tvHandVerify.text = "未认证"
                }
                5 -> {//手持认证成功
                    tvCodeVerify.text = "未认证"
                    tvHandVerify.text = "已认证"
                }
            }
        }
    }

}