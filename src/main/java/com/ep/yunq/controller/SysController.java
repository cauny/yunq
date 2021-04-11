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
@Api
@Slf4j
@RestController
public class SysController {

    @Autowired
    SysParamService sysParamService;
    @Autowired
    UserService userService;

    /** -------------------------- 系统参数 -------------------------------------- **/

    @ApiOperation("获取所有系统参数")
    @GetMapping(value = "/api/sys/param/all")
    public Result getAllSysParam() {
        log.info("---------------- 获取所有系统参数 ----------------------");
        List<SysParam> sysParams= sysParamService.list();
        return ResultUtil.buildSuccessResult(sysParams);
    }

    @ApiOperation("修改系统参数")
    @PutMapping(value = "/api/sys/param/edit")
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

    @ApiOperation("搜索系统参数")
    @GetMapping(value = "/api/sys/param/search")
    public Result searchSysParam(@RequestParam String keywords) {
        log.info("---------------- 搜索系统参数 ----------------------");
        List<SysParam> sysParams= sysParamService.search(keywords);
        return ResultUtil.buildSuccessResult(sysParams);
    }

    @ApiOperation("获取登陆用户的系统参数")
    @GetMapping(value = "/api/sys/param/get")
    public Result getSysParamByLoginUser(@RequestParam String phone) {
        log.info("---------------- 获取登陆用户的系统参数 ----------------------");
        SysParam sysParam = sysParamService.getByUserId(userService.findByPhone(phone).getId());
        List<SysParam> sysParams = new ArrayList<>();
        sysParams.add(sysParam);
        return ResultUtil.buildSuccessResult(sysParams);
    }

/*
    *//** -------------------------- 学校机构 -------------------------------------- **//*

    @GetMapping(value = "/api/sys/school/all")
    public Result getAllSchoolInstitution() {
        log.info("---------------- 获取所有学校机构 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.list();
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }

    @PostMapping(value = "/api/sys/school/add")
    public Result addSchoolInstitution(@RequestBody SchoolInstitution sInstitution) {
        log.info("---------------- 增加学校机构 ----------------------");
        String message = schoolInstitutionService.add(sInstitution);
        if ("添加成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @GetMapping(value = "/api/sys/school/delete")
    public Result deleteSchoolInstitution(@RequestParam int siid) {
        log.info("---------------- 删除学校机构 ----------------------");
        String message = schoolInstitutionService.delete(siid);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @PutMapping(value = "/api/sys/school/edit")
    public Result editSchoolInstitution(@RequestBody SchoolInstitution sInstitution) {
        log.info("---------------- 修改学校机构 ----------------------");
        String message = schoolInstitutionService.edit(sInstitution);
        if ("修改成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @GetMapping(value = "/api/sys/school/search")
    public Result searchSchoolInstitution(@RequestParam String keywords) {
        log.info("---------------- 搜索学校机构 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.search(keywords);
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }

    @GetMapping("/api/userInfo/school/get")
    public Result getSchool() {
        log.info("---------------- 获取学校级别 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.getSchool(0);
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }

    @GetMapping("/api/userInfo/school/get/{parentId}")
    public Result getCollegeAndMajor(@PathVariable("parentId") int parentId) {
        log.info("---------------- 获取院校系 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.getSchool(parentId);
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }*/

}
