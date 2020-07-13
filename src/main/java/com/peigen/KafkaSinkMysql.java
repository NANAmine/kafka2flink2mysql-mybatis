package com.peigen;

/**
 * Created by Administrator on 2020/7/4.
 */
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaSinkMysql {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties props = new Properties();
        String brokers = "zmnode06:6667,zmnode07:6667,zmnode08:6667";
        String KAFKA_GROUP ="rt_xgeop";
        String topic="rt_xgeop_jxcgoodslist";
        props.put("bootstrap.servers", brokers);
        props.put("group.id", KAFKA_GROUP);
        //props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("enable.auto.commit", "true");
        props.put("auto.offset.reset", "latest");
        SingleOutputStreamOperator<OrderDetail> empStream = env.addSource(new FlinkKafkaConsumer011<String>(
                //这个 kafka topic 需和生产消息的 topic 一致
                topic,
                new SimpleStringSchema(),
                props)).setParallelism(1)
                .map(new MapFunction<String, OrderDetail>() {
                    //解析字符串成JSON对象
                    @Override
                    public OrderDetail map(String string) throws Exception {
                        //Gson gson = new Gson();
                        String[] list = string.split(",");
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setBillno(list[0]);
                        orderDetail.setDjlb(list[1]);
                        orderDetail.setHjzje(list[2]);
                        orderDetail.setHjzke(list[3]);
                        orderDetail.setMkt(list[4]);
                        orderDetail.setRqsj(list[5]);
                        return orderDetail;
                    }
                });

        //开个一分钟的窗口去聚合
        empStream.timeWindowAll(Time.seconds(10)).apply(new AllWindowFunction<OrderDetail, List<OrderDetail>, TimeWindow>() {
            @Override
            public void apply(TimeWindow window, Iterable<OrderDetail> values, Collector<List<OrderDetail>> out) throws Exception {
                ArrayList<OrderDetail> orderDetails = Lists.newArrayList(values);
                if (orderDetails.size() > 0) {
                    System.out.println("10 秒内收集到 orderDetails 的数据条数是：" + orderDetails.size());
                    out.collect(orderDetails);
                }
            }
        }).addSink(new SinkOrderToMySQL());

        empStream.print(); //调度输出
        env.execute("flink kafka to Mysql");

    }
}
