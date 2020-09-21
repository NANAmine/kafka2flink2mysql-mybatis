package com.flink.conn;


import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import com.flink.unit.Constant;
import org.apache.ibatis.datasource.DataSourceFactory;
import com.alibaba.druid.pool.DruidDataSource;
/**
 * @Author LT-0024
 * @Date 2020/9/17 16:35
 * @Version 1.0
 */
public class DruidDataSourceFactory implements DataSourceFactory {
    private Properties props;

    @Override
    public DataSource getDataSource() {
        Constant constant = new Constant();
        //注意，替换成自己本地的 mysql 数据库地址和用户名、密码
        DruidDataSource dds = new DruidDataSource();
        /*dds.setDriverClassName("com.mysql.jdbc.Driver");
        dds.setUrl(constant.jdbc);
        dds.setUsername(constant.name);
        dds.setPassword(constant.password);*/
        dds.setDriverClassName(this.props.getProperty("driver"));
        dds.setUrl(this.props.getProperty("url"));
        dds.setUsername(this.props.getProperty("username"));
        dds.setPassword(this.props.getProperty("password"));
        // 其他配置可以根据MyBatis主配置文件进行配置
        try {
            dds.init();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
        return dds;
    }

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }
}
