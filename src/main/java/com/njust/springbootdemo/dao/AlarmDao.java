package com.njust.springbootdemo.dao;

import com.njust.springbootdemo.common.HBaseService;
import com.njust.springbootdemo.domain.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AlarmDao {
    @Autowired
    private HBaseService hBaseService;
    private Logger log = LoggerFactory.getLogger(AlarmDao.class);

    public boolean addAlarm(Alarm alarm){
        String[] basic = {"Cleartime", "Pictureaddress", "Recordaddress", "Reportway"};
        String[] values = {alarm.getCleanTime(), alarm.getPictureAddress(), alarm.getRecordAddress(), alarm.getReportWay()};
        hBaseService.putData("set", alarm.getSid(), "basic",basic ,values);
        return true;
    }
}
