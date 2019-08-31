package com.dongz.spring.my_spring.aspactj;

import com.dongz.spring.my_spring.mvcsramework.annotation.CacheResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author dongzhi
 * @date 2019/8/29
 * @desc
 */
//@Component
@Aspect
public class CacheResultAspect {

    @Pointcut("@annotation(com.dongz.spring.my_spring.mvcsramework.annotation.CacheResult)")
    public void annotationPointCut() {
        System.out.println("cache aspect");
    }

    @After("annotationPointCut()")
    public void after(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheResult cacheResult = method.getAnnotation(CacheResult.class);
        System.out.println("拦截器：" + cacheResult.cacheName());
    }
}
