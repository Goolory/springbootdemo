package com.njust.springbootdemo.dao;

import com.njust.springbootdemo.common.HBaseService;
import com.njust.springbootdemo.domain.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LimitDao {
    @Autowired
    private HBaseService hBaseService;

    public boolean putLimitData(Factory factory){
        String tem = factory.getRoom().replace('{', ' ').replace('}', ' ').replace('=', ',');
        System.out.println(tem);
        String[] Rooms = tem.split(",");
        System.out.println(Rooms.length/2);
        List<String> listRoomname = new ArrayList<String>();
        List<String> listRoomnum = new ArrayList<String>();

        for (int i = 0; i<Rooms.length/2; i++) {
            listRoomname.add(Rooms[i*2].trim());
            listRoomnum.add(Rooms[i*2+1].trim());
        }
        int size = listRoomname.size();
        String[] Roomname = (String[])listRoomname.toArray(new String[size]);
        String[] Roomnum = (String[])listRoomnum.toArray(new String[size]);

        hBaseService.putData("factory", factory.getSid(), "change", Roomname, Roomnum);
        String[] columns = {"Fcatoryname","Stationnum","Roomnum","Station",};
        String[] value = {factory.getFactoryname(), factory.getStationnum(), factory.getRoomnum(), factory.getStation()};
        hBaseService.putData("factory", factory.getSid(), "base", columns, value);
        String[] Stationexplain = {"Stationexplain"};
        String[] StationexplainValue = {factory.getStationexplain()};
        hBaseService.putData("factory", factory.getSid(), "change", Stationexplain, StationexplainValue);
        return true;
    }

    private static String[] insert(String[] arr, int i, String str) {
        System.out.println(i);
        System.out.println(str);
        String[] arr1 = new String[arr.length+1];
        arr1[i] = str;
        return arr1;
    }

    public Map<String, String> getFactoryBySid(String Sid){
        return hBaseService.getRowData("factory", Sid);
    }
}
