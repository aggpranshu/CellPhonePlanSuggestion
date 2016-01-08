
package com.lochbridge.cellphoneplan.model;

import java.io.Serializable;

public class AggregatedLogStats implements Serializable {
    private int ldsInSeconds;

    private int lddInSeconds;

    private int lnsInSeconds;

    private int lndInSeconds;

    private int sdsInSeconds;

    private int sddInSeconds;

    private int snsInSeconds;

    private int sndInSeconds;

    private int smsCount;

    private int totalCallDurationInSeconds;

    private int totalCalls;

    public int getLdsInSeconds() {
        return ldsInSeconds;
    }


    public int getLddInSeconds() {
        return lddInSeconds;
    }


    public int getLnsInSeconds() {
        return lnsInSeconds;
    }


    public int getLndInSeconds() {
        return lndInSeconds;
    }

    public int getSdsInSeconds() {
        return sdsInSeconds;
    }

    public int getSddInSeconds() {
        return sddInSeconds;
    }

    public int getSnsInSeconds() {
        return snsInSeconds;
    }

    public int getSndInSeconds() {
        return sndInSeconds;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public int getTotalCallDurationInSeconds() {
        return totalCallDurationInSeconds;
    }

    public void setTotalCallDurationInSeconds(int totalCallDurationInSeconds) {
        this.totalCallDurationInSeconds = totalCallDurationInSeconds;
    }

    public int getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(int totalCalls) {
        this.totalCalls = totalCalls;
    }
}
