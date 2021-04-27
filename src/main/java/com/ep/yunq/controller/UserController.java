package com.ep.yunq.controller;

import com.ep.yunq.application.appservice.LoginAppService;
import com.ep.yunq.application.appservice.RegisterAppService;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.RedisUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import com.ep.yunq.infrastructure.util.SmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @classname: UserController
 * @Author: yan
 * @Date: 2021/4/9 21:50
 * 功能描述：
 **/

@Api(tags = "用户管理")
@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SmsUtil smsUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoginAppService loginAppService;
    @Autowired
    RegisterAppService registerAppService;

    //管理员可以访问主页和测试页，普通用户访问主页
    /*手机密码登录*/
    @ApiOperation("密码登录")
    @PostMapping(value = "/api/loginByPwd")
    public Result phoneLoginByPwd(@RequestParam String phone, @RequestParam String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
/*        phone = HtmlUtils.htmlEscape(phone);
        password = HtmlUtils.htmlEscape(password);*/
        try {
            Result responseData = loginAppService.phoneLoginByPwd(phone, password);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }
    }

    /*手机验证码登录*/
    @ApiOperation("验证码登录")
    @PostMapping(value = "/api/loginByVerificationCode")
    public Result phoneLoginByVerificationCode(@RequestParam String phone, @RequestParam String verificationCode) {
        /*phone = HtmlUtils.htmlEscape(phone);
        verificationCode = HtmlUtils.htmlEscape(verificationCode);*/
        try {
            Result responseData = loginAppService.phoneLoginByVerificationCode(phone, verificationCode);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }
    }


    @ApiOperation("获取验证码")
    @GetMapping(value = "/api/getCode")
    public Result getCode(@RequestParam String phone) {
        /*phone = HtmlUtils.htmlEscape(phone);*/
        try {
            Result responseData = loginAppService.getCode(phone);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }
    }

    @ApiOperation("忘记密码")
    @PostMapping(value = "/api/forgetPassword")
    public Result forgetPassword(@RequestParam String phone,
                                 @RequestParam String password,
                                 @RequestParam String verificationCode) {
        /*phone = HtmlUtils.htmlEscape(phone);*/
        try {
            Result responseData = loginAppService.forgetPassword(phone, password, verificationCode);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }
    }

    @ApiOperation("判断手机号是否存在，存在为true")
    @GetMapping("/api/phone-exist")
    public Result isPhoneExist(@RequestParam String phone) {
        boolean message;
        try {
            message = userService.isExistByPhone(phone);
            if (message == true) {
                return ResultUtil.buildSuccessResult(true);
            }
            return ResultUtil.buildSuccessResult(false);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }

    }

    @CrossOrigin
    @ApiOperation("web端注册")
    @PostMapping(value = "/api/register")
    public Result register(@RequestParam String username, @RequestParam String phone,
                           @RequestParam String password, @RequestParam String role,
                           @RequestParam String verificationCode) {
        try {
            Result responseData = registerAppService.register(username, phone, password, role, verificationCode);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }
    }

    @ApiOperation("移动端注册")
    @PostMapping(value = "/api/mobileRegister")
    public Result mobileRegister(@RequestParam String username, @RequestParam String phone,
                                 @RequestParam String password, @RequestParam String verificationCode) {
        try {
            Result responseData = registerAppService.mobileRegister(username, phone, password, verificationCode);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }
    }
}
