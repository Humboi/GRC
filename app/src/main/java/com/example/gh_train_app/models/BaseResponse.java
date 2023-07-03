package com.example.gh_train_app.models;

public class BaseResponse {
    private String status;
    private String reason;
    private String transactionid;
    private String clienttransid;
    private String statusdate;
    private String clientreference;
    private String telcotransid;
    private String brandtransid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getClienttransid() {
        return clienttransid;
    }

    public void setClienttransid(String clienttransid) {
        this.clienttransid = clienttransid;
    }

    public String getStatusdate() {
        return statusdate;
    }

    public void setStatusdate(String statusdate) {
        this.statusdate = statusdate;
    }

    public String getClientreference() {
        return clientreference;
    }

    public void setClientreference(String clientreference) {
        this.clientreference = clientreference;
    }

    public String getTelcotransid() {
        return telcotransid;
    }

    public void setTelcotransid(String telcotransid) {
        this.telcotransid = telcotransid;
    }

    public String getBrandtransid() {
        return brandtransid;
    }

    public void setBrandtransid(String brandtransid) {
        this.brandtransid = brandtransid;
    }
}
