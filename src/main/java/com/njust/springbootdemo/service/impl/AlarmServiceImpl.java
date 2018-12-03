package com.njust.springbootdemo.service.impl;

import com.njust.springbootdemo.dao.AlarmDao;
import com.njust.springbootdemo.domain.Alarm;
import com.njust.springbootdemo.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmServiceImpl implements AlarmService {
    @Autowired
    private AlarmDao alarmDao;
    @Override
    public boolean addAlarm(Alarm alarm){
        boolean result = alarmDao.addAlarm(alarm);
        return result;
    }
}
