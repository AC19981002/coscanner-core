package com.example.demo.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author chenzhen
 * 2022/6/22
 * email:1351170669@qq.com
 */

@Component
@Aspect
public class MyAdvice {

    @Pointcut("execution(Void com.example.demo.entity.impl.BookDao.update())")
    private void pt() {}

    @Before("pt()")
    public void method() {
        System.out.println(System.currentTimeMillis());
    }

}
