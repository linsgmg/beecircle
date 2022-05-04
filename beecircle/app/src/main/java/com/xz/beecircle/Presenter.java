package com.xz.beecircle;

import com.lin.baselib.net.AbstractDialogSubscriber;
import com.lin.baselib.net.RxHelper;
import com.lin.baselib.utils.AppUtils;
import com.lin.baselib.utils.SharedPreferencesUtils;
import com.xz.beecircle.base.ModulePresenter;

import java.io.File;
import java.util.List;

import cn.ycbjie.ycstatusbarlib.utils.RomUtils;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class Presenter {

    /**
     *             dealRequest(request.getSSMCode(account), getView(), baseResponse -> getView().onGetSSMCode(baseResponse));
     *             dealRequest(request.getSSMCode(account), getView(), getView()::onGetSSMCode);
     */

    /**
     * 用户登录注册模块
     */
    public static class LoginPresenter extends ModulePresenter<CommonContact.LoginView> {

        private String uid = SharedPreferencesUtils.getString(AppUtils.getApp(), "uid", "");

        public void register(String phone, String password, String uid, String smsCode) {
            dealRequest(request.register(phone, password, uid, smsCode), getView(), getView()::onRegisterSuccess);
        }

        public void getSSMCode(String phone, String action) {
            dealRequest(request.sendSMS(phone, action), getView(), getView()::onGetSSMCode);
        }

        public void changeLoginPwd(String phone, String smsCode, String newPsd, String secondPsd) {
            dealRequest(request.changeLoginPwd(phone, smsCode, newPsd, secondPsd), getView(), getView()::onChangeLoginPwdSuccess);
        }

        public void login(String phone, String password) {
//            dealRequest(request.login(account, password), getView(), baseResponse -> getView().onLoginSuccess(baseResponse));
            dealRequest(request.login(phone, password), getView(), getView()::onLoginSuccess);
        }

        public void getUserInfo() {
            dealRequest(request.userInfo(), getView(), getView()::onGetUserInfoSuccess);
        }

        public void exit() {
            dealRequest(request.exit(), getView(), getView()::onExitSuccess);
        }

    }


    /**
     * 首页模块
     */
    public static class HomePresenter extends ModulePresenter<CommonContact.HomeView> {

        private String uid = SharedPreferencesUtils.getString(AppUtils.getApp(), "uid", "");

        public void inviteFriend(String uid) {
            dealRequest(request.inviteFriend(uid), getView(), getView()::onInviteFriendSuccess);
        }

        public void teamInfo(String uid) {
            dealRequest(request.upTeamLevel(uid), getView(), getView()::onGetTeamInfoSuccess);
        }

        public void myTeam(String uid, String key) {
            dealRequest(request.myTeam(uid, key), getView(), getView()::onGetMyTeamSuccess);
        }

        public void getNoticeDetail(String id) {
            dealRequest(request.getNoticeByID(id), getView(), getView()::onGetNoticeDetailSuccess);
        }

        public void getNoticeList(int page, int rows, int noticeType, int homeKeyWord) {
            dealRequest(request.getNoticeList(page, rows, noticeType, homeKeyWord), getView(), getView()::onGetNoticeListSuccess);
        }

        String type = "Android";

        public void getVersion() {
            dealRequest(request.getVersion(type), getView(), getView()::onGetVersionSuccess);
        }

        public void getNotReceiveHoneyList(String uid) {
            dealRequest(request.notReceivedHoney(uid), getView(), getView()::onGetNotReceiveListSuccess);
        }

        public void receiveHoney(String uid, String keyWord) {
            dealRequest(request.receivedHoney(uid, keyWord), getView(), getView()::onReceiveHoneySuccess);
        }

        public void getBeeCenterJSList(int page, int rows, String uid) {
            dealRequest(request.getHoneyList(page, rows, uid), getView(), getView()::onGetBeeCenterJSListSuccess);
        }

        public void myHoney(int page, int rows, String uid) {
            dealRequest(request.myHoney(page, rows, uid), getView(), getView()::onGetBeeCenterMyListSuccess);
        }

        public void overHoney(int page, int rows, String uid) {
            dealRequest(request.overHoney(page, rows, uid), getView(), getView()::onGetBeeCenterGQListSuccess);
        }

        public void buyHoney(String uid, String honeyId, String payPsd, String payType, String smsCode) {
            dealRequest(request.buyHoney(uid, honeyId, payPsd, payType, smsCode), getView(), getView()::onBuyHoneySuccess);
        }

        public void getAd() {
            dealRequest(request.randomOne(), getView(), getView()::onGetAdSuccess);
        }
    }

    /**
     * 生态圈模块
     */
    public static class EcospherePresenter extends ModulePresenter<CommonContact.EcosphereView> {

        private String uid = SharedPreferencesUtils.getString(AppUtils.getApp(), "uid", "");

        public void getPhoneRechargeList(int page, int rows, String uid) {
            dealRequest(request.phoneRechargeList(page, rows, uid), getView(), getView()::onGetChargePhoneListSuccess);
        }

        public void getVipRechargeList(int page, int rows, String uid) {
            dealRequest(request.vipRechargeList(page, rows, uid), getView(), getView()::onGetChargeVideoListSuccess);
        }

        public void getRechargePhone(String uid) {
            dealRequest(request.userGetRechargePhone(uid), getView(), getView()::onGetPhoneChargeInfoSuccess);
        }

        public void rechargePhone(String uid, String account, String payPsd, String buyNum) {
            dealRequest(request.rechargePhone(uid, account, payPsd, buyNum), getView(), getView()::onRechargePhoneSuccess);
        }

        public void getRechargeVip(String uid, String vipid) {
            dealRequest(request.userGetRechargeVip(uid, vipid), getView(), getView()::onGetVipChargeInfoSuccess);
        }

        public void rechargeVip(String uid, String account, String payPsd, String buyId) {
            dealRequest(request.rechargeVip(uid, account, payPsd, buyId), getView(), getView()::onRechargeVipSuccess);
        }

        public void mineState(String uid) {
            dealRequest(request.mineState(uid), getView(), getView()::onGetMineStateSuccess);
        }

        public void mineSignUp(String uid) {
            dealRequest(request.mineSignUp(uid), getView(), getView()::onMineSignUpSuccess);
        }

        public void addPowerByHoney(String uid, String num) {
            dealRequest(request.addPowerByHoney(uid, num), getView(), getView()::onAddPowerByHoneySuccess);
        }

        public void getCountPowerConfig(String uid) {
            dealRequest(request.getCountPowerConfig(uid), getView(), getView()::onGetCountPowerConfigSuccess);
        }

        public void getMineConfig() {
            dealRequest(request.getMineConfig(), getView(), getView()::onGetMineConfigSuccess);
        }

        public void addPowerByAD(String uid) {
            dealRequest(request.addPowerByAD(uid), getView(), getView()::onAddPowerByADSuccess);
        }

        public void mineRecordList(String uid, int page, int rows) {
            dealRequest(request.mineRecordList(uid, page, rows), getView(), getView()::onGetMineRecordListSuccess);
        }

        public void receivedMine(String uid) {
            dealRequest(request.receivedMine(uid), getView(), getView()::onReceivedMineSuccess);
        }

        public void notReceivedMine(String uid) {
            dealRequest(request.notReceivedMine(uid), getView(), getView()::onGetNotReceivedMineSuccess);
        }

        public void getMyMinerDetails(String uid, int page, int rows,String level) {
            dealRequest(request.getMyMinerDetails(uid, page, rows, level), getView(), getView()::onGetMyMinerDetailsSuccess);
        }

        public void getMyPowerDetails(String uid, int page, int rows) {
            dealRequest(request.getMyPowerDetails(uid, page, rows), getView(), getView()::onGetMyPowerDetailsSuccess);
        }


    }

    /**
     * 置换中心模块
     */
    public static class ChangerPresenter extends ModulePresenter<CommonContact.ChangeView> {

        private String uid = SharedPreferencesUtils.getString(AppUtils.getApp(), "uid", "");

        public void tradingCenterParameter() {
            dealRequest(request.tradingCenterParameter(), getView(), getView()::onGetTradeCenterInfoSuccess);
        }

        public void getOrderList(String keyWord, String orderBy, String num) {
            dealRequest(request.getOrderList(keyWord, orderBy, num), getView(), getView()::onGetChangeCenterListSuccess);
        }

        public void beforePayJudge(String uid, String keyWord) {
            dealRequest(request.beforePayJudge(uid, keyWord), getView(), getView()::onGetBeforePayJudgeDSuccess);
        }

        public void getMyReleaseList(String uid) {
            dealRequest(request.getMyReleaseList(uid), getView(), getView()::onGetMyPublishListSuccess);
        }

        public void getReleaseConfig(String keyWord) {
            dealRequest(request.getReleaseConfig(keyWord), getView(), getView()::onGetPublishInfoSuccess);
        }

        public void releaseOrder(String uid, String num, String unitprice, String keyWord) {
            dealRequest(request.releaseOrder(uid, num, unitprice, keyWord), getView(), getView()::onReleaseOrderSuccess);
        }

        public void cancelOrder(String id, String uid) {
            dealRequest(request.cancelOrder(id, uid), getView(), getView()::onCancelOrderSuccess);
        }

        public void getMyBuy(int page, int rows, String uid, String state) {
            dealRequest(request.getMyBuy(page, rows, uid, state), getView(), getView()::onGetMyBuyListSuccess);
        }

        public void getMySell(int page, int rows, String uid, String state) {
            dealRequest(request.getMySell(page, rows, uid, state), getView(), getView()::onGetMySellListSuccess);
        }

        public void getOrderDetails(String orderId) {
            dealRequest(request.getOrderDetails(orderId), getView(), getView()::onGetOrderDetailSuccess);
        }

        public void appealOrder(String orderid, String text, String imgList, String uid) {
            dealRequest(request.appealOrder(orderid, text, imgList, uid), getView(), getView()::onAppealOrderSuccess);
        }

        public void finishOver(String orderId) {
            dealRequest(request.finishOver(orderId), getView(), getView()::onFinishOrderSuccess);
        }

        public void finishPay(String orderId) {
            dealRequest(request.finishPay(orderId), getView(), getView()::onFinishPaySuccess);
        }

        public void sellHoney(String uid, String id, String payType, String payPsd, String smsCode) {
            dealRequest(request.sellHoney(uid, id, payType, payPsd, smsCode), getView(), getView()::onSellHoneySuccess);
        }

        public void finishAppeal(String uid, String orderId) {
            dealRequest(request.finishAppeal(uid, orderId), getView(), getView()::onFinishAppealSuccess);
        }
    }

    /**
     * 个人中心模块
     */
    public static class MinePresenter extends ModulePresenter<CommonContact.MineView> {

        private String uid = SharedPreferencesUtils.getString(AppUtils.getApp(), "uid", "");

        public void getMyPayType() {
            dealRequest(request.getMyPayType(uid), getView(), getView()::onGetMyPayTypeSuccess);
        }

        public void setCollection(String paytype, String account, String bankname, String branchname,
                                  String payeenme, String qrcode, String smsCode) {
            dealRequest(request.setCollection(uid, paytype, account, bankname, branchname, payeenme, qrcode,smsCode), getView(), getView()::onSetCollectionSuccess);
        }

        public void updatePassword(String smsCode, String newPsd, String secondPsd) {
            dealRequest(request.updatePassword(uid, smsCode, newPsd, secondPsd), getView(), getView()::onUpdatePasswordSuccess);
        }

        public void updatePayPsd(String smsCode, String newPsd, String secondPsd) {
            dealRequest(request.updatePayPsd(uid, smsCode, newPsd, secondPsd), getView(), getView()::onUpdatePayPsdSuccess);
        }

        public void outBtfConfig() {
            dealRequest(request.outBtfConfig(), getView(), getView()::onGetOutBtfConfigSuccess);
        }

        public void outBtf(String outId, String payPsd, String smsCode, String num) {
            dealRequest(request.outBtf(uid, outId, payPsd, smsCode, num), getView(), getView()::onOutBtfSuccess);
        }

        public void outHoney(String outId, String payPsd, String smsCode, String num) {
            dealRequest(request.outHoney(uid, outId, payPsd, smsCode, num), getView(), getView()::onOutHoneySuccess);
        }

        public void outHoneyConfig() {
            dealRequest(request.outHoneyConfig(), getView(), getView()::onGetOutHoneyConfigSuccess);
        }

        public void honey(int page, int rows) {
            dealRequest(request.honey(page, rows, uid), getView(), getView()::onGetHoneyListSuccess);
        }

        public void teamActive() {
            dealRequest(request.teamActive(uid), getView(), getView()::onGetTeamActiveListSuccess);
        }

        public void sincerity(int page, int rows) {
            dealRequest(request.sincerity(page, rows, uid), getView(), getView()::onGetHoneyListSuccess);
        }

        public void active(int page, int rows) {
            dealRequest(request.active(page, rows, uid), getView(), getView()::onGetHoneyListSuccess);
        }

        public void btf(int page, int rows) {
            dealRequest(request.btf(page, rows, uid), getView(), getView()::onGetHoneyListSuccess);
        }

        public void shared(int page, int rows) {
            dealRequest(request.shared(page, rows, uid), getView(), getView()::onGetHoneyListSuccess);
        }

        public void updateHeadPic(String imgUrl) {
            dealRequest(request.updateHeadPic(uid, imgUrl), getView(), getView()::onUpdateHeadPicSuccess);
        }

        public void updateUserName(String name) {
            dealRequest(request.updateUserName(uid, name), getView(), getView()::onUpdateUserNameSuccess);
        }

        public void realNameConfig() {
            dealRequest(request.realNameConfig(), getView(), getView()::onGetRealNameConfigSuccess);
        }

        public void realNameCode(String code,String name,String IdNumber,String phone) {
            dealRequest(request.realNameCode(uid, code, name, IdNumber, phone), getView(), getView()::onRealNameCodeSuccess);
        }

        public void getNotice(String key) {
            dealRequest(request.getNotice(key), getView(), getView()::onGetNoticeSuccess);
        }

        public void getAttBuy() {
            dealRequest(request.getAttBuy(), getView(), getView()::onGetAttBuySuccess);
        }

        public void checkCode(String code) {
            dealRequest(request.checkCode(code), getView(), getView()::onCheckCodeSuccess);
        }

        public void realNameHead(String name,String IdNumber,String IdCardPhoto,String IdVideo) {
            dealRequest(request.realNameHead(uid, name, IdNumber, IdCardPhoto, IdVideo), getView(), getView()::onRealNameHeadSuccess);
        }

        public void getRealState() {
            dealRequest(request.getRealState(uid), getView(), getView()::onGetRealStateSuccess);
        }

        public void teamStrategy(String level) {
            dealRequest(request.teamStrategy(level), getView(), getView()::onGetTeamStrategySuccess);
        }

        public void stqImg() {
            dealRequest(request.stqImg(), getView(), getView()::onGetImgListSuccess);
        }
    }

    /**
     * 上传文件（图片/视频）
     */
    public static class UpLoadPresenter extends ModulePresenter<CommonContact.UploadFileView> {

        public void uploadFile(List<String> pathList, String type) {

            // 2. 采用Observable<...>形式 对 网络请求 进行封装
            if (pathList.size() > 0) {

                for (int i = 0; i < pathList.size(); i++) {
                    File file = new File(pathList.get(i));
                    RequestBody requestBody;
                    if (pathList.get(i).contains("mp4")) {
                        requestBody = RequestBody.create(MediaType.parse("video/mp4"), file);
                    } else {
                        requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                    }
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    MultipartBody multipartBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type", type)
                            .addFormDataPart("file", file.getName(), requestBody)
                            .build();
//                    Observable<BaseBean<FileData>> observable = request.uploadFile(body,type);
                    Observable<BaseBean<FileData>> observable = request.uploadFile(multipartBody);
                    observable.compose(RxHelper.observableIO2Main(AppUtils.getApp()))
                            .safeSubscribe(new AbstractDialogSubscriber<BaseBean<FileData>>(getView()) {
                                @Override
                                public void onNext(BaseBean<FileData> bean) {
                                    if (bean.getCode() == 200) {
                                        getView().onUploadFileSuccess(bean);
                                    }
                                }
                            });
                }

            }
        }

    }
}
