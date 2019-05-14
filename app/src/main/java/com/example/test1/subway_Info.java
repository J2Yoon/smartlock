package com.example.test1;

/**
 * Created by 또리또리 on 2019-02-15.
 */

public class subway_Info {
    private String trainNum;
    private String trainName;
    private String trainId;
    private String subwaynum;

    public subway_Info(){};

    public subway_Info(String tn, String tnm, String trainIds, String subwaynums)
    {
        trainNum=tnm;
        trainName=tn;
        trainId=trainIds;
        subwaynum=subwaynums;
    }

    public String getTrainNum() {
        return trainNum;
    }

    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getSubwaynum() {
        return subwaynum;
    }

    public void setSubwaynum(String subwaynum1) {
        this.subwaynum = subwaynum1;
    }
}
