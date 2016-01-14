package com.lochbridge.cellphoneplan.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

/**
 * Created by PAggarwal1 on 11/30/2015.
 */
public class BillPlansList implements Serializable {

    private HashSet<BillPlans> bill;

    public HashSet<BillPlans> getBill() {
        return bill;
    }

    public void setBill(HashSet<BillPlans> bill) {
        this.bill = bill;
    }
}
