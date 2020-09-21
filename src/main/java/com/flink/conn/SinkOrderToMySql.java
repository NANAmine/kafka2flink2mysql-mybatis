package com.flink.conn;
import com.flink.entity.OrderDetail;
import com.flink.mapper.OrderDetailMapper;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2020/7/4
 */
public class SinkOrderToMySql extends RichSinkFunction<List<OrderDetail>> {
    String table;
    String topic;
    public SinkOrderToMySql(String table,String topic) {
        this.table = table;
        this.topic = topic;
    }
    private static final Logger LOG = LoggerFactory.getLogger(SinkOrderToMySql.class);
    @Override
    public void invoke(List<OrderDetail> value, Context context) throws Exception {
        SqlSession sqlSession = MybatisSessionFactory.getSqlSessionFactory().openSession();
        OrderDetailMapper orderDetailMapper  = sqlSession.getMapper(OrderDetailMapper.class);
        try{
            //插入
            LOG.error("MysqlSinkFunction start to do insert data0"+table);
            if ("all_eop_online_ddmx".equals(table)) {
                LOG.error("MysqlSinkFunction start to do insert data1");
                orderDetailMapper.saveListAll(value);
                LOG.error("MysqlSinkFunction start to do insert data2");
            } else if ("eop_online_ddmx".equals(table)) {
                LOG.error("MysqlSinkFunction start to do insert data1");
                orderDetailMapper.saveListSanYa(value);
                LOG.error("MysqlSinkFunction start to do insert data2");
            }
            LOG.error("MysqlSinkFunction start to do insert data3");
            sqlSession.commit();
            LOG.error("MysqlSinkFunction commit transaction success");
            System.out.println("成功插入" +topic+"--"+value.size() + "行数据");
        }
        catch (Throwable e){
            sqlSession.rollback();
            LOG.error("MysqlSinkFunction cause Exception,sqlSession transaction rollback...",e);
        }
    }
}
