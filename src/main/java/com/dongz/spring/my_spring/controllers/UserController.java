package com.dongz.spring.my_spring.controllers;

import com.dongz.spring.my_spring.mvcsramework.annotation.Autowired;
import com.dongz.spring.my_spring.mvcsramework.annotation.Controller;
import com.dongz.spring.my_spring.mvcsramework.annotation.RequestMapping;
import com.dongz.spring.my_spring.mvcsramework.annotation.RequestParam;
import com.dongz.spring.my_spring.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dongzhi
 * @date 2019/8/29
 * @desc
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/all")
    public void query(HttpServletRequest req, HttpServletResponse resp, @RequestParam String name) {

    }
}
