package com.xz.beecircle.fragment

import android.content.Context
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lin.baselib.utils.DensityUtil
import com.lin.baselib.utils.SharedPreferencesUtils
import com.lin.baselib.utils.ToastUtils
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.xz.beecircle.*
import com.xz.beecircle.Constant.WEB_TYPE_TRADE_RULE
import com.xz.beecircle.activity.change.MyOrderActivity
import com.xz.beecircle.activity.change.PublishBuyActivity
import com.xz.beecircle.activity.login.PortraitActivity
import com.xz.beecircle.adapter.ChangeCenterAdapter
import com.xz.beecircle.adapter.TimeAdapter
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.ModuleFragment
import com.xz.beecircle.databinding.FragmentChangeCenterBinding
import com.xz.beecircle.widget.SimpleTipDialog
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChangeCenterFragment : ModuleFragment<FragmentChangeCenterBinding>(), CommonContact.ChangeView,
        CommonContact.LoginView {
    private var currentSelect = 0
    private var changeCenterAdapter: ChangeCenterAdapter? = null
    private var changerPresenter = Presenter.ChangerPresenter()
    private var bigOrderSelectList: MutableList<BigOrderSelect>? = null
    private var bigSelectNum = ""
    private var bigSelectUpOrDown = ""
    private var phone = ""
    private var tradingState = ""
    private val loginPresenter = Presenter.LoginPresenter()
    private var tvGetVerifyCode: TextView? = null

    override fun initView() {
        binding?.apply {
            tvTime.tag = 0
            setSelect()
            setSelectView()
            tvRule.setOnClickListener {
                startActivity(PortraitActivity::class.java, hashMapOf("entryType" to WEB_TYPE_TRADE_RULE))
            }
            tvOrder.setOnClickListener {
                startActivity(MyOrderActivity::class.java)
            }
            llNum2.setOnClickListener {
                if (currentSelect == 1) {
                    bigOrderSelectList?.let { it1 -> showPopwindow(llNum2, it1) }
                }
            }
            tvUnit.setOnClickListener {
                if (currentSelect == 1) {
                    tvUnit.isSelected = !tvUnit.isSelected
                    bigSelectUpOrDown = if (tvUnit.isSelected) "unitprice" else "unitprice desc"
                    loadDate()
                }
            }
            tvTotal.setOnClickListener {
                if (currentSelect == 1) {
                    tvTotal.isSelected = !tvTotal.isSelected
                    bigSelectUpOrDown = if (tvUnit.isSelected) "totalprice" else "totalprice desc"
                    loadDate()
                }
            }
            tvConfrim.setOnClickListener {
                if (tradingState == "1"){//0 开启 1关闭
                    ToastUtils.showShort("市场已关闭")
                    return@setOnClickListener
                }
                startActivity(PublishBuyActivity::class.java)
            }
        }
        initRecyclerView()
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetMessage(message: MainChangeBean) {
        if (message.index == 2 && isInitData) {
            changerPresenter.tradingCenterParameter()
            loginPresenter.getUserInfo()
            loadDate()
        }
    }

    override fun initDate() {
        initPresenter(listOf(changerPresenter, loginPresenter))
        changerPresenter.tradingCenterParameter()
        loginPresenter.getUserInfo()
        loadDate()
    }

    override fun loadDate() {
        when (currentSelect) {//大小单关键字（0小单（1大单
            0 -> {
                changerPresenter.getOrderList("0", "", "")
            }
            1 -> {
                changerPresenter.getOrderList("1", bigSelectUpOrDown, bigSelectNum)
            }
            2 -> {
                changerPresenter.getMyReleaseList(SharedPreferencesUtils.getString(context, "uid", ""))
            }
        }
    }

    override fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {
        phone = response.data.phone
    }

    override fun onGetSSMCode(response: BaseBean<Any>) {
        ToastUtils.showShort("验证码已发送")
        tvGetVerifyCode?.let { setTime(it) }
    }

    override fun onGetMyPublishListSuccess(response: BaseBean<List<TradeCenterListData>>) {
        dealData(changeCenterAdapter, response.data, null)
    }


    override fun onGetChangeCenterListSuccess(response: BaseBean<List<TradeCenterListData>>) {
        dealData(changeCenterAdapter, response.data, null)
    }

    override fun onGetTradeCenterInfoSuccess(response: BaseBean<TradeCenterInfoData>) {
        binding?.apply {
            response.data.apply {
                this@ChangeCenterFragment.tradingState = tradingState
                tvRate.text = "手续费：${charge}"
                tvDeal.text = "今日交易量：${turnover}"
                tvCurrentPrice.text = "当前价格${littleGuidePrice}CNY"
                tvCurrentPrice2.text = "当前区间价格\n${bigGuidePrice}CNY"
                bigOrderSelectList = bigOrderSelect.toMutableList()
                tvTime.text = bigOrderSelect[0].buynum
                bigSelectNum = bigOrderSelect[0].buynum
            }
        }
    }

    override fun onSellHoneySuccess(response: BaseBean<Any>) {
        bottomSheetDialog?.dismiss()
        ToastUtils.showShort("购买成功")
        loadDate()
    }

    /**
     * 购买确认弹框
     */
    private var bottomSheetDialog: BottomSheetDialog? = null

    private fun showBugDialog(id: String?) {
        bottomSheetDialog = BottomSheetDialog(context!!)
        bottomSheetDialog?.setCancelable(false)
        bottomSheetDialog?.setCanceledOnTouchOutside(true)
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_buy, null)
        tvGetVerifyCode = view.findViewById<TextView>(R.id.tv_get_verify_code)
        val tvConfirm = view.findViewById<TextView>(R.id.tv_confirm)
        val etInputPwd = view.findViewById<EditText>(R.id.et_input_pwd)
        val etInputVerifyCode = view.findViewById<EditText>(R.id.et_input_verify_code)
        val group = view.findViewById<RadioGroup>(R.id.group)
        group.visibility = View.VISIBLE
        val group2 = view.findViewById<RadioGroup>(R.id.group2)
        group2.visibility = View.GONE
        val llZfb = view.findViewById<LinearLayout>(R.id.ll_zfb)
        val llWx = view.findViewById<LinearLayout>(R.id.ll_wx)
        val llYhk = view.findViewById<LinearLayout>(R.id.ll_yhk)
        val ivZfb = view.findViewById<TextView>(R.id.iv_zfb)
        val ivWx = view.findViewById<TextView>(R.id.iv_wx)
        val ivYhk = view.findViewById<TextView>(R.id.iv_yhk)
        var selectTyp = "-1";
        llZfb.setOnClickListener {
            ivZfb.isSelected = true
            ivWx.isSelected = false
            ivYhk.isSelected = false
            selectTyp = "0";
        }
        llWx.setOnClickListener {
            ivZfb.isSelected = false
            ivWx.isSelected = true
            ivYhk.isSelected = false
            selectTyp = "1";
        }
        llYhk.setOnClickListener {
            ivZfb.isSelected = false
            ivWx.isSelected = false
            ivYhk.isSelected = true
            selectTyp = "2";
        }
        tvGetVerifyCode?.setOnClickListener {
            loginPresenter.getSSMCode(phone, "pay")
        }
        tvConfirm.setOnClickListener {
            val pwd = etInputPwd.text.toString().trim()
            val verifyCode = etInputVerifyCode.text.toString().trim()
            if (selectTyp == "-1") {
                ToastUtils.showShort("请选择收款方式")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.showShort("请输入交易密码")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(verifyCode)) {
                ToastUtils.showShort("请输入验证码")
                return@setOnClickListener
            }
            changerPresenter.sellHoney(SharedPreferencesUtils.getString(context, "uid", ""),
                    id, selectTyp, pwd, verifyCode)
        }
        bottomSheetDialog?.setOnDismissListener {
            if (timer != null) timer?.cancel()
            bottomSheetDialog = null
        }
        val layoutParams: ViewGroup.LayoutParams = LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(context),
                ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomSheetDialog?.setContentView(view, layoutParams)
        bottomSheetDialog?.delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
        bottomSheetDialog?.show()
    }

    private var timer: CountDownTimer? = null

    private fun setTime(view: TextView) {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val dhms = (millisUntilFinished / 1000).toString() + "秒"
                view.text = dhms
                view.isEnabled = false
            }

            override fun onFinish() {
                view.text = "获取验证码"
                view.isEnabled = true
            }
        }
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer!!.start()
    }

    /**
     * 弹窗
     */
    private var popupWindow: PopupWindow? = null
    private fun showPopwindow(view: View?, list: MutableList<BigOrderSelect>) {
        val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = layoutInflater.inflate(R.layout.pop_list, null, false)
        //列表
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        var timeAdapter = TimeAdapter(binding?.tvTime?.tag as Int)
        timeAdapter.data = list
        initRecyclerView(timeAdapter, recyclerView)
        popupWindow = PopupWindow(contentView, DensityUtil.dip2px(context, 40f),
                LinearLayout.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isOutsideTouchable = true
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
                binding?.tvTime?.tag = position
                binding?.tvTime?.text = timeAdapter.data[position].buynum
                bigSelectNum = timeAdapter.data[position].buynum
                loadDate()
            }
        })
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        changeCenterAdapter = ChangeCenterAdapter(currentSelect)
        initRecyclerView(changeCenterAdapter, binding?.recyclerView, true)
//        addRefreshListener(binding?.refreshLayout)
        changeCenterAdapter!!.setItemClick(object : ModuleAdapter.OnMyItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                when (view.id) {
                    R.id.tv_operate -> {
                        if (tradingState == "1"){//0 开启 1关闭
                            ToastUtils.showShort("市场已关闭")
                            return
                        }
                        val textView = view as? TextView
                        if (textView?.text == "卖出") {
                            showBugDialog(changeCenterAdapter?.data?.get(position)?.id)
                        } else {
                            changerPresenter.cancelOrder(changeCenterAdapter?.data?.get(position)?.id,
                                    SharedPreferencesUtils.getString(context, "uid", ""))
                        }
                    }
                }
            }
        })
    }

    override fun onCancelOrderSuccess(response: BaseBean<Any>) {
        ToastUtils.showShort("取消成功")
        loadDate()
    }

    private fun FragmentChangeCenterBinding.setSelectView() {
        llSmallMarket.isSelected = true
        llBigMarket.isSelected = false
        llMyPublish.isSelected = false

        llSmallMarket.setOnClickListener {
            llSmallMarket.isSelected = true
            llBigMarket.isSelected = false
            llMyPublish.isSelected = false
            currentSelect = 0
            setSelect()
            loadDate()
        }
        llBigMarket.setOnClickListener {
            llSmallMarket.isSelected = false
            llBigMarket.isSelected = true
            llMyPublish.isSelected = false
            currentSelect = 1
            setSelect()
            loadDate()
        }
        llMyPublish.setOnClickListener {
            llSmallMarket.isSelected = false
            llBigMarket.isSelected = false
            llMyPublish.isSelected = true
            currentSelect = 2
            setSelect()
            loadDate()
        }
    }

    private fun FragmentChangeCenterBinding.setSelect() {
        changeCenterAdapter?.currentSelect = currentSelect
        changeCenterAdapter?.notifyDataSetChanged()
        when (currentSelect) {
            0 -> {
                tvHonorValue.visibility = View.VISIBLE
                tvType.visibility = View.GONE
                llNum2.visibility = View.GONE
                tvFee.visibility = View.GONE
                tvStatus.visibility = View.GONE
                tvUnit.setCompoundDrawables(null, null, null, null)
                tvTotal.setCompoundDrawables(null, null, null, null)
            }
            1 -> {
                tvHonorValue.visibility = View.VISIBLE
                tvType.visibility = View.GONE
                llNum2.visibility = View.VISIBLE
                tvFee.visibility = View.GONE
                tvStatus.visibility = View.GONE
                val drawable = ContextCompat.getDrawable(context!!, R.drawable.selector_up_down)
                drawable?.setBounds(0, 0, DensityUtil.dip2px(context, 16f), DensityUtil.dip2px(context, 10f));
                val drawable2 = ContextCompat.getDrawable(context!!, R.drawable.selector_up_down)
                drawable2?.setBounds(0, 0, DensityUtil.dip2px(context, 16f), DensityUtil.dip2px(context, 10f));
                tvUnit.setCompoundDrawables(null, null, drawable, null)
                tvTotal.setCompoundDrawables(null, null, drawable2, null)
            }
            2 -> {
                tvHonorValue.visibility = View.GONE
                tvType.visibility = View.VISIBLE
                llNum2.visibility = View.GONE
                tvFee.visibility = View.VISIBLE
                tvStatus.visibility = View.VISIBLE
                tvUnit.setCompoundDrawables(null, null, null, null)
                tvTotal.setCompoundDrawables(null, null, null, null)
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


}