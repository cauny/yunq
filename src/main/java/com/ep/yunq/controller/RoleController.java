package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.*;
import com.ep.yunq.domain.service.*;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @classname: RoleController
 * @Author: yan
 * @Date: 2021/4/23 15:23
 * 功能描述：
 **/
@Api(tags = "角色管理")
@Slf4j
@RestController
public class RoleController {
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    PermissionResourceService permissionResourceService;
    @Autowired
    AdminRoleToPerService adminRoleToPerService;
    @Autowired
    AdminRoleToMenuService adminRoleToMenuService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;
    @Autowired
    AdminMenuService adminMenuService;
    @Autowired
    UserService userService;

    @ApiOperation("添加新角色")
    @PostMapping(value = "/api/role")
    public Result addRole(@RequestBody AdminRole requestRole) {
        log.info("---------------- 添加新角色 ----------------------");
        AdminRole adminRoleInDB = adminRoleService.findByName(requestRole.getName());
        if (null != adminRoleInDB) {
            String message = "该角色已存在";
            return ResultUtil.buildFailResult(message);
        } else {
            adminRoleService.addOrUpdate(requestRole);
            String message = "添加角色成功";
            return ResultUtil.buildSuccessResult(message,null);
        }
    }

    @ApiOperation("删除角色")
    @DeleteMapping(value = "/api/role")
    public Result deleteRole(@RequestParam int rid) {
        log.info("---------------- 删除角色 ----------------------");
        String message = adminRoleService.delete(rid);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("批量删除角色")
    @DeleteMapping(value = "/api/role-batch")
    public Result batchDeleteRole(@RequestBody List<Integer> roleIds) {
        log.info("---------------- 批量删除角色 ----------------------");
        String message = adminRoleService.batchDelete(roleIds);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("更新角色状态")
    @PutMapping(value = "/api/role/status")
    public Result updateRoleStatus(@RequestBody AdminRole requestRole) {
        log.info("---------------- 更新角色状态 ----------------------");
        String message = adminRoleService.updateRoleStatus(requestRole);
        if (!"更新成功".equals(message)) {
            return ResultUtil.buildFailResult(message);
        } else {
            return ResultUtil.buildSuccessResult(message,null);
        }
    }

    @ApiOperation("修改角色信息")
    @PutMapping(value = "/api/role")
    public Result editRole(@RequestBody AdminRole requestRole) {
        log.info("---------------- 修改角色信息 ----------------------");
        String message = adminRoleService.edit(requestRole);
        if (!"更新成功".equals(message)) {
            return ResultUtil.buildFailResult(message);
        } else {
            return ResultUtil.buildSuccessResult(message,null);
        }
    }

    @ApiOperation("搜索角色")
    @GetMapping(value = "/api/role/search")
    public Result search(@RequestParam String keywords) {
        log.info("---------------- 搜索角色 ----------------------");
        List<AdminRole> rs = adminRoleService.search(keywords);
        return ResultUtil.buildSuccessResult(rs);
    }

    @ApiOperation("获取所有角色")
    @GetMapping(value = "/api/role/all")
    public Result listRoles() {
        log.info("---------------- 获取所有角色 ----------------------");
        List<AdminRole> ad = adminRoleService.list();
        return ResultUtil.buildSuccessResult(ad);
    }

    @ApiOperation("分配用户")
    @PutMapping(value = "/api/role/user")
    public Result assistUser(@RequestParam int rid, @RequestBody LinkedHashMap userIds) {
        log.info("---------------- 分配用户 ----------------------");
        String message = adminUserToRoleService.assistUser(rid, userIds);
        if (!"分配成功".equals(message)) {
            return ResultUtil.buildFailResult(message);
        } else {
            return ResultUtil.buildSuccessResult(message,null);
        }
    }

    @ApiOperation("获取所有权限")
    @GetMapping(value = "/api/role/perm")
    public Result listPerms() {
        log.info("---------------- 获取所有权限 ----------------------");
        List<PermissionResource> ps = permissionResourceService.list();
        return ResultUtil.buildSuccessResult(ps);
    }

    @ApiOperation("获取所有菜单")
    @GetMapping(value = "/api/role/menu")
    public Result listMenus() {
        log.info("---------------- 获取所有菜单 ----------------------");
        List<AdminMenu> ms = adminMenuService.list();
        return ResultUtil.buildSuccessResult(ms);
    }

    @ApiOperation("获取所有用户")
    @GetMapping(value = "/api/role/user")
    public Result listUsers() {
        log.info("---------------- 获取所有用户 ----------------------");
        List<User> users = userService.listIsEnabled();
        return ResultUtil.buildSuccessResult(users);
    }

}
