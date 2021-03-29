package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.User;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import com.sun.crypto.provider.PBKDF2HmacSHA1Factory;
import com.usthe.sureness.util.JsonWebTokenUtil;
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
    public Result login(@RequestBody User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String username=user.getUsername();
        String password=user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        password = HtmlUtils.htmlEscape(password);



        String message=userService.authUser(username,password);
        if(!"登录成功".equals(message)){
            log.info("用户：{},登录失败",username);
            return ResultUtil.buildFailResult(message);
        }
        //获取角色信息
        String roles=null;
        roles=userService.findRoleById(userService.findByUserName(username).getId()).getName();

        //刷新时间5小时
        long refreshPeriodTime = 36000L;
        String jwt = JsonWebTokenUtil.issueJwt(UUID.randomUUID().toString(), user.getUsername(),
                "tom-auth-server", refreshPeriodTime >> 1, Collections.singletonList(roles),
                null, false);
        Map<String, String> responseData = Collections.singletonMap("token", jwt);

        long currentTimeMillis = System.currentTimeMillis();

        log.info("用户：{},登录成功",username);
        return ResultUtil.buildSuccessResult(responseData);



    }
}
