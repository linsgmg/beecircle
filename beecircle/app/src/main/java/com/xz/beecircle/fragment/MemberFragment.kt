package com.xz.beecircle.fragment

import com.xz.beecircle.MyPushYesRealName
import com.xz.beecircle.adapter.MemberAdapter
import com.xz.beecircle.base.ModuleFragment
import com.xz.beecircle.bean.UpOrDownBean
import com.xz.beecircle.databinding.LayoutListBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MemberFragment: ModuleFragment<LayoutListBinding>() {
    private var entryType: Int? = null
    private var memberAdapter: MemberAdapter? = null
    private var myPushYesRealName: List<MyPushYesRealName>? = null
    private var myPushNoRealName: List<MyPushYesRealName>? = null

    override fun initView() {
        entryType = arguments!!.getInt("entryType", -1)
        myPushYesRealName = arguments!!.getParcelableArrayList("myPush_yesRealName")
        myPushNoRealName = arguments!!.getParcelableArrayList("myPush_notRealName")
        initRecyclerView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetMessage(message: UpOrDownBean) {
        memberAdapter?.data?.reverse()
        memberAdapter?.notifyDataSetChanged()
    }

    /**
     * 初始化列表
     */
    private fun initRecyclerView() {
        memberAdapter = MemberAdapter(entryType!!)
        if (entryType == 0) myPushYesRealName?.let { memberAdapter?.addData(it) }
        else myPushNoRealName?.let { memberAdapter?.addData(it) }
        initRecyclerView(memberAdapter, binding.recyclerView, true)
        binding.refreshLayout.setEnableLoadMore(false)
    }

//    /**
//     * 购买确认弹框
//     */
//    private var bottomSheetDialog: BottomSheetDialog? = null
//
//    private fun showAddressDialog() {
//        bottomSheetDialog = BottomSheetDialog(context!!)
//        bottomSheetDialog?.setCancelable(false)
//        bottomSheetDialog?.setCanceledOnTouchOutside(true)
//        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_buy, null)
//        val tvGetVerifyCode = view.findViewById<TextView>(R.id.tv_get_verify_code)
//        val tvConfirm = view.findViewById<TextView>(R.id.tv_confirm)
//        val etInputPwd = view.findViewById<EditText>(R.id.et_input_pwd)
//        val etInputVerifyCode = view.findViewById<EditText>(R.id.et_input_verify_code)
//        tvGetVerifyCode.setOnClickListener {
//            setTime(tvGetVerifyCode)
//        }
//        tvConfirm.setOnClickListener {
//            val pwd = etInputPwd.text.toString().trim()
//            val verifyCode = etInputVerifyCode.text.toString().trim()
//            if (TextUtils.isEmpty(pwd)){
//                ToastUtils.showShort("请输入交易密码")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(verifyCode)){
//                ToastUtils.showShort("请输入验证码")
//                return@setOnClickListener
//            }
//            ToastUtils.showShort(pwd+verifyCode)
//        }
//        bottomSheetDialog?.setOnDismissListener {
//            if (timer!=null) timer?.cancel()
//            bottomSheetDialog = null
//        }
//        val layoutParams: ViewGroup.LayoutParams = LinearLayout.LayoutParams(QMUIDisplayHelper.getScreenWidth(context),
//                ViewGroup.LayoutParams.WRAP_CONTENT)
//        bottomSheetDialog?.setContentView(view, layoutParams)
//        bottomSheetDialog?.delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(ContextCompat.getColor(context!!,android.R.color.transparent))
//        bottomSheetDialog?.show()
//    }

//    private var timer: CountDownTimer? = null
//
//    private fun setTime(view: TextView) {
//        /**
//         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
//         * 第一个参数表示总时间，第二个参数表示间隔时间。
//         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
//         */
//        timer = object : CountDownTimer(60000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                val dhms = (millisUntilFinished / 1000).toString() + "秒"
//                view.text = dhms
//                view.isEnabled = false
//            }
//
//            override fun onFinish() {
//                view.text = "获取验证码"
//                view.isEnabled = true
//            }
//        }
//        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
//        timer!!.start()
//    }

    override fun loadDate() {
        dealData(memberAdapter, null, binding.refreshLayout)
    }


    override fun initDate() {

    }

}