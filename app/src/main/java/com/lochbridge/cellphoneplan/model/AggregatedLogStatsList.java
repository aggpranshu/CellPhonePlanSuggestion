package com.lochbridge.cellphoneplan.model;

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
    
}
