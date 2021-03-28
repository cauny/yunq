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
    @RequestMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestParam String username,@RequestParam String password){
        username = HtmlUtils.htmlEscape(username);
        password = HtmlUtils.htmlEscape(password);

        log.info("username:{},password:{}",username,password);
        User user = userService.findByUsernameAndPassword(username,password);


//        log.info(user.getCollege());
        if (null == user) {
            return ResultUtil.buildFailResult("不存在该用户");
        } else {
            return ResultUtil.buildSuccessResult(user);
        }
    }
}
