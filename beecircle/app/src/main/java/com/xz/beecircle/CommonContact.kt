package com.xz.beecircle

import com.lin.baselib.Base.BaseResponse
import com.lin.baselib.Base.BaseView

/**
 * @author lin
 * 创建日期：2020/7/9
 */
class CommonContact {
    interface RegisterView : BaseView {
    }

    interface SSMCodeView : BaseView {
    }

    interface LoginView : BaseView {
        fun onRegisterSuccess(response: BaseBean<Any>) {}
        fun onGetSSMCode(response: BaseBean<Any>) {}
        fun onLoginSuccess(response: BaseBean<LoginData>) {}
        fun onChangeLoginPwdSuccess(response: BaseBean<Any>) {}
        fun onGetUserInfoSuccess(response: BaseBean<UserInfoData>) {}
        fun onExitSuccess(response: BaseBean<Any>) {}
    }

    interface HomeView : BaseView {
        fun onInviteFriendSuccess(response: BaseBean<InviteFriendData>) {}
        fun onGetTeamInfoSuccess(response: BaseBean<TeamData>) {}
        fun onGetMyTeamSuccess(response: BaseBean<MyTeamData>) {}
        fun onGetNoticeDetailSuccess(response: BaseBean<NoticeDetailData>) {}
        fun onGetNoticeListSuccess(response: BaseBean<List<NoticeListData>>) {}
        fun onGetVersionSuccess(response: BaseBean<VersionData>) {}
        fun onGetNotReceiveListSuccess(response: BaseBean<List<NotReceivedHoneyData>>) {}
        fun onReceiveHoneySuccess(response: BaseBean<Any>) {}
        fun onGetBeeCenterJSListSuccess(response: BaseBean<List<BeeCenterData>>) {}
        fun onGetBeeCenterMyListSuccess(response: BaseBean<List<BeeCenterData>>) {}
        fun onGetBeeCenterGQListSuccess(response: BaseBean<List<BeeCenterData>>) {}
        fun onBuyHoneySuccess(response: BaseBean<Any>) {}
        fun onGetAdSuccess(response: BaseBean<AdData>) {}
    }

    interface EcosphereView : BaseView {
        fun onGetChargePhoneListSuccess(response: BaseBean<List<RechargeListData>>) {}
        fun onGetChargeVideoListSuccess(response: BaseBean<List<RechargeListData>>) {}
        fun onGetPhoneChargeInfoSuccess(response: BaseBean<PhoneChargeInfoData>) {}
        fun onRechargePhoneSuccess(response: BaseBean<AdData>) {}
        fun onGetVipChargeInfoSuccess(response: BaseBean<VipChargeInfoData>) {}
        fun onRechargeVipSuccess(response: BaseBean<AdData>) {}
        fun onGetMineStateSuccess(response: BaseBean<MineStatusData>) {}
        fun onMineSignUpSuccess(response: BaseBean<Any>) {}
        fun onAddPowerByHoneySuccess(response: BaseBean<Any>) {}
        fun onGetCountPowerConfigSuccess(response: BaseBean<CountPowerConfigData>) {}
        fun onGetMineConfigSuccess(response: BaseBean<MineConfigData>) {}
        fun onAddPowerByADSuccess(response: BaseBean<Any>) {}
        fun onGetMineRecordListSuccess(response: BaseBean<List<MineRecordListDate>>) {}
        fun onReceivedMineSuccess(response: BaseBean<Any>) {}
        fun onGetNotReceivedMineSuccess(response: BaseBean<NotReceivedMineDate>) {}
        fun onGetMyMinerDetailsSuccess(response: BaseBean<MyMinerDetailsDate>) {}
        fun onGetMyPowerDetailsSuccess(response: BaseBean<List<MyPowerDetailsDate>>) {}

    }

    interface ChangeView : BaseView {
        fun onGetTradeCenterInfoSuccess(response: BaseBean<TradeCenterInfoData>) {}
        fun onGetChangeCenterListSuccess(response: BaseBean<List<TradeCenterListData>>) {}
        fun onGetBeforePayJudgeDSuccess(response: BaseBean<BeforePayJudgeData>) {}
        fun onGetMyPublishListSuccess(response: BaseBean<List<TradeCenterListData>>) {}
        fun onGetPublishInfoSuccess(response: BaseBean<PublishInfoData>) {}
        fun onReleaseOrderSuccess(response: BaseBean<ReleaseOrderData>) {}
        fun onCancelOrderSuccess(response: BaseBean<Any>) {}
        fun onGetMyBuyListSuccess(response: BaseBean<List<MyBuyListData>>) {}
        fun onGetMySellListSuccess(response: BaseBean<List<MyBuyListData>>) {}
        fun onGetOrderDetailSuccess(response: BaseBean<OrderDetailData>) {}
        fun onAppealOrderSuccess(response: BaseBean<Any>) {}
        fun onFinishOrderSuccess(response: BaseBean<Any>) {}
        fun onFinishPaySuccess(response: BaseBean<Any>) {}
        fun onSellHoneySuccess(response: BaseBean<Any>) {}
        fun onFinishAppealSuccess(response: BaseBean<Any>) {}

    }

    interface MineView : BaseView {
        fun onGetMyPayTypeSuccess(response: BaseBean<List<MyPayWayData>>){}
        fun onSetCollectionSuccess(response: BaseBean<Any>){}
        fun onUpdatePasswordSuccess(response: BaseBean<Any>){}
        fun onUpdatePayPsdSuccess(response: BaseBean<Any>){}
        fun onGetOutBtfConfigSuccess(response: BaseBean<OutBtfConfigData>){}
        fun onOutBtfSuccess(response: BaseBean<Any>){}
        fun onOutHoneySuccess(response: BaseBean<Any>){}
        fun onGetOutHoneyConfigSuccess(response: BaseBean<OutBtfConfigData>){}
        fun onGetHoneyListSuccess(response: BaseBean<List<HoneyListData>>) {}
        fun onGetTeamActiveListSuccess(response: BaseBean<List<HoneyListData>>) {}
        fun onGetSincerityListSuccess(response: BaseBean<List<HoneyListData>>) {}
        fun onGetActiveListSuccess(response: BaseBean<List<HoneyListData>>) {}
        fun onGetBtfListSuccess(response: BaseBean<List<HoneyListData>>) {}
        fun onGetSharedListSuccess(response: BaseBean<List<HoneyListData>>) {}
        fun onUpdateHeadPicSuccess(response: BaseBean<Any>){}
        fun onUpdateUserNameSuccess(response: BaseBean<Any>){}
        fun onGetRealNameConfigSuccess(response: BaseBean<RealNameConfigData>){}
        fun onRealNameCodeSuccess(response: BaseBean<Any>){}
        fun onGetNoticeSuccess(response: BaseBean<ArticleData>){}
        fun onGetAttBuySuccess(response: BaseBean<AttBuyData>){}
        fun onCheckCodeSuccess(response: BaseBean<Any>){}
        fun onRealNameHeadSuccess(response: BaseBean<Any>){}
        fun onGetRealStateSuccess(response: BaseBean<RealStateData>){}
        fun onGetTeamStrategySuccess(response: BaseBean<TeamStrategyData>){}
        fun onGetImgListSuccess(response: BaseBean<List<StqImgData>>) {}
    }


    interface UploadFileView : BaseView {
        fun onUploadFileSuccess(response: BaseBean<FileData>)
    }


}