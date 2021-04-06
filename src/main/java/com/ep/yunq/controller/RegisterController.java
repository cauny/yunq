package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.User;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @classname: RegisterController
 * @Author: yan
 * @Date: 2021/3/26 23:20
 * 功能描述：实现注册功能
 **/
@Slf4j
@RestController
@CrossOrigin
public class RegisterController {

    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/register")
    public Result register(@RequestParam String username,
                           @RequestParam String phone,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String realname,
                           @RequestParam String role,
                           @RequestParam String verificationCode){

        log.info("---------------- 注册新用户 ----------------------");
        User user=new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        user.setRealname(realname);
        log.info("---------------- 验证验证码 ----------------------");

        String message=userService.verifyCode(user,verificationCode);
        if(!message.equals("验证成功")){
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证成功 ----------------------");
        message=userService.register(user,role);
        log.info("---------------- {} ----------------------",message);
        if("注册成功".equals(message)){
            return ResultUtil.buildSuccessResult(message);
        }else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @PostMapping(value = "/api/mobileRegister")
    public Result mobileRegister(@RequestParam String username,
                           @RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String role,
                           @RequestParam String verificationCode){

        log.info("---------------- 注册新用户 ----------------------");
        User user=new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(password);
        log.info("---------------- 验证验证码 ----------------------");

        String message=userService.verifyCode(user,verificationCode);
        if(!message.equals("验证成功")){
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证成功 ----------------------");
        message=userService.register(user,"学生");
        log.info("---------------- {} ----------------------",message);
        if("注册成功".equals(message)){
            return ResultUtil.buildSuccessResult(message);
        }else {
            return ResultUtil.buildFailResult(message);
        }
    }
}
