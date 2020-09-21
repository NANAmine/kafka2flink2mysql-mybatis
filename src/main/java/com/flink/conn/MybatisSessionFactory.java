package com.flink.conn;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @Author LT-0024
 * @Date 2020/9/17 17:27
 * @Version 1.0
 */
public class MybatisSessionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(MybatisSessionFactory.class);
    private static SqlSessionFactory sqlSessionFactory;
    private MybatisSessionFactory(){
        super();
    }
    public synchronized static SqlSessionFactory getSqlSessionFactory(){
        if(null==sqlSessionFactory){
            InputStream inputStream=null;
            try{
                inputStream = MybatisSessionFactory.class.getClassLoader().getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            }
            catch (Exception e){
                LOG.error("create MybatisSessionFactory read mybatis-config.xml cause Exception",e);
            }
            if(null!=sqlSessionFactory){
                LOG.error("get Mybatis sqlsession sucessed....");
            }
            else {
                LOG.error("get Mybatis sqlsession failed....");
            }
        }
        return sqlSessionFactory;
    }
}
