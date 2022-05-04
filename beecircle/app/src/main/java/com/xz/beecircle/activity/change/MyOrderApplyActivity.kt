package com.xz.beecircle.activity.change

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lin.baselib.adapter.GridImageAdapter
import com.lin.baselib.utils.*
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityMyOrderApplyBinding
import org.greenrobot.eventbus.EventBus

class MyOrderApplyActivity : ModuleActivity<ActivityMyOrderApplyBinding>(), CommonContact.UploadFileView, CommonContact.ChangeView {
    private var orderId: String? = null
    private var entryType: String? = null
    private var reason: String? = null
    private lateinit var adapter: GridImageAdapter
    private var upLoadPresenter = Presenter.UpLoadPresenter()
    private var changerPresenter = Presenter.ChangerPresenter()

    override fun initView() {
        orderId = intent.getStringExtra("orderId")
        entryType = intent.getStringExtra("entryType")

        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "申诉"
        }

        initRecyclerView()

        binding.apply {
            tvSummitApply.setOnClickListener {
                reason = etInput.text.toString().trim()
                if (TextUtils.isEmpty(reason)) {
                    ToastUtils.showShort("请输入您的申诉理由")
                    return@setOnClickListener
                }
                if (adapter.data.size == 0) {
                    ToastUtils.showShort("请上传您的申诉凭证")
                    return@setOnClickListener
                }
                val list = mutableListOf<String>()
                for (item in adapter.data) {
                    list.add(if (item.isCompressed) item.compressPath else item.realPath)
                }
                upLoadPresenter.uploadFile(list,"appeal")
            }
        }
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        adapter = GridImageAdapter(this) {
            PicUtils.beginCameraDialog(this, PictureConfig.CHOOSE_REQUEST,
                    5 - adapter.data.size)
        }

//        val localMedia = LocalMedia("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1730713693,2130926401&fm=26&gp=0.jpg", 0, PictureMimeType.ofImage(), "image/jpeg")
//        adapter.setList(MutableList(5) { i -> localMedia })
        val manager = FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        adapter.setSelectMax(5)
        binding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                //outRect就是表示在item的上下左右所撑开的距离,默认值为0
                val leftOrRight = DensityUtil.dip2px(this@MyOrderApplyActivity, 15f)
                outRect[leftOrRight, leftOrRight, 0] = 0
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
                            PictureSelector.create(this).themeStyle(R.style.picture_default_style).openExternalPreview(position, adapter.data)
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
                    val data1 = adapter.data
                    data1.addAll(res)
                    adapter.setList(data1)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(changerPresenter, upLoadPresenter))
    }

    private var count = 0
    private var sb: StringBuffer = StringBuffer()
    override fun onUploadFileSuccess(response: BaseBean<FileData>) {
        count++
        if (count == 1) {
            sb = StringBuffer()
        }
        sb.append(response.data.url)
        if (count != adapter.data.size) {
            sb.append(",")
        } else {
            changerPresenter.appealOrder(orderId, reason, sb.toString().trim(), SharedPreferencesUtils.getString(this, "uid", ""))
        }
    }

    override fun onAppealOrderSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("申诉成功")
        setResult(RESULT_OK,intent)
        finish()
    }
}