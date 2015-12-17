
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


    /**
     * @return the smsCount
     */
    public int getSmsCount() {

        return smsCount;
    }



    /**
     * @param smsCount the smsCount to set
     */
    public void setSmsCount(int smsCount) {

        this.smsCount = smsCount;
    }


    /**
     * @return the ldsInSeconds
     */
    public int getLdsInSeconds() {

        return ldsInSeconds;
    }


    /**
     * @param ldsInSeconds the ldsInSeconds to set
     */
    public void setLdsInSeconds(int ldsInSeconds) {

        this.ldsInSeconds = ldsInSeconds;
    }


    /**
     * @return the lddInSeconds
     */
    public int getLddInSeconds() {

        return lddInSeconds;
    }


    /**
     * @param lddInSeconds the lddInSeconds to set
     */
    public void setLddInSeconds(int lddInSeconds) {

        this.lddInSeconds = lddInSeconds;
    }


    /**
     * @return the lnsInSeconds
     */
    public int getLnsInSeconds() {

        return lnsInSeconds;
    }


    /**
     * @param lnsInSeconds the lnsInSeconds to set
     */
    public void setLnsInSeconds(int lnsInSeconds) {

        this.lnsInSeconds = lnsInSeconds;
    }


    /**
     * @return the lndInSeconds
     */
    public int getLndInSeconds() {

        return lndInSeconds;
    }


    /**
     * @param lndInSeconds the lndInSeconds to set
     */
    public void setLndInSeconds(int lndInSeconds) {

        this.lndInSeconds = lndInSeconds;
    }


    /**
     * @return the sdsInSeconds
     */
    public int getSdsInSeconds() {

        return sdsInSeconds;
    }


    /**
     * @param sdsInSeconds the sdsInSeconds to set
     */
    public void setSdsInSeconds(int sdsInSeconds) {

        this.sdsInSeconds = sdsInSeconds;
    }


    /**
     * @return the sddInSeconds
     */
    public int getSddInSeconds() {

        return sddInSeconds;
    }


    /**
     * @param sddInSeconds the sddInSeconds to set
     */
    public void setSddInSeconds(int sddInSeconds) {

        this.sddInSeconds = sddInSeconds;
    }


    /**
     * @return the snsInSeconds
     */
    public int getSnsInSeconds() {

        return snsInSeconds;
    }


    /**
     * @param snsInSeconds the snsInSeconds to set
     */
    public void setSnsInSeconds(int snsInSeconds) {

        this.snsInSeconds = snsInSeconds;
    }


    /**
     * @return the sndInSeconds
     */
    public int getSndInSeconds() {

        return sndInSeconds;
    }


    /**
     * @param sndInSeconds the sndInSeconds to set
     */
    public void setSndInSeconds(int sndInSeconds) {

        this.sndInSeconds = sndInSeconds;
    }

}
