
package com.lochbridge.cellphoneplan.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PAggarwal1 on 9/30/2015.
 */
public class CallLogs implements Serializable {
    private Long number;
    private int durationDay;
    private int durationNight;
    private int totalCalls;
    private int durationInSeconds;
    private int smsCount;

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public CallLogs(String number) {
        this.number = Long.valueOf(number);
    }


    public int getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(int totalCalls) {
        this.totalCalls = totalCalls;
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

    public void setDuration(int duration, Date d) {

        durationInSeconds += duration;

        this.totalCalls += Math.ceil(Double.valueOf(duration) / 60.0);

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

}
