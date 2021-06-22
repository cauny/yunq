package com.ep.yunq.controller;

import com.ep.yunq.application.dto.PermissionDTO;
import com.ep.yunq.application.dto.RoleDTO;
import com.ep.yunq.application.dto.SchoolInstitutionDTO;
import com.ep.yunq.domain.entity.*;
import com.ep.yunq.domain.service.*;
import com.ep.yunq.infrastructure.util.CommonUtil;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import com.usthe.sureness.matcher.TreePathRoleMatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    TreePathRoleMatcher treePathRoleMatcher;

    @ApiOperation("添加新角色")
    @PostMapping(value = "/api/roles/roles")
    public Result<String> addRole(@RequestBody AdminRole requestRole) {
        log.info("---------------- 添加新角色 ----------------------");
        AdminRole adminRoleInDB = adminRoleService.findByName(requestRole.getName());
        if (null != adminRoleInDB) {
            String message = "该角色已存在";
            return ResultUtil.buildFailResult(message);
        } else {
            adminRoleService.add(requestRole);
            String message = "添加角色成功";
            treePathRoleMatcher.rebuildTree();
            return ResultUtil.buildSuccessResult(message,null);
        }
    }

    @ApiOperation("删除角色")
    @DeleteMapping(value = "/api/roles/roles")
    public Result<String> deleteRole(@RequestParam int rid) {
        log.info("---------------- 删除角色 ----------------------");
        String message = adminRoleService.delete(rid);
        if ("删除成功".equals(message)) {
            treePathRoleMatcher.rebuildTree();
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("批量删除角色")
    @DeleteMapping(value = "/api/roles/batches")
    public Result<String> batchDeleteRole(@RequestBody List<Integer> roleIds) {
        log.info("---------------- 批量删除角色 ----------------------");
        String message = adminRoleService.batchDelete(roleIds);
        if ("删除成功".equals(message)) {
            treePathRoleMatcher.rebuildTree();
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    /*@ApiOperation("更新角色状态")
    @PutMapping(value = "/api/roles/status")
    public Result<String> updateRoleStatus(@RequestBody AdminRole requestRole) {
        log.info("---------------- 更新角色状态 ----------------------");
        String message = adminRoleService.updateRoleStatus(requestRole);
        if (!"更新成功".equals(message)) {
            return ResultUtil.buildFailResult(message);
        } else {
            return ResultUtil.buildSuccessResult(message,null);
        }
    }*/

    @ApiOperation("修改角色信息")
    @PutMapping(value = "/api/roles/roles")
    public Result<String> editRole(@RequestBody AdminRole requestRole) {
        log.info("---------------- 修改角色信息 ----------------------");
        String message = adminRoleService.edit(requestRole);
        if (!"更新成功".equals(message)) {
            return ResultUtil.buildFailResult(message);
        } else {
            treePathRoleMatcher.rebuildTree();
            return ResultUtil.buildSuccessResult(message,null);
        }
    }

    @ApiOperation("搜索角色")
    @GetMapping(value = "/api/roles/search")
    public Result<Page<RoleDTO>> search(@RequestParam String keywords,
                                          @RequestParam int pageNum,
                                          @RequestParam int pageSize) {
        log.info("---------------- 搜索角色 ----------------------");
        List<AdminRole> rs = adminRoleService.search(keywords);
        Page<AdminRole> adminRolesPage= PageUtil.listToPage(rs,pageNum,pageSize);
        Page<RoleDTO> roleDTOS=PageUtil.pageChange(adminRolesPage,RoleDTO.class);
        return ResultUtil.buildSuccessResult(roleDTOS);
    }

    @ApiOperation("获取所有角色")
    @GetMapping(value = "/api/roles/roles")
    public Result<Page<RoleDTO>> listRoles(@RequestParam int pageNum,
                                             @RequestParam int pageSize) {
        log.info("---------------- 获取所有角色 ----------------------");
        List<AdminRole> ad = adminRoleService.list();
        Page<AdminRole> adminRolesPage= PageUtil.listToPage(ad,pageNum,pageSize);
        Page<RoleDTO> roleDTOS=PageUtil.pageChange(adminRolesPage,RoleDTO.class);
        return ResultUtil.buildSuccessResult(roleDTOS);
    }

    @ApiOperation("获取所有可用角色")
    @GetMapping(value = "/api/roles/enabled")
    public Result<Page<RoleDTO>> getAllRoleIsEnabled(@RequestParam int pageNum,
                                                     @RequestParam int pageSize) {
        log.info("---------------- 获取所有角色 ----------------------");
        List<AdminRole> ad = adminRoleService.listIsEnabled();
        Page<AdminRole> adminRolesPage= PageUtil.listToPage(ad,pageNum,pageSize);
        Page<RoleDTO> roleDTOS=PageUtil.pageChange(adminRolesPage,RoleDTO.class);
        return ResultUtil.buildSuccessResult(roleDTOS);
    }

    @ApiOperation("修改当前用户默认角色")
    @PutMapping(value = "/api/roles/defaults")
    public Result<String> editDefaultRole(@RequestParam String role) {
        log.info("---------------- 修改当前用户默认角色 ----------------------");
        Integer uid= CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }
        String message= userInfoService.editDefaultRole(role,uid);
        if(message.equals("修改成功")){
            return ResultUtil.buildSuccessResult(message);
        }else {
            return ResultUtil.buildFailResult(message);
        }

    }

    /*@ApiOperation("分配用户")
    @PutMapping(value = "/api/role/user")
    public Result<String> assistUser(@RequestParam int rid, @RequestBody LinkedHashMap userIds) {
        log.info("---------------- 分配用户 ----------------------");
        String message = adminUserToRoleService.assistUser(rid, userIds);
        if (!"分配成功".equals(message)) {
            return ResultUtil.buildFailResult(message);
        } else {
            return ResultUtil.buildSuccessResult(message,null);
        }
    }*/



   /* @ApiOperation("获取所有用户")
    @GetMapping(value = "/api/roles/users")
    public Result<List<User>> listUsers() {
        log.info("---------------- 获取所有用户 ----------------------");
        List<User> users = userService.listIsEnabled();
        return ResultUtil.buildSuccessResult(users);
    }*/

}
