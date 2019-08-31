package com.dongz.spring.my_spring.aspactj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;

/**
 * @author dongzhi
 * @date 2019/8/29
 * @desc
 */
//@Component
@Aspect
public class NotVeryUserfulAspect {

    @Pointcut("execution(* com.dongz.spring.my_spring.services..*.*(..))")
    public void pointCut() {
        System.out.println("this is a ");
    }

    @Before("pointCut()")
    public void before () {
        System.out.println("aop -- before");
    }
}
