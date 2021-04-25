package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.PermissionResource;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.service.PermissionResourceService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @classname: PermissionController
 * @Author: yan
 * @Date: 2021/4/24 10:34
 * 功能描述：
 **/
@Api(tags = "权限管理")
@Slf4j
@RestController
public class PermissionController {

    @Autowired
    PermissionResourceService permissionResourceService;

    @ApiOperation("添加权限")
    @PostMapping("api/perm")
    public Result addPerm(@RequestBody PermissionResource adminPermission){
        log.info("---------------- 添加权限 ----------------------");
        String message = permissionResourceService.add(adminPermission);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/api/perm")
    public Result deletePerm(@RequestParam int pid) {
        log.info("---------------- 删除权限 ----------------------");
        String message = permissionResourceService.delete(pid);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("批量删除权限")
    @DeleteMapping("/api/perm/delete-batch")
    public Result batchDeletePerm(@RequestBody List<Integer> permIds) {
        log.info("---------------- 批量删除权限 ----------------------");
        String message = permissionResourceService.batchDelete(permIds);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("修改权限")
    @PutMapping("/api/perm")
    public Result editPerm(@RequestBody PermissionResource adminPermission) {
        log.info("---------------- 修改权限 ----------------------");
        String message = permissionResourceService.edit(adminPermission);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }


    @ApiOperation("搜索权限")
    @GetMapping(value = "/api/perm/search")
    public Result search(@RequestParam String keywords){
        log.info("---------------- 搜索权限 ----------------------");
        List<PermissionResource> ps = permissionResourceService.search(keywords);
        return ResultUtil.buildSuccessResult(ps);
    }

    @ApiOperation("获取所有权限")
    @GetMapping(value = "/api/perm/all")
    public Result all(){
        log.info("---------------- 获取所有权限 ----------------------");
        List<PermissionResource> ps = permissionResourceService.all();
        return ResultUtil.buildSuccessResult(ps);
    }
}
