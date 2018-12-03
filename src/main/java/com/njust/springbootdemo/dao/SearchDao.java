package com.njust.springbootdemo.dao;

import com.njust.springbootdemo.common.HBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class SearchDao {

    @Autowired
    private HBaseService hBaseService;

    public String[] findBySidAndRoomid(String Sid, String Roomid){
        System.out.println("SearchDao");
        String[] num = null;
        try {
            num = hBaseService.findnum_according_sid_roomid("display",Sid, Roomid);
            System.out.println(num);
        } finally {
            return num;
        }


    }
}
