package com.lin.baselib.Base;

public class CheckVersionBean {

    /**
     * code : 200
     * data : {"address":"http://192.168.101.184:8884/api/personal/ios/reg/link","update":"Y","version":"1.0.0","content":"更新"}
     * msg : 返回结果
     * success : true
     */

    private int code;
    private DataBean data;
    private String msg;
    private boolean success;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * address : http://192.168.101.184:8884/api/personal/ios/reg/link
         * update : Y
         * version : 1.0.0
         * content : 更新
         */

        private String address;
        private String update;
        private String version;
        private String content;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
