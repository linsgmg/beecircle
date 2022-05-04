package com.xz.beecircle

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class BaseBean<T>(
        val code: Int,
        val `data`: T,
        val message: String,
        val token: String
)

data class BaseListBean<T>(
        val current: String,
        val pages: String,
        val records: List<T>,
        val searchCount: Boolean,
        val size: String,
        val total: String
)

data class LoginData(
        val frozen: String,//冻结状态（0未冻结（1交易冻结（2账号冻结
        val frozenReason: String, //冻结原因
        val uid: String
)


data class MainChangeBean(
        val index: Int
)

data class BannerData(
        val carouselId: String,
        val img: String,
        val link: String,
        val title: String
)

data class VersionData(
        val content: String,
        val httpaddress: String,
        val id: String,
        val type: String,
        val version: String
)

@SuppressLint("ParcelCreator")
@Parcelize
data class UserInfoData(
        val activity: String,
        val btf: String,
        val creattime: String,
        val frozen: String,
        val honey: String,
        val level: Int,
        val minesignup: String,
        val phone: String,
        val realnamesystem: String,
        val shard: String,
        val sincerity: String,
        val token: String,
        val uid: String,
        val upuid: String,
        val headerpic: String,
        val realname: String,
        val username: String
) : Parcelable


data class FileData(
        val url: String
)

data class InviteFriendData(
        val base64: String,
        val uid: String,
        val url: String
)

data class TeamData(
        val needPushActiveSize: Int,
        val needPushLevel: Int,
        val needPushLevelSize: Int,
        val needRealNameSize: Int,
        val upTeam: Boolean,
        val needTeamActive: Int
)

data class MyTeamData(
        val hasTeam: Boolean,
        val myLevel: String,
        val myInvitees: MyInvitees,
        val myPush_notRealName: List<MyPushYesRealName>,
        val myPush_yesRealName: List<MyPushYesRealName>,
        val mySelfTeam: MySelfTeam,
        val myTeam: List<MyTeam>
)

data class MyInvitees(
        val creattime: String,
        val phone: String,
        val headerpic: String,
        val uid: String
)

@SuppressLint("ParcelCreator")
@Parcelize
data class MyPushYesRealName(
        val creattime: String,
        val myActive: String,
        val phone: String,
        val teamsize: String,
        val temaActive: String,
        val headerpic: String,
        var type: String,
        val uid: String
) : Parcelable

data class MySelfTeam(
        val name: String,
        val peopleNum: String,
        val temaActive: String
)

data class MyTeam(
        val name: String,
        val peopleNum: String,
        val greaterOneSize: String,
        val temaActive: String
)

data class NoticeDetailData(
        val content: String,
        val createtime: String,
        val id: String,
        val isshow: String,
        val noticetype: String,
        val title: String,
        val updatetime: String
)

@SuppressLint("ParcelCreator")
@Parcelize
data class NoticeListData(
        val content: String,
        val id: String,
        val createtime: String,
        val updatetime: String,
        val noticetype: String,
        val isshow: String,
        val title: String
) : Parcelable

data class NotReceivedHoneyData(
        val lostTime: String,
        val getHoney: String,
        val keyWord: String
)

data class BeeCenterData(
        val activity: String,
        val buynum: String,
        val nowtime: String,
        val canbuy: String,
        val cycle: Int,
        val dayoutput: String,
        val id: String,
        val level: String,
        val limited: Int,
        val price: String,
        val rateofreturn: String,
        val total: String,
        val upshare: String,
        val starttime: String,
        val endtime: String,
        val cumulativenum: String,
        val honeyid: String,
        val uid: String,
        val img: String,
        val gettype: String,
        var isStartTime: Boolean = false,
        val state: String,
        val overcreat: String,
)

data class AdData(
        val creatTime: String,
        val type: String,
        var url: String
)

data class RechargeListData(
        val creattime: String,
        val id: Int,
        val paynum: Int,
        val rechargeid: String,
        val rechargenum: Int,
        val rechargetype: Int,
        val state: Int,
        val uid: String,
        val vipid: Int
)

data class PhoneChargeInfoData(
        val phoneConfig: PhoneConfig,
        val user: User
)

data class PhoneConfig(
        val payhoney: String,
        val rechargenum: String
)

data class User(
        val honey: String,
        val uid: String
)

data class VipChargeInfoData(
        val vipConfig: List<VipConfig>,
        val user: User
)

data class VipConfig(
        val id: String,
        val payhoney: String,
        val rechargenum: String,
        val vipid: String
)

data class MineStatusData(
        val state: String
)

data class CountPowerConfigData(
        val ad: Ad,
        val honey: Honey,
        val miner: Miner,
        val myBtf: String,
        val myPower: String
)

data class Ad(
        val max: String,
        val getPower: String,
        val outDay: String,
        val myWatch: String
)

data class Honey(
        val getPower: String,
        val maxPower: String,
        val payHoney: String
)

data class Miner(
        val MinerPower: String,
        val userSize: String
)

data class MineConfigData(
        val countPower: String,
        val currencySize: String,
        val height: String,
        val mineOverTime: String,
        val profit: String,
        val totalSize: String,
        val time: String
)

data class MineRecordListDate(
        val creatnum: String,
        val height: String,
        val hashcode: String,
        val creatsize: String,
        val creattime: String
)

data class BtfRecord(
        val creattime: String,
        val num: String,
        val ouid: String,
        val type: String
)

data class MineRecord(
        val creatnum: String,
        val creatsize: String,
        val creattime: String,
        val hashcode: String,
        val height: String
)

data class NotReceivedMineDate(
        val notgetmine: String,
        val notgetsize: String,
        val state: String,
        val timoouthour: String,
        val uid: String
)

data class MyMinerDetailsDate(
        val minerNum: String,
        val minerPower: String,
        val minerList: List<Minerlevel1>,
        val minerlevel2: List<Minerlevel1>
)

data class Minerlevel1(
        val creatTime: String,
        val phone: String,
        val power: String,
        val state: String,
        val upPower: String
)

data class MyPowerDetailsDate(
        val starttime: String,
        val counttype: String,
        val power: String
)


data class TradeCenterInfoData(
        val bigOrderSelect: List<BigOrderSelect>,
        val charge: String,
        val bigGuidePrice: String,
        val littleGuidePrice: String,
        val tradingState: String,
        val littleOrderSelect: List<LittleOrderSelect>,
        val turnover: String
)

data class BigOrderSelect(
        val buynum: String,
        val id: String,
        val type: String
)

data class LittleOrderSelect(
        val buynum: String,
        val id: String,
        val type: String
)

data class TradeCenterListData(
        val charge: String,
        val creattime: String,
        val creatuid: String,
        val num: String,
        val orderid: String,
        val overtime: String,
        val overuid: String,
        val sincerity: String,
        val state: Int,
        val id: String,
        val totalprice: String,
        val type: Int,
        val unitprice: String
)

data class BeforePayJudgeData(
        val account: String,
        val bankname: String,
        val branchname: String,
        val payeenme: String,
        val paytype: Int
)

data class MyPublishListData(
        val charge: String,
        val creattime: String,
        val creatuid: String,
        val num: String,
        val orderid: String,
        val overtime: String,
        val overuid: String,
        val sincerity: String,
        val state: Int,
        val totalprice: String,
        val type: Int,
        val unitprice: String
)

data class MyBuyListData(
        val account: String,
        val bankname: String,
        val branchname: String,
        val buyeruid: String,
        val creattime: String,
        val num: String,
        val orderid: String,
        val overtime: String,
        val payeenme: String,
        val paytime: String,
        val paytype: Int,
        val selleruid: String,
        val sincerity: String,
        val state: Int,
        val totalprice: String,
        val unitprice: String
)


data class PublishInfoData(
        val guidancePrice: String,
        val tradingHoneySysteams: List<BigOrderSelect>
)

data class TradingHoneySysteam(
        val buynum: String
)

data class ReleaseOrderData(
        val keyWord: String,
        val max: String
)

data class OrderDetailData(
        val appealDet: List<AppealDet>,
        val tradeDet: TradeDet,
)

data class TradeDet(
        val account: String,
        val bankname: String,
        val branchname: String,
        val buyeruid: String,
        val creattime: String,
        val qrcode: String,
        val num: String,
        val orderid: String,
        val overtime: String,
        val payeenme: String,
        val paytime: String,
        val surplustime: String,
        val paytype: Int,
        val selleruid: String,
        val sincerity: String,
        val state: String,
        val totalprice: String,
        val unitprice: String
)

data class AppealDet(
        val appealid: Int,
        val creattime: String,
        val imgs: String,
        val orderid: String,
        val text: String,
        val type: String
)

@SuppressLint("ParcelCreator")
@Parcelize
data class MyPayWayData(
    val account: String,
    val bankname: String?,
    val branchname: String?,
    val payeenme: String,
    val paytype: Int,
    val qrcode: String,
    val uid: String
): Parcelable

data class OutBtfConfigData(
    val charge: String,
    val min: String
)

data class HoneyListData(
    val creattime: String,
    val id: Int,
    val num: String,
    val orderid: String,
    val name: String,
    val temaActive: String,
    val type: Int,
    val uid: String
)

data class TeamActivieListData(
    val name: String,
    val temaActive: String
)

data class RealNameConfigData(
        val buyUrl: String,
        val codeMore: String,
        val examplePhone: String,
        val exampleVideo: String,
        val headMore: String,
        val buyWx: String
)

data class AttBuyData(
    val buyUrl: String,
    val buyWx: String
)

data class RealStateData(
        val msg: String,
        val state: Int
)

data class TeamStrategyData(
    val more: More,
    val pushRule: List<String>,
    val reward: Reward,
    val teamRule: List<String>
)

data class More(
    val content: String,
    val id: String,
    val title: String
)

data class Reward(
    val global: String,
    val honey: String,
    val team: String
)

data class StqImgData(
    val configkey: String,
    val configval: String
)

data class ForgetData(
        val account: String,
        val password: String
)

data class ArticleData(
        val articleId: String,
        val articleType: String,
        val content: String,
        val createTime: String,
        val del: String,
        val mainImg: String,
        val remark: String,
        val solvedNumber: String,
        val status: String,
        val subImg: String,
        val title: String,
        val unsolvedNumber: String,
        val updateTime: String
)


data class BankPayment(
        val bank: String,
        val branch: String,
        val city: String,
        val code: String,
        val createTime: String,
        val createUser: String,
        val del: String,
        val idcard: String,
        val img: String,
        val memberId: String,
        val name: String,
        val paymentId: String,
        val province: String,
        val remark: String,
        val status: String,
        val type: String,
        val updateTime: String,
        val updateUser: String
)

data class WxPayment(
        val bank: String,
        val branch: String,
        val city: String,
        val code: String,
        val createTime: String,
        val createUser: String,
        val del: String,
        val idcard: String,
        val img: String,
        val memberId: String,
        val name: String,
        val paymentId: String,
        val province: String,
        val remark: String,
        val status: String,
        val type: String,
        val updateTime: String,
        val updateUser: String
)

data class ZfbPayment(
        val bank: String,
        val branch: String,
        val city: String,
        val code: String,
        val createTime: String,
        val createUser: String,
        val del: String,
        val idcard: String,
        val img: String,
        val memberId: String,
        val name: String,
        val paymentId: String,
        val province: String,
        val remark: String,
        val status: String,
        val type: String,
        val updateTime: String,
        val updateUser: String
)

data class PublishAccountData(
        val type: Int,
        val name: String,
        val isType: Boolean,
        var isSelect: Boolean,
        val paymentId: String,
        val idcard: String
)

data class GuidePriceData(
        val BLC: List<String>,
        val BLT: List<String>
)

data class PublishOrderData(
        val coin: String,
        val head: String,
        val maxQuota: String,
        val minQuota: String,
        val minNumber: String,
        val maxNumber: String,
        val nickName: String,
        val payType: String,
        val publishOrderId: String,
        val type: Int,
        val unitPrice: String,
        val totalPrice: String,
        val createTime: String,
        val usedPrice: String
)

data class AdvData(
        val coin: String,
        val coinNumber: String,
        val createTime: String,
        val publishOrderId: String,
        val publishStatus: Int,
        val surplusCoinNumber: String,
        val type: Int,
        val unitPrice: String
)

data class AdvDetailData(
        val bank: String,
        val bankIdcard: String,
        val bankName: String,
        val branch: String,
        val cancelTime: String,
        val coin: String,
        val coinNumber: String,
        val createTime: String,
        val createUser: String,
        val memberId: String,
        val minNumber: String,
        val nickName: String,
        val payType: String,
        val publishOrderId: String,
        val publishStatus: Int,
        val surplusCoinNumber: String,
        val type: Int,
        val unitPrice: String,
        val updateTime: String,
        val updateUser: String,
        val wxIdcard: String,
        val wxImg: String,
        val wxName: String,
        val zfbIdcard: String,
        val zfbImg: String,
        val zfbName: String
)

data class DealOrderData(
        val buyHead: String,
        val buyMemberId: String,
        val buyNickName: String,
        val coin: String,
        val id: String,
        val orderStatus: String,
        val sellHead: String,
        val sellMemberId: String,
        val sellNickName: String,
        val unitPrice: String,
        val createTime: String,
        val coinNumber: String,
        val totalPrice: String
)

data class DealOrderDetailData(
        val bank: String,
        val branch: String,
        val buyComplaint: String,
        val buyComplaintImg: String,
        val buyComplaintTime: String,
        val buyHead: String,
        val buyMemberId: String,
        val buyNickName: String,
        val buyPhone: String,
        val cancelTime: String,
        val coin: String,
        val coinNumber: String,
        val completionTime: String,
        val dealType: Int,
        val endTime: String,
        val id: String,
        val idcard: String,
        val img: String,
        val name: String,
        val orderNo: String,
        val orderStatus: String,
        val payTime: String,
        val payType: String,
        val sellComplaint: String,
        val sellComplaintImg: String,
        val sellComplaintTime: String,
        val sellHead: String,
        val sellMemberId: String,
        val sellNickName: String,
        val sellPhone: String,
        val systemTime: String,
        val totalPrice: String,
        val createTime: String,
        val cancelNumber: String,
        val cancelWarn: String,
        val quotaTime: String,
        val unitPrice: String
)

data class ApplyCloseBean(val bank: String = "")

data class ChargeData(
        val address: String
)

data class WithdrawPageData(
        val price: String,
        val symbol: Symbol
)

data class Symbol(
        val coin: String,
        val createTime: String,
        val del: String,
        val feeNum: String,
        val feeRate: String,
        val minNum: String,
        val name: String,
        val remark: String,
        val status: String,
        val symbolId: String,
        val updateTime: String
)

data class AssetInfoData(
        val BLC: AssetCoinData,
        val BLT: AssetCoinData,
        val GOLDEN_BEAN: AssetCoinData,
        val USDT: AssetCoinData,
        val totalValuation: String
)

data class AssetCoinData(
        val coin: String,
        val usedPrice: String,
        val valuation: String
)

data class WaitBuyOrSellData(
        val id: String,
        val money: String,
        val productName: String,
        val status: String,
        val orderId: String,
        val value: String
)

@SuppressLint("ParcelCreator")
@Parcelize
data class WaitDetailData(
        val bank: String,
        val branch: String,
        val buyPhone: String,
        val buyRefereePhone: String,
        val paidanId: String,
        val payAccount: String,
        val payImg: String,
        val payMethod: String,
        val payName: String,
        val finishTime: String,
        val payTime: String,
        val paymentList: List<Payment>,
        val productMoney: String,
        val productName: String,
        val productValue: String,
        val sellPhone: String,
        val sellRefereePhone: String,
        val usdtUsedPrice: String,
        val sellComplaint: String,
        val buyComplaint: String,
        val buyComplaintImg: String,
        val sellComplaintImg: String,
        val voucher: String
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Payment(
        val adminPhone: String,
        val adminRefereePhone: String,
        val bank: String,
        val branch: String,
        val city: String,
        val code: String,
        val createTime: String,
        val createUser: String,
        val del: String,
        val idcard: String,
        val img: String,
        val memberId: String,
        val name: String,
        val paymentId: String,
        val province: String,
        val remark: String,
        val status: String,
        val type: String,
        val updateTime: String,
        var isChoose: Boolean = false,
        val updateUser: String
) : Parcelable

data class FinishRecordData(
        val finishTime: String,
        val orderId: String,
        val paidanId: String,
        val payMethod: String,
        val productMoney: String,
        val productName: String,
        val goldenBeanRate: String,
        val usdtRate: String,
        val yieldMoney: String,
        val yieldRate: String,
        val productValue: String
)

data class FinishDetailData(
        val bank: String,
        val branch: String,
        val buyPhone: String,
        val buyRefereePhone: String,
        val finishTime: String,
        val paidanId: String,
        val payAccount: String,
        val payImg: String,
        val payMethod: String,
        val payName: String,
        val payTime: String,
        val productMoney: String,
        val productName: String,
        val productValue: String,
        val sellPhone: String,
        val sellRefereePhone: String,
        val voucher: String,
        val yieldMoney: String,
        val goldenBeanRate: String,
        val usdtRate: String,
        val yieldRate: String
)

data class PaiDanData(
        val beginMoney: String,
        val beginTime: String,
        val cycleTime: String,
        val cappingMoney: String,
        val endTime: String,
        val maxNumber: String,
        val name: String,
        val number: String,
        val productId: String,
        val productImg: String,
        val status: String,
        val blc: String,
        val failureTime: Long,
        val yieldRate: Double
)

data class YieldData(
        val shareYield: String,
        val totalYield: String,
        val yesterdayYield: String
)

data class YieldCashFlowData(
        val createTime: String,
        val flowCoin: String,
        val flowOp: String,
        val flowPrice: String,
        val flowType: String
)

data class SymbolData(
        val coin: String,
        val createTime: String,
        val del: String,
        val feeNum: Int,
        val feeRate: String,
        val minNum: Int,
        val name: String,
        val remark: String,
        val status: String,
        val symbolId: String,
        val updateTime: String
)

data class WithdrawRecordData(
        val actualPrice: String,
        val bank: String,
        val branch: String,
        val city: String,
        val code: String,
        val createTime: String,
        val del: String,
        val fee: String,
        val idcard: String,
        val memberId: String,
        val name: String,
        val orderNo: String,
        val price: String,
        val province: String,
        val remark: String,
        val response: String,
        val status: String,
        val toAddress: String,
        val txHash: String,
        val type: String,
        val updateTime: String,
        val withdrawId: String
)

data class TransferPageData(
        val type: String,
        val usedPrice: String
)

data class TransferRecordData(
        val coin: String,
        val createTime: String,
        val memberId: String,
        val price: String,
        val toMemberId: String,
        val toUid: String,
        val transferId: String,
        val updateTime: String
)

data class CashflowData(
        val createTime: String,
        val flowCoin: String,
        val flowPrice: String,
        val flowType: String,
        val name: String,
        val remark: String,
        val symbol: String
)

data class RechargeRecordData(
        val actualPrice: String,
        val createTime: String,
        val del: String,
        val fromAddress: String,
        val gas: String,
        val memberId: String,
        val orderNo: String,
        val price: String,
        val rechargeAddress: String,
        val rechargeId: String,
        val remark: String,
        val status: String,
        val txHash: String,
        val type: String,
        val updateTime: String
)