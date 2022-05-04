package com.xz.beecircle.activity.mine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.xz.beecircle.BaseBean
import com.xz.beecircle.CommonContact
import com.xz.beecircle.Presenter
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityChangeNicknameBinding

class ChangeNicknameActivity : ModuleActivity<ActivityChangeNicknameBinding>(),
    CommonContact.MineView {
    private var name: String? = null
    private val minePresenter = Presenter.MinePresenter()

    override fun initView() {
        name = intent.getStringExtra("name")

        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "修改昵称"
        }

        binding?.apply {
            etInput.setText(name)

            tvChange.setOnClickListener {
                val nickname = etInput.text.toString().trim()
                if (TextUtils.isEmpty(nickname)) {
                    ToastUtils.showShort("请输入您的昵称")
                    return@setOnClickListener
                }
                minePresenter.updateUserName(nickname)
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(minePresenter))
    }

    override fun onUpdateUserNameSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("昵称修改成功")
    }

}