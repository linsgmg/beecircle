package com.xz.beecircle;


import com.lin.baselib.Base.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CommonService {


    /**
     * 1.用户注册
     */
    @POST("/user/userLand")
    @FormUrlEncoded
    Observable<BaseBean<Object>> register(@Field("phone") String phone,
                                          @Field("password") String password,
                                          @Field("uid") String uid,
                                          @Field("smsCode") String smsCode);

    /**
     * 2.发送短信验证码
     * <p>
     * land	| [string]|注册
     * login	| [string]|登录
     * updatepsd	| [string]|改密码
     * pay	| [string]| 支付
     */
    @POST("/unit/sendSMS")
    @FormUrlEncoded
    Observable<BaseBean<Object>> sendSMS(@Field("phone") String phone,
                                         @Field("action") String action);

    /**
     * 3.登录
     *
     * @param account
     * @param password
     * @return
     */
    @POST("/user/userLogin")
    @FormUrlEncoded
    Observable<BaseBean<LoginData>> login(@Field("phone") String phone,
                                          @Field("password") String password);

    /**
     * 4.修改密码（忘记密码）
     *
     * @return
     */
    @POST("/user/updatePsd")
    @FormUrlEncoded
    Observable<BaseBean<Object>> changeLoginPwd(@Field("phone") String phone,
                                                @Field("smsCode") String smsCode,
                                                @Field("newPsd") String newPsd,
                                                @Field("secondPsd") String secondPsd);


    /**
     * 5.获取用户信息
     *
     * @return
     */
    @POST("/user/getUserInfo")
    Observable<BaseBean<UserInfoData>> userInfo();

    /**
     * 6.上传文件
     *
     * @return
     */
    @POST("/file/upload")
    Observable<BaseBean<FileData>> uploadFile(@Body RequestBody body);
//    Observable<BaseBean<FileData>> uploadFile(@Part MultipartBody.Part file,@Field("type") String type);

    /**
     * 7.退出登录
     *
     * @return
     */
    @POST("/user/outLogin")
    Observable<BaseBean<Object>> exit();

    /**
     * 8.邀请链接uid解密
     *
     * @return
     */
    @POST("/unit/idDecrypt")
    @FormUrlEncoded
    Observable<BaseBean<Object>> codeDecrypt(@Field("code") String code);

    /**
     * 9.邀请好友
     *
     * @return
     */
    @POST("/unit/getInvitationCode")
    @FormUrlEncoded
    Observable<BaseBean<InviteFriendData>> inviteFriend(@Field("uid") String uid);

    /**
     * 10.团队升级/创建团队
     *
     * @return
     */
    @POST("/team/upTeamLevel")
    @FormUrlEncoded
    Observable<BaseBean<TeamData>> upTeamLevel(@Field("uid") String uid);

    /**
     * 11.获取我的团队
     *
     * @return
     */
    @POST("/team/myTeam")
    @FormUrlEncoded
    Observable<BaseBean<MyTeamData>> myTeam(@Field("uid") String uid, @Field("key") String key);

    /**
     * 12.获取公告内容
     *
     * @return
     */
    @POST("/notice/getNoticeByID")
    @FormUrlEncoded
    Observable<BaseBean<NoticeDetailData>> getNoticeByID(@Field("id") String id);

    /**
     * 13.获取公告列表
     *
     * @return
     */
    @POST("/notice/getNoticeList")
    @FormUrlEncoded
    Observable<BaseBean<List<NoticeListData>>> getNoticeList(@Field("page") int page,
                                                             @Field("rows") int rows,
                                                             @Field("noticeType") int noticeType,
                                                             @Field("homeKeyWord") int homeKeyWord);

    /**
     * 14.获取版本号
     *
     * @return
     */
    @POST("/system/getVersion")
    @FormUrlEncoded
    Observable<BaseBean<VersionData>> getVersion(@Field("type") String type);

    /**
     * 15.获取待领取蜂蜜列表
     *
     * @return
     */
    @POST("/honeyProduce/notReceivedHoney")
    @FormUrlEncoded
    Observable<BaseBean<List<NotReceivedHoneyData>>> notReceivedHoney(@Field("uid") String uid);

    /**
     * 16.收取蜂蜜
     *
     * @return
     */
    @POST("/honeyProduce/receivedHoney")
    @FormUrlEncoded
    Observable<BaseBean<Object>> receivedHoney(@Field("uid") String uid,
                                               @Field("keyWord") String keyWord);

    /**
     * 17.蜜源集市
     *
     * @return
     */
    @POST("/honeySystem/getHoneyList")
    @FormUrlEncoded
    Observable<BaseBean<List<BeeCenterData>>> getHoneyList(@Field("page") int page,
                                                           @Field("rows") int rows,
                                                           @Field("uid") String uid);

    /**
     * 18.我的蜜源
     *
     * @return
     */
    @POST("/honeyProduce/myHoney")
    @FormUrlEncoded
    Observable<BaseBean<List<BeeCenterData>>> myHoney(@Field("page") int page,
                                                      @Field("rows") int rows,
                                                      @Field("uid") String uid);

    /**
     * 19.过期蜜源
     *
     * @return
     */
    @POST("/honeyProduce/overHoney")
    @FormUrlEncoded
    Observable<BaseBean<List<BeeCenterData>>> overHoney(@Field("page") int page,
                                                        @Field("rows") int rows,
                                                        @Field("uid") String uid);

    /**
     * 20.购买蜜源
     *
     * @return
     */
    @POST("/honeyProduce/buyHoney")
    @FormUrlEncoded
    Observable<BaseBean<Object>> buyHoney(@Field("uid") String uid,
                                          @Field("honeyId") String honeyId,
                                          @Field("payPsd") String payPsd,
                                          @Field("payType") String payType,
                                          @Field("smsCode") String smsCode);

    /**
     * 21.随机获取一条广告
     *
     * @return
     */
    @POST("/ad/randomOne")
    Observable<BaseBean<AdData>> randomOne();

    /**
     * 22.获取我的话费充值列表
     *
     * @return
     */
    @POST("/rechargeList/phoneRechargeList")
    @FormUrlEncoded
    Observable<BaseBean<List<RechargeListData>>> phoneRechargeList(@Field("page") int page,
                                                                   @Field("rows") int rows,
                                                                   @Field("uid") String uid);

    /**
     * 23.获取我的vip充值列表
     *
     * @return
     */
    @POST("/rechargeList/vipRechargeList")
    @FormUrlEncoded
    Observable<BaseBean<List<RechargeListData>>> vipRechargeList(@Field("page") int page,
                                                                 @Field("rows") int rows,
                                                                 @Field("uid") String uid);

    /**
     * 24.获取话费参数
     *
     * @return
     */
    @POST("/rechargeSysteam/userGetRechargePhone")
    @FormUrlEncoded
    Observable<BaseBean<PhoneChargeInfoData>> userGetRechargePhone(@Field("uid") String uid);

    /**
     * 25.冲话费
     *
     * @return
     */
    @POST("/rechargeList/rechargePhone")
    @FormUrlEncoded
    Observable<BaseBean<Object>> rechargePhone(@Field("uid") String uid,
                                               @Field("account") String account,
                                               @Field("payPsd") String payPsd,
                                               @Field("buyNum") String buyNum);

    /**
     * 26.获取充值会员参数
     *
     * @return
     */
    @POST("/rechargeSysteam/userGetRechargeVip")
    @FormUrlEncoded
    Observable<BaseBean<VipChargeInfoData>> userGetRechargeVip(@Field("uid") String uid,
                                                               @Field("vipType") String vipType);


    /**
     * 27.充值vip
     *
     * @return
     */
    @POST("/rechargeList/rechargeVip")
    @FormUrlEncoded
    Observable<BaseBean<Object>> rechargeVip(@Field("uid") String uid,
                                             @Field("account") String account,
                                             @Field("payPsd") String payPsd,
                                             @Field("buyId") String buyId);

    /**
     * 28.获取用户报名->矿场状态
     *
     * @return
     */
    @POST("/mine/mineState")
    @FormUrlEncoded
    Observable<BaseBean<MineStatusData>> mineState(@Field("uid") String uid);

    /**
     * 29.报名矿场
     *
     * @return
     */
    @POST("/mine/mineSignUp")
    @FormUrlEncoded
    Observable<BaseBean<Object>> mineSignUp(@Field("uid") String uid);

    /**
     * 30.蜂蜜兑换算力
     *
     * @return
     */
    @POST("/countpower/addPowerByHoney")
    @FormUrlEncoded
    Observable<BaseBean<Object>> addPowerByHoney(@Field("uid") String uid,
                                                 @Field("num") String num);

    /**
     * 31.获取矿场算力基础参数
     *
     * @return
     */
    @POST("/countpower/getCountPowerConfig")
    @FormUrlEncoded
    Observable<BaseBean<CountPowerConfigData>> getCountPowerConfig(@Field("uid") String uid);

    /**
     * 32.获取矿场基础参数
     *
     * @return
     */
    @POST("/mine/getMineConfig")
    Observable<BaseBean<MineConfigData>> getMineConfig();

    /**
     * 33.看广告增加算力
     *
     * @return
     */
    @POST("/countpower/addPowerByAD")
    @FormUrlEncoded
    Observable<BaseBean<Object>> addPowerByAD(@Field("uid") String uid);

    /**
     * 34.我的btf-获取挖矿明细(btf明细在 个人中心-btf明细）
     *
     * @return
     */
    @POST("/mine/mineRecordList")
    @FormUrlEncoded
    Observable<BaseBean<List<MineRecordListDate>>> mineRecordList(@Field("uid") String uid,
                                                                  @Field("page") int page,
                                                                  @Field("rows") int rows);

    /**
     * 35.领取矿物
     *
     * @return
     */
    @POST("/mine/receivedMine")
    @FormUrlEncoded
    Observable<BaseBean<Object>> receivedMine(@Field("uid") String uid);

    /**
     * 36.待领取的矿物信息（未领取的btf）
     *
     * @return
     */
    @POST("/mine/notReceivedMine")
    @FormUrlEncoded
    Observable<BaseBean<NotReceivedMineDate>> notReceivedMine(@Field("uid") String uid);

    /**
     * 37.获取我的矿工详情
     *
     * @return
     */
    @POST("/mine/getMyMinerDetails")
    @FormUrlEncoded
    Observable<BaseBean<MyMinerDetailsDate>> getMyMinerDetails(@Field("uid") String uid,
                                                               @Field("page") int page,
                                                               @Field("rows") int rows,
                                                               @Field("level") String level);

    /**
     * 38.我的算力详情（挖矿我的算力）
     *
     * @return
     */
    @POST("/countpower/getMyPowerDetails")
    @FormUrlEncoded
    Observable<BaseBean<List<MyPowerDetailsDate>>> getMyPowerDetails(@Field("uid") String uid,
                                                                     @Field("page") int page,
                                                                     @Field("rows") int rows);

    /**
     * 39.获取置换中心基础参数（成交量与手续费
     *
     * @return
     */
    @POST("/tradingCenter/tradingCenterParameter")
    Observable<BaseBean<TradeCenterInfoData>> tradingCenterParameter();

    /**
     * 40.获取大/小单市场
     *
     * @return
     */
    @POST("/tradingCenter/getOrderList")
    @FormUrlEncoded
    Observable<BaseBean<List<TradeCenterListData>>> getOrderList(@Field("keyWord") String keyWord,
                                                                 @Field("orderBy") String orderBy,
                                                                 @Field("num") String num);

    /**
     * 41.卖出（购买）前判断是否有支付密码，收款方式等
     *
     * @return
     */
    @POST("/user/beforePayJudge")
    @FormUrlEncoded
    Observable<BaseBean<BeforePayJudgeData>> beforePayJudge(@Field("uid") String uid,
                                                            @Field("keyWord") String keyWord);

    /**
     * 42.获取我的发布列表
     *
     * @return
     */
    @POST("/tradingCenter/getMyReleaseList")
    @FormUrlEncoded
    Observable<BaseBean<List<TradeCenterListData>>> getMyReleaseList(@Field("uid") String uid);

    /**
     * 43.获取指导价与购买数量下拉框
     *
     * @return
     */
    @POST("/tradingCenter/getReleaseConfig")
    @FormUrlEncoded
    Observable<BaseBean<PublishInfoData>> getReleaseConfig(@Field("keyWord") String keyWord);

    /**
     * 44.发布求购
     *
     * @return
     */
    @POST("/tradingCenter/releaseOrder")
    @FormUrlEncoded
    Observable<BaseBean<ReleaseOrderData>> releaseOrder(@Field("uid") String uid,
                                                        @Field("num") String num,
                                                        @Field("unitprice") String unitprice,
                                                        @Field("keyWord") String keyWord);

    /**
     * 45.取消发布
     *
     * @return
     */
    @POST("/tradingCenter/cancelOrder")
    @FormUrlEncoded
    Observable<BaseBean<Object>> cancelOrder(@Field("id") String id,
                                             @Field("uid") String uid);

    /**
     * 45.获取我的购买列表
     *
     * @return
     */
    @POST("/tradeList/getMyBuy")
    @FormUrlEncoded
    Observable<BaseBean<List<MyBuyListData>>> getMyBuy(@Field("page") int page,
                                                       @Field("rows") int rows,
                                                       @Field("uid") String uid,
                                                       @Field("state") String state);

    /**
     * 46.获取我的卖出列表
     *
     * @return
     */
    @POST("/tradeList/getMySell")
    @FormUrlEncoded
    Observable<BaseBean<List<MyBuyListData>>> getMySell(@Field("page") int page,
                                                        @Field("rows") int rows,
                                                        @Field("uid") String uid,
                                                        @Field("state") String state);

    /**
     * 47.获取订单详情
     *
     * @return
     */
    @POST("/tradeList/getOrderDetails")
    @FormUrlEncoded
    Observable<BaseBean<OrderDetailData>> getOrderDetails(@Field("orderId") String orderId);


    /**
     * 48.提交申诉内容
     *
     * @return
     */
    @POST("/tradeList/appealOrder")
    @FormUrlEncoded
    Observable<BaseBean<Object>> appealOrder(@Field("orderId") String orderid,
                                             @Field("text") String text,
                                             @Field("imgList") String imgList,
                                             @Field("uid") String uid);

    /**
     * 49.已完成收款
     *
     * @return
     */
    @POST("/tradeList/finishOver")
    @FormUrlEncoded
    Observable<BaseBean<Object>> finishOver(@Field("orderId") String orderId);

    /**
     * 50.已完成付款
     *
     * @return
     */
    @POST("/tradeList/finishPay")
    @FormUrlEncoded
    Observable<BaseBean<Object>> finishPay(@Field("orderId") String orderId);

    /**
     * 51.卖出蜂蜜
     *
     * @return
     */
    @POST("/tradingCenter/sellHoney")
    @FormUrlEncoded
    Observable<BaseBean<Object>> sellHoney(@Field("uid") String uid,
                                           @Field("id") String id,
                                           @Field("payType") String payType,
                                           @Field("payPsd") String payPsd,
                                           @Field("smsCode") String smsCode);

    /**
     * 52.获取我的收款方式
     *
     * @return
     */
    @POST("/payType/getMyPayType")
    @FormUrlEncoded
    Observable<BaseBean<List<MyPayWayData>>> getMyPayType(@Field("uid") String uid);

    /**
     * 53.新增/修改收款方式
     *
     * @return
     */
    @POST("/payType/setCollection")
    @FormUrlEncoded
    Observable<BaseBean<Object>> setCollection(@Field("uid") String uid,
                                               @Field("payType") String paytype,
                                               @Field("account") String account,
                                               @Field("bankName") String bankname,
                                               @Field("branchName") String branchname,
                                               @Field("payeeNme") String payeenme,
                                               @Field("qrCodeUrl") String qrcode,
                                               @Field("smsCode") String smsCode);

    /**
     * 54.修改密码（个人中心
     *
     * @return
     */
    @POST("/user/updatePassword")
    @FormUrlEncoded
    Observable<BaseBean<Object>> updatePassword(@Field("uid") String uid,
                                                @Field("smsCode") String smsCode,
                                                @Field("newPsd") String newPsd,
                                                @Field("secondPsd") String secondPsd);

    /**
     * 55.修改（设置）支付密码
     *
     * @return
     */
    @POST("/user/updatePayPsd")
    @FormUrlEncoded
    Observable<BaseBean<Object>> updatePayPsd(@Field("uid") String uid,
                                              @Field("smsCode") String smsCode,
                                              @Field("newPsd") String newPsd,
                                              @Field("secondPsd") String secondPsd);


    /**
     * 56.获取Btf转出基础参数
     *
     * @return
     */
    @POST("/honeyProduce/outBtfConfig")
    Observable<BaseBean<OutBtfConfigData>> outBtfConfig();

    /**
     * 57.转出BTF
     *
     * @return
     */
    @POST("/honeyProduce/outBtf")
    @FormUrlEncoded
    Observable<BaseBean<Object>> outBtf(@Field("uid") String uid,
                                        @Field("outId") String outId,
                                        @Field("payPsd") String payPsd,
                                        @Field("smsCode") String smsCode,
                                        @Field("num") String num);

    /**
     * 58.转出蜂蜜
     *
     * @return
     */
    @POST("/honeyProduce/outHoney")
    @FormUrlEncoded
    Observable<BaseBean<Object>> outHoney(@Field("uid") String uid,
                                          @Field("outId") String outId,
                                          @Field("payPsd") String payPsd,
                                          @Field("smsCode") String smsCode,
                                          @Field("num") String num);

    /**
     * 59.获取蜂蜜转出基础参数
     *
     * @return
     */
    @POST("/honeyProduce/outHoneyConfig")
    Observable<BaseBean<OutBtfConfigData>> outHoneyConfig();

    /**
     * 60.获取蜂蜜明细
     *
     * @return
     */
    @POST("/record/honey")
    @FormUrlEncoded
    Observable<BaseBean<List<HoneyListData>>> honey(@Field("page") int page,
                                                    @Field("rows") int rows,
                                                    @Field("uid") String uid);


    /**
     * 61.获取团队活跃度明细
     *
     * @return
     */
    @POST("/record/teamActive")
    @FormUrlEncoded
    Observable<BaseBean<List<HoneyListData>>> teamActive(@Field("uid") String uid);

    /**
     * 62.获取信誉值明细
     *
     * @return
     */
    @POST("/record/sincerity")
    @FormUrlEncoded
    Observable<BaseBean<List<HoneyListData>>> sincerity(@Field("page") int page,
                                                        @Field("rows") int rows,
                                                        @Field("uid") String uid);

    /**
     * 63.获取活跃度明细
     *
     * @return
     */
    @POST("/record/active")
    @FormUrlEncoded
    Observable<BaseBean<List<HoneyListData>>> active(@Field("page") int page,
                                                     @Field("rows") int rows,
                                                     @Field("uid") String uid);

    /**
     * 64.获取BTF明细
     *
     * @return
     */
    @POST("/record/btf")
    @FormUrlEncoded
    Observable<BaseBean<List<HoneyListData>>> btf(@Field("page") int page,
                                                  @Field("rows") int rows,
                                                  @Field("uid") String uid);

    /**
     * 65.获取分享值明细
     *
     * @return
     */
    @POST("/record/shared")
    @FormUrlEncoded
    Observable<BaseBean<List<HoneyListData>>> shared(@Field("page") int page,
                                                     @Field("rows") int rows,
                                                     @Field("uid") String uid);

    /**
     * 66.修改用户头像
     *
     * @return
     */
    @POST("/user/updateHeadPic")
    @FormUrlEncoded
    Observable<BaseBean<Object>> updateHeadPic(@Field("uid") String uid,
                                               @Field("imgUrl") String imgUrl);

    /**
     * 67.修改用户昵称
     *
     * @return
     */
    @POST("/user/updateUserName")
    @FormUrlEncoded
    Observable<BaseBean<Object>> updateUserName(@Field("uid") String uid,
                                                @Field("name") String name);

    /**
     * 68.实名认证基础参数
     *
     * @return
     */
    @POST("/user/realNameConfig")
    Observable<BaseBean<RealNameConfigData>> realNameConfig();

    /**
     * 69.实名认证-认证码
     *
     * @return
     */
    @POST("/user/realName_code")
    @FormUrlEncoded
    Observable<BaseBean<Object>> realNameCode(@Field("uid") String uid,
                                              @Field("code") String code,
                                              @Field("name") String name,
                                              @Field("IdNumber") String IdNumber,
                                              @Field("phone") String phone);

    /**
     * 70.获取独立公告
     * <p>
     * <p>
     * land_yszc	| [string]|注册-隐私政策
     * land_fwtk	| [string]|注册-服务条款
     * mine_bps	| [string]|生态圈-蜜蜂矿场-白皮书
     * trading_rule	| [object]|置换中心-规则
     *
     * @return
     */
    @POST("/noticeConfig/getNotice")
    @FormUrlEncoded
    Observable<BaseBean<ArticleData>> getNotice(@Field("key") String key);

    /**
     * 71.获取购买认证码基础参数
     *
     * @return
     */
    @POST("/system/getAttBuy")
    Observable<BaseBean<AttBuyData>> getAttBuy();

    /**
     * 72.验证认证码
     *
     * @return
     */
    @POST("/user/checkCode")
    @FormUrlEncoded
    Observable<BaseBean<Object>> checkCode(@Field("code") String code);

    /**
     * 73.实名认证-手动认证
     *
     * @return
     */
    @POST("/user/realName_head")
    @FormUrlEncoded
    Observable<BaseBean<Object>> realNameHead(@Field("uid") String uid,
                                              @Field("name") String name,
                                              @Field("IdNumber") String IdNumber,
                                              @Field("IdCardPhoto") String IdCardPhoto,
                                              @Field("IdVideo") String IdVideo);

    /**
     * 74.获取审核状态
     *
     * @return
     */
    @POST("/user/getRealState")
    @FormUrlEncoded
    Observable<BaseBean<RealStateData>> getRealState(@Field("uid") String uid);

    /**
     * 75.升级攻略
     *
     * @return
     */
    @POST("/tradeList/finishAppeal")
    @FormUrlEncoded
    Observable<BaseBean<Object>> finishAppeal(@Field("uid") String uid,
                                              @Field("orderId") String orderId);

    /**
     * 76.完成申诉
     *
     * @return
     */
    @POST("/team/teamStrategy")
    @FormUrlEncoded
    Observable<BaseBean<TeamStrategyData>> teamStrategy(@Field("level") String level);

    /**
     * 77.生态圈应用图
     *
     * @return
     */
    @POST("/Admin/stqImg")
    Observable<BaseBean<List<StqImgData>>> stqImg();

}

