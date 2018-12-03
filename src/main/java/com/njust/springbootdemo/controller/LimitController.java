package com.njust.springbootdemo.controller;

import com.njust.springbootdemo.domain.Factory;
import com.njust.springbootdemo.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/limit")
public class LimitController {
    @Autowired
    private LimitService limitService;
    @ResponseBody
    @RequestMapping(value = "postFactory")
    private boolean putLimitData(@RequestBody Map<String, Object> reqMap){
        String Sid = reqMap.get("Sid").toString();
        String Factoryname = reqMap.get("Factoryname").toString();
        String Station = reqMap.get("Station").toString();
        String Roomnum = reqMap.get("Roomnum").toString();
        String Rooms = reqMap.get("Rooms").toString();
        String Stationnum = reqMap.get("Stationnum").toString();
        String Stationexplain = reqMap.get("Stationexplain").toString();

        Factory factory = new Factory();
        factory.setSid(Sid);
        factory.setFactoryname(Factoryname);
        factory.setStation(Station);
        factory.setRoom(Rooms);
        factory.setRoomnum(Roomnum);
        factory.setStationnum(Stationnum);
        factory.setStationexplain(Stationexplain);
        System.out.println(Rooms);
        boolean result = limitService.putLimitData(factory);

        return result;
    }


    @RequestMapping(value = "findBySid")
    private Map<String, String> getFactoryBySid(@RequestParam("Sid") String Sid){
        System.out.println(Sid);
        return limitService.getFactoryBySid(Sid);
    }


}
