package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.SysParam;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.RedisUtil;
import com.ep.yunq.util.ResultUtil;
import com.ep.yunq.util.SmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @classname: UserController
 * @Author: yan
 * @Date: 2021/4/9 21:50
 * 功能描述：
 **/

@Api
@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SmsUtil smsUtil;
    @Autowired
    RedisUtil redisUtil;

    @GetMapping("/loginCode")
    public void loginCode(@RequestParam String phone) {
        smsUtil.sendSms(phone);
        return;
    }

    @ApiOperation("判断手机号是否存在，存在为true")
    @GetMapping("/api/phone-exist")
    public Result isPhoneExist(@RequestParam String phone){
        boolean message;
        message=userService.isExistByPhone(phone);
        if (message==true){
            return ResultUtil.buildSuccessResult(true);
        }
        return ResultUtil.buildSuccessResult(false);

    }
}
