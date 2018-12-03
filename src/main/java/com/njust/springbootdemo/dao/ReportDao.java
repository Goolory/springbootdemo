package com.njust.springbootdemo.dao;

import com.njust.springbootdemo.common.HBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ReportDao {
    @Autowired
    private HBaseService hBaseService;

    public Map<String, Map<String, String>> getAllReport() {
        return hBaseService.getResultScanner("report");
    }
}
