package com.lochbridge.cellphoneplan.spring;

import java.io.Serializable;

/**
 * Created by PAggarwal1 on 12/1/2015.
 */
public class PlanDetailsList implements Serializable {

    private CircleList circleList;

    public CircleList getCircleList() {
        return circleList;
    }

    public void setCircleList(CircleList circleList) {
        this.circleList = circleList;
    }
}
