package com.flink.conn;
import com.flink.entity.OrderDetail;
import com.flink.mapper.OrderDetailMapper;
import com.flink.unit.Constant;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.flink.configuration.Configuration;
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
    private static final Logger LOG = LoggerFactory.getLogger(SinkOrderToMySql.class);
    @Override
    public void invoke(List<OrderDetail> value, Context context) throws Exception {
        SqlSession sqlSession = MybatisSessionFactory.getSqlSessionFactory().openSession();
        OrderDetailMapper orderDetailMapper  = sqlSession.getMapper(OrderDetailMapper .class);

        try{
            //插入
            LOG.info("MysqlSinkFunction start to do insert data...");
            orderDetailMapper.saveListAll(value);
            sqlSession.commit();
            LOG.info("MysqlSinkFunction commit transaction success...");
            System.out.println("成功了插入了" + value.size() + "行数据");
        }
        catch (Throwable e){
            sqlSession.rollback();
            LOG.error("MysqlSinkFunction cause Exception,sqlSession transaction rollback...",e);
        }
    }
}
