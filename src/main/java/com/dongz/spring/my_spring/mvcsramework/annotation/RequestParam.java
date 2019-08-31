package com.dongz.spring.my_spring.mvcsramework.annotation;

import java.lang.annotation.*;

/**
 * @author dongzhi
 * @date 2019/8/30
 * @desc
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value() default "";
}
