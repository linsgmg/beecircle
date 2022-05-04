package com.xz.beecircle.activity.mine

import android.content.Intent
import android.widget.TextView
import com.lin.baselib.net.API
import com.lin.baselib.utils.PicUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityAccountInfoSettingBinding

class AccountInfoSettingActivity : ModuleActivity<ActivityAccountInfoSettingBinding>(),
    CommonContact.LoginView,
    CommonContact.MineView,
    CommonContact.UploadFileView {
    private var picUrl: String? = null
    private var avatar: String? = null
    private var name: String? = null
    private val minePresenter = Presenter.MinePresenter()
    private val loginPresenter = Presenter.LoginPresenter()
    private val upLoadPresenter = Presenter.UpLoadPresenter()

    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "账户信息设置"
        }
        avatar = intent.getStringExtra("avatar")
        name = intent.getStringExtra("name")
        binding?.apply {
            PicUtils.uploadVideo(
                this@AccountInfoSettingActivity,
                API.URL_HOST_USER_IMAGE + avatar,
                ivAvatar
            )
            tvNickname.text = name

            ivAvatar.setOnClickListener {
                PicUtils.beginCameraDialog(
                    this@AccountInfoSettingActivity,
                    PictureConfig.CHOOSE_REQUEST,
                    1
                )
            }
            llNickname.setOnClickListener {
                startActivity(ChangeNicknameActivity::class.java, hashMapOf("name" to name))
            }
            tvVersion.text = "v" + getVersionCode(this@AccountInfoSettingActivity)
        }
    }

    override fun initDate() {
        initPresenter(listOf(loginPresenter, upLoadPresenter, minePresenter))
    }

    override fun onResume() {
        super.onResume()
        loginPresenter.getUserInfo()
    }

    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
        response.data.apply {
            avatar = headerpic
            name = username
            binding?.apply {
                tvNickname.text = username
                PicUtils.uploadPic(
                    this@AccountInfoSettingActivity,
                    API.URL_HOST_USER_IMAGE + headerpic,
                    ivAvatar
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val res = PictureSelector.obtainMultipleResult(data)
                    // 图片选择结果回调
                    picUrl = if (res[0].isCompressed) res[0].compressPath else res[0].realPath
                    upLoadPresenter.uploadFile(
                        mutableListOf(picUrl),
                        "headPic"
                    )
                }
            }
        }
    }

    override fun onUploadFileSuccess(response: BaseBean<FileData>) {
        picUrl = response.data.url
        minePresenter.updateHeadPic(picUrl)
    }

    override fun onUpdateHeadPicSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("头像修改成功")
        PicUtils.uploadPic(this, API.URL_HOST_USER_IMAGE + picUrl, binding.ivAvatar)
    }
}