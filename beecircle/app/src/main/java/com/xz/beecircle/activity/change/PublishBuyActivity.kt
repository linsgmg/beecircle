package com.xz.beecircle.activity.change

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lin.baselib.utils.*
import com.luck.picture.lib.tools.DoubleUtils
import com.xz.beecircle.*
import com.xz.beecircle.activity.ecosphere.ChargeRecordActivity
import com.xz.beecircle.adapter.TimeAdapter
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.databinding.ActivityPublishBuyBinding
import com.xz.beecircle.widget.SimpleTipDialog

class PublishBuyActivity : ModuleActivity<ActivityPublishBuyBinding>(), CommonContact.ChangeView {
    private var numList: MutableList<BigOrderSelect>? = null
    private var selectNumId = ""
    private var bigOrderSelectList: MutableList<BigOrderSelect>? = null
    private var smallOrderSelectList: MutableList<BigOrderSelect>? = null
    private var changerPresenter = Presenter.ChangerPresenter()
    private var guidancePrice: String? = null

    override fun initView() {
        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "发布求购"
        }

        binding?.apply {
            tvNum.tag = 0
            tvSmall.isSelected = true
            etInputUnit.isEnabled = false
            tvSmall.setOnClickListener {
                tvSmall.isSelected = true
                tvBig.isSelected = false
                etInputUnit.isEnabled = false
                numList = smallOrderSelectList
                tvNum.text = numList?.get(0)?.buynum
                changerPresenter.getReleaseConfig("0")//	（0小单（1大单
            }
            tvBig.setOnClickListener {
                tvSmall.isSelected = false
                tvBig.isSelected = true
                etInputUnit.isEnabled = true
                numList = bigOrderSelectList
                tvNum.text = numList?.get(0)?.buynum
                changerPresenter.getReleaseConfig("1")//	（0小单（1大单
            }
            llNum.setOnClickListener {
                numList?.let { it1 -> showPopWindow(llNum, it1) }
            }
            etInputUnit.addTextChangedListener(object : MyTextWatcher(-1, 2) {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    setTotalPrice()
                }
            })
            tvConfirm.setOnClickListener {
                if (!DoubleUtils.isFastDoubleClick()) {
                    val value = etInputUnit.text.toString().trim()
                    if (value == "0." || TextUtils.isEmpty(value)) {
                        ToastUtils.showShort("请输入参考单价")
                        return@setOnClickListener
                    }
                    if (tvSmall.isSelected) {
                        changerPresenter.releaseOrder(SharedPreferencesUtils.getString(this@PublishBuyActivity,
                                "uid", ""), tvNum.text.toString().trim(), value, "0")
                    } else {
                        val split = guidancePrice?.split("-")
                        if (Arith.sub(value, split?.get(0)).toFloat() < 0 || Arith.sub(value, split?.get(1)).toFloat() > 0) {
                            ToastUtils.showShort("请输入${guidancePrice}范围内的区间买入单价")
                            return@setOnClickListener
                        }
                        changerPresenter.releaseOrder(SharedPreferencesUtils.getString(this@PublishBuyActivity,
                                "uid", ""), tvNum.text.toString().trim(), value, "1")
                    }
                }
            }
        }
    }

    override fun initDate() {
        initPresenter(listOf(changerPresenter))
        changerPresenter.getReleaseConfig("0")//	（0小单（1大单
    }

    override fun onReleaseOrderSuccess(response: BaseBean<ReleaseOrderData>) {
        ToastUtils.showShort("发布成功")
    }

    override fun onGetPublishInfoSuccess(response: BaseBean<PublishInfoData>) {
        binding?.apply {
            response.data.apply {
                if (tvSmall.isSelected) {
                    etInputUnit.setText(guidancePrice)
                    etInputUnit.hint = "请输入买入单价"
                    smallOrderSelectList = tradingHoneySysteams.toMutableList()
                    numList = smallOrderSelectList
                } else {
                    etInputUnit.setText("")
                    etInputUnit.hint = "请输入${guidancePrice}范围内的区间买入单价"
                    bigOrderSelectList = tradingHoneySysteams.toMutableList()
                    numList = bigOrderSelectList
                }
                tvNum.text = tradingHoneySysteams[0].buynum
                this@PublishBuyActivity.guidancePrice = guidancePrice
            }
        }
    }

    /**
     * 弹窗
     */
    private var popupWindow: PopupWindow? = null
    private fun showPopWindow(view: View?, list: MutableList<BigOrderSelect>) {
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = layoutInflater.inflate(R.layout.pop_list, null, false)
        //列表
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        var timeAdapter = TimeAdapter(binding?.tvNum?.tag as Int)
        timeAdapter.data = list
        initRecyclerView(timeAdapter, recyclerView)
        popupWindow = PopupWindow(contentView, DensityUtil.dip2px(this, 80f),
                LinearLayout.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(ColorDrawable(0x11333333))
        popupWindow!!.setOnDismissListener {
            popupWindow = null
        }
        popupWindow!!.showAsDropDown(view, 0, 5)
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView(timeAdapter: TimeAdapter, recyclerView: RecyclerView) {
        initRecyclerView(timeAdapter, recyclerView, true)
        timeAdapter.setItemClick(object : ModuleAdapter.OnMyItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                popupWindow?.dismiss()
                binding?.tvNum?.tag = position
                binding?.tvNum?.text = timeAdapter.data[position].buynum
                selectNumId = timeAdapter.data[position].id
                setTotalPrice()
            }
        })
    }

    private fun setTotalPrice() {
        binding?.apply {
            val input = etInputUnit.text?.toString()?.trim()
            val num = tvNum.text?.toString()?.trim()
            if (TextUtils.isEmpty(input) || input == "0.") {
                tvTotal.text = "0.00 CNY"
            } else {
                tvTotal.text = "${Arith.mul(input, num)} CNY"
            }
        }
    }


    override fun tokenInvalid(type: Int, message: String?) {
        super.tokenInvalid(type, message)
        if (type == 210) {
            val split = message?.split(";")
            if (split?.size == 2) {
                setTipDialog(split[0], split[1])
            }
        }
    }

    private var simpleTipDialog: SimpleTipDialog? = null

    private fun setTipDialog(vararg value: String) {
        if (simpleTipDialog != null && simpleTipDialog!!.isShowing) {
            return
        }
        simpleTipDialog = SimpleTipDialog(this)
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

}