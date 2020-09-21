package com.flink;

/**
 * Created by Administrator on 2020/7/4.
 */
import com.flink.unit.Constant;
import com.flink.unit.OrderDetailStreamUnit;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author LT-0024
 */
public class KafkaSinkMysql {
    public static final String STRING = "0";
    /**
     * 使用指定类初始化日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(KafkaSinkMysql.class);
    public static void main(String[] args) throws Exception {
       // PropertyConfigurator.configure(System.getProperty("user.dir") + "/conf/log4j.properties");
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//    设置检查点时间为10秒
        env.enableCheckpointing(60000);
//    设置检查模式  恰好一次
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//    设置检查点之间的最小暂停时间
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
//    设置检查点超时 60秒
        env.getCheckpointConfig().setCheckpointTimeout(60000);
//    设置最大并发检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//    外部的检查点  保留撤销
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        Constant constant = new Constant();
        Properties props = new Properties();
        props.put("bootstrap.servers", constant.brokers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("enable.auto.commit", constant.commit);
        props.put("auto.offset.reset", constant.reset);
        logger.debug("开始消费kafka数据");
        //OrderDetailStreamUnit.startStream(env,constant,constant.topic1,constant.table1,constant.kafka_group1,props);
        OrderDetailStreamUnit.startStream(env,constant,constant.topic1,constant.table3,constant.kafka_group2,props);
        OrderDetailStreamUnit.startStream(env,constant,constant.topic2,constant.table3,constant.kafka_group3,props);
        OrderDetailStreamUnit.startStream(env,constant,constant.topic3,constant.table3,constant.kafka_group4,props);
        OrderDetailStreamUnit.startStream(env,constant,constant.topic4,constant.table3,constant.kafka_group5,props);
        try {
            env.execute("flink kafka to Mysql");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
