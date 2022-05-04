package com.xz.beecircle.activity.ecosphere

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lin.baselib.utils.*
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.xz.beecircle.*
import com.xz.beecircle.Constant.WEB_TYPE_BEE_BOOK
import com.xz.beecircle.activity.home.NoticeListActivity
import com.xz.beecircle.activity.login.PortraitActivity
import com.xz.beecircle.adapter.BroadCastAdapter2
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBeeMine2Binding
import com.xz.beecircle.widget.MinerDialog
import com.xz.beecircle.widget.NoticeDialog
import com.youth.banner.Banner

class BeeMine2Activity : ModuleActivity<ActivityBeeMine2Binding>(), CommonContact.HomeView,
        CommonContact.EcosphereView, CommonContact.LoginView {
    private var homePresenter: Presenter.HomePresenter = Presenter.HomePresenter()
    private var ecospherePresenter: Presenter.EcospherePresenter = Presenter.EcospherePresenter()
    private var loginPresenter: Presenter.LoginPresenter = Presenter.LoginPresenter()
    private var beeBalance: String? = null
    private var beeRate: String? = null

    override fun initView() {
        setStatusBarColor(R.color.white)

        addTitleBar {
            val llTitle = it.findViewById<RelativeLayout>(R.id.ll_title)
            llTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            val tvTitle = it.findViewById<TextView>(R.id.tv_title)
            val tvTitleRight = it.findViewById<TextView>(R.id.tv_title_right)
            tvTitle.text = "蜂蜜矿场"
            tvTitleRight.text = "白皮书"
            tvTitleRight.setTextColor(ContextCompat.getColor(this, R.color.text))
            val drawable = ContextCompat.getDrawable(this, R.drawable.baipishu)
            drawable?.setBounds(0, 0, DensityUtil.dip2px(this, 22f), DensityUtil.dip2px(this, 16f))
            tvTitleRight.compoundDrawablePadding = DensityUtil.dip2px(this, 5f)
            tvTitleRight.setCompoundDrawables(drawable, null, null, null)
            tvTitleRight.setOnClickListener {
                startActivity(PortraitActivity::class.java, hashMapOf("entryType" to WEB_TYPE_BEE_BOOK))
            }
        }

        binding?.apply {
//            val layoutParams = ivGif.layoutParams
//            layoutParams.height= QMUIDisplayHelper.getScreenWidth(this@BeeMine2Activity)
//            ivGif.layoutParams = layoutParams
            PicUtils.uploadGif(this@BeeMine2Activity, R.raw.mine, ivGif)

            ivCheck.setOnClickListener {
                startActivity(NoticeListActivity::class.java)
            }
            llNotice.setOnClickListener {
                startActivity(NoticeListActivity::class.java)
            }
            rlPick.setOnClickListener {
                ecospherePresenter.receivedMine(SharedPreferencesUtils.getString(this@BeeMine2Activity, "uid", ""))
            }
            llMyExam.setOnClickListener {
                startActivity(MyExamActivity::class.java, hashMapOf("myExam" to tvMyExam.text.toString().trim()))
            }
            llMyMiner.setOnClickListener {
                val wokers = tvMyMiner.text.toString().trim()
                startActivity(MyMinerActivity::class.java,hashMapOf("wokers" to wokers.substring(0,wokers.length-1),
                "exam" to tvMyExam2.text.toString().trim()))
            }

            llMyAsset.setOnClickListener {
                startActivity(MyAssetActivity::class.java, hashMapOf("myAsset" to tvMyAsset.text.toString().trim()))
            }
            llAdToPower.setOnClickListener {
                homePresenter.getAd()
            }

            llBeeToPower.setOnClickListener {
                beeBalance?.let { it1 -> setTipDialog("0", it1, "6") }
            }
        }

        //TODO 暂时关闭
//        setNotice("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2496571732,442429806&fm=26&gp=0.jpg")
//        setTipDialog("20", "200", "6")
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "onResume: HomeFragment")
        if (adDialog != null && adDialog?.isShowing == true) {
            adDialog?.dismiss()
            ecospherePresenter.addPowerByAD(SharedPreferencesUtils.getString(this, "uid", ""))
        }
    }

    override fun initDate() {
        initPresenter(listOf(homePresenter, ecospherePresenter, loginPresenter))
        ecospherePresenter.getMineConfig()
        ecospherePresenter.getCountPowerConfig(SharedPreferencesUtils.getString(this, "uid", ""))
        ecospherePresenter.notReceivedMine(SharedPreferencesUtils.getString(this, "uid", ""))
        homePresenter.getNoticeList(1, 20, 1, 0)
        loginPresenter.getUserInfo()
    }

    override fun onGetNoticeListSuccess(response: BaseBean<List<NoticeListData>>) {
        initBanner2(response.data)
    }

    override fun onAddPowerByHoneySuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("兑换成功")
        //TODO
        //刷新时间
        ecospherePresenter.getCountPowerConfig(SharedPreferencesUtils.getString(this, "uid", ""))
    }

    override fun onAddPowerByADSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("获取成功")
        //TODO
        //刷新时间
        ecospherePresenter.getCountPowerConfig(SharedPreferencesUtils.getString(this, "uid", ""))
    }

    override fun onGetNotReceivedMineSuccess(response: BaseBean<NotReceivedMineDate>) {
        binding?.apply {
            response.data.apply {
                tvTip.text = "(未领取区块奖励超过${timoouthour}小时，自动暂停参与挖矿）"
                rlPick.isEnabled = notgetsize != "0"
            }
        }
    }

    override fun onGetMineConfigSuccess(response: BaseBean<MineConfigData>) {
        binding?.apply {
            response.data.apply {
                tvTotal.text = if (Arith.sub(totalSize, "10000").toFloat() > 0) Arith.round(Arith.div(totalSize, "10000"), 0) + "万枚" else totalSize
                setTime(time)
                tvTotalNum.text = countPower
                tvCurrentCoin.text = currencySize
                tvHeight.text = height
                tvIncome.text = "$profit M/块"
                ecospherePresenter.notReceivedMine(SharedPreferencesUtils.getString(this@BeeMine2Activity, "uid", ""))
            }
        }
    }

    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
        beeBalance = response.data.honey
    }

    override fun onGetCountPowerConfigSuccess(response: BaseBean<CountPowerConfigData>) {
        binding?.apply {
            response.data.apply {
                tvMyExam.text = myPower
                tvMyMiner.text = "${miner.userSize}人"
                tvMyExam2.text = miner.MinerPower
                tvMyAsset.text = myBtf + "BTF"
                tvAdIncomeKey.text = "看广告获取算力（${ad.myWatch}/${ad.max})"
                tvAdIncomeValue.text = "${ad.getPower}/广告\n有效期${ad.outDay}天"
                tvBeeToExam.text = "${honey.payHoney}蜂蜜=${honey.getPower}\n最高兑换${honey.maxPower}"
                var power = honey.getPower
                if (honey.getPower.contains("K")){
                    power = Arith.mul(power.substring(0,power.length-1),"1000")
                }
                beeRate = Arith.div(honey.payHoney,power)
            }
        }
    }

    override fun onReceivedMineSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("领取成功")
        ecospherePresenter.notReceivedMine(SharedPreferencesUtils.getString(this, "uid", ""))
    }

    override fun onGetAdSuccess(response: BaseBean<AdData>) {
        response.data.apply {
//            url = "http://vfx.mtime.cn/Video/2019/07/12/mp4/190712140656051701.mp4"
//            if (type == "png") {
                setAdNotice(url, type)
//            } else {
//                Thread {
//                    val bitmap = NoticeDialog.getVideoThumb(url)
//                    run {
//                        val msg = handler.obtainMessage()
//                        //一般都是规定好一个Int的常量，来表示what
//                        msg.what = 1
//                        //传递Int数据
//                        msg.obj = bitmap
//                        val b = Bundle()
//                        b.putString("url", url)
//                        b.putString("type", "type")
//                        msg.data = b
//                        handler.sendMessage(msg)
//                    }
//                }.start()
//            }
        }
    }

//    private val handler = @SuppressLint("HandlerLeak")
//    object : Handler() {
//        override fun handleMessage(msg: Message) {
//            if (msg.what == 1) {
//                val data = msg.data
//                setAdNotice(data["url"] as String, data["type"] as String, msg.obj as Bitmap)
//            }
//        }
//    }

    private var timer: CountDownTimer? = null

    private fun setTime(time: String) {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        timer = object : CountDownTimer(time.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvOutTime.text = TimeUtils.secondToMS(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                ecospherePresenter.getMineConfig()
            }
        }
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer!!.start()
    }

    private var adDialog: NoticeDialog? = null

    private fun setAdNotice(url: String, type: String) {
        if (adDialog != null && adDialog!!.isShowing) {
            return
        }
        adDialog = NoticeDialog(this)
        adDialog?.setOnClickBottomListener(object : NoticeDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                adDialog!!.dismiss()
                ecospherePresenter.addPowerByAD(SharedPreferencesUtils.getString(this@BeeMine2Activity, "uid", ""))
            }

            override fun onPositiveClick(view: View?) {
            }
        })?.setUrl(url)?.setType(type)?.show()
    }

    private var minerDialog: MinerDialog? = null

    private fun setTipDialog(vararg value: String) {
        if (minerDialog != null && minerDialog!!.isShowing) {
            return
        }
        minerDialog = MinerDialog(this)
        minerDialog?.setNeedBee(value[0])?.setBalanceBee(value[1])?.setTip(value[2])?.setRate(beeRate)
                ?.setOnClickBottomListener(object : MinerDialog.OnClickBottomListener {
                    override fun onNegativeClick(view: View?) {
                        minerDialog!!.dismiss()
                    }

                    override fun onPositiveClick(view: View?) {
                        minerDialog!!.dismiss()
                        ecospherePresenter.addPowerByHoney(SharedPreferencesUtils.getString(this@BeeMine2Activity,
                                "uid", ""), view?.tag as String)
                    }
                })?.show()
    }

    private var noticeDialog: NoticeDialog? = null

    private fun setNotice(url: String) {
        if (noticeDialog != null && noticeDialog!!.isShowing) {
            return
        }
        noticeDialog = NoticeDialog(this)
        noticeDialog?.setOnClickBottomListener(object : NoticeDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                noticeDialog!!.dismiss()
            }

            override fun onPositiveClick(view: View?) {
            }
        })?.setUrl(url)?.show()
    }

    /**
     * 初始化广播
     *
     * @param datas
     */
    private fun initBanner2(datas: List<NoticeListData>) {
        binding?.apply {
            banner2.setOrientation(Banner.VERTICAL)
                    .addBannerLifecycleObserver(this@BeeMine2Activity) //添加生命周期观察者
                    .setAdapter(BroadCastAdapter2(this@BeeMine2Activity, datas))
                    .setOnBannerListener { _: Any?, i ->
                        startActivity(PortraitActivity::class.java,
                                hashMapOf("entryType" to Constant.WEB_TYPE_NOTICE,
                                        "bean" to datas[i]))
                    }
                    .start()
        }
    }

}