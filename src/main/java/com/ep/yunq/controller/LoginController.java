package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.User;
import com.ep.yunq.pojo.UserBasicInfo;
import com.ep.yunq.pojo.UserInfo;
import com.ep.yunq.service.AdminRoleService;
import com.ep.yunq.service.UserInfoService;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ConstantUtil;
import com.ep.yunq.util.RedisUtil;
import com.ep.yunq.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * @classname: LoginController
 * @Author: yan
 * @Date: 2021/3/26 23:21
 * 功能描述：实现登录功能
 **/
@Api(tags = "登录管理")
@Slf4j
@RestController
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    RedisUtil redisUtil;


    //管理员可以访问主页和测试页，普通用户访问主页
    /*手机密码登录*/
    @ApiOperation("密码登录")
    @PostMapping(value = "/api/loginByPwd")
    public Result phoneLoginByPwd(@RequestParam String phone,@RequestParam String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        phone = HtmlUtils.htmlEscape(phone);
        password = HtmlUtils.htmlEscape(password);
        String username=userService.findByPhone(phone).getUsername();

        String message=userService.authUser(username,password);
        if(!"登录成功".equals(message)){
            log.info("用户：{},登录失败",username);
            return ResultUtil.buildFailResult(message);
        }
        log.info("用户：{},登录成功",username);
        /* 如果设置了记住我选项，使用token */
//        if (userLogin.getRememberMe()){
//            Map<String, String> responseData= userService.useToken(userLogin);
//            return ResultUtil.buildSuccessResult(responseData);
//        }
        User user=new User();
        user.setUsername(username);
        int uid=userService.findByUserName(username).getId();

        UserInfo userInfo=userInfoService.findByUid(uid);
        UserBasicInfo userBasicInfo=new UserBasicInfo();
        userBasicInfo.setPhone(userService.findByUserName(username).getPhone());
        userBasicInfo.setAvatar(userInfo.getAvatar());
        userBasicInfo.setUsername(userInfo.getUsername());
        userBasicInfo.setRoles(adminRoleService.listRolesNameByUser(uid));
        userBasicInfo.setDefaultRole(userInfo.getDefaultRole());

        String token= userService.useToken(user);
        Map<String, Object> responseData= new HashMap<>(Collections.singletonMap("token", token));
        responseData.put("userinfo",userBasicInfo);

        return ResultUtil.buildSuccessResult(responseData);
    }

    /*手机验证码登录*/
    @ApiOperation("验证码登录")
    @PostMapping(value = "/api/loginByVerificationCode")
    public Result phoneLoginByVerficationCode(@RequestParam String phone,@RequestParam String verificationCode) throws InvalidKeySpecException, NoSuchAlgorithmException {
        phone = HtmlUtils.htmlEscape(phone);
        verificationCode = HtmlUtils.htmlEscape(verificationCode);

        if(!userService.isExistByPhone(phone)){
            String message="用户不存在";
            return ResultUtil.buildFailResult(message);
        }

        String message=userService.authUserByVerificationCode(phone,verificationCode);
        if(!"登录成功".equals(message)){
            log.info("用户：{},登录失败",phone);
            return ResultUtil.buildFailResult(message);
        }
        log.info("用户：{},登录成功",phone);

        User user= new User();
        user=userService.findByPhone(phone);
        UserInfo userInfo=userInfoService.findByUid(user.getId());
        UserBasicInfo userBasicInfo=new UserBasicInfo();
        userBasicInfo.setPhone(user.getPhone());
        userBasicInfo.setAvatar(userInfo.getAvatar());
        userBasicInfo.setUsername(userInfo.getUsername());
        userBasicInfo.setRoles(adminRoleService.listRolesNameByUser(user.getId()));
        userBasicInfo.setDefaultRole(userInfo.getDefaultRole());

        String token= userService.useToken(user);
        Map<String, Object> responseData= new HashMap<>(Collections.singletonMap("token", token));
        responseData.put("userinfo",userBasicInfo);

        return ResultUtil.buildSuccessResult(responseData);
    }



    @ApiOperation("获取验证码")
    @GetMapping(value = "/api/getCode")
    public Result getCode(@RequestParam String phone){
        log.info("---------------- 获取验证码 ----------------------");
        phone = HtmlUtils.htmlEscape(phone);
        String verificationCode;
        //1. 判断是否缓存该账号验证码
        Object redisVerificationCode = redisUtil.get(phone + ConstantUtil.SMS_Verification_Code.code);
        if (!ObjectUtils.isEmpty(redisVerificationCode)) {
            verificationCode = redisVerificationCode.toString();
        } else {
            verificationCode = userService.sendCode();
        }
        //2.存入redis缓存
        boolean isSuccess = redisUtil.set(phone + ConstantUtil.SMS_Verification_Code.code, verificationCode, 180);
        if (true == isSuccess) {
            log.info("---------------- " + phone +" 验证码成功存入redis ----------------------");
            return ResultUtil.buildSuccessResult(verificationCode);

        } else {
            String message = "失败";
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("忘记密码")
    @PostMapping(value = "/api/forgetPassword")
    public Result forgetPassword(@RequestParam String phone,
                                 @RequestParam String password,
                                 @RequestParam String verificationCode){
        log.info("---------------- 重置密码 ----------------------");
        phone = HtmlUtils.htmlEscape(phone);
        if (StringUtils.isEmpty(phone)) {
            String message = "手机号为空，重置失败";
            return ResultUtil.buildFailResult(message);
        }
        if (StringUtils.isEmpty(password)) {
            String message = "密码为空，重置失败";
            return ResultUtil.buildFailResult(message);
        }
        if (!userService.isExistByPhone(phone)){
            String message="用户不存在";
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证验证码 ----------------------");
        User user=new User();
        user.setPhone(phone);
        String message=userService.verifyCode(user,verificationCode);
        if(!message.equals("验证成功")){
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 验证成功 ----------------------");

        user.setUsername(userService.findByPhone(phone).getUsername());
        user.setPassword(password);
        message=userService.resetPassword(user);
        if(!message.equals("重置成功")){
            return ResultUtil.buildFailResult(message);
        }
        log.info("---------------- 重置成功 ----------------------");
        return ResultUtil.buildSuccessResult(message);
    }


}
