package com.flink.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * @Author LT-0024
 * @Date 2020/7/13 16:46
 * @Version 1.0
 */
public class Constant {
    public  final String brokers = PropertiesUnit.getStringByKey("brokers", "config.properties");
    public  final String kafka_group1 = PropertiesUnit.getStringByKey("kafka_group1", "config.properties");
    public  final String kafka_group2 = PropertiesUnit.getStringByKey("kafka_group2", "config.properties");
    public  final String kafka_group3 = PropertiesUnit.getStringByKey("kafka_group3", "config.properties");
    public  final String kafka_group4 = PropertiesUnit.getStringByKey("kafka_group4", "config.properties");
    public  final String topic1 = PropertiesUnit.getStringByKey("topic1", "config.properties");
    public  final String topic2 = PropertiesUnit.getStringByKey("topic2", "config.properties");
    public  final String topic3 = PropertiesUnit.getStringByKey("topic3", "config.properties");
    public  final String table1 = PropertiesUnit.getStringByKey("table1", "config.properties");
    public  final String table2 = PropertiesUnit.getStringByKey("table2", "config.properties");
    public  final String jdbc = PropertiesUnit.getStringByKey("jdbc", "config.properties");
    public  final String name = PropertiesUnit.getStringByKey("name", "config.properties");
    public  final String password = PropertiesUnit.getStringByKey("password", "config.properties");
    public  final String commit = PropertiesUnit.getStringByKey("enable.auto.commit", "config.properties");
    public  final String reset = PropertiesUnit.getStringByKey("auto.offset.reset", "config.properties");
    public  final String parallelism = PropertiesUnit.getStringByKey("Parallelism", "config.properties");
    public  final String timeWindow = PropertiesUnit.getStringByKey("timeWindow", "config.properties");
    public  final String initialSiz = PropertiesUnit.getStringByKey("dataSource.setInitialSiz", "config.properties");
    public  final String maxTota = PropertiesUnit.getStringByKey("dataSource.setMaxTota", "config.properties");
    public  final String minIdle = PropertiesUnit.getStringByKey("dataSource.setMinIdle", "config.properties");
    public  final String startFromTimestamp = PropertiesUnit.getStringByKey("startFromTimestamp", "config.properties");
}
