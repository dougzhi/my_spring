package com.dongz.spring.my_spring.mvcsramework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dongzhi
 * @date 2019/8/29
 * @desc 自定义缓存
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface CacheResult {
    //缓存的键值
    String key();

    String cacheName();
    //备份缓存名字
    String backupCacheName() default "";
    //是否需要布隆过滤器
    boolean needBloomFilter() default false;
    //是否需要分布式锁
    boolean needLock() default false;
}
