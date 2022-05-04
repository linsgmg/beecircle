package com.xz.beecircle.activity.mine

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.xz.beecircle.Constant.*
import com.xz.beecircle.R
import com.xz.beecircle.activity.home.MyTeamActivity
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityVerifyStatusBinding

class VerifyStatusActivity : ModuleActivity<ActivityVerifyStatusBinding>() {
    private var status = -1
    private var msg:String? = null
    private var title: TextView? = null
    override fun initView() {
        status = intent.getIntExtra("status", -1)
        msg = intent.getStringExtra("msg")

        addTitleBar {
            title = it.findViewById<TextView>(R.id.tv_title)
        }

        binding?.apply {
            when (status) {
//                VERIFY_STATUS_CODE_SSZ -> {
//                    title?.text = "认证码认证"
//                    ivIcon.setImageDrawable(ContextCompat.getDrawable(this@VerifyStatusActivity, R.drawable.shenhezhong))
//                    tvContent1.text = "审核中......"
//                }
                2 -> {//认证码审核失败
                    title?.text = "认证码认证"
                    tvContent1.text = "审核失败"
                    tvContent2.visibility = View.VISIBLE
                    tvContent2.text = "信息有误，请确认手机号与身份信息相符"
                    tvConfirm.visibility = View.VISIBLE
                    tvConfirm.text = "重新认证"
                }
                3 -> {//认证码二次失败
                    title?.text = "认证码认证"
                    tvConfirm.visibility = View.VISIBLE
                    tvContent2.visibility = View.VISIBLE
                    tvConfirm.text = "去手持认证"
                }
                0 -> {//待审核
                    title?.text = "手持认证"
                    ivIcon.setImageDrawable(ContextCompat.getDrawable(this@VerifyStatusActivity, R.drawable.shenhezhong))
                    tvContent1.text = "审核中......"
                }
                4 -> {//手持认证失败
                    title?.text = "手持认证"
                    tvContent1.text = "审核失败"
                    tvContent2.visibility = View.VISIBLE
                    tvContent2.text = "失败原因：$msg"
                    tvConfirm.visibility = View.VISIBLE
                    tvConfirm.text = "重新认证"
                }
                MY_TEAM -> {
                    title?.text = "我的团队"
                    ivIcon.setImageDrawable(ContextCompat.getDrawable(this@VerifyStatusActivity, R.drawable.wodetuandui))
                    tvContent1.visibility = View.GONE
                    tvConfirm.visibility = View.VISIBLE
                    tvConfirm.text = "创建团队"
                }
            }

            tvConfirm.setOnClickListener {
                when (tvConfirm.text.toString().trim()) {
                    "重新认证" -> {
                        when(status){
                            2->startActivity(RealVerifyCodeActivity::class.java)
                            4->startActivity(RealVerifyHandActivity::class.java)
                        }
                    }
                    "去手持认证" -> {
                        startActivity(RealVerifyHandActivity::class.java)
                    }
                    "创建团队" -> {
                        startActivity(MyTeamActivity::class.java)
                    }
                }
                finish()
            }
        }
    }

    override fun initDate() {

    }

}