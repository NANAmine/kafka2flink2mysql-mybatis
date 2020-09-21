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
            if ("all_eop_online_ddmx".equals(table)) {
                orderDetailMapper.saveListAll(value);
            } else if ("eop_online_ddmx".equals(table)) {
                orderDetailMapper.saveListSanYa(value);
            }else if ("all_eop_online_ddmx_test".equals(table)) {
                orderDetailMapper.saveListAllJpz(value);
            }
            sqlSession.commit();
            sqlSession.close();
            LOG.error("MysqlSinkFunction commit transaction success"+table);
            System.out.println("成功插入" +topic+"--"+value.size() + "行数据");
        }
        catch (Throwable e){
            sqlSession.rollback();
            LOG.error("MysqlSinkFunction cause Exception,sqlSession transaction rollback...",e);
        }
    }
}
