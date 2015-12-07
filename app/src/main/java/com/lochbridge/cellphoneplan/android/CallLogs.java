package com.lochbridge.cellphoneplan.android;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PAggarwal1 on 9/30/2015.
 */
class CallLogs implements Serializable {
    private Long number;
    private int durationDay;
    private int durationNight;
    private Date date;
    private String callType;
    private int durationMoreThan30;
    private int durationLessThan30;
    private int duration;
    private int totalCalls;
    private String providerName;
    private int smsCount;

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    CallLogs(){}

    CallLogs(String number, String duration, Date date, String providerName) {
        this.number = Long.valueOf(number);
        this.date = date;
        this.providerName = "Vodafone";
        this.callType = "local";
    }

    public int getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(int totalCalls) {
        this.totalCalls = totalCalls;
    }

    public int getDurationMoreThan30() {
        return durationMoreThan30;
    }

    public void setDurationMoreThan30(int durationMoreThan30) {
        this.durationMoreThan30 = durationMoreThan30;
    }

    public int getDurationLessThan30() {
        return durationLessThan30;
    }

    public void setDurationLessThan30(int dutrationLessThan30) {
        this.durationLessThan30 = dutrationLessThan30;
    }

    public int getDurationDay() {
        return durationDay;
    }

    public void setDurationDay(int durationDay) {
        this.durationDay = durationDay;
    }

    public int getDurationNight() {
        return durationNight;
    }

    public void setDurationNight(int durationNight) {
        this.durationNight = durationNight;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDuration(int duration, Date d) {

        if (duration > 30) {
            durationMoreThan30 += duration;
        } else if (duration < 30) {
            durationLessThan30 += duration;
        }
        this.totalCalls += Math.ceil(Double.valueOf(duration)/60.0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        Date startTime = null;
        Date endTime = null;
        Date currentTime = null;
        try {
            startTime = dateFormat.parse("07:00");
            endTime = dateFormat.parse("23:00");
            currentTime = dateFormat.parse(dateFormat.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (currentTime.after(startTime) && currentTime.after(endTime)) {
            durationNight += duration;
        } else
            durationDay += duration;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

}
