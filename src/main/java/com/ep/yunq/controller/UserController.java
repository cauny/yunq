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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
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
    public Result addUser(@RequestBody UserDTO user) {
        log.info("---------------- 增加用户 ----------------------");
        String message = userService.registerByAdmin(user);
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
        if("修改成功".equals(message)){
            message=userInfoService.editByAdmin(requestUser);
        }
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
        List<UserDTO> userDTOS= userService.initUserDTO(us);
        Page<UserDTO> userDTOPages = PageUtil.listToPage(userDTOS,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(userDTOPages);
    }

    @ApiOperation("获取所有用户")
    @GetMapping(value = "/api/admins/users")
    public Result<Page<UserDTO>> listUsers(@RequestParam int pageNum, @RequestParam int pageSize) {
        log.info("---------------- 获取所有用户 ----------------------");
        List<User> us = userService.list();
        List<UserDTO> userDTOS=userService.initUserDTO(us);
        Page<UserDTO> users=PageUtil.listToPage(userDTOS,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(users);
    }

    /**
     * -------------------------- 用户信息 --------------------------------------
     **/

    @ApiOperation("修改用户信息表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "avatar", value = "头像", required = true, dataTypeClass = File.class),
            @ApiImplicitParam(name = "ino", value = "学号/工号", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "school", value = "学校", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "college", value = "院系", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "major", value = "专业", dataTypeClass = String.class),
            @ApiImplicitParam(name = "nickname", value = "昵称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "phone", value = "电话", dataTypeClass = String.class),
            @ApiImplicitParam(name = "realname", value = "真实姓名",  dataTypeClass = String.class),
            @ApiImplicitParam(name = "sex", value = "性别",  dataTypeClass = String.class),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataTypeClass = String.class),
    })
    @PutMapping(value = "/api/admins/userInfos")
    public Result editUserInfo(@RequestBody HttpServletRequest request) {
        log.info("---------------- 修改用户信息表信息 ----------------------");
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("avatar");
        UserInfo userInfo=new UserInfo();
        int userId=Integer.parseInt(params.getParameter("userId"));
        int id=userInfoService.findByUid(userId).getId();
        userInfo.setId(id);
        userInfo.setCollege(params.getParameter("college"));
        userInfo.setSchool(params.getParameter("school"));
        userInfo.setMajor(params.getParameter("major"));
        userInfo.setIno(params.getParameter("ino"));
        userInfo.setNickname(params.getParameter("nickname"));
        userInfo.setPhone(params.getParameter("phone"));
        userInfo.setRealname(params.getParameter("realname"));
        userInfo.setSex(Integer.parseInt(params.getParameter("sex")));
        userInfo.setUsername(params.getParameter("username"));

        String message="";
        if(files.size()==0){
            message=userInfoService.edit(userInfo,null);
        }else {
            message = userInfoService.edit(userInfo,files.get(0));
        }

        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("修改用户学号工号")
    @PutMapping(value = "/api/admins/ino")
    public Result editUserIno(@RequestParam String ino){
        String message="";
        Integer uid=CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }
        UserInfo userInfo=userInfoService.findByUid(uid);
        userInfo.setIno(ino);
        message=userInfoService.edit(userInfo,null);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);


    }


}
