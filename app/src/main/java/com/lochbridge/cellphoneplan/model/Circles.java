
package com.lochbridge.cellphoneplan.model;

import java.io.Serializable;

/**
 * Created by PAggarwal1 on 11/24/2015.
 */
public class Circles implements Serializable {

    private String circleID;
    private String circleName;

    public String setCircleID() {
        return circleID;
    }

    public void setCircleID(String circleID) {
        this.circleID = circleID;
    }

    public String getCircleName() {
        return circleName;
    }

}
