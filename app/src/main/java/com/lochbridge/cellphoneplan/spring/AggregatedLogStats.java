
package com.lochbridge.cellphoneplan.spring;

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

    public void setLdsInSeconds(int ldsInSeconds) {
        this.ldsInSeconds = ldsInSeconds;
    }

    public int getLddInSeconds() {
        return lddInSeconds;
    }

    public void setLddInSeconds(int lddInSeconds) {
        this.lddInSeconds = lddInSeconds;
    }

    public int getLnsInSeconds() {
        return lnsInSeconds;
    }

    public void setLnsInSeconds(int lnsInSeconds) {
        this.lnsInSeconds = lnsInSeconds;
    }

    public int getLndInSeconds() {
        return lndInSeconds;
    }

    public void setLndInSeconds(int lndInSeconds) {
        this.lndInSeconds = lndInSeconds;
    }

    public int getSdsInSeconds() {
        return sdsInSeconds;
    }

    public void setSdsInSeconds(int sdsInSeconds) {
        this.sdsInSeconds = sdsInSeconds;
    }

    public int getSddInSeconds() {
        return sddInSeconds;
    }

    public void setSddInSeconds(int sddInSeconds) {
        this.sddInSeconds = sddInSeconds;
    }

    public int getSnsInSeconds() {
        return snsInSeconds;
    }

    public void setSnsInSeconds(int snsInSeconds) {
        this.snsInSeconds = snsInSeconds;
    }

    public int getSndInSeconds() {
        return sndInSeconds;
    }

    public void setSndInSeconds(int sndInSeconds) {
        this.sndInSeconds = sndInSeconds;
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
