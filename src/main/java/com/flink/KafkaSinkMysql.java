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
        OrderDetailStreamUnit.startStream(env,constant,constant.topic1,constant.table2,constant.kafka_group2,props);
        OrderDetailStreamUnit.startStream(env,constant,constant.topic2,constant.table2,constant.kafka_group3,props);
        OrderDetailStreamUnit.startStream(env,constant,constant.topic3,constant.table2,constant.kafka_group4,props);
        try {
            env.execute("flink kafka to Mysql");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*FlinkKafkaConsumer011<String> stream = new FlinkKafkaConsumer011<String>(
                //这个 kafka topic 需和生产消息的 topic 一致
                constant.topic1,
                new SimpleStringSchema(),
                props);
        if(!STRING.equals(constant.startFromTimestamp)){
            stream.setStartFromTimestamp(Long.parseLong(constant.startFromTimestamp));
        }
        SingleOutputStreamOperator<OrderDetail> empStream = env.addSource(stream).setParallelism(Integer.parseInt(constant.parallelism))
                .map(new MapFunction<String, OrderDetail>() {
                    //解析字符串成JSON对象
                    @Override
                    public OrderDetail map(String string) throws Exception {
                        //Gson gson = new Gson();
                        String[] list = string.split(",");
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setBillno(list[1].replaceAll("\"",""));
                        orderDetail.setShgwkh(list[7].replaceAll("\"",""));
                        orderDetail.setDjlb(list[4].replaceAll("\"",""));
                        orderDetail.setHjzje(Double.valueOf(list[5].replaceAll("\"","")));
                        orderDetail.setHjzke(Double.valueOf(list[6].replaceAll("\"","")));
                        orderDetail.setMkt(list[3].replaceAll("\"",""));
                        orderDetail.setRqsj(list[2].replaceAll("\"|\'",""));
                        return orderDetail;
                    }
                });

        //开个一分钟的窗口去聚合
        logger.info("开启timeWindow");
        empStream.timeWindowAll(Time.seconds(Long.parseLong(constant.timeWindow))).apply(new AllWindowFunction<OrderDetail, List<OrderDetail>, TimeWindow>() {
            @Override
            public void apply(TimeWindow window, Iterable<OrderDetail> values, Collector<List<OrderDetail>> out) throws Exception {
                ArrayList<OrderDetail> orderDetails = Lists.newArrayList(values);
                //数据去重
                Set set = new  HashSet();
                List<OrderDetail> newList = new  ArrayList();
                set.addAll(orderDetails);
                newList.addAll(set);
                if (orderDetails.size() > 0) {
                    System.out.println("10 秒内收集到 orderDetails 的数据条数是：" + orderDetails.size());
                    System.out.println("重复数据条数" + (orderDetails.size()-newList.size()));
                    out.collect(newList);
                }
            }
        }).addSink(new SinkOrderToMySql());
        empStream.print(); //调度输出
        env.execute("flink kafka to Mysql");*/
    }
}
