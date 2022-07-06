package com.example.demo.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author chenzhen
 * 2022/6/14
 * email:1351170669@qq.com
 */
@Configuration
@ComponentScan("com.example.demo")
@PropertySource({"jdbc.properties"})
@Import({JdbcTemplate.class,MybatisConfig.class})
@EnableAspectJAutoProxy
public class SpringConfig {

}
