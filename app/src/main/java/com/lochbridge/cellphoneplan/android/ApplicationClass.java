package com.lochbridge.cellphoneplan.android;

import android.app.Application;

import org.springframework.util.StringUtils;

/**
 * Created by PAggarwal1 on 11/2/2015.
 */
public class ApplicationClass extends Application {

    private String days;

    private String circleName;

    private  String providerName;
    private  int dataUsage;

    public int getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(int dataUsage) {
        if(dataUsage==1){
            this.dataUsage = 50;
        }
        if(dataUsage==2){
            this.dataUsage = 250;
        }
        if(dataUsage==3){
            this.dataUsage = 750;
        }
        if(dataUsage==4){
            this.dataUsage = 1500;
        }
        if(dataUsage==5){
            this.dataUsage = 2500;
        }
        if(dataUsage==6){
            this.dataUsage = 3500;
        }
        if(dataUsage==7){
            this.dataUsage = 4500;
        }
        if(dataUsage==8){
            this.dataUsage = 5000;
        }
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        String[] start = StringUtils.split(days, " ");
        this.days = start[0];
    }

}
