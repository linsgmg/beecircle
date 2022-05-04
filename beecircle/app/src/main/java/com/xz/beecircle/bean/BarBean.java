package com.xz.beecircle.bean;

public class BarBean {
    private String date;
    private double money;
    private double count;

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    private boolean isUp;

    public BarBean(String date, double money, double count) {
        this.date = date;
        this.money = money;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
