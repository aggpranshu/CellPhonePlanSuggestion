package com.lochbridge.cellphoneplan.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PAggarwal1 on 11/30/2015.
 */
public class BillPlansList implements Serializable {

    private List<BillPlans> bill;

    public List<BillPlans> getBill() {
        return bill;
    }

    public void setBill(List<BillPlans> bill) {
        this.bill = bill;
    }
}
