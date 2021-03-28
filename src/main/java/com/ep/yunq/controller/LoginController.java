package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.User;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import com.sun.crypto.provider.PBKDF2HmacSHA1Factory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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

    @CrossOrigin
    @PostMapping(value = "/api/login")
    public Result login(@RequestBody User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String username=user.getUsername();
        String password=user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        password = HtmlUtils.htmlEscape(password);

        log.info("username:{},password:{}",username,password);

        String message=userService.authUser(username,password);
        if("登录成功".equals(message)){
            return ResultUtil.buildSuccessResult(message);
        }else{
            return ResultUtil.buildFailResult(message);
        }


    }
}
