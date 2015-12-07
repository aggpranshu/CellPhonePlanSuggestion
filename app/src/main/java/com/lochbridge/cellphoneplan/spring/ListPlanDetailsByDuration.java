package com.lochbridge.cellphoneplan.spring;

import java.io.Serializable;

/**
 * Created by PAggarwal1 on 11/26/2015.
 */
public class ListPlanDetailsByDuration implements Serializable {

    private Long planID;
    private String planDetails;
    private String rechargeMaster;
    private int id;

    public Long getPlanID() {
        return planID;
    }

    public void setPlanID(Long planID) {
        this.planID = planID;
    }

    public String getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(String planDetails) {
        this.planDetails = planDetails;
    }

    public String getRechargeMaster() {
        return rechargeMaster;
    }

    public void setRechargeMaster(String rechargeMaster) {
        this.rechargeMaster = rechargeMaster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
