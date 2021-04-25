package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.domain.entity.UserBasicInfo;
import com.ep.yunq.domain.service.UserInfoService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @classname: RegisterController
 * @Author: yan
 * @Date: 2021/3/26 23:20
 * 功能描述：实现注册功能
 **/
@Api(tags = "注册")
@Slf4j
@RestController
@CrossOrigin
public class RegisterController {

    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;

    @CrossOrigin
    @ApiOperation("web端注册")
    @PostMapping(value = "/api/register")
    public Result register(@RequestParam String username,
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

        String message=userService.verifyCode(phone,verificationCode);
        if(!message.equals("验证成功")){
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证成功 ----------------------");
        message=userService.register(user,role);
        log.info("---------------- {} ----------------------",message);
        if("注册成功".equals(message)){
            return ResultUtil.buildSuccessResult(message,null);
        }else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("移动端注册")
    @PostMapping(value = "/api/mobileRegister")
    public Result mobileRegister(@RequestParam String username,
                           @RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String verificationCode){

        log.info("---------------- 注册新用户 ----------------------");
        User user=new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(password);
        log.info("---------------- 验证验证码 ----------------------");

        String message=userService.verifyCode(phone,verificationCode);
        if(!message.equals("验证成功")){
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证成功 ----------------------");
        message=userService.register(user,"student");
        log.info("---------------- {} ----------------------",message);
        if("注册成功".equals(message)){

            user=userService.findByPhone(phone);
            UserBasicInfo userBasicInfo= userInfoService.createByUser(user);

            String token= userService.useToken(user);
            Map<String, Object> responseData= new HashMap<>(Collections.singletonMap("token", token));
            responseData.put("userInfo",userBasicInfo);
            return ResultUtil.buildSuccessResult(responseData);
        }else {
            return ResultUtil.buildFailResult(message);
        }
    }
}
