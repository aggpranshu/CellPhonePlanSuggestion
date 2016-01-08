package com.lochbridge.cellphoneplan.spring;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PAggarwal1 on 12/1/2015.
 */
public class PlanDetailsList implements Serializable {
    private List<PlanDetails> listPlanDetails;

    public List<PlanDetails> getListPlanDetails() {
        return listPlanDetails;
    }

    public void setListPlanDetails(List<PlanDetails> listPlanDetails) {
        this.listPlanDetails = listPlanDetails;
    }
}
