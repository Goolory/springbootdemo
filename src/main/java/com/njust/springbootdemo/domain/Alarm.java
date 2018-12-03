package com.njust.springbootdemo.domain;

public class Alarm {
    private String Sid;
    private String CleanTime;
    private String PictureAddress;
    private String RecordAddress;
    private String ReportWay;

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getCleanTime() {
        return CleanTime;
    }

    public void setCleanTime(String cleanTime) {
        CleanTime = cleanTime;
    }

    public String getPictureAddress() {
        return PictureAddress;
    }

    public void setPictureAddress(String pictureAddress) {
        PictureAddress = pictureAddress;
    }

    public String getRecordAddress() {
        return RecordAddress;
    }

    public void setRecordAddress(String recordAddress) {
        RecordAddress = recordAddress;
    }

    public String getReportWay() {
        return ReportWay;
    }

    public void setReportWay(String reportWay) {
        ReportWay = reportWay;
    }
}
