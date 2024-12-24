package com.yonasamare.fsc.models;

import lombok.Data;

@Data
public class ScratchCard {
    private long id;
    private String scratchCardNumber;
    private String createdDate;
    private String redeemedDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScratchCardNumber() {
        return scratchCardNumber;
    }

    public void setScratchCardNumber(String scratchCardNumber) {
        this.scratchCardNumber = scratchCardNumber;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getRedeemedDate() {
        return redeemedDate;
    }

    public void setRedeemedDate(String redeemedDate) {
        this.redeemedDate = redeemedDate;
    }

    public boolean isRedeemed() {
        return isRedeemed;
    }

    public void setRedeemed(boolean redeemed) {
        isRedeemed = redeemed;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    private boolean isRedeemed;
    private double balance;
}
