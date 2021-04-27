package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.domain.entity.UserAuths;
import com.ep.yunq.domain.service.GithubLoginService;
import com.ep.yunq.domain.service.UserAuthsService;
import com.ep.yunq.domain.service.UserInfoService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import com.ep.yunq.infrastructure.util.GithubConstant;
import com.ep.yunq.infrastructure.util.HttpClientUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @classname: GithubLoginController
 * @Author: yan
 * @Date: 2021/4/14 17:30
 * 功能描述：
 **/
@Api
@Slf4j
@RestController
public class GithubLoginController {
    @Autowired
    GithubLoginService githubLoginService;
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserAuthsService userAuthsService;

    //回调地址
    @ApiOperation("访问回调")
    @GetMapping("/api/callback")
    public Result callback(@RequestParam String code, @RequestParam String state) throws Exception {
        String message = "";
        Map<String, Object> responseData = new IdentityHashMap<>();
        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(state)) {
            //拿到我们的code,去请求token
            //发送一个请求到
            String token_url = GithubConstant.TOKEN_URL.replace("CODE", code);
            //得到的responseStr是一个字符串需要将它解析放到map中
            String responseStr = HttpClientUtil.doGet(token_url);
            log.info("responseStr" + responseStr);
            // 调用方法从map中获得返回的--》 令牌
            String token = HttpClientUtil.getMap(responseStr).get("access_token");

            //根据token发送请求获取登录人的信息  ，通过令牌去获得用户信息
            String userinfo_url = GithubConstant.USER_INFO_URL;
            log.info(userinfo_url);
            responseStr = HttpClientUtil.doGetUser(userinfo_url, token);//json
            log.info(responseStr);

            Map<String, String> responseMap = HttpClientUtil.getMapByJson(responseStr);

            int id = Integer.parseInt(responseMap.get("id"));
            message = githubLoginService.isExistUserByGithubId(id);
            String username = responseMap.get("login");
            String avatar = responseMap.get("avatar_url");
            if (!message.equals("用户存在")) {
                User user = new User();
                /*user.setUsername(username);*/
                user.setEnabled(1);
                user = userService.addAndReturn(user);

                UserAuths userAuths = new UserAuths();
                userAuths.setUserId(user.getId());
                userAuths.setIdentifier(String.valueOf(id));
                userAuths.setCredential(token);
                userAuths.setIdentityType("github");
                //默认角色为学生
                message = githubLoginService.githubRegister(user, "student", avatar, userAuths);
                if (!message.equals("注册成功")) {
                    return ResultUtil.buildFailResult(message);
                }
                responseData = userService.loginMessageByUserId(user.getId());
                return ResultUtil.buildSuccessResult(message, responseData);

            }
            // 成功则登陆,判断上次是否绑定手机号
            /*User user=userService.findByGithubId(id);
            if(user.getPhone()==null){
                message="补充手机号和密码";
                responseData.clear();
                responseData.put("githubId",id);
                return ResultUtil.buildResult(ConstantUtil.USER_INFO_UNCOMPLETE,message,responseData);
            }*/
            /*responseData=userService.loginMessage(user.getPhone());*/
            responseData = userService.loginMessageByUserId(userAuthsService.findByIdentityTypeAndIdentifier("github", String.valueOf(id)).getUserId());
            return ResultUtil.buildSuccessResult(responseData);
        }
        message = "授权登录失败";
        return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("绑定手机")
    @PutMapping("/api/bind-user")
    public Result bindUser(@RequestParam String phone,
                           @RequestParam String verificationCode,
                           @RequestParam Integer userId,
                           @RequestParam String password) {
        log.info("---------------- 补充电话和密码 ----------------------");
        String message = "";
        if(userService.findByPhone(phone)!=null){
            message="该号码已被使用";
            return ResultUtil.buildResult(ConstantUtil.PHONE_IS_USED,message,null);
        }
        message = userService.verifyCode(phone, verificationCode);
        if (!message.equals("验证成功")) {
            log.info("---------------- 验证失败 ----------------------");
            return ResultUtil.buildResult(ConstantUtil.VERIFIED_CODE_ERROR, message, null);
        }
        User user=new User();
        user= userService.findById(userId);
        user.setPhone(phone);
        user.setPassword(password);
        message = githubLoginService.fillPasswordAndPhone(user);
        if (!message.equals("重置成功")) {
            return ResultUtil.buildFailResult(message);
        }
        return ResultUtil.buildSuccessResult("手机绑定成功",null);

    }


    /*@ApiOperation("补充电话和密码")
    @PutMapping("/api/password-phone")
    public Result fillPasswordAndPhone(@RequestParam String phone,
                                       @RequestParam Integer githubId,
                                       @RequestParam String verificationCode,
                                       @RequestParam String password) {
        log.info("---------------- 补充电话和密码 ----------------------");
        String message = "";
        *//*if(userService.findByPhone(phone)!=null){
            message="该号码已被使用";
            return ResultUtil.buildResult(ConstantUtil.PHONE_IS_USED,message,null);
        }*//*
        message = userService.verifyCode(phone, verificationCode);
        if (!message.equals("验证成功")) {
            log.info("---------------- 验证失败 ----------------------");
            return ResultUtil.buildResult(ConstantUtil.VERIFIED_CODE_ERROR, message, null);
        }
        log.info("---------------- 开始绑定手机号 ----------------------");
        *//*message=githubLoginService.bindGithubAndUser(phone,githubId);*//*
        if (message.equals("用户已绑定github")) {
            log.info("---------------- 用户已绑定github ----------------------");
            return ResultUtil.buildResult(ConstantUtil.PHONE_IS_USED, message, null);
        }

        if (message.equals("用户不存在，新建账户")) {
            log.info("---------------- 用户不存在，新建账户 ----------------------");
            User user = new User();
            user.setPhone(phone);
            user.setPassword(password);
            message = githubLoginService.fillPasswordAndPhone(user);
            if (!message.equals("重置成功")) {
                return ResultUtil.buildFailResult(message);
            }
            Map<String, Object> responseData = userService.loginMessage(phone);
            responseData.put("Message", "成功绑定用户");
            return ResultUtil.buildSuccessResult(responseData);

        }
        //否则成功绑定用户，返回用户信息；
        log.info("---------------- 成功绑定用户 ----------------------");
        Map<String, Object> responseData = userService.loginMessage(phone);
        responseData.put("Message", "成功绑定用户");
        return ResultUtil.buildSuccessResult(responseData);
    }*/
}
