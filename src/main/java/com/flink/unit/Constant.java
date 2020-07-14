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
    public  final String kafka_group = PropertiesUnit.getStringByKey("kafka_group", "config.properties");
    public  final String topic = PropertiesUnit.getStringByKey("topic", "config.properties");
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
}
