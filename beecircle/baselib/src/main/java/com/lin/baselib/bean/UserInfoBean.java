package com.lin.baselib.bean;


public class UserInfoBean {


    /**
     * code : 1001
     * data : {"headImg":"","id":"6","ifAudit":1,"mobile":"13297532981","nickname":"13297532981","storeId":"3","storeType":1,"token":"a25ee198f99d441cb2694bfe60660ed7","userType":1}
     * ifsuccess : true
     * message : 请求成功
     */

    private int code;
    private DataBean data;
    private boolean ifsuccess;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isIfsuccess() {
        return ifsuccess;
    }

    public void setIfsuccess(boolean ifsuccess) {
        this.ifsuccess = ifsuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * headImg :
         * id : 6
         * ifAudit : 1
         * mobile : 13297532981
         * nickname : 13297532981
         * storeId : 3
         * storeType : 1
         * token : a25ee198f99d441cb2694bfe60660ed7
         * userType : 1
         */

        private String headImg;
        private String id;
        private int ifAudit;
        private String mobile;
        private String nickname;
        private String storeId;
        private int storeType;
        private String token;
        private int userType;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIfAudit() {
            return ifAudit;
        }

        public void setIfAudit(int ifAudit) {
            this.ifAudit = ifAudit;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public int getStoreType() {
            return storeType;
        }

        public void setStoreType(int storeType) {
            this.storeType = storeType;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
