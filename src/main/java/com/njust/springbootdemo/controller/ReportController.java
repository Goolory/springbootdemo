package com.njust.springbootdemo.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njust.springbootdemo.domain.User;
import com.njust.springbootdemo.service.ReportService;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {
    private Logger log = LoggerFactory.getLogger(ReportController.class);
    @Autowired
    private ReportService reportService;

    @RequestMapping("hello")
    private User index(){
        User user = new User();
        user.setUsername("xxx");
        user.setPassword("yyy");
        return null;


    }

    @RequestMapping("test")
    private JSONObject test() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", reportService.getAllReport());

//        JsonArray array=new JsonArray();
//        Map<String,Map<String,String>> mpt = reportService.getAllReport();
//
//        JsonConfig config = new JsonConfig();
//        JSONObject json = JSONObject.fromObject(reportService.getAllReport(), config);
        return jsonObject;
    }
    @RequestMapping("find")
    private Map<String, Map<String, String>> find() {
        return reportService.getAllReport();
    }
}
