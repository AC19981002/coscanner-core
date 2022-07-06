package com.example.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author chenzhen
 * 2022/6/14
 * email:1351170669@qq.com
 */

@Deprecated
public class JdbcConfig {
    public DataSource dataSource() {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://81.68.222.166:3306/JY_mysql");
        ds.setUsername("root");
        ds.setPassword("123456");
        return ds;
    }
}
