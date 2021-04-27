package com.ep.yunq.application.appservice;

import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.application.dto.UserBasicInfo;
import com.ep.yunq.domain.service.AdminRoleService;
import com.ep.yunq.domain.service.UserInfoService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import com.ep.yunq.infrastructure.util.RedisUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import com.ep.yunq.infrastructure.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    public Result phoneLoginByPwd(String phone, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String username = userService.findByPhone(phone).getUsername();
        String message = userService.authUser(username, password);
        if (!"登录成功".equals(message)) {
            log.info("用户：{},登录失败", username);
            return ResultUtil.buildFailResult(message);
        }
        log.info("用户：{},登录成功", username);
        Map<String, Object> responseData = userService.loginMessage(phone);
        return ResultUtil.buildSuccessResult(responseData);
    }

    public Result phoneLoginByVerificationCode(String phone, String verificationCode) {
        if (!userService.isExistByPhone(phone)) {
            String message = "用户不存在";
            return ResultUtil.buildFailResult(message);
        }

        String message = smsUtil.checkSms(phone, verificationCode);
        /*String message=userService.authUserByVerificationCode(phone,verificationCode);*/
        if (!"验证成功".equals(message)) {
            log.info("用户：{},登录失败", phone);
            return ResultUtil.buildFailResult(message);
        }
        log.info("用户：{},登录成功", phone);

        User user = new User();
        user = userService.findByPhone(phone);
        UserBasicInfo userBasicInfo = userInfoService.createByUser(user);

        String token = userService.useToken(user);
        Map<String, Object> responseData = new HashMap<>(Collections.singletonMap("token", token));
        responseData.put("userInfo", userBasicInfo);

        return ResultUtil.buildSuccessResult(responseData);
    }

    public Result getCode(String phone) {
        log.info("---------------- 获取验证码 ----------------------");
        String verificationCode;
        //1. 判断是否缓存该账号验证码
        Object redisVerificationCode = redisUtil.get(phone + ConstantUtil.SMS_Verification_Code.code);
        if (!ObjectUtils.isEmpty(redisVerificationCode)) {
            verificationCode = redisVerificationCode.toString();
            verificationCode = smsUtil.sendSms(phone, verificationCode);
        } else {
            /*verificationCode= userService.sendCode();*/
            verificationCode = smsUtil.createRandomVcode();
            verificationCode = smsUtil.sendSms(phone, verificationCode);
        }
        //2.存入redis缓存
        boolean isSuccess = redisUtil.set(phone + ConstantUtil.SMS_Verification_Code.code, verificationCode, 180);
        if (true == isSuccess) {
            log.info("---------------- " + phone + " 验证码成功存入redis ----------------------");
            return ResultUtil.buildSuccessResult(verificationCode);
            /*return ResultUtil.buildSuccessResult(null);*/
        } else {
            String message = "失败";
            return ResultUtil.buildFailResult(message);
        }
    }

    public Result forgetPassword(String phone, String password, String verificationCode) {
        log.info("---------------- 重置密码 ----------------------");
        if (StringUtils.isEmpty(phone)) {
            String message = "手机号为空，重置失败";
            return ResultUtil.buildFailResult(message);
        }
        if (StringUtils.isEmpty(password)) {
            String message = "密码为空，重置失败";
            return ResultUtil.buildFailResult(message);
        }
        if (!userService.isExistByPhone(phone)) {
            String message = "用户不存在";
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证验证码 ----------------------");
        User user = new User();
        user.setPhone(phone);
        String message = userService.verifyCode(phone, verificationCode);
        if (!message.equals("验证成功")) {
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证成功 ----------------------");

        user.setUsername(userService.findByPhone(phone).getUsername());
        user.setPassword(password);
        message = userService.resetPassword(user);
        if (!message.equals("重置成功")) {
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 重置成功 ----------------------");
        return ResultUtil.buildSuccessResult(message, null);
    }
}
