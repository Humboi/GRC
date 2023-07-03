package com.example.gh_train_app.models;

public class SaveUserTransaction {
    private String userId;
    private String postId;
    private double amount;
    private String appId;
    private String clientReference;
    private String clientTransId;
    private String description;
    private String nickName;
    private String paymentOption;
    private String walletNumber;
    private String transactionId;
    private String transactionStatus;
    private String transactionDate;

    public SaveUserTransaction(){}

    public SaveUserTransaction(String userId, String postId, double amount, String appId, String clientReference, String clientTransId, String description, String nickName, String paymentOption, String walletNumber, String transactionId, String transactionStatus, String transactionDate) {
        this.userId = userId;
        this.postId = postId;
        this.amount = amount;
        this.appId = appId;
        this.clientReference = clientReference;
        this.clientTransId = clientTransId;
        this.description = description;
        this.nickName = nickName;
        this.paymentOption = paymentOption;
        this.walletNumber = walletNumber;
        this.transactionId = transactionId;
        this.transactionStatus = transactionStatus;
        this.transactionDate = transactionDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    public String getClientTransId() {
        return clientTransId;
    }

    public void setClientTransId(String clientTransId) {
        this.clientTransId = clientTransId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
