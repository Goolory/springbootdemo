package com.njust.springbootdemo.service.impl;

import com.njust.springbootdemo.dao.SearchDao;
import com.njust.springbootdemo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;

    @Override
    public String[] findBySidAndRoomid(String Sid, String Roomid){
        System.out.println("Impl+"+ Sid);
        return searchDao.findBySidAndRoomid(Sid, Roomid);

    };
}
