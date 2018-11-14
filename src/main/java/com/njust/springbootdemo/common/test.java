package com.njust.springbootdemo.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class test {

    static private HBaseService baseService;
    public static void main(String[] args) {
        baseService.getResultScanner("report");

    }
}
