package com.njust.springbootdemo.service.impl;

import com.njust.springbootdemo.dao.ReportDao;
import com.njust.springbootdemo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportDao reportDao;
    @Override
    public Map<String, Map<String, String>> getAllReport() {
        return reportDao.getAllReport();
    }
}
