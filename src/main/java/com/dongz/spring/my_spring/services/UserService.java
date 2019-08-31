package com.dongz.spring.my_spring.services;

import com.dongz.spring.my_spring.mvcsramework.annotation.CacheResult;
import com.dongz.spring.my_spring.mvcsramework.annotation.Service;

/**
 * @author dongzhi
 * @date 2019/8/29
 * @desc
 */
@Service
@CacheResult(key = "class-user",cacheName = "class-user-cache")
public class UserService {

    @CacheResult(key = "method-show",cacheName = "method-user-show-cache")
    public void show() {
        System.out.println("this is a user service");
    }
}
