package com.xz.beecircle.activity.home

import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.lin.baselib.utils.PicUtils
import com.lin.baselib.utils.SharedPreferencesUtils
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.xz.beecircle.*
import com.xz.beecircle.adapter.MyTeamAdapter
import com.xz.beecircle.adapter.PagerAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.bean.UpOrDownBean
import com.xz.beecircle.databinding.ActivityMyTeamBinding
import com.xz.beecircle.fragment.MemberFragment
import com.xz.beecircle.widget.MemberTipDialog
import org.greenrobot.eventbus.EventBus


class MyTeamActivity : ModuleActivity<ActivityMyTeamBinding>(), CommonContact.HomeView {
    private var myTeamAdapter: MyTeamAdapter? = null
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private val homePresenter = Presenter.HomePresenter()
    private var nextLevel = "普"
    private var levelRequire = "一"
    private var directLevel = "普"
    private var upOrDown = "down"
    override fun initView() {
        addTitleBar2 {
            it?.apply {
                tvTitle.text = "我的团队"
            }
        }
        initRecyclerView()

        binding?.apply {
            tvConfirm.setOnClickListener {
                homePresenter.teamInfo(SharedPreferencesUtils.getString(this@MyTeamActivity, "uid", ""))
            }
            tvUpdate.setOnClickListener {
//                setTipDialog("三")
//                setTipDialog("三","二","10","三","12","100")
                homePresenter.teamInfo(SharedPreferencesUtils.getString(this@MyTeamActivity, "uid", ""))
            }
            tvUpdateWay.setOnClickListener {
                startActivity(UpdateActivity::class.java)
            }
            tvUpDown.setOnClickListener {
                setUpOrDown()
            }
            tvUpDown2.setOnClickListener {
                setUpOrDown()
            }
        }
    }

    /**
     * 设置升序和降序
     */
    private fun ActivityMyTeamBinding.setUpOrDown() {
        tvUpDown2.isSelected = !tvUpDown2.isSelected
        upOrDown = if (upOrDown == "down") "up" else "down"
        EventBus.getDefault().postSticky(UpOrDownBean())
    }

    override fun initDate() {
        initPresenter(listOf(homePresenter))
        homePresenter.myTeam(SharedPreferencesUtils.getString(this, "uid", ""), upOrDown)
    }

    private var memberTipDialog: MemberTipDialog? = null

    private fun setTipDialog(vararg value: String) {
        if (memberTipDialog != null && memberTipDialog!!.isShowing) {
            return
        }
        memberTipDialog = MemberTipDialog(this)
        if (value.size == 1) {
            memberTipDialog?.setCurrentStar(value[0])
        } else {
            memberTipDialog?.setCurrentStar(value[0])
                    ?.setLack(value[1], value[2])
                    ?.setPerson(value[3], value[4])
                    ?.setActivity(value[5])
                    ?.setActivity2(value[6])
        }
        memberTipDialog?.setOnClickBottomListener(object : MemberTipDialog.OnClickBottomListener {
            override fun onNegativeClick(view: View?) {
                memberTipDialog!!.dismiss()
            }

            override fun onPositiveClick(view: View?) {
                memberTipDialog!!.dismiss()

            }
        })?.show()
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        myTeamAdapter = MyTeamAdapter()
        initRecyclerView(myTeamAdapter, binding?.recyclerView, true)
    }

    private fun addFragment(myPush_yesRealName: List<MyPushYesRealName>, myPush_notRealName: List<MyPushYesRealName>) {
        for (i in 0..1) {
            val memberFragment = MemberFragment()
            val bundle = Bundle()
            bundle.putInt("entryType", i)
            if (i == 0) bundle.putParcelableArrayList("myPush_yesRealName", myPush_yesRealName as java.util.ArrayList<out Parcelable>)
            else bundle.putParcelableArrayList("myPush_notRealName", myPush_notRealName as java.util.ArrayList<out Parcelable>)
            memberFragment.arguments = bundle
            fragmentList.add(memberFragment)
        }
    }

    private fun initTabAndPager() {
        val adapter = PagerAdapter(fragmentList, supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        binding.apply {
            viewPager.adapter = adapter
            tabSegment.reset()
            tabSegment.addTab(QMUITabSegment.Tab("实名会员"))
            tabSegment.addTab(QMUITabSegment.Tab("未实名会员"))
            tabSegment.setIndicatorWidthAdjustContent(true)
            tabSegment.setDefaultNormalColor(ContextCompat.getColor(this@MyTeamActivity, R.color.black))
            tabSegment.setDefaultSelectedColor(ContextCompat.getColor(this@MyTeamActivity, R.color.text))
            tabSegment.setupWithViewPager(viewPager, false)
            tabSegment.mode = QMUITabSegment.MODE_FIXED
        }
    }

    override fun onGetMyTeamSuccess(response: BaseBean<MyTeamData>) {
        binding?.apply {
            response.data.apply {
//                if (hasTeam) {
//                    llStatus.visibility = View.GONE
                    mySelfTeam.apply {
                        when (name.substring(0,1)) {
                            "会" -> nextLevel = "普通"
                            "普" -> nextLevel = "一星"
                            "一" -> nextLevel = "二星"
                            "二" -> nextLevel = "三星"
                            "三" -> nextLevel = "四星"
                        }
                        when (myLevel) {
                            "2" -> {
                                directLevel = "二"
                                levelRequire = "三"
                            }
                            "3" -> {
                                directLevel = "三"
                                levelRequire = "四"
                            }
                            "4" -> {
                                directLevel = "四"
                                levelRequire = "五"
                            }
                        }
                        tvName.text = name
                        tvTeamNum.text = "团队人数：${peopleNum}"
                        tvTeamActivity.text = "团队活跃度：${temaActive}"
                    }
                    myInvitees.apply {
                        if (!TextUtils.isEmpty(phone)) {
                            PicUtils.uploadPic(this@MyTeamActivity, headerpic, ivMyInvite)
                            tvInviteUid.text = "UID:${uid}"
                            tvInvitePhone.text = if (phone.length > 7) "${phone.substring(0, 3)}+****+${phone.substring(phone.length - 4)}" else phone
                        }
                    }
                    myTeamAdapter?.data?.clear()
                    myTeamAdapter?.addData(myTeam)
                    myTeamAdapter?.notifyDataSetChanged()
                    addFragment(myPush_yesRealName, myPush_notRealName)
                    initTabAndPager()
//                } else {
//                    llStatus.visibility = View.VISIBLE
//                }
            }
        }
    }

    override fun onGetTeamInfoSuccess(response: BaseBean<TeamData>) {
        response.data.apply {
            if (upTeam) {
                setTipDialog(nextLevel)
                homePresenter.myTeam(SharedPreferencesUtils.getString(this@MyTeamActivity, "uid", ""), upOrDown)
            } else {
                setTipDialog(nextLevel, directLevel, needPushLevelSize.toString(), levelRequire,
                        needRealNameSize.toString(), needTeamActive.toString(), needPushActiveSize.toString())
            }
        }
    }
}