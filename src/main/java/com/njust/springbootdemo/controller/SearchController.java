package com.njust.springbootdemo.controller;


import com.njust.springbootdemo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/find")
    private String[] find(@RequestParam("Sid") String Sid, @RequestParam("Roomid") String Roomid){
        System.out.println(Sid+Roomid);
        return  searchService.findBySidAndRoomid(Sid, Roomid);

    }
}
