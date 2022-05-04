package com.xz.beecircle.activity.mine

import android.content.Intent
import android.media.MediaPlayer
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.lin.baselib.net.API
import com.lin.baselib.utils.PicUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.lin.baselib.utils.WebViewInitUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityRealVerifyHandBinding
import com.xz.beecircle.widget.SimpleTipDialog
import com.zhaoss.weixinrecorded.activity.RecordedActivity


class RealVerifyHandActivity : ModuleActivity<ActivityRealVerifyHandBinding>(),
    CommonContact.MineView,
    CommonContact.LoginView,
    CommonContact.UploadFileView {
    private var picUrl: String? = null
    private var videoUrl: String? = null
    private val loginPresenter = Presenter.LoginPresenter()
    private val upLoadPresenter = Presenter.UpLoadPresenter()
    private val minePresenter = Presenter.MinePresenter()

    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "手持认证"
        }

        binding?.apply {
            llPic.setOnClickListener {
                PicUtils.beginCameraDialog(
                    this@RealVerifyHandActivity,
                    PictureConfig.CHOOSE_REQUEST,
                    1
                )
            }
            ivPic.setOnClickListener {
                PicUtils.addPicAndShow(this@RealVerifyHandActivity, mutableListOf(API.URL_HOST_USER_IMAGE+picUrl), 0)
            }
//            PicUtils.uploadPic(
//                this@RealVerifyHandActivity,
//                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3101694723,748884042&fm=26&gp=0.jpg",
//                binding?.ivExample
//            )
            ivExample.setOnClickListener {
                PicUtils.addPicAndShow(
                    this@RealVerifyHandActivity,
                    mutableListOf(it.tag.toString()),
                    0
                )
            }
            llVideo.setOnClickListener {
                PicUtils.beginVideoDialog(this@RealVerifyHandActivity, 100, 1)
            }
            ivVideo1.setOnClickListener {
                PicUtils.addVideoAndShow(this@RealVerifyHandActivity, API.URL_HOST_USER_IMAGE+videoUrl)
            }
//            PicUtils.uploadVideo(
//                this@RealVerifyHandActivity,
//                "http://eyepetizer-videos.oss-cn-beijing.aliyuncs.com/video_poster_share/21c579be05e9da90d1814d2f429f8e38.mp4",
//                binding?.ivVideo2
//            )
            ivVideo2.setOnClickListener {
                PicUtils.addVideoAndShow(
                    this@RealVerifyHandActivity,
                    it.tag as String?
                )
            }
            ivClose1.setOnClickListener {
                PicUtils.uploadVideo(this@RealVerifyHandActivity, "", ivPic)
                ivClose1.visibility = View.GONE
                ivPic.visibility = View.GONE
                tvPicTip.visibility = View.VISIBLE
                picUrl = ""
            }
            ivClose2.setOnClickListener {
                PicUtils.uploadVideo(this@RealVerifyHandActivity, "", ivVideo1)
                ivVideo1.visibility = View.GONE
                ivClose2.visibility = View.GONE
                ivStart.visibility = View.GONE
                tvVideoTip.visibility = View.VISIBLE
                videoUrl = ""
            }
            tvConfirm.setOnClickListener {
                judge()
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(upLoadPresenter, minePresenter, loginPresenter))
        loginPresenter.getUserInfo()
        minePresenter.realNameConfig()
    }

    override fun onGetRealNameConfigSuccess(response: BaseBean<RealNameConfigData>) {
        binding?.apply {
            response.data.apply {
                WebViewInitUtils.initWebView(webView, headMore)
                PicUtils.uploadPic(
                    this@RealVerifyHandActivity,
                    API.URL_HOST_USER_IMAGE + examplePhone,
                    ivExample
                )
                ivExample.tag = API.URL_HOST_USER_IMAGE + examplePhone
                PicUtils.uploadVideo(
                    this@RealVerifyHandActivity,
                    API.URL_HOST_USER_IMAGE + exampleVideo,
                    ivVideo2
                )
                ivVideo2.tag = API.URL_HOST_USER_IMAGE + exampleVideo
            }
        }
    }


    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
//        binding?.etInputPhone?.text = response.data.phone
    }

    private var simpleTipDialog: SimpleTipDialog? = null

    private fun setTipDialog(res: Int, vararg title: String) {
        if (simpleTipDialog != null && simpleTipDialog!!.isShowing) {
            return
        }
        simpleTipDialog = SimpleTipDialog(this)
        simpleTipDialog?.setIcon(res)?.setTitle(title[0])
        simpleTipDialog?.setOnClickBottomListener(object : SimpleTipDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                simpleTipDialog!!.dismiss()
                finish()
            }

            override fun onPositiveClick(view: View?) {
                simpleTipDialog!!.dismiss()
            }
        })?.show()
    }

    private fun judge() {
        binding?.apply {
            val name = etInputName.text.toString().trim()
            val id = etInputId.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShort("请输入您的姓名")
                return
            }
            if (TextUtils.isEmpty(id)) {
                ToastUtils.showShort("请输入您的身份证号码")
                return
            }
            if (TextUtils.isEmpty(picUrl)) {
                ToastUtils.showShort("请上传身份证照片")
                return
            }
            if (TextUtils.isEmpty(videoUrl)) {
                ToastUtils.showShort("请上传视频")
                return
            }
            minePresenter.realNameHead( name, id, picUrl, videoUrl)
        }
    }

    override fun onRealNameHeadSuccess(response: BaseBean<Any>) {
        setTipDialog(R.drawable.tijiaochenggong, "提交成功!")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val res = PictureSelector.obtainMultipleResult(data)
                    // 图片选择结果回调
                    picUrl = if (res[0].isCompressed) res[0].compressPath else res[0].realPath
                    PicUtils.uploadVideo(this, picUrl, binding?.ivPic)
                    binding?.apply {
                        tvPicTip.visibility = View.GONE
                        ivClose1.visibility = View.VISIBLE
                        ivPic.visibility = View.VISIBLE
                    }
                    upLoadPresenter.uploadFile(listOf(picUrl), "idcardPic")
                }
                100 -> {
                    if (data?.getStringExtra(RecordedActivity.INTENT_PATH) != null) {
                        videoUrl = data.getStringExtra(RecordedActivity.INTENT_PATH)
                        Log.d("onActivityResult", "onActivityResult: $videoUrl")
                        PicUtils.uploadPic(this, videoUrl, binding?.ivVideo1)
                    } else {
                        val res = PictureSelector.obtainMultipleResult(data)
                        videoUrl = res[0].realPath
                        // 视频选择结果回调
                        Log.d("onActivityResult", "onActivityResult: 视频选择结果回调")
                        PicUtils.uploadVideo(this, videoUrl, binding?.ivVideo1)
                    }
                    upLoadPresenter.uploadFile(listOf(videoUrl), "idcardVideo")
                    binding?.apply {
                        tvVideoTip.visibility = View.GONE
                        ivClose2.visibility = View.VISIBLE
                        ivStart.visibility = View.VISIBLE
                        ivVideo1.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onUploadFileSuccess(response: BaseBean<FileData>) {
        response.data.apply {
            if (url.contains("mp4")) {
                videoUrl = url
                PicUtils.uploadVideo(
                    this@RealVerifyHandActivity,
                    API.URL_HOST_USER_IMAGE + videoUrl,
                    binding?.ivVideo1
                )
            } else {
                picUrl = url
                PicUtils.uploadPic(this@RealVerifyHandActivity, picUrl, binding?.ivPic)
            }
        }

    }

}