package com.xz.beecircle.fragment

import android.util.Log
import android.view.View
import com.lin.baselib.Base.BaseActivity.finishAll
import com.lin.baselib.net.API
import com.lin.baselib.utils.PicUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.xz.beecircle.*
import com.xz.beecircle.Constant.WEB_TYPE_ABOUT_US
import com.xz.beecircle.activity.change.MyOrderActivity
import com.xz.beecircle.activity.home.BeeCenterActivity
import com.xz.beecircle.activity.home.UpdateActivity
import com.xz.beecircle.activity.login.LoginActivity
import com.xz.beecircle.activity.login.PortraitActivity
import com.xz.beecircle.activity.mine.*
import com.xz.beecircle.base.ModuleFragment
import com.xz.beecircle.databinding.FragmentMineBinding
import com.xz.beecircle.widget.ExitDialog
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MineFragment : ModuleFragment<FragmentMineBinding>(), CommonContact.LoginView,
        CommonContact.UploadFileView {
    private lateinit var upLoadPresenter: Presenter.UpLoadPresenter
    private lateinit var bean: UserInfoData
    private val loginPresenter = Presenter.LoginPresenter()

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetMessage(message: MainChangeBean) {
        if (message.index == 4 && isInitData) {
            Log.d("onGetMessage", "onGetMessage: MineFragment")
            loginPresenter.getUserInfo()
        }
    }

    override fun initView() {
        Log.d("onResume", "initView: MineFragment")
        binding?.apply {

            tvAccountInfo.setOnClickListener {
                startActivity(AccountInfoSettingActivity::class.java, hashMapOf("avatar" to bean.headerpic,
                "name" to bean.username))
            }

            tvMyOrder.setOnClickListener {
                startActivity(MyOrderActivity::class.java)
            }

            tvBillDetail.setOnClickListener {
                startActivity(BillDetailActivity::class.java)
            }

            tvBeeOut.setOnClickListener {
                startActivity(BeeOutActivity::class.java)
            }

            tvVerifyCodeBuy.setOnClickListener {
                startActivity(VerifyCodeBuyActivity::class.java)
            }

            tvSafeSetting.setOnClickListener {
                startActivity(SafeSettingActivity::class.java)
            }

            tvReceiveInfo.setOnClickListener {
                startActivity(ReceiveInfoActivity::class.java)
            }

            tvRealVerify.setOnClickListener {
                startActivity(RealVerifyActivity::class.java)
            }

            tvBeeCenter.setOnClickListener {
                startActivity(BeeCenterActivity::class.java)
            }

            tvUpdateWay.setOnClickListener {
                startActivity(UpdateActivity::class.java)
            }

            tvAboutUs.setOnClickListener {
                startActivity(PortraitActivity::class.java, hashMapOf("entryType" to WEB_TYPE_ABOUT_US))
            }

            tvOnlineService.setOnClickListener {
                startActivity(BrowserActivity::class.java, hashMapOf("link" to "https://www.baidu.com/?tn=21002492_21_hao_pg"))
            }

            tvHelpCenter.setOnClickListener {
                startActivity(HelpCenterActivity::class.java)
            }

            tvExit.setOnClickListener {
                setNotice("确定退出登录？")
            }
        }
    }


    override fun initDate() {
        initPresenter(listOf(loginPresenter))
        loginPresenter.getUserInfo()
    }

    private var exitDialog: ExitDialog? = null

    private fun setNotice(tipTitle: String) {
        if (exitDialog != null && exitDialog!!.isShowing) {
            return
        }
        exitDialog = ExitDialog(context)
        exitDialog!!.setTitle(tipTitle)
                .setNegative("取消")
                .setPositive("确定")
                .setOnClickBottomListener(object : ExitDialog.OnClickBottomListener {
                    override fun onNegativeClick(view: View?) {
                        exitDialog!!.dismiss()
                    }

                    override fun onPositiveClick(view: View?) {
                        exitDialog!!.dismiss()
                        loginPresenter.exit()
                    }
                }).show()
    }

    override fun onExitSuccess(response: BaseBean<Any>) {
        SharedPreferencesUtils.saveString(context, "password", "")
        SharedPreferencesUtils.saveString(context, "token", "")
        SharedPreferencesUtils.saveString(context, "uid", "")
        finishAll()
        startActivity(LoginActivity::class.java)
    }

    override fun onUploadFileSuccess(response: BaseBean<FileData>) {

    }

    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
        binding?.apply {
            response.data.apply {
                bean = this
                PicUtils.uploadPic(context, API.URL_HOST_USER_IMAGE+headerpic, ivAvatar)
                tvName.text = "用户昵称：${username}"
                tvUid.text = "UID:${uid}"
                when (level) {
                    0 -> tvLevel.text = "幼蜂"
                    1 -> tvLevel.text = "小蜜蜂"
                    2 -> tvLevel.text = "工蜂"
                    3 -> tvLevel.text = "守卫蜂"
                    4 -> tvLevel.text = "雄蜂"
                    5 -> tvLevel.text = "蜂王"
                }
                tvBeeCount.text = honey
                tvBtf.text = btf
                tvShareValue.text = shard
                tvHonestValue.text = sincerity
                tvActivityValue.text = activity
            }
        }
    }

}