package com.njust.springbootdemo.controller;

import com.njust.springbootdemo.domain.Alarm;
import com.njust.springbootdemo.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @RequestMapping("add")
    private boolean addAlarm(@RequestBody Map<String, Object> reqMap){
        String clean_time = reqMap.get("clean_time").toString();
        String picture_address = reqMap.get("picture_address").toString();
        String record_address = reqMap.get("record_address").toString();
        String report_way = reqMap.get("report_way").toString();

        Alarm alarm = new Alarm();
        alarm.setSid("0");  //设定只有一个set数据
        alarm.setCleanTime(clean_time);
        alarm.setPictureAddress(picture_address);
        alarm.setRecordAddress(record_address);
        alarm.setReportWay(report_way);

        boolean result = alarmService.addAlarm(alarm);
        return result;


    }
}
