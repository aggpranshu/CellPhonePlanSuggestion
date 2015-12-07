package com.lochbridge.cellphoneplan.spring;

import java.io.Serializable;

public class AggregatedLogStats implements Serializable{

    private int totalCallDuringDayInSeconds;

    private double totalCallDuringDayInMinutes;

    private int totalCallDuringNightInSeconds;

    private double totalCallDuringNightInMinutes;

    private int totalCalls;

    private int smsCount;

    public int getSmsCount() {
        return smsCount;
    }

    public int getTotalCalls() {
        return totalCalls;
    }



    public int getTotalCallDuringDayInSeconds() {

        return totalCallDuringDayInSeconds;
    }

    public void setTotalCallDuringDayInSeconds(
            int totalCallDuringDayInSeconds) {

        this.totalCallDuringDayInSeconds = totalCallDuringDayInSeconds;
    }

    public double getTotalCallDuringDayInMinutes() {

        return totalCallDuringDayInMinutes;
    }

    public void setTotalCallDuringDayInMinutes(
            double totalCallDuringDayInMinutes) {

        this.totalCallDuringDayInMinutes = totalCallDuringDayInMinutes;
    }

    public int getTotalCallDuringNightInSeconds() {

        return totalCallDuringNightInSeconds;
    }

    public void setTotalCallDuringNightInSeconds(
            int totalCallDuringNightInSeconds) {

        this.totalCallDuringNightInSeconds = totalCallDuringNightInSeconds;
    }

    public double getTotalCallDuringNightInMinutes() {

        return totalCallDuringNightInMinutes;
    }

    public void setTotalCallDuringNightInMinutes(
            double totalCallDuringNightInMinutes) {

        this.totalCallDuringNightInMinutes = totalCallDuringNightInMinutes;
    }

}
