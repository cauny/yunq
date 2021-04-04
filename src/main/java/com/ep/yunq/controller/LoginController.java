package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.User;
import com.ep.yunq.pojo.UserLogin;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    UserService userService;
//    @Autowired

    //管理员可以访问主页和测试页，普通用户访问主页

    @CrossOrigin
    @PostMapping(value = "/api/login")
    public Result login(@RequestBody UserLogin userLogin) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String username=userLogin.getUsername();
        String password=userLogin.getPassword();
        username = HtmlUtils.htmlEscape(username);
        password = HtmlUtils.htmlEscape(password);

        String message=userService.authUser(username,password);
        if(!"登录成功".equals(message)){
            log.info("用户：{},登录失败",username);
            return ResultUtil.buildFailResult(message);
        }
        log.info("用户：{},登录成功",username);
        /* 如果设置了记住我选项，使用token */
        if (userLogin.getRememberMe()){
            Map<String, String> responseData= userService.useToken(userLogin);
            return ResultUtil.buildSuccessResult(responseData);
        }

        return ResultUtil.buildSuccessResult(message);
    }
}
