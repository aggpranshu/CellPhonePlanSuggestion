package com.lochbridge.cellphoneplan.spring;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PAggarwal1 on 11/26/2015.
 */
public class ListPlanDetailsByDurationList implements Serializable {

    List<ListPlanDetailsByDuration> listPlanDetailsByDuration;

    public List<ListPlanDetailsByDuration> getListPlanDetailsByDuration() {
        return listPlanDetailsByDuration;
    }

    public void setListPlanDetailsByDuration(List<ListPlanDetailsByDuration> listPlanDetailsByDuration) {
        this.listPlanDetailsByDuration = listPlanDetailsByDuration;
    }
}
