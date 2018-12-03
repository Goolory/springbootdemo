package com.njust.springbootdemo.service.impl;

import com.njust.springbootdemo.dao.LimitDao;
import com.njust.springbootdemo.domain.Factory;
import com.njust.springbootdemo.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LimitServiceImpl implements LimitService {
    @Autowired
    private LimitDao limitDao;
    @Override
    public boolean putLimitData(Factory factory) {
        boolean result = limitDao.putLimitData(factory);
        return result;
    }
    @Override
    public Map<String, String> getFactoryBySid(String Sid){
        return limitDao.getFactoryBySid(Sid);
    }
}
