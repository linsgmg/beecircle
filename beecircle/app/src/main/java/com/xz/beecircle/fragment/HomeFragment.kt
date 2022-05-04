package com.xz.beecircle.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.TextView
import com.lin.baselib.utils.*
import com.xz.beecircle.*
import com.xz.beecircle.Constant.*
import com.xz.beecircle.activity.home.BeeCenterActivity
import com.xz.beecircle.activity.home.InviteFriendActivity
import com.xz.beecircle.activity.home.MyTeamActivity
import com.xz.beecircle.activity.home.NoticeListActivity
import com.xz.beecircle.activity.login.PortraitActivity
import com.xz.beecircle.adapter.BroadCastAdapter2
import com.xz.beecircle.base.ModuleFragment
import com.xz.beecircle.databinding.FragmentHomeBinding
import com.xz.beecircle.widget.NoticeDialog
import com.youth.banner.Banner
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class HomeFragment : ModuleFragment<FragmentHomeBinding>(), CommonContact.HomeView {
    private var homePresenter: Presenter.HomePresenter = Presenter.HomePresenter()
    private var notReceivedHoneyData: List<NotReceivedHoneyData>? = null
    private var pickBallIndex = -1

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "onResume: HomeFragment")
        if (noticeDialog != null && noticeDialog?.isShowing == true) {
            noticeDialog?.dismiss()
//            homePresenter.receiveHoney(
//                SharedPreferencesUtils.getString(context, "uid", ""),
//                notReceivedHoneyData?.get(pickBallIndex)?.keyWord
//            )
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetMessage(message: MainChangeBean) {
        if (message.index == 0) {
            Log.d("onGetMessage", "onGetMessage: HomeFragment")
            homePresenter.getNoticeList(1, 20, 0, 0)
            homePresenter.getNotReceiveHoneyList(
                SharedPreferencesUtils.getString(
                    context,
                    "uid",
                    ""
                )
            )
        }
    }

    override fun initView() {
        Log.d("onResume", "initView: HomeFragment")

        binding?.apply {
            PicUtils.uploadGif(context, R.raw.homebg, ivBg)
            tvBall1.setOnClickListener {
                receiveHoney(0)
            }
            tvBall2.setOnClickListener {
                receiveHoney(1)
            }
            tvBall3.setOnClickListener {
                receiveHoney(2)
            }
            tvBall4.setOnClickListener {
                receiveHoney(3)
            }
            ivCheck.setOnClickListener {
                startActivity(NoticeListActivity::class.java)
            }
            llNoticeCenter.setOnClickListener {
                startActivity(NoticeListActivity::class.java)
            }
            llBeeCenter.setOnClickListener {
                startActivity(BeeCenterActivity::class.java)
            }
            llMyTeam.setOnClickListener {
                startActivity(MyTeamActivity::class.java)
            }
            llInviteFriend.setOnClickListener {
                startActivity(InviteFriendActivity::class.java)
            }
        }
        //TODO 暂时关闭
//        setNotice("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2496571732,442429806&fm=26&gp=0.jpg")

    }

    private fun receiveHoney(it: Int) {
        pickBallIndex = it
        homePresenter.getAd()
    }

    private var noticeDialog: NoticeDialog? = null

    private fun setNotice(url: String, type: String, bitmap: Bitmap? = null) {
        if (noticeDialog != null && noticeDialog!!.isShowing) {
            return
        }
        noticeDialog = NoticeDialog(context)
        noticeDialog?.setOnClickBottomListener(object : NoticeDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                noticeDialog!!.dismiss()
                homePresenter.receiveHoney(
                    SharedPreferencesUtils.getString(context, "uid", ""),
                    notReceivedHoneyData?.get(pickBallIndex)?.keyWord
                )
            }

            override fun onPositiveClick(view: View?) {
            }
        })?.setUrl(url)?.setType(type)?.setBitmap(bitmap)?.show()
    }

    /**
     * 初始化广播
     *
     * @param datas
     */
    private fun initBanner2(datas: List<NoticeListData>) {
        binding?.apply {
            banner2.setOrientation(Banner.VERTICAL)
                .addBannerLifecycleObserver(this@HomeFragment) //添加生命周期观察者
                .setAdapter(BroadCastAdapter2(context, datas))
                .setOnBannerListener { _: Any?, i ->
                    startActivity(
                        PortraitActivity::class.java,
                        hashMapOf(
                            "entryType" to WEB_TYPE_NOTICE,
                            "bean" to datas[i]
                        )
                    )
                }.start()
        }
    }

    override fun initDate() {
        initPresenter(listOf(homePresenter))
        homePresenter.getNoticeList(1, 20, 0, 0)
        homePresenter.getNotReceiveHoneyList(SharedPreferencesUtils.getString(context, "uid", ""))
        EventBus.getDefault().postSticky(MainChangeBean(0))
    }

    override fun onReceiveHoneySuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("领取成功")
        homePresenter.getNotReceiveHoneyList(SharedPreferencesUtils.getString(context, "uid", ""))
    }

    override fun onGetNoticeListSuccess(response: BaseBean<List<NoticeListData>>) {
        initBanner2(response.data)
    }

    override fun onGetAdSuccess(response: BaseBean<AdData>) {
        response.data.apply {
            setNotice(url, type)
        }
    }

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 1) {
                val data = msg.data
                setNotice(data["url"] as String, data["type"] as String, msg.obj as Bitmap)
            }
        }
    }

    override fun onGetNotReceiveListSuccess(response: BaseBean<List<NotReceivedHoneyData>>) {
        response.apply {
            notReceivedHoneyData = this.data
            binding?.apply {
                when (data.size) {
                    0 -> {
                        tvBall1.visibility = View.GONE
                        tvBall2.visibility = View.GONE
                        tvBall3.visibility = View.GONE
                        tvBall4.visibility = View.GONE
                    }
                    1 -> {
                        tvBall1.visibility = View.VISIBLE
                        setTime(
                            data[0].lostTime.toLong(),
                            tvBall1,
                            Arith.round(data[0].getHoney, 2)
                        )
                        tvBall2.visibility = View.GONE
                        tvBall3.visibility = View.GONE
                        tvBall4.visibility = View.GONE
                    }
                    2 -> {
                        tvBall1.visibility = View.VISIBLE
                        setTime(
                            data[0].lostTime.toLong(),
                            tvBall1,
                            Arith.round(data[0].getHoney, 2)
                        )
                        tvBall2.visibility = View.VISIBLE
                        setTime(
                            data[1].lostTime.toLong(),
                            tvBall2,
                            Arith.round(data[1].getHoney, 2)
                        )
                        tvBall3.visibility = View.GONE
                        tvBall4.visibility = View.GONE
                    }
                    3 -> {
                        tvBall1.visibility = View.VISIBLE
                        setTime(
                            data[0].lostTime.toLong(),
                            tvBall1,
                            Arith.round(data[0].getHoney, 2)
                        )
                        tvBall2.visibility = View.VISIBLE
                        setTime(
                            data[1].lostTime.toLong(),
                            tvBall2,
                            Arith.round(data[1].getHoney, 2)
                        )
                        tvBall3.visibility = View.VISIBLE
                        setTime(
                            data[2].lostTime.toLong(),
                            tvBall3,
                            Arith.round(data[2].getHoney, 2)
                        )
                        tvBall4.visibility = View.GONE
                    }
                    else -> {
                        tvBall1.visibility = View.VISIBLE
                        setTime(
                            data[0].lostTime.toLong(),
                            tvBall1,
                            Arith.round(data[0].getHoney, 2)
                        )
                        tvBall2.visibility = View.VISIBLE
                        setTime(
                            data[1].lostTime.toLong(),
                            tvBall2,
                            Arith.round(data[1].getHoney, 2)
                        )
                        tvBall3.visibility = View.VISIBLE
                        setTime(
                            data[2].lostTime.toLong(),
                            tvBall3,
                            Arith.round(data[2].getHoney, 2)
                        )
                        tvBall4.visibility = View.VISIBLE
                        setTime(
                            data[3].lostTime.toLong(),
                            tvBall4,
                            Arith.round(data[3].getHoney, 2)
                        )
                    }
                }
            }
        }
    }

    private var timer: CountDownTimer? = null

    private fun setTime(time: Long, view: TextView, honeyValue: String) {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        if (view.tag != null) {
            return
        }
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view.text = honeyValue + "g蜂蜜\n" + TimeUtils.secondToHMS(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                homePresenter.getNotReceiveHoneyList(
                    SharedPreferencesUtils.getString(
                        context,
                        "uid",
                        ""
                    )
                )
                view.tag = null
            }
        }
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        view.tag = timer
        timer!!.start()
    }
}