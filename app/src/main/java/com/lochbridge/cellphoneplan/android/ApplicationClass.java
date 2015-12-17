
package com.lochbridge.cellphoneplan.android;

import org.springframework.util.StringUtils;

import android.app.Application;

/**
 * Created by PAggarwal1 on 11/2/2015.
 */
public class ApplicationClass extends Application {

    private String days;

    private String circleName;

    private String providerName;

    private String dataUsage;

    public String getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(String dataUsage) {
        this.dataUsage = dataUsage;
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
