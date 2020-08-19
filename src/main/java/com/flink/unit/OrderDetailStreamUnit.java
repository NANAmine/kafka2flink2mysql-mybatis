package com.flink.unit;

import com.flink.KafkaSinkMysql;
import com.flink.conn.SinkOrderToMySql;
import com.flink.entity.OrderDetail;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author LT-0024
 * @Date 2020/8/18 11:15
 * @Version 1.0
 */
public class OrderDetailStreamUnit {

    /**
     * 使用指定类初始化日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(KafkaSinkMysql.class);
    public static final String STRING = "0";
    public static void startStream(StreamExecutionEnvironment env, Constant constant, final String topic, String table, String group, Properties props){
        props.put("group.id", group);
        FlinkKafkaConsumer011<String> stream = new FlinkKafkaConsumer011<String>(
                //这个 kafka topic 需和生产消息的 topic 一致
                topic,
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
                        String value5= list[5].replaceAll("\"","");
                        String value6= list[6].replaceAll("\"","");
                        if("".equals(value5)){
                            value5="0";
                        }
                        if("".equals(value6)){
                            value6="0";
                        }
                        orderDetail.setHjzje(Double.valueOf(value5));
                        orderDetail.setHjzke(Double.valueOf(value6));
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
                Set set = new HashSet();
                List<OrderDetail> newList = new  ArrayList();
                set.addAll(orderDetails);
                newList.addAll(set);
                if (orderDetails.size() > 0) {
                    System.out.println("10 秒内收集到"+ topic +" 的数据条数是：" + orderDetails.size());
                    System.out.println("重复数据条数" + (orderDetails.size()-newList.size()));
                    out.collect(newList);
                }
            }
        }).addSink(new SinkOrderToMySql(table,topic));
        //empStream.print(); //调度输出
    }
}
