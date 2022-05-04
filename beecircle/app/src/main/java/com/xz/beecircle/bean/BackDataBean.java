package com.xz.beecircle.bean;

public class BackDataBean {
    private String id;
    private String account;
    private String pics;

    public BackDataBean() {
    }

    public BackDataBean(String id, String account, String pics) {
        this.id = id;
        this.account = account;
        this.pics = pics;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }
}
