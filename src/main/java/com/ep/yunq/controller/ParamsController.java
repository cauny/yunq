package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.SysParam;
import com.ep.yunq.service.SysParamService;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @classname: SysController
 * @Author: yan
 * @Date: 2021/4/9 21:31
 * 功能描述：
 **/
@Api(tags = "系统参数")
@Slf4j
@RestController
public class ParamsController {

    @Autowired
    SysParamService sysParamService;
    @Autowired
    UserService userService;

    /** -------------------------- 系统参数 -------------------------------------- **/

    @ApiOperation("获取所有系统参数")
    @GetMapping(value = "/api/params")
    public Result getAllSysParam(@RequestParam(required = false) String phone) {
        log.info("---------------- 获取所有系统参数 ----------------------");
        if(phone==null){
            List<SysParam> sysParams= sysParamService.list();
            return ResultUtil.buildSuccessResult(sysParams);
        }else{
            SysParam sysParam = sysParamService.getByUserId(userService.findByPhone(phone).getId());
            List<SysParam> sysParams = new ArrayList<>();
            sysParams.add(sysParam);
            return ResultUtil.buildSuccessResult(sysParams);

        }

    }

    @ApiOperation("修改系统参数")
    @PutMapping(value = "/api/params")
    public Result editSysParam(@RequestBody SysParam sysParam) {
        log.info("---------------- 修改系统参数 ----------------------");
        String message = sysParamService.edit(sysParam);
        if ("修改成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    /*@ApiOperation("搜索系统参数")
    @GetMapping(value = "/api/params/search")
    public Result searchSysParam(@RequestParam String keywords) {
        log.info("---------------- 搜索系统参数 ----------------------");
        List<SysParam> sysParams= sysParamService.search(keywords);
        return ResultUtil.buildSuccessResult(sysParams);
    }*/

    /*@ApiOperation("获取登陆用户的系统参数")
    @GetMapping(value = "/api/params/users")
    public Result getSysParamByLoginUser(@RequestParam String phone) {
        log.info("---------------- 获取登陆用户的系统参数 ----------------------");
        SysParam sysParam = sysParamService.getByUserId(userService.findByPhone(phone).getId());
        List<SysParam> sysParams = new ArrayList<>();
        sysParams.add(sysParam);
        return ResultUtil.buildSuccessResult(sysParams);
    }*/

}
