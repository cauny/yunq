package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.User;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @classname: RegisterController
 * @Author: yan
 * @Date: 2021/3/26 23:20
 * 功能描述：实现注册功能
 **/
public class RegisterController {

    @Autowired
    UserService userService;


    @PostMapping(value = "/api/register")
    public Result register(@RequestBody User user){

        String username = user.getUsername();
        String password = user.getPassword();


        String message=userService.register(user);
        if("注册成功".equals(message)){
            return ResultUtil.buildSuccessResult(message);
        }else {
            return ResultUtil.buildFailResult(message);
        }
    }
}
