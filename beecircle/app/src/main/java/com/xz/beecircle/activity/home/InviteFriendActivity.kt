package com.xz.beecircle.activity.home

import android.graphics.Bitmap
import android.view.*
import com.lin.baselib.utils.DensityUtil
import com.lin.baselib.utils.SaveQRCodeUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityInviteFriendBinding
import java.util.*

class InviteFriendActivity : ModuleActivity<ActivityInviteFriendBinding>() ,CommonContact.HomeView{
    private val homePresenter = Presenter.HomePresenter()
    private var qr:Bitmap? = null
    private var url:String? = null
    private var uid:String? = null

    override fun initView() {

        fullScreen()

        binding.tvInviteCode.setOnClickListener {
            SaveQRCodeUtils.copy(this, uid)
        }

        binding.tvSave.setOnClickListener {
            SaveQRCodeUtils.copy(this,url)
        }
    }

    override fun initDate() {
        initPresenter(listOf(homePresenter))
        homePresenter.inviteFriend(SharedPreferencesUtils.getString(this,"uid",""))
    }

    override fun onInviteFriendSuccess(response: BaseBean<InviteFriendData>) {
        binding?.apply {
            response.data.apply {
                qr = CodeUtils.createImage(url, DensityUtil.dip2px(this@InviteFriendActivity,125f), DensityUtil.dip2px(this@InviteFriendActivity,125f), null)
                ivQr.setImageBitmap(qr)
                this@InviteFriendActivity.uid = uid
                this@InviteFriendActivity.url = url
                tvInviteCode.text = "我的邀请码 （UID）：${uid}"
            }
        }
    }

    



}