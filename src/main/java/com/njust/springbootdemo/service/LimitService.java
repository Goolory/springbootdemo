package com.njust.springbootdemo.service;

import com.njust.springbootdemo.domain.Factory;

import java.util.Map;

public interface LimitService {
    boolean putLimitData(Factory factory);
    Map<String, String> getFactoryBySid(String Sid);
}
