package com.njust.springbootdemo.dao;

import com.njust.springbootdemo.common.HBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ReportDao {
    private Logger log = LoggerFactory.getLogger(ReportDao.class);
    @Autowired
    private HBaseService hBaseService;

    public Map<String, Map<String, String>> getAllReport() {
        log.debug("ddd");
        return hBaseService.getResultScanner("report");
    }
}
