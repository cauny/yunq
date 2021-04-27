package com.ep.yunq.application.appservice;

import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.application.dto.UserBasicInfo;
import com.ep.yunq.domain.service.UserInfoService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @classname: RegisterAppService
 * @Author: yan
 * @Date: 2021/4/26 16:32
 * 功能描述：
 **/
@Slf4j
@Service
public class RegisterAppService {

    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;

    public Result register(String username, String phone, String password, String role, String verificationCode) {

        log.info("---------------- 注册新用户 ----------------------");
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(password);
        log.info("---------------- 验证验证码 ----------------------");

        String message = userService.verifyCode(phone, verificationCode);
        if (!message.equals("验证成功")) {
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证成功 ----------------------");
        message = userService.register(user, role);
        log.info("---------------- {} ----------------------", message);
        if ("注册成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message, null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    public Result mobileRegister(String username, String phone, String password, String verificationCode) {

        log.info("---------------- 注册新用户 ----------------------");
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(password);
        log.info("---------------- 验证验证码 ----------------------");

        String message = userService.verifyCode(phone, verificationCode);
        if (!message.equals("验证成功")) {
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证成功 ----------------------");
        message = userService.register(user, "student");
        log.info("---------------- {} ----------------------", message);
        if ("注册成功".equals(message)) {

            user = userService.findByPhone(phone);
            UserBasicInfo userBasicInfo = userInfoService.createByUser(user);

            String token = userService.useToken(user);
            Map<String, Object> responseData = new HashMap<>(Collections.singletonMap("token", token));
            responseData.put("userInfo", userBasicInfo);
            return ResultUtil.buildSuccessResult(responseData);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }
}
