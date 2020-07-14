package com.flink;

/**
 * Created by Administrator on 2020/7/4.
 */
import com.flink.conn.SinkOrderToMySQL;
import com.flink.entity.OrderDetail;
import com.flink.unit.Constant;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
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
//    设置检查点时间为10秒
        env.enableCheckpointing(10000);
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

        Properties props = new Properties();
        Constant constant = new Constant();
        props.put("bootstrap.servers", constant.brokers);
        props.put("group.id", constant.kafka_group);
        props.put("enable.auto.commit", constant.commit);
        props.put("auto.offset.reset", constant.reset);
        SingleOutputStreamOperator<OrderDetail> empStream = env.addSource(new FlinkKafkaConsumer011<String>(
                //这个 kafka topic 需和生产消息的 topic 一致
                constant.topic,
                new SimpleStringSchema(),
                props)).setParallelism(Integer.parseInt(constant.parallelism))
                .map(new MapFunction<String, OrderDetail>() {
                    //解析字符串成JSON对象
                    @Override
                    public OrderDetail map(String string) throws Exception {
                        //Gson gson = new Gson();
                        String[] list = string.split(",");
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setBillno(list[2].replaceAll("\"",""));
                        orderDetail.setDjlb(list[3].replaceAll("\"",""));
                        orderDetail.setHjzje(Double.valueOf(list[4].replaceAll("\"","")));
                        orderDetail.setHjzke(Double.valueOf(list[5].replaceAll("\"","")));
                        orderDetail.setMkt(list[1].replaceAll("\"",""));
                        orderDetail.setRqsj(list[0].replaceAll("\"|\'",""));
                        return orderDetail;
                    }
                });

        //开个一分钟的窗口去聚合
        empStream.timeWindowAll(Time.seconds(Long.parseLong(constant.timeWindow))).apply(new AllWindowFunction<OrderDetail, List<OrderDetail>, TimeWindow>() {
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
