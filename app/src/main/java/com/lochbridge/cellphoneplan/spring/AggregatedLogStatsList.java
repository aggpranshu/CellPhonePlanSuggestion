package com.lochbridge.cellphoneplan.spring;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PAggarwal1 on 11/25/2015.
 */
class AggregatedLogStatsList implements Serializable {

    private List<AggregatedLogStats> aggregatedLogStatsList;

    public List<AggregatedLogStats> getAggregatedLogStatsList() {
        return aggregatedLogStatsList;
    }

    public void setAggregatedLogStatsList(List<AggregatedLogStats> aggregatedLogStatsList) {
        this.aggregatedLogStatsList = aggregatedLogStatsList;
    }
}
