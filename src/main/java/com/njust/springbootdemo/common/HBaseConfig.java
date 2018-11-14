package com.njust.springbootdemo.common;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HBaseConfig {
    @Value("HBase.nodes")
    private String node;

    @Value("${HBase.maxsize}")
    private String maxsize;

    @Value("${HBase.port}")
    private String port;

    @Value("${HBase.master}")
    private String master;


    @Bean
    public HBaseService getHbaseService(){
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "127.0.0.1");
        conf.set("hbase.client.keyvalue.maxsize", "50000");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.master", "127.0.0.1:7777");

        return new HBaseService(conf);
    }
}
