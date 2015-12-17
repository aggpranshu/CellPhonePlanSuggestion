package com.lochbridge.cellphoneplan.spring;

import java.io.Serializable;

/**
 * Created by PAggarwal1 on 11/28/2015.
 */
public class BillPlans implements Serializable {

    private Long id;
    private Float bill;
    private String type;
    private Float rechargeRate;
    private int rechargeValidity;
    private boolean mesg;
    private boolean internet;
    private boolean call;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getBill() {
        return bill;
    }

    public void setBill(Float bill) {
        this.bill = bill;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getRechargeRate() {
        return rechargeRate;
    }

    public void setRechargeRate(Float rechargeRate) {
        this.rechargeRate = rechargeRate;
    }

    public int getRechargeValidity() {
        return rechargeValidity;
    }

    public void setRechargeValidity(int rechargeValidity) {
        this.rechargeValidity = rechargeValidity;
    }

    public boolean isMesg() {
        return mesg;
    }

    public void setMesg(boolean mesg) {
        this.mesg = mesg;
    }

    public boolean isInternet() {
        return internet;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public boolean isCall() {
        return call;
    }

    public void setCall(boolean call) {
        this.call = call;
    }
}
