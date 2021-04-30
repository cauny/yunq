package com.ep.yunq.controller;

import com.ep.yunq.application.dto.UserDTO;
import com.ep.yunq.application.dto.UserLoginDTO;
import com.ep.yunq.domain.entity.*;
import com.ep.yunq.domain.service.AdminRoleService;
import com.ep.yunq.domain.service.AdminUserToRoleService;
import com.ep.yunq.domain.service.UserInfoService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @classname: UserController
 * @Author: yan
 * @Date: 2021/4/9 21:50
 * 功能描述：
 **/

@Api(tags = "用户管理")
@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    UserInfoService userInfoService;


    /**
     * -------------------------- 用户 --------------------------------------
     **/

    @ApiOperation("增加用户")
    @PostMapping(value = "/api/admins/users")
    public Result addUser(@RequestBody User user) {
        log.info("---------------- 增加用户 ----------------------");
        String message = userService.registerByAdmin(user, "student");
        if ("注册成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("删除用户")
    @DeleteMapping(value = "/api/admins/users")
    public Result deleteUser(@RequestParam int uid) {
        log.info("---------------- 删除用户 ----------------------");
        String message = userService.delete(uid);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("批量删除用户")
    @DeleteMapping(value = "/api/admins/users/batches")
    public Result batchDeleteUser(@RequestBody List<Integer> userIds) {
        log.info("---------------- 批量删除用户 ----------------------");
        String message = userService.batchDelete(userIds);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/api/admins/users")
    public Result editUser(@RequestBody UserDTO requestUser) {
        log.info("---------------- 修改用户信息 ----------------------");
        String message = userService.editUser(requestUser);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }


    @ApiOperation("重置密码")
    @PutMapping(value = "/api/admins/users/passwords")
    public Result resetPassword(@RequestBody User requestUser) {
        log.info("---------------- 重置密码 ----------------------");
        String message = userService.resetPassword(requestUser);
        if ("重置成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("搜索用户")
    @GetMapping(value = "/api/admins/users/search")
    public Result<Page<UserDTO>> search(@RequestParam String keywords, @RequestParam int pageNum,
                                        @RequestParam int pageSize) {
        log.info("---------------- 搜索用户 ----------------------");
        List<User> us = userService.search(keywords);
        log.info("搜索"+us);
        Page<User> usersPage = PageUtil.listToPage(us, pageNum, pageSize);
        Page<UserDTO> userDTOS = PageUtil.pageChange(usersPage, UserDTO.class);
        return ResultUtil.buildSuccessResult(userDTOS);
    }

    @ApiOperation("获取所有用户")
    @GetMapping(value = "/api/admins/users")
    public Result<Page<User>> listUsers(@RequestParam int pageNum, @RequestParam int pageSize) {
        log.info("---------------- 获取所有用户 ----------------------");
        Page<User> us = userService.list(pageNum, pageSize);
        return ResultUtil.buildSuccessResult(us);
    }

    /**
     * -------------------------- 用户信息 --------------------------------------
     **/

    @ApiOperation("修改用户信息表信息")
    @PutMapping(value = "/api/admins/userInfos")
    public Result editUserInfo(@RequestBody UserInfo userInfo) {
        log.info("---------------- 修改用户信息 ----------------------");
        String message = userInfoService.edit(userInfo);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }


}
