package com.peigen;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by Administrator on 2020/7/4.
 */
public class SinkOrderToMySQL extends RichSinkFunction<List<OrderDetail>> {
    PreparedStatement ps;
    BasicDataSource dataSource;
    private Connection connection;

    /**
     * open() 方法中建立连接，这样不用每次 invoke 的时候都要建立连接和释放连接
     *
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        dataSource = new BasicDataSource();
        connection = getConnection(dataSource);
        String sql = "insert into eop_online_ddmx(billno, mkt, djlb, rqsj, hjzje, hjzke) values(?, ?, ?, ?, ?, ?);";
        ps = this.connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {
        super.close();
        //关闭连接和释放资源
        if (connection != null) {
            connection.close();
        }
        if (ps != null) {
            ps.close();
        }
    }

    /**
     * 每条数据的插入都要调用一次 invoke() 方法
     *
     * @param value
     * @param context
     * @throws Exception
     */
    @Override
    public void invoke(List<OrderDetail> value, Context context) throws Exception {
        //遍历数据集合
        for (OrderDetail orderDetail : value) {
            ps.setString(1, orderDetail.getBillno());
            ps.setString(2, orderDetail.getDjlb());
            ps.setString(3, orderDetail.getHjzje());
            ps.setString(4, orderDetail.getHjzke());
            ps.setString(5, orderDetail.getMkt());
            ps.setString(6, orderDetail.getRqsj());
            ps.addBatch();
        }
        //批量后执行
        int[] count = ps.executeBatch();
        System.out.println("成功了插入了" + count.length + "行数据");
    }


    private static Connection getConnection(BasicDataSource dataSource) {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        //注意，替换成自己本地的 mysql 数据库地址和用户名、密码
        dataSource.setUrl("jdbc:mysql://10.153.252.141:4000/jeesite_dev?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        //设置连接池的一些参数
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(50);
        dataSource.setMinIdle(2);

        Connection con = null;
        try {
            con = dataSource.getConnection();
            System.out.println("创建连接池：" + con);
        } catch (Exception e) {
            System.out.println("-----------mysql get connection has exception , msg = " + e.getMessage());
        }
        return con;
    }
}
