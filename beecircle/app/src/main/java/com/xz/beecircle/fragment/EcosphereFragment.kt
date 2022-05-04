package com.xz.beecircle.fragment

import android.util.Log
import android.view.View
import com.lin.baselib.net.API
import com.lin.baselib.utils.PicUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.xz.beecircle.*
import com.xz.beecircle.activity.ecosphere.BeeMine1Activity
import com.xz.beecircle.activity.ecosphere.BeeMine2Activity
import com.xz.beecircle.activity.ecosphere.ChargeAndMemberActivity
import com.xz.beecircle.base.ModuleFragment
import com.xz.beecircle.databinding.FragmentEcosphereBinding
import com.xz.beecircle.widget.SimpleTipDialog
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class EcosphereFragment : ModuleFragment<FragmentEcosphereBinding>(), CommonContact.EcosphereView,
    CommonContact.MineView {
    private var ecospherePresenter: Presenter.EcospherePresenter = Presenter.EcospherePresenter()
    private var minePresenter = Presenter.MinePresenter()

    override fun initView() {
        binding?.apply {
            tvCharge.setOnClickListener {
                startActivity(
                    ChargeAndMemberActivity::class.java,
                    hashMapOf("entryType" to "charge")
                )
            }
            tvMember.setOnClickListener {
                startActivity(
                    ChargeAndMemberActivity::class.java,
                    hashMapOf("entryType" to "member")
                )
            }
            ivMine.setOnClickListener {
//                startActivity(BeeMine2Activity::class.java)
                if (ivMine.tag as? String == "0") startActivity(BeeMine2Activity::class.java)
                else startActivity(BeeMine1Activity::class.java, hashMapOf("mineState" to (ivMine.tag as? String)))
            }
            ivTurn.setOnClickListener {
                //                startActivity(MainActivity::class.java)
                setTipDialog("", "待开发中.......")
            }
            ivTrip.setOnClickListener {
                //                startActivity(MainActivity::class.java)
                setTipDialog("", "待开发中.......")
            }
            ivMakeMoney.setOnClickListener {
                //                startActivity(MainActivity::class.java)
                setTipDialog("", "待开发中.......")
            }
        }
    }

    private var simpleTipDialog: SimpleTipDialog? = null

    private fun setTipDialog(vararg value: String) {
        if (simpleTipDialog != null && simpleTipDialog!!.isShowing) {
            return
        }
        simpleTipDialog = SimpleTipDialog(context)
        simpleTipDialog?.setTitle(value[0])
            ?.setContent(value[1])

        simpleTipDialog?.setOnClickBottomListener(object : SimpleTipDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                simpleTipDialog!!.dismiss()
            }

            override fun onPositiveClick(view: View?) {
                simpleTipDialog!!.dismiss()

            }
        })?.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetMessage(message: MainChangeBean) {
        if (message.index == 1) {
            Log.d("onGetMessage", "onGetMessage: HomeFragment")
            ecospherePresenter.mineState(SharedPreferencesUtils.getString(context, "uid", ""))
        }
    }

    override fun initDate() {
        initPresenter(listOf(ecospherePresenter, minePresenter))
        ecospherePresenter.mineState(SharedPreferencesUtils.getString(context, "uid", ""))
        minePresenter.stqImg()
    }

    override fun onGetMineStateSuccess(response: BaseBean<MineStatusData>) {
        binding?.ivMine?.tag = response.data.state
    }

    override fun onGetImgListSuccess(response: BaseBean<List<StqImgData>>) {
        binding?.apply {
            response.apply {
                PicUtils.uploadVideo(context, API.URL_HOST_USER_IMAGE + data[0].configval,ivMine)
                PicUtils.uploadVideo(context, API.URL_HOST_USER_IMAGE + data[1].configval,ivTurn)
                PicUtils.uploadVideo(context, API.URL_HOST_USER_IMAGE + data[2].configval,ivTrip)
                PicUtils.uploadVideo(context, API.URL_HOST_USER_IMAGE + data[3].configval,ivMakeMoney)
            }
        }
    }

}