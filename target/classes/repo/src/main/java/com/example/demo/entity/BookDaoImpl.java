package com.example.demo.entity;

import com.example.demo.entity.impl.BookDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author chenzhen
 * 2022/6/14
 * email:1351170669@qq.com
 */

@Repository
@Scope("singleton")
public class BookDaoImpl implements BookDao {

    @Value("${name}")
    private String name;

    @Override
    public void save(){
        System.out.println("book dao save ......");
    }

    @Override
    public void update() {
        System.out.println("Book dao update ...");
    }

    @PostConstruct
    public void init () {
        System.out.println("init ..." + name);
    }

    @PreDestroy
    public void destory() {
        System.out.println("destory ...");
    }
}
