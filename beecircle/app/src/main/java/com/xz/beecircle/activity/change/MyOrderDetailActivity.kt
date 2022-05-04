package com.xz.beecircle.activity.change

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.ycbjie.ycstatusbarlib.utils.StatusBarUtils
import com.lin.baselib.adapter.GridImageAdapter
import com.lin.baselib.net.API
import com.lin.baselib.utils.*
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.xz.beecircle.*
import com.xz.beecircle.Constant.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityMyOrderDetailBinding

class MyOrderDetailActivity : ModuleActivity<ActivityMyOrderDetailBinding>(),
    CommonContact.ChangeView {
    private var entryType: Int? = null
    private var orderId: String? = null
    private lateinit var adapter: GridImageAdapter
    private lateinit var adapter2: GridImageAdapter
    private var changerPresenter = Presenter.ChangerPresenter()

    override fun initView() {
        setStatusBarColor(R.color.yellow2)
        StatusBarUtils.StatusBarLightMode(this)

        addTitleBar {
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = "订单详情"
            val llTitle = it.findViewById<RelativeLayout>(R.id.ll_title)
            llTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow2))
        }

        entryType = intent.getIntExtra("entryType", 0)
        orderId = intent.getStringExtra("orderId")

        initRecyclerView1()
        initRecyclerView2()

        setView()
    }


    override fun initDate() {
        initPresenter(listOf(changerPresenter))
        changerPresenter.getOrderDetails(orderId)
    }

    override fun onGetOrderDetailSuccess(response: BaseBean<OrderDetailData>) {
        binding?.apply {
            response.data.apply {
                tradeDet.apply {
                    tvCountValue.text = num
                    tvUnitValue.text = "$unitprice CNY"
                    tvTotal.text = "$totalprice CNY"
                    if (Arith.sub(surplustime, "0").toLong() > 0) {
                        setTime(tvCountDown, surplustime.toLong())
                    }
                    when (paytype) {
                        0 -> {//支付宝
                            ivZfb.visibility = View.VISIBLE
                            tvWayValue.text = "支付宝"
                        }
                        1 -> {//微信
                            ivWx.visibility = View.VISIBLE
                            tvWayValue.text = "微信"
                            llReceiveCode.visibility = View.VISIBLE
                            tvReceiveCode.tag = qrcode
                        }
                        2 -> {//银行卡
                            ivYhk.visibility = View.VISIBLE
                            tvWayValue.text = "银行卡"
                            llBank1.visibility = View.VISIBLE
                            llBank2.visibility = View.VISIBLE
                            tvBank1.text = bankname
                            tvBank2.text = branchname
                        }
                    }
                    when (entryType) {
                        0, 1, 2, 3 -> {//卖出
                            tvBuyerOrSellerValue.text = buyeruid
                        }
                        else -> {//买入
                            tvBuyerOrSellerValue.text = selleruid
                        }
                    }
                    tvReceiver.text = payeenme
                    tvReceiveAccount.text = account
                    tvOrderId.text = orderId
                    tvOrderTime.text = creattime
                    tvPayTime.text = paytime
                    tvFinishTime.text = overtime
                }
                if (entryType == MY_ORDER_PUBLISH_SSZ || entryType == MY_ORDER_SELL_SSZ) {
                    for (index in appealDet.indices) {
                        setAdapter(this, index)
                    }
                    val uid = SharedPreferencesUtils.getString(this@MyOrderDetailActivity, "uid", "")
                    var canApply = false
                    if (appealDet.isNotEmpty()){
                        if (uid == appealDet[0].type) canApply = true
                    }
                    if (canApply) tvRight.visibility = View.VISIBLE else tvRight.visibility = View.GONE
                }
            }
        }
    }

    private var timer: CountDownTimer? = null

    private fun setTime(view: TextView, time: Long) {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view.text = "倒计时：${TimeUtils.secondToHMS(millisUntilFinished / 1000)}"
            }

            override fun onFinish() {
                finish()
            }
        }
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer!!.start()
    }

    private fun ActivityMyOrderDetailBinding.setAdapter(
        orderDetailData: OrderDetailData,
        index: Int
    ) {
        val appealDet1 = orderDetailData.appealDet[index]
        val uid = SharedPreferencesUtils.getString(this@MyOrderDetailActivity, "uid", "")
        appealDet1.apply {
            if (uid == appealDet1.type) {
                llApply1.visibility = View.VISIBLE
                tvApplyTime1.text = creattime
                tvApplyReason1.text = text
                val split = imgs.split(",")
                for (item in split) {
                    val localMedia = LocalMedia(
                        API.URL_HOST_USER_IMAGE + item,
                        0,
                        PictureMimeType.ofImage(),
                        "image/jpeg"
                    )
                    adapter.data.add(localMedia)
                }
                adapter.setSelectMax(split.size)
                adapter.notifyDataSetChanged()
            } else {
                llApply2.visibility = View.VISIBLE
                tvApplyTime2.text = creattime
                tvApplyReason2.text = text
                val split = imgs.split(",")
                for (item in split) {
                    val localMedia = LocalMedia(
                        API.URL_HOST_USER_IMAGE + item,
                        0,
                        PictureMimeType.ofImage(),
                        "image/jpeg"
                    )
                    adapter2.data.add(localMedia)
                }
                adapter2.setSelectMax(split.size)
                adapter2.notifyDataSetChanged()
            }
        }
    }

    private fun setView() {
        binding?.apply {
            when (entryType) {
                MY_ORDER_SELL_DSK -> {
                    tvRight.visibility = View.GONE
                }
                MY_ORDER_SELL_DQR -> {
                    tvStatus.text = "待确认"
                    llBtn.visibility = View.VISIBLE
                }
                MY_ORDER_SELL_SSZ -> {
                    tvStatus.text = "申诉中"
                    ivStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@MyOrderDetailActivity,
                            R.drawable.shensuzhong
                        )
                    )
                    tvCountDown.visibility = View.GONE
                    llPayTime.visibility = View.VISIBLE
//                    llApply1.visibility = View.VISIBLE
//                    llApply2.visibility = View.VISIBLE
                    tvLeft1.text = "未解决"
                    tvLeft2.visibility = View.VISIBLE
                    tvLeft2.text = "继续申诉"
                    tvRight.text = "已解决"
                }
                MY_ORDER_SELL_YWC -> {
                    tvStatus.text = "已完成"
                    ivStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@MyOrderDetailActivity,
                            R.drawable.wancheng
                        )
                    )
                    tvCountDown.visibility = View.GONE
                    llPayTime.visibility = View.VISIBLE
                    llFinishTime.visibility = View.VISIBLE
                    llBtn.visibility = View.GONE
                }
                MY_ORDER_PUBLISH_DFK -> {
                    tvStatus.text = "待付款"
                    tvCountKey.text = "求购数量："
                    tvUnitKey.text = "求购单价："
                    tvWayKey.text = "支付方式"
                    tvBuyerOrSellerKey.text = "卖家"
                    tvRight.visibility = View.VISIBLE
                    tvRight.text = "已完成付款"
                    tvLeft1.text = "申诉"
                }
                MY_ORDER_PUBLISH_DSH -> {
                    tvStatus.text = "待收货"
                    ivStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@MyOrderDetailActivity,
                            R.drawable.daishouhuo
                        )
                    )
                    tvCountKey.text = "求购数量："
                    tvUnitKey.text = "求购单价："
                    tvWayKey.text = "支付方式"
                    tvBuyerOrSellerKey.text = "卖家"
                    llBtn.visibility = View.GONE
                }
                MY_ORDER_PUBLISH_SSZ -> {
                    tvStatus.text = "申诉中"
                    ivStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@MyOrderDetailActivity,
                            R.drawable.shensuzhong
                        )
                    )
                    tvCountKey.text = "求购数量："
                    tvUnitKey.text = "求购单价："
                    tvWayKey.text = "支付方式"
                    tvBuyerOrSellerKey.text = "卖家"
                    tvCountDown.visibility = View.GONE
                    llPayTime.visibility = View.VISIBLE
//                    llApply1.visibility = View.VISIBLE
//                    llApply2.visibility = View.VISIBLE
                    tvApplyTitle2.text = "卖家申诉内容"
                    tvLeft1.text = "未解决"
                    tvLeft2.visibility = View.VISIBLE
                    tvLeft2.text = "继续申诉"
                    tvRight.text = "已解决"
                }
                MY_ORDER_PUBLISH_YWC -> {
                    tvStatus.text = "已完成"
                    ivStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@MyOrderDetailActivity,
                            R.drawable.wancheng
                        )
                    )
                    tvCountKey.text = "求购数量："
                    tvUnitKey.text = "求购单价："
                    tvWayKey.text = "支付方式"
                    tvBuyerOrSellerKey.text = "卖家"
                    tvCountDown.visibility = View.GONE
                    llPayTime.visibility = View.VISIBLE
                    llFinishTime.visibility = View.VISIBLE
                    llBtn.visibility = View.GONE
                }
            }

            tvReceiveAccount.setOnClickListener {
                SaveQRCodeUtils.copy(
                    this@MyOrderDetailActivity,
                    tvReceiveAccount.text.toString().trim()
                )
            }

            tvOrderId.setOnClickListener {
                SaveQRCodeUtils.copy(
                    this@MyOrderDetailActivity,
                    tvOrderId.text.toString().trim()
                )
            }

            tvReceiveCode.setOnClickListener {
                showCheckDialog(tvReceiveCode.tag as String)
            }

            llLeft.setOnClickListener {
                when (tvLeft1.text) {
                    "申诉", "未解决" -> {
                        startActivity(
                            MyOrderApplyActivity::class.java,
                            hashMapOf("orderId" to orderId, "entryType" to entryType),
                            100
                        )
                    }
                }
            }
            tvRight.setOnClickListener {
                when (tvRight.text) {
                    "已收款" -> {
                        changerPresenter.finishOver(orderId)
                    }
                    "已解决" -> {
                        changerPresenter.finishAppeal(
                            SharedPreferencesUtils.getString(
                                this@MyOrderDetailActivity,
                                "uid",
                                ""
                            ), orderId
                        )
                    }
                    "已完成付款" -> {
                        changerPresenter.finishPay(orderId)
                    }
                }
            }
        }
    }

    override fun onFinishAppealSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("解决成功")
        finish()
    }

    override fun onFinishOrderSuccess(response: BaseBean<Any>) {
        if (binding?.tvRight?.text == "已解决") ToastUtils.showShort("成功解决")
        else ToastUtils.showShort("付款成功")
        finish()
    }

    override fun onFinishPaySuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("收款成功")
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) finish()
    }

    private fun showCheckDialog(url: String) {
        val dialog = AlertDialog.Builder(this).create()
        dialog.show()
        val window = dialog.window
        if (window != null) {
            window.setContentView(R.layout.dialog_check_code)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //修改动画样式
            //设置属性
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            //弹出一个窗口，让背后的窗口变暗一点
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            //dialog背景层
            params.dimAmount = 0.5f
            window.attributes = params
            val rlCode = window.findViewById<View>(R.id.rl_code) as CardView
            val layoutParams = rlCode.layoutParams
            layoutParams.height =
                QMUIDisplayHelper.getScreenWidth(this) - QMUIDisplayHelper.dp2px(this, 120)
            layoutParams.width =
                QMUIDisplayHelper.getScreenWidth(this) - QMUIDisplayHelper.dp2px(this, 120)
            rlCode.layoutParams = layoutParams
            val ivCode = window.findViewById<View>(R.id.iv_code) as ImageView
            PicUtils.uploadPic(this, API.URL_HOST_USER_IMAGE + url, ivCode)
            val ivClose = window.findViewById<View>(R.id.iv_close) as ImageView
            ivClose.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView1() {
        adapter = GridImageAdapter(this, false)
        val manager = FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerView1.layoutManager = manager
        binding.recyclerView1.adapter = adapter
        binding.recyclerView1.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                //outRect就是表示在item的上下左右所撑开的距离,默认值为0
                val leftOrRight = DensityUtil.dip2px(this@MyOrderDetailActivity, 10f)
                outRect[0, leftOrRight, leftOrRight] = 0
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
                            PictureSelector.create(this)
                                .themeStyle(R.style.picture_default_style)
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

    /**
     * 初始化列表
     */
    private fun initRecyclerView2() {
        adapter2 = GridImageAdapter(this, false)
        val manager = FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerView2.layoutManager = manager
        binding.recyclerView2.adapter = adapter2
        binding.recyclerView2.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                //outRect就是表示在item的上下左右所撑开的距离,默认值为0
                val leftOrRight = DensityUtil.dip2px(this@MyOrderDetailActivity, 10f)
                outRect[0, leftOrRight, leftOrRight] = 0
            }
        })
        adapter2.setOnItemClickListener { _, position ->
            if (adapter2.data.size > 0) {
                val media: LocalMedia = adapter2.data[position]
                val pictureType = media.mimeType
                when (PictureMimeType.getMimeType(pictureType)) {
                    1 ->                             // 预览图片 可自定长按保存路径
                        //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                        if (!media.path.startsWith("http")) {
                            PictureSelector.create(this)
                                .themeStyle(R.style.picture_default_style)
                                .openExternalPreview(position, adapter2.data)
                        } else {
                            PictureSelector.create(this)
                                .themeStyle(R.style.picture_default_style)
                                .isNotPreviewDownload(true)
                                .imageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                                .openExternalPreview(position, adapter2.data)
                        }
                    2 ->                             // 预览视频
                        PictureSelector.create(this).externalPictureVideo(media.path)
                    3 ->                             // 预览音频
                        PictureSelector.create(this).externalPictureAudio(media.path)
                }
            }
        }
    }
}