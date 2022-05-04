package com.xz.beecircle.activity.mine

import android.content.Intent
import android.graphics.Rect
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lin.baselib.adapter.GridImageAdapter
import com.lin.baselib.net.API
import com.lin.baselib.utils.*
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityReceiveInfoDetailBinding

class ReceiveInfoDetailActivity : ModuleActivity<ActivityReceiveInfoDetailBinding>(),
    CommonContact.MineView,
    CommonContact.LoginView,
    CommonContact.UploadFileView {
    private var entryType = ""
    private var phone = ""
    private var picUrl = ""
    private var payType = ""
    private lateinit var adapter: GridImageAdapter
    private val upLoadPresenter = Presenter.UpLoadPresenter()
    private val minePresenter = Presenter.MinePresenter()
    private val loginPresenter = Presenter.LoginPresenter()
    private var bean: MyPayWayData? = null

    override fun initView() {
        entryType = intent.getStringExtra("entryType")!!
        bean = intent.getParcelableExtra("bean")

        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            when (entryType) {
                "zfb" -> {
                    title.text = "修改支付宝"
                }
                "wx" -> {
                    title.text = "修改微信"
                }
                "yhk" -> {
                    title.text = "修改银行卡"
                }
            }
        }

        binding?.apply {

            initRecyclerView()

            if (bean != null) {
                bean?.apply {
                    etInputName.setText(payeenme)
                    when (entryType) {
                        "zfb" -> {
                            etInputAccount.setText(account)
                        }
                        "wx" -> {
                            etInputAccount.setText(account)
                            val localMedia = LocalMedia(
                                API.URL_HOST_USER_IMAGE + qrcode,
                                0,
                                PictureMimeType.ofImage(),
                                "image/jpeg"
                            )
                            picUrl = qrcode
                            adapter.data.add(localMedia)
                            adapter.notifyDataSetChanged()
                        }
                        "yhk" -> {
                            etInputBank1.setText(bankname)
                            etInputBank2.setText(branchname)
                            val sb = StringBuffer()
                            for (i in account.split("").indices) {
                                sb.append(account.split("")[i])
                                if (i % 4 == 0) sb.append(" ")
                            }
                            etInputAccount.setText(sb.toString().trim())
                        }
                    }
                }
            }
            when (entryType) {
                "zfb" -> {
                    payType = "0"
                }
                "wx" -> {
                    payType = "1"
                    llCode.visibility = View.VISIBLE
                    etInputAccount.hint = "请输入您本人微信账号"
                }
                "yhk" -> {
                    payType = "2"
                    llBank.visibility = View.VISIBLE
                    tvAccountText.text = "银行卡号："
                    etInputAccount.hint = "请输入您本人银行卡号"
                }
            }

            tvGetVerifyCode.setOnClickListener {
                loginPresenter.getSSMCode(phone, "qrcode")
            }
            tvConfirm.setOnClickListener {
                summit()
            }
        }

    }

    override fun onGetSSMCode(response: BaseBean<Any>) {
        ToastUtils.showShort(response.message)
        setTime()
    }

    override fun initDate() {
        initPresenter(listOf(upLoadPresenter, minePresenter, loginPresenter))
        loginPresenter.getUserInfo()
    }

    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
        this.phone = response.data.phone
        binding?.etInputName?.text = response.data.realname
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        adapter = GridImageAdapter(this) {
            PicUtils.beginCameraDialog(
                this, PictureConfig.CHOOSE_REQUEST,
                1 - adapter.data.size
            )
        }
        val manager = FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        adapter.setSelectMax(1)
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                //outRect就是表示在item的上下左右所撑开的距离,默认值为0
                val leftOrRight = DensityUtil.dip2px(this@ReceiveInfoDetailActivity, 15f)
                outRect[0, leftOrRight, 0] = 0
            }
        })
        adapter.setOnItemClickListener { _, position ->
            if (adapter.data.size > 0) {
                val media: LocalMedia = adapter.data[position]
                val pictureType = media.mimeType
                when (PictureMimeType.getMimeType(pictureType)) {
                    1 ->                             // 预览图片 可自定长按保存路径
                        //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                        if (!media.path.startsWith("http")) {
                            PictureSelector.create(this).themeStyle(R.style.picture_default_style)
                                .openExternalPreview(position, adapter.data)
                        } else {
                            PictureSelector.create(this)
                                .themeStyle(R.style.picture_default_style)
                                .isNotPreviewDownload(true)
                                .imageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                                .openExternalPreview(position, adapter.data)
                        }
                    2 ->                             // 预览视频
                        PictureSelector.create(this).externalPictureVideo(media.path)
                    3 ->                             // 预览音频
                        PictureSelector.create(this).externalPictureAudio(media.path)
                }
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
                    val localMedia = res[0]
                    upLoadPresenter.uploadFile(
                        mutableListOf(if (localMedia.isCompressed) localMedia.compressPath else localMedia.realPath),
                        "qrcode"
                    )
                }
            }
        }
    }

    override fun onUploadFileSuccess(response: BaseBean<FileData>) {
        picUrl = response.data.url
        val media =
            LocalMedia(API.URL_HOST_USER_IMAGE + picUrl, 0, PictureMimeType.ofImage(), "image/jpeg")
        adapter.data.clear()
        adapter.data.add(media)
        adapter.notifyDataSetChanged()
    }

    private fun summit() {
        binding?.apply {
            val name = etInputName.text.toString().trim()
            val account = etInputAccount.text.toString().trim()
            val bank1 = etInputBank1.text.toString().trim()
            val bank2 = etInputBank2.text.toString().trim()
            val verifyCode = etInputVerifyCode.text.toString().trim()
            when (entryType) {
                "zfb" -> {
                    if (TextUtils.isEmpty(account)) {
                        ToastUtils.showShort("请输入您本人支付宝账号")
                        return
                    }
                    if (judgeCode(verifyCode)) return
                    minePresenter.setCollection(payType, account, "", "", name, "", verifyCode)
                }
                "wx" -> {
                    if (TextUtils.isEmpty(account)) {
                        ToastUtils.showShort("请输入您本人微信账号")
                        return
                    }
                    if (TextUtils.isEmpty(picUrl)) {
                        ToastUtils.showShort("请添加微信收款二维码")
                        return
                    }
                    if (judgeCode(verifyCode)) return
                    minePresenter.setCollection(payType, account, "", "", name, picUrl, verifyCode)

                }
                "yhk" -> {
                    if (TextUtils.isEmpty(account)) {
                        ToastUtils.showShort("请输入您本人银行卡号")
                        return
                    }
                    if (TextUtils.isEmpty(bank1)) {
                        ToastUtils.showShort("请输入开户银行名称")
                        return
                    }
                    if (judgeCode(verifyCode)) return
                    minePresenter.setCollection( payType, account, bank1, bank2, name, "", verifyCode)
                }
            }

        }
    }

    override fun onSetCollectionSuccess(response: BaseBean<Any>) {
        if (bean != null) ToastUtils.showShort("保存成功") else ToastUtils.showShort("提交成功")
        finish()
    }


    private fun judgeCode(verifyCode: String): Boolean {
        if (TextUtils.isEmpty(verifyCode) || verifyCode.length != 6) {
            ToastUtils.showShort("请输入验证码")
            return true
        }
        return false
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