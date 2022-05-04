package com.xz.beecircle.activity

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.lin.baselib.Base.CheckVersionBean
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.xz.beecircle.*
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityMainBinding
import com.xz.beecircle.fragment.*
import org.greenrobot.eventbus.EventBus
import java.util.*


class MainActivity : ModuleActivity<ActivityMainBinding>() {
    private var index = 0
    private var checked = 0
    private lateinit var manager: FragmentManager
    private var cacheFrag: Fragment? = null

    override fun onResume() {
        super.onResume()
        QMUIStatusBarHelper.setStatusBarLightMode(this@MainActivity);
        EventBus.getDefault().postSticky(MainChangeBean(index))
    }

    override fun initView() {
        fullScreen()
        manager = supportFragmentManager
        showFrag(R.id.main_home)
        binding.group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.main_home -> {
                    index = 0
                    checked = checkedId
                }
                R.id.main_ecosphere -> {
                    index = 1
                    checked = checkedId
                }
                R.id.main_change_center -> {
                    index = 2
                    checked = checkedId
                }
                R.id.main_mine -> {
                    index = 4
                    checked = checkedId
                }
            }
            EventBus.getDefault().postSticky(MainChangeBean(index))
            EventBus.getDefault().postSticky(CheckVersionBean())
            showFrag(checkedId)
        }
    }

    /**
     * fragment选中切换
     *
     * @param checkedId
     */
    private fun showFrag(checkedId: Int) {
        val transaction: FragmentTransaction = manager.beginTransaction()
        if (null != cacheFrag) {
            transaction.hide(cacheFrag!!)
        }
        var frag: Fragment? = manager.findFragmentByTag("tab$checkedId")
        if (null == frag) {
            when (checkedId) {
                R.id.main_home -> {
                    frag = HomeFragment() //首页
                }
                R.id.main_ecosphere -> {
                    frag = EcosphereFragment() //生态圈
                }
                R.id.main_change_center -> {
                    frag = ChangeCenterFragment() //置换中心
                }
                R.id.main_mine -> {
                    frag = MineFragment() //我的
                }
            }
            frag?.let { transaction.add(R.id.main_content, it, "tab$checkedId") }
        } else {
            transaction.show(frag)
        }
        cacheFrag = frag
        transaction.commit()
    }

    override fun initDate() {

    }


}