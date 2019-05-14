package com.example.test1;

import java.sql.Struct;

/**
 * Created by 또리또리 on 2019-02-24.
 */

public class weekLockInfo {
    private int alarm_id;
    private int alarm_op;
    private String weeknum;
    private String alarm_time;

    public weekLockInfo() {
        this.alarm_id = -1;
        this.alarm_op = -1;
        this.weeknum="None";
        this.alarm_time = "None";
    }

    public weekLockInfo(int alarm_id, int alarm_op, String week_nm, String alarm) {
        this.alarm_id = alarm_id;
        this.alarm_op = alarm_op;
        this.weeknum=week_nm;
        this.alarm_time = alarm;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public int getAlarm_op() {
        return alarm_op;
    }

    public void setAlarm_op(int alarm_op) {
        this.alarm_op = alarm_op;
    }

    public String getWeeknum() {
        return weeknum;
    }

    public void setWeeknum(String weeknum) {
        this.weeknum = weeknum;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }
}
