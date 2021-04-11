package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.SysParam;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @classname: UserController
 * @Author: yan
 * @Date: 2021/4/9 21:50
 * 功能描述：
 **/

@Api
@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;

    /*@ApiOperation("获取当前登陆用户的信息")
    @GetMapping(value = "/api/user/get")
    public Result getInfoByLoginUser(@RequestParam String phone) {
        log.info("---------------- 获取登陆用户的系统参数 ----------------------");
        SysParam sysParam = sysParamService.getByUserId(userService.findByPhone(phone).getId());
        List<SysParam> sysParams = new ArrayList<>();
        sysParams.add(sysParam);
        return ResultUtil.buildSuccessResult(sysParams);
    }*/
}
