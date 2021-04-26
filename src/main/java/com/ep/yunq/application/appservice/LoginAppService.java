package com.ep.yunq.application.appservice;

import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.domain.entity.UserBasicInfo;
import com.ep.yunq.domain.service.AdminRoleService;
import com.ep.yunq.domain.service.UserInfoService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.RedisUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import com.ep.yunq.infrastructure.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @classname: LoginAppService
 * @Author: yan
 * @Date: 2021/4/26 15:55
 * 功能描述：
 **/
@Slf4j
@Service
public class LoginAppService {
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AdminRoleService adminRoleService;
    @Resource
    RedisUtil redisUtil;
    @Resource
    SmsUtil smsUtil;

    public String phoneLoginByPwd(String phone,String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String username=userService.findByPhone(phone).getUsername();
        String message=userService.authUser(username,password);
        return message;
    }

    public Result phoneLoginByVerificationCode(String phone, String verificationCode){
        if(!userService.isExistByPhone(phone)){
            String message="用户不存在";
            return ResultUtil.buildFailResult(message);
        }

        String message=smsUtil.checkSms(phone,verificationCode);
        /*String message=userService.authUserByVerificationCode(phone,verificationCode);*/
        if(!"验证成功".equals(message)){
            log.info("用户：{},登录失败",phone);
            return ResultUtil.buildFailResult(message);
        }
        log.info("用户：{},登录成功",phone);

        User user= new User();
        user=userService.findByPhone(phone);
        UserBasicInfo userBasicInfo= userInfoService.createByUser(user);

        String token= userService.useToken(user);
        Map<String, Object> responseData= new HashMap<>(Collections.singletonMap("token", token));
        responseData.put("userInfo",userBasicInfo);

        return ResultUtil.buildSuccessResult(responseData);

    }
}
