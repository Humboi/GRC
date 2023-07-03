package com.example.gh_train_app.models;

public class RecieveMoney {
    private double amount;
    private String appid;
    private String clientreference;
    private String clienttransid;
    private String description;
    private String nickname;
    private String paymentoption;
    private String walletnumber;


    public RecieveMoney(){}

    public RecieveMoney(double amount, String appid, String clientreference, String clienttransid, String description, String nickname, String paymentoption, String walletnumber) {
        this.amount = amount;
        this.appid = appid;
        this.clientreference = clientreference;
        this.clienttransid = clienttransid;
        this.description = description;
        this.nickname = nickname;
        this.paymentoption = paymentoption;
        this.walletnumber = walletnumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getClientreference() {
        return clientreference;
    }

    public void setClientreference(String clientreference) {
        this.clientreference = clientreference;
    }

    public String getClienttransid() {
        return clienttransid;
    }

    public void setClienttransid(String clienttransid) {
        this.clienttransid = clienttransid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPaymentoption() {
        return paymentoption;
    }

    public void setPaymentoption(String paymentoption) {
        this.paymentoption = paymentoption;
    }

    public String getWalletnumber() {
        return walletnumber;
    }

    public void setWalletnumber(String walletnumber) {
        this.walletnumber = walletnumber;
    }
}
